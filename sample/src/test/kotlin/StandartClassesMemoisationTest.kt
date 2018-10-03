import com.factorymarket.memo.sample.StandartJavaClassesInterface
import com.factorymarket.memo.sample.StandartJavaClassesInterfaceMemoized
import com.factorymarket.memo.sample.TestEnum
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StandartClassesMemoisationTest {

    class ActuaStandartView : StandartJavaClassesInterface {

        var setStringCounter: Int = 0
        var setLongCounter: Int = 0
        var setIntegerCounter: Int = 0
        var setShortCounter: Int = 0
        var setByteCounter: Int = 0
        var setBooleanCounter: Int = 0
        var setFloatCounter: Int = 0
        var setCharacterCounter: Int = 0
        var setEnumCounter: Int = 0

        override fun setString(newValue: String?) {
            setStringCounter++
        }

        override fun setLong(newValue: Long?) {
            setLongCounter++
        }

        override fun setInteger(newValue: Int?) {
            setIntegerCounter++
        }

        override fun setShort(newValue: Short?) {
            setShortCounter++
        }

        override fun setByte(newValue: Byte?) {
            setByteCounter++
        }

        override fun setBoolean(newValue: Boolean?) {
            setBooleanCounter++
        }

        override fun setFloat(newValue: Float?) {
            setFloatCounter++
        }

        override fun setCharacter(newValue: Char?) {
            setCharacterCounter++
        }

        override fun setEnum(newValue: TestEnum?) {
            setEnumCounter++
        }

    }

    lateinit var actuaStandartView: ActuaStandartView
    lateinit var memoized : StandartJavaClassesInterface

    @Before
    fun setUp() {
        actuaStandartView = ActuaStandartView()
        memoized = StandartJavaClassesInterfaceMemoized(actuaStandartView)
    }

    @Test
    fun string(){
        Assert.assertEquals(0, actuaStandartView.setStringCounter)
        memoized.setString(null)
        Assert.assertEquals(0, actuaStandartView.setStringCounter)
        memoized.setString("foo")
        memoized.setString("foo")
        Assert.assertEquals(1, actuaStandartView.setStringCounter)
        memoized.setString("bar")
        memoized.setString("bar")
        Assert.assertEquals(2, actuaStandartView.setStringCounter)
        memoized.setString(null)
        Assert.assertEquals(3, actuaStandartView.setStringCounter)
    }

    @Test
    fun longObject(){
        Assert.assertEquals(0, actuaStandartView.setLongCounter)
        memoized.setLong(null)
        Assert.assertEquals(0, actuaStandartView.setLongCounter)
        memoized.setLong(1)
        memoized.setLong(1)
        Assert.assertEquals(1, actuaStandartView.setLongCounter)
        memoized.setLong(2)
        memoized.setLong(2)
        Assert.assertEquals(2, actuaStandartView.setLongCounter)
        memoized.setLong(null)
        Assert.assertEquals(3, actuaStandartView.setLongCounter)
    }

    @Test
    fun integerObject(){
        Assert.assertEquals(0, actuaStandartView.setIntegerCounter)
        memoized.setInteger(null)
        Assert.assertEquals(0, actuaStandartView.setIntegerCounter)
        memoized.setInteger(1)
        memoized.setInteger(1)
        Assert.assertEquals(1, actuaStandartView.setIntegerCounter)
        memoized.setInteger(2)
        memoized.setInteger(2)
        Assert.assertEquals(2, actuaStandartView.setIntegerCounter)
        memoized.setInteger(null)
        Assert.assertEquals(3, actuaStandartView.setIntegerCounter)
    }

    @Test
    fun shortObject(){
        Assert.assertEquals(0, actuaStandartView.setShortCounter)
        memoized.setShort(null)
        Assert.assertEquals(0, actuaStandartView.setShortCounter)
        memoized.setShort(1)
        memoized.setShort(1)
        Assert.assertEquals(1, actuaStandartView.setShortCounter)
        memoized.setShort(2)
        memoized.setShort(2)
        Assert.assertEquals(2, actuaStandartView.setShortCounter)
        memoized.setShort(null)
        Assert.assertEquals(3, actuaStandartView.setShortCounter)
    }

    @Test
    fun byteObject(){
        Assert.assertEquals(0, actuaStandartView.setByteCounter)
        memoized.setByte(null)
        Assert.assertEquals(0, actuaStandartView.setByteCounter)
        memoized.setByte(Byte.MIN_VALUE)
        memoized.setByte(Byte.MIN_VALUE)
        Assert.assertEquals(1, actuaStandartView.setByteCounter)
        memoized.setByte(Byte.MAX_VALUE)
        memoized.setByte(Byte.MAX_VALUE)
        Assert.assertEquals(2, actuaStandartView.setByteCounter)
        memoized.setByte(null)
        Assert.assertEquals(3, actuaStandartView.setByteCounter)
    }

    @Test
    fun booleanObject(){
        Assert.assertEquals(0, actuaStandartView.setBooleanCounter)
        memoized.setBoolean(null)
        Assert.assertEquals(0, actuaStandartView.setBooleanCounter)
        memoized.setBoolean(true)
        memoized.setBoolean(true)
        Assert.assertEquals(1, actuaStandartView.setBooleanCounter)
        memoized.setBoolean(false)
        memoized.setBoolean(false)
        Assert.assertEquals(2, actuaStandartView.setBooleanCounter)
        memoized.setBoolean(null)
        Assert.assertEquals(3, actuaStandartView.setBooleanCounter)
    }

    @Test
    fun floatObject(){
        Assert.assertEquals(0, actuaStandartView.setFloatCounter)
        memoized.setFloat(null)
        Assert.assertEquals(0, actuaStandartView.setFloatCounter)
        memoized.setFloat(1.0f)
        memoized.setFloat(1.0f)
        Assert.assertEquals(1, actuaStandartView.setFloatCounter)
        memoized.setFloat(2.0f)
        memoized.setFloat(2.0f)
        Assert.assertEquals(2, actuaStandartView.setFloatCounter)
        memoized.setFloat(null)
        Assert.assertEquals(3, actuaStandartView.setFloatCounter)
    }

    @Test
    fun characterObject(){
        Assert.assertEquals(0, actuaStandartView.setCharacterCounter)
        memoized.setCharacter(null)
        Assert.assertEquals(0, actuaStandartView.setCharacterCounter)
        memoized.setCharacter('f')
        memoized.setCharacter('f')
        Assert.assertEquals(1, actuaStandartView.setCharacterCounter)
        memoized.setCharacter('b')
        memoized.setCharacter('b')
        Assert.assertEquals(2, actuaStandartView.setCharacterCounter)
        memoized.setCharacter(null)
        Assert.assertEquals(3, actuaStandartView.setCharacterCounter)
    }

    @Test
    fun enum(){
        Assert.assertEquals(0, actuaStandartView.setEnumCounter)
        memoized.setEnum(null)
        Assert.assertEquals(0, actuaStandartView.setEnumCounter)
        memoized.setEnum(TestEnum.Bar)
        memoized.setEnum(TestEnum.Bar)
        Assert.assertEquals(1, actuaStandartView.setEnumCounter)
        memoized.setEnum(TestEnum.Foo)
        memoized.setEnum(TestEnum.Foo)
        Assert.assertEquals(2, actuaStandartView.setEnumCounter)
        memoized.setEnum(null)
        Assert.assertEquals(3, actuaStandartView.setEnumCounter)
    }


}