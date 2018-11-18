package com.factorymarket.memo.sample

import com.factorymarket.memo.annotations.Memoized

interface BaseInterface {

    fun baseMethod()
}

interface Child1Interface : BaseInterface {

    fun child1Method()
}

interface Child2Interface : Child1Interface {

    fun child2Method()
}

@Memoized
interface ClientInterface : Child2Interface {

    fun clientMethod()
}

