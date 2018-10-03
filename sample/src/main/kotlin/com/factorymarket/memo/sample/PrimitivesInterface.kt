package com.factorymarket.memo.sample

import com.factorymarket.memo.annotations.Memoized

@Memoized
interface PrimitivesInterface {
    fun setInt(value: Int)
    fun setLong(value: Long)
    fun setShort(value: Short)
    fun setFloat(value: Float)
    fun setDouble(value: Double)
    fun setBoolean(value: Boolean)
    fun setCharacter(value: Char)
    fun setByte(value: Byte)

}