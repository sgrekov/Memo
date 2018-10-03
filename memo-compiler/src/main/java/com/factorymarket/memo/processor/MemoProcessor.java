package com.factorymarket.memo.processor;

import com.factorymarket.memo.annotations.IgnoreMemo;
import com.factorymarket.memo.annotations.Memoized;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MemoProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //get all elements annotated with MemoProcessor
        Collection<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(Memoized.class);

        //filter out elements we don't need
        List<TypeElement> types = new LinkedList<>(ElementFilter.typesIn(annotatedElements));

        for (TypeElement type : types) {
            //interfaces are types too, but we only need classes
            //we need to check if the TypeElement is a valid class
            if (isValidClass(type)) {
                writeSourceFile(type);
            } else {
                return true;
            }
        }

        // We are the only ones handling MemoProcessor annotations
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Memoized.class.getCanonicalName());

        return annotations;
    }

    private void writeSourceFile(TypeElement originatingType) {

        //get the current annotated class name
        TypeVariableName typeVariableName = TypeVariableName.get(originatingType.getSimpleName().toString());
        Memoized sourceMemoized = originatingType.getAnnotation(Memoized.class);

        ClassName sourceClassName = ClassName.get(originatingType);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeVariableName, "view")
                .addStatement("this.view = view")
                .build();

        FieldSpec view = FieldSpec.builder(sourceClassName, "view")
                .addModifiers(Modifier.PRIVATE)
                .build();

        ClassName baseClassName = ClassName.get("com.factorymarket.memo.annotations", "BaseMemoised");

        TypeSpec.Builder memoizedClassBuider = TypeSpec.classBuilder(originatingType.getSimpleName().toString() + "Memoized")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(view)
                .addMethod(constructor)
                .addSuperinterface(sourceClassName)
                .superclass(baseClassName);

        //go through methods of interface, and generate memoized methods
        for (Element methodElement : originatingType.getEnclosedElements()) {
            if (methodElement.getKind() == ElementKind.METHOD) {
                addMemoizedMethod((ExecutableElement) methodElement, memoizedClassBuider, sourceMemoized.debugTag());
            }
        }

        //if annotated interface has super interface, generate their methods as well
        List<? extends TypeMirror> interfaces = originatingType.getInterfaces();
        if (interfaces.size() > 0) {
            for (TypeMirror typeMirror : interfaces) {
                if (typeMirror.getKind() == TypeKind.DECLARED) {
                    DeclaredType kind = (DeclaredType) typeMirror;
                    for (Element methodElement : kind.asElement().getEnclosedElements()) {
                        if (methodElement.getKind() == ElementKind.METHOD) {
                            addMemoizedMethod((ExecutableElement) methodElement, memoizedClassBuider, sourceMemoized.debugTag());
                        }
                    }
                }

            }
        }

        //create the file
        JavaFile javaFile = JavaFile
                .builder(originatingType.getEnclosingElement().toString(), memoizedClassBuider.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMemoizedMethod(ExecutableElement methodElement, TypeSpec.Builder memoizedClass, String tag) {
        if (!methodElement.getReturnType().getKind().equals(TypeKind.VOID)) {
            propagateMethodWithReturnValue(methodElement, memoizedClass);
            return;
        }

        List<? extends VariableElement> params = methodElement.getParameters();
        switch (params.size()) {
            case 0:
                propagateParameterlessMethod(methodElement, memoizedClass, tag);
                break;
            case 1:
                if (methodElement.getAnnotation(IgnoreMemo.class) == null) {
                    createOneParamMethod(methodElement, params.get(0), memoizedClass, tag);
                } else {
                    propagateOneParamMethod(methodElement, params.get(0), memoizedClass, tag);
                }
                break;
            default:
                generatedMethodWithSeveralParams(methodElement, memoizedClass, params, tag);
                break;
        }
    }

    private void generatedMethodWithSeveralParams(ExecutableElement methodElement,
                                                  TypeSpec.Builder memoizedClass,
                                                  List<? extends VariableElement> params,
                                                  String tag) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        List<String> paramsCheck = new LinkedList<>();
        List<String> paramsNames = new LinkedList<>();
        boolean isIgnored = methodElement.getAnnotation(IgnoreMemo.class) != null;
        for (VariableElement param : params) {
            String paramName = param.getSimpleName().toString();
            String signature = methodElement.getSimpleName().toString() + "_" + paramName;

            TypeName className = ClassName.get(param.asType());
            ParameterSpec paramSpec = ParameterSpec.builder(className, paramName)
                    .addModifiers(Modifier.FINAL)
                    .build();
            methodBuilder.addParameter(paramSpec);
            paramsNames.add(paramName);

            if (!isIgnored) {
                createKeyConstant(signature, memoizedClass);

                String paramCheckName = paramName + "IsDifferent";
                paramsCheck.add(paramCheckName);
                if (isStandartObject(param)) {
                    methodBuilder
                            .addStatement("boolean " + paramCheckName + " = isStandartObjectDifferent(" + signature + "_key, $L)", paramName);
                } else if (isObject(param)) {
                    methodBuilder
                            .addStatement("boolean " + paramCheckName + " = isObjectDifferent(" + signature + "_key, $L)", paramName);
                } else {
                    String primitiveType = getPrimitiveType(param.asType().getKind());
                    methodBuilder
                            .addStatement("boolean " + paramCheckName + " = is" + primitiveType + "Different(" + signature + "_key, $L)", paramName);
                }
            }
        }
        StringBuilder statement = new StringBuilder();
        for (String check : paramsCheck) {
            if (statement.length() == 0) {
                statement.append(check);
            } else {
                statement.append(" || ").append(check);
            }
        }

        String viewCallStatement = createViewCallStatement(methodElement, paramsNames);

        if (isIgnored) {
            addLogMessage(methodBuilder, methodElement.getSimpleName(), tag);
            methodBuilder.addStatement(viewCallStatement);
        } else {
            methodBuilder.beginControlFlow("if (" + statement.toString() + ")");
            addLogMessage(methodBuilder, methodElement.getSimpleName(), tag);
            methodBuilder.addStatement(viewCallStatement);
            methodBuilder.endControlFlow();
        }

        memoizedClass.addMethod(methodBuilder.build());
    }

    private String getPrimitiveType(TypeKind kind) {
        switch (kind) {
            case BOOLEAN:
                return "Boolean";
            case SHORT:
                return "Short";
            case INT:
                return "Int";
            case LONG:
                return "Long";
            case CHAR:
                return "Char";
            case FLOAT:
                return "Float";
            case DOUBLE:
                return "Double";
            case BYTE:
                return "Byte";
        }
        throw new IllegalArgumentException("unknown type passed to method");
    }

    private void addLogMessage(MethodSpec.Builder methodBuilder, Name simpleName, String debugTag) {
        if (debugTag != null) {
            //TODO: add logging
        }
    }

    private String createViewCallStatement(ExecutableElement methodElement, List<String> paramsNames) {
        StringBuilder paramsSignature = new StringBuilder();
        for (String param : paramsNames) {
            if (paramsSignature.length() == 0) {
                paramsSignature.append(param);
            } else {
                paramsSignature.append(",").append(param);
            }
        }

        return "this.view." + methodElement.getSimpleName() + "(" + paramsSignature + ")";
    }

    private void propagateOneParamMethod(ExecutableElement methodElement,
                                         VariableElement variableElement,
                                         TypeSpec.Builder memoizedClass,
                                         String debugTag) {
        String paramName = variableElement.getSimpleName().toString();
        TypeName className = ClassName.get(variableElement.asType());

        ParameterSpec param = ParameterSpec.builder(
                className,
                paramName)
                .addModifiers(Modifier.FINAL)
                .build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(param);

        addLogMessage(methodBuilder, methodElement.getSimpleName(), debugTag);
        methodBuilder
                .addStatement("this.view." + methodElement.getSimpleName() + "(" + paramName + ")");
        memoizedClass.addMethod(methodBuilder.build());
    }

    private void createOneParamMethod(ExecutableElement methodElement,
                                      VariableElement variableElement,
                                      TypeSpec.Builder memoizedClass,
                                      String debugTag) {
        String paramName = variableElement.getSimpleName().toString();
        String signature = methodElement.getSimpleName().toString()
                + "_" + paramName;
        createKeyConstant(signature, memoizedClass);

        TypeName className = ClassName.get(variableElement.asType());
        ParameterSpec param = ParameterSpec.builder(
                className,
                paramName)
                .addModifiers(Modifier.FINAL)
                .build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(param);
        if (isStandartObject(variableElement)) {
            methodBuilder
                    .beginControlFlow("if (isStandartObjectDifferent(" + signature + "_key, $L))", paramName);
        } else if (isObject(variableElement)) {
            methodBuilder
                    .beginControlFlow("if (isObjectDifferent(" + signature + "_key, $L))", paramName);
        } else {
            String primitiveType = getPrimitiveType(variableElement.asType().getKind());
            methodBuilder
                    .beginControlFlow("if (is" + primitiveType + "Different(" + signature + "_key, $L))", paramName);
        }
        addLogMessage(methodBuilder, methodElement.getSimpleName(), debugTag);
        methodBuilder.addStatement("this.view." + methodElement.getSimpleName() + "(" + paramName + ")")
                .endControlFlow();


        memoizedClass.addMethod(methodBuilder.build());
    }

    private void createKeyConstant(String signature, TypeSpec.Builder memoizedClass) {
        FieldSpec key = FieldSpec.builder(String.class, signature + "_key")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\"" + signature + "\"")
                .build();
        memoizedClass.addField(key);
    }

    private boolean isStandartObject(VariableElement variableElement) {
        String typeFullName = variableElement.asType().toString();
        return typeFullName.equals(String.class.getName())
                || typeFullName.equals(Long.class.getName())
                || typeFullName.equals(Float.class.getName())
                || typeFullName.equals(Character.class.getName())
                || typeFullName.equals(Integer.class.getName())
                || typeFullName.equals(Short.class.getName())
                || typeFullName.equals(Byte.class.getName())
                || typeFullName.equals(Boolean.class.getName())
                || typeFullName.equals(Enum.class.getName());
    }

    private boolean isObject(VariableElement variableElement) {
        TypeKind kind = variableElement.asType().getKind();
        if (kind.isPrimitive()) {
            return false;
        } else if (kind.equals(TypeKind.ARRAY) || kind.equals(TypeKind.DECLARED)) {
            return true;
        } else {
            throw new IllegalArgumentException("unknown type passed to method");
        }
    }

    private void propagateParameterlessMethod(ExecutableElement methodElement, TypeSpec.Builder memoizedClass, String debugTag) {
        MethodSpec.Builder method = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        addLogMessage(method, methodElement.getSimpleName(), debugTag);

        method.addStatement("this.view." + methodElement.getSimpleName() + "()");

        if (methodElement.getSimpleName().toString().equals("onDestroyView")) {
            method.addStatement("this.clearCache();");
        }

        memoizedClass.addMethod(method.build());
    }

    private void propagateMethodWithReturnValue(ExecutableElement methodElement, TypeSpec.Builder memoizedClass) {
        MethodSpec method = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return this.view." + methodElement.getSimpleName() + "()")
                .returns(ClassName.get(methodElement.getReturnType()))
                .build();
        memoizedClass.addMethod(method);
    }

    private boolean isValidClass(TypeElement type) {
        if (type.getKind() != ElementKind.INTERFACE) {
            messager.printMessage(Diagnostic.Kind.ERROR, type.getSimpleName() + " only interfaces can be annotated with Memoized");
            return false;
        }

        if (type.getModifiers().contains(Modifier.PRIVATE)) {
            messager.printMessage(Diagnostic.Kind.ERROR, type.getSimpleName() + " only public classes can be annotated with Log");
            return false;
        }

        return true;
    }
}
