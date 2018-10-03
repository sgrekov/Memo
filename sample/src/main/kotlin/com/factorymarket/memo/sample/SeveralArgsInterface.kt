package com.factorymarket.memo.sample

import com.factorymarket.memo.annotations.Memoized

class Foo(val bar : Int)

@Memoized
interface SeveralArgsInterface {

    fun customClass(foo : Foo?)

    fun severalArgs(arg1 : Int, arg2 : String)

}