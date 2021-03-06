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
    public void testGetFromRE1() throws Exception {
        NFA_Handler handler = new NFA_Handler();
        handler.getFromRE("ab·a|*", "ID");
    }

    /**
     * Method: getFromRE(String re)
     */
    @Test
    public void testGetFromRE2() throws Exception {
        NFA_Handler handler = new NFA_Handler();
        handler.getFromRE("ab·c·\\{·ε\\{|·ε\\{|·", "ID");
    }

    /**
     * Method: getFromRE(String re)
     */
    @Test
    public void testGetFromRE3() throws Exception {
        NFA_Handler handler = new NFA_Handler();
        handler.getFromRE("\\{a*·b*·c·εc|·εc|·\\}·", "ID");
    }

    /**
     * Method: combine(List<NFA> nfaList)
     */
    @Test
    public void testCombine() throws Exception {
    }


}
