import com.factorymarket.memo.sample.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NonStandartMemoisationTest {

    class ActualSeveralArgsInterface : SeveralArgsInterface {

        var setCustomClassCounter: Int = 0
        var setSeveralArgsCounter: Int = 0

        override fun customClass(foo: Foo?) {
            setCustomClassCounter++
        }

        override fun severalArgs(arg1: Int, arg2: String) {
            setSeveralArgsCounter++
        }

    }

    lateinit var actualSeveralArgsInterface: ActualSeveralArgsInterface
    lateinit var memoized: SeveralArgsInterface

    @Before
    fun setUp() {
        actualSeveralArgsInterface= ActualSeveralArgsInterface()
        memoized = SeveralArgsInterfaceMemoized(actualSeveralArgsInterface)
    }

    /*
    Custom objects are compared by references
     */
    @Test
    fun customClass() {
        Assert.assertEquals(0, actualSeveralArgsInterface.setCustomClassCounter)
        memoized.customClass(Foo(1))
        Assert.assertEquals(1, actualSeveralArgsInterface.setCustomClassCounter)
        memoized.customClass(Foo(1))
        memoized.customClass(Foo(1))
        Assert.assertEquals(3, actualSeveralArgsInterface.setCustomClassCounter)
        val foo = Foo(1)
        memoized.customClass(foo)
        memoized.customClass(foo)
        Assert.assertEquals(4, actualSeveralArgsInterface.setCustomClassCounter)
        memoized.customClass(null)
        Assert.assertEquals(5, actualSeveralArgsInterface.setCustomClassCounter)
    }

    @Test
    fun severalArgs(){
        Assert.assertEquals(0, actualSeveralArgsInterface.setSeveralArgsCounter)
        memoized.severalArgs(1, "foo")
        Assert.assertEquals(1, actualSeveralArgsInterface.setSeveralArgsCounter)
        memoized.severalArgs(1, "foo")
        Assert.assertEquals(1, actualSeveralArgsInterface.setSeveralArgsCounter)
        memoized.severalArgs(2, "foo")
        memoized.severalArgs(2, "foo2")
        Assert.assertEquals(3, actualSeveralArgsInterface.setSeveralArgsCounter)
        memoized.severalArgs(2, "foo2")
        Assert.assertEquals(3, actualSeveralArgsInterface.setSeveralArgsCounter)
    }
}