import com.factorymarket.memo.sample.PrimitivesInterface
import com.factorymarket.memo.sample.PrimitivesInterfaceMemoized
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PrimitivesMemoisationTest {

    class ActualPrimitivesView : PrimitivesInterface {

        var setIntCounter : Int = 0
        var setLongCounter : Int = 0
        var setShortCounter : Int = 0
        var setFloatCounter : Int = 0
        var setDoubleCounter : Int = 0
        var setBooleanCounter : Int = 0
        var setCharacterCounter : Int = 0
        var setByteCounter : Int = 0

        override fun setInt(value: Int) {
            setIntCounter++
        }

        override fun setLong(value: Long) {
            setLongCounter++
        }

        override fun setShort(value: Short) {
            setShortCounter++
        }

        override fun setFloat(value: Float) {
            setFloatCounter++
        }

        override fun setDouble(value: Double) {
            setDoubleCounter++
        }

        override fun setBoolean(value: Boolean) {
            setBooleanCounter++
        }

        override fun setCharacter(value: Char) {
            setCharacterCounter++
        }

        override fun setByte(value: Byte) {
            setByteCounter++
        }

    }

    lateinit var primitivesView : ActualPrimitivesView
    lateinit var memoized : PrimitivesInterface

    @Before
    fun setUp(){
        primitivesView = ActualPrimitivesView()
        memoized = PrimitivesInterfaceMemoized(primitivesView)
    }


    @Test
    fun checkIntPrimitives(){
        Assert.assertEquals(0, primitivesView.setIntCounter)
        memoized.setInt(1)
        memoized.setInt(1)
        memoized.setInt(1)
        Assert.assertEquals(1, primitivesView.setIntCounter)
        memoized.setInt(2)
        memoized.setInt(2)
        Assert.assertEquals(2, primitivesView.setIntCounter)
    }

    @Test
    fun checkLongPrimitives(){
        Assert.assertEquals(0, primitivesView.setLongCounter)
        memoized.setLong(1)
        memoized.setLong(1)
        memoized.setLong(1)
        Assert.assertEquals(1, primitivesView.setLongCounter)
        memoized.setLong(2)
        memoized.setLong(2)
        Assert.assertEquals(2, primitivesView.setLongCounter)
    }

    @Test
    fun checkShortPrimitives(){
        Assert.assertEquals(0, primitivesView.setShortCounter)
        memoized.setShort(1)
        memoized.setShort(1)
        memoized.setShort(1)
        Assert.assertEquals(1, primitivesView.setShortCounter)
        memoized.setShort(2)
        memoized.setShort(2)
        Assert.assertEquals(2, primitivesView.setShortCounter)
    }

    @Test
    fun checkFloatPrimitives(){
        Assert.assertEquals(0, primitivesView.setFloatCounter)
        memoized.setFloat(1.0f)
        memoized.setFloat(1.0f)
        memoized.setFloat(1.0f)
        Assert.assertEquals(1, primitivesView.setFloatCounter)
        memoized.setFloat(2.0f)
        memoized.setFloat(2.0f)
        Assert.assertEquals(2, primitivesView.setFloatCounter)
    }

    @Test
    fun checkDoublePrimitives(){
        Assert.assertEquals(0, primitivesView.setDoubleCounter)
        memoized.setDouble(1.0)
        memoized.setDouble(1.0)
        memoized.setDouble(1.0)
        Assert.assertEquals(1, primitivesView.setDoubleCounter)
        memoized.setDouble(2.0)
        memoized.setDouble(2.0)
        Assert.assertEquals(2, primitivesView.setDoubleCounter)
    }

    @Test
    fun checkBooleanPrimitives(){
        Assert.assertEquals(0, primitivesView.setBooleanCounter)
        memoized.setBoolean(true)
        memoized.setBoolean(true)
        memoized.setBoolean(true)
        Assert.assertEquals(1, primitivesView.setBooleanCounter)
        memoized.setBoolean(false)
        memoized.setBoolean(false)
        Assert.assertEquals(2, primitivesView.setBooleanCounter)
    }

    @Test
    fun checkCharacterPrimitives(){
        Assert.assertEquals(0, primitivesView.setCharacterCounter)
        memoized.setCharacter('f')
        memoized.setCharacter('f')
        memoized.setCharacter('f')
        Assert.assertEquals(1, primitivesView.setCharacterCounter)
        memoized.setCharacter('b')
        memoized.setCharacter('b')
        Assert.assertEquals(2, primitivesView.setCharacterCounter)
    }

    @Test
    fun checkBytePrimitives(){
        Assert.assertEquals(0, primitivesView.setByteCounter)
        memoized.setByte(Byte.MIN_VALUE)
        memoized.setByte(Byte.MIN_VALUE)
        memoized.setByte(Byte.MIN_VALUE)
        Assert.assertEquals(1, primitivesView.setByteCounter)
        memoized.setByte(Byte.MAX_VALUE)
        memoized.setByte(Byte.MAX_VALUE)
        Assert.assertEquals(2, primitivesView.setByteCounter)
    }

}