[![Maven Central](https://img.shields.io/maven-central/v/com.factorymarket.memo/memo-core.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.factorymarket.memo%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-yellowgreen.svg)](https://github.com/FactoryMarketRetailGmbH/RxElm/blob/master/LICENSE)

# Memo
Small annotation processing library for generating memoized variants of interfaces

The main goal of the library is to work in connection with Unidirectional Dataflow libraries, like RxElm, MVI, Mobius, RxRedux, etc.
Because of the nature of this pattern, render function can be call very often, as long as new signals for state arrive.


## Example
Lets say we have some Profile screen in our MVP application and have interface ProfileView
```kotlin
interface ProfileView {
    fun setFirstName(firstName : String)
    fun setLastName(lastName : String)
    fun setEmail(email : String)
}
```

Now, every time now action arrives to our reducers, all methods of implementor of ProfileView will called.
To avoid that, the interface must annotaited with @Memoized annotation, and the ProfileViewMemoized class will be generated.

```java
public final class ProfileViewMemoized extends BaseMemoised implements ProfileView {
  public static final String setFirstName_firstName_key = "setFirstName_firstName";
  public static final String setLastName_lastName_key = "setLastName_lastName";
  public static final String setEmail_email_key = "setEmail_email";
  private ProfileView view;
  
  public ProfileViewMemoized(ProfileView view) {
    this.view = view;
  }

  @Override
  public void setFirstName(final String firstName) {
    if (isStandartObjectDifferent(setFirstName_firstName_key, firstName)) {
      this.view.setFirstName(firstName);
    }
  }

  @Override
  public void setLastName(final String lastName) {
    if (isStandartObjectDifferent(setLastName_lastName_key, lastName)) {
      this.view.setLastName(lastName);
    }
  }

  @Override
  public void setEmail(final String email) {
    if (isStandartObjectDifferent(setEmail_email_key, email)) {
      this.view.setEmail(email);
    }
  }
}
``` 
 
You can use as a view. You need to pass actual implementor to the constructor of generated class:

```kotlin
val actualView : ProfileView = ...
val view = ProfileViewMemoized(actualView)
```

Now, actual view methods will be called only if the argument is changed.

## Diffing algorithm
Diffing algorithm is very simple - it checks arguments by value if they are primitives,
by .equals() method if they are standart Java classes, and by references if they are custom classses.

**Why by references?**  
Since most UDF architectures treat their state/model values as immutable data structure and all new values 
already come to the view layer in the form of new objects.    


## Dependency

```
implementation 'com.factorymarket.memo:memo-core:0.1.0'
kapt 'com.factorymarket.memo:memo-compiler:0.1.0'
```



