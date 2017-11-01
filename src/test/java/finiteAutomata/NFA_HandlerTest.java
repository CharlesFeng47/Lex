package finiteAutomata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * NFA Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 26, 2017</pre>
 */
public class NFA_HandlerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFromRE(String re)
     */
    @Test
    public void testGetFromRE() throws Exception {
        NFA_Handler handler = new NFA_Handler();
        handler.getFromRE("ab·a|*", "ID");
    }

    /**
     * Method: combine(List<NFA> nfaList)
     */
    @Test
    public void testCombine() throws Exception {
//TODO: Test goes here...
    }


}
