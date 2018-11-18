import com.factorymarket.memo.sample.ClientInterface
import com.factorymarket.memo.sample.ClientInterfaceMemoized
import org.junit.Before
import org.junit.Test

class InheritanceTest {


    class ActualClientInterface : ClientInterface {
        override fun child1Method() {

        }

        override fun child2Method() {

        }

        override fun clientMethod() {

        }

        override fun baseMethod() {
        }

    }

    lateinit var actualClientInterface: ActualClientInterface
    lateinit var memoized: ClientInterfaceMemoized

    @Before
    fun setUp() {
        actualClientInterface = ActualClientInterface()
        memoized = ClientInterfaceMemoized(actualClientInterface)
    }

    @Test
    fun customClass() {
        memoized.child1Method()
        memoized.child2Method()
        memoized.clientMethod()
        memoized.baseMethod()
    }
}