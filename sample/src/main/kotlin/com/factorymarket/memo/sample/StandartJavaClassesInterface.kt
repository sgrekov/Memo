package com.factorymarket.memo.sample

import com.factorymarket.memo.annotations.Memoized

enum class TestEnum {
    Foo, Bar
}

@Memoized
interface StandartJavaClassesInterface {

    fun setString(newValue: String?)
    fun setLong(newValue: Long?)
    fun setFloat(newValue: Float?)
    fun setCharacter(newValue: Char?)
    fun setInteger(newValue: Int?)
    fun setShort(newValue: Short?)
    fun setByte(newValue: Byte?)
    fun setBoolean(newValue: Boolean?)
    fun setEnum(newValue: TestEnum?)

}