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
public class NFATest {

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
        NFA nfa = new NFA();
//        nfa.getFromRE("ab|*");
        nfa.getFromRE("ab·a|*");
    }

    /**
     * Method: combine(List<NFA> nfaList)
     */
    @Test
    public void testCombine() throws Exception {
//TODO: Test goes here... 
    }


} 
