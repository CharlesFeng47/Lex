package finiteAutomata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * FA_Controller Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 27, 2017</pre>
 */
public class FA_ControllerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: lexicalAnalysis(List<String> res)
     */
    @Test
    public void testLexicalAnalysis() throws Exception {
        FA_Controller controller = new FA_Controller();

        List<String> res = new LinkedList<>();
        res.add("a+");
        res.add("a*|b*");
        res.add("(a|b)*");
        res.add("(a*|b*)*");
        res.add("((ε|a)b*)*");
        res.add("(a|b)*abb(a|b)*");
        res.add("aba?a+abb+cc");
//        res.add("");
//        res.add("");
//        res.add("");

        controller.lexicalAnalysis(res);

    }


} 
