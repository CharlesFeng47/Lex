package finiteAutomata.entity;

import finiteAutomata.FA_Controller;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * DFA Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 2, 2017</pre>
 */
public class DFATest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: isValid(String s)
     */
    @Test
    public void testIsValid() throws Exception {
        List<String> res = new LinkedList<>();
        res.add("aa+");

        List<String> patterns = new LinkedList<>();
        patterns.add("ID");

        FA_Controller controller = new FA_Controller();
        DFA dfa = controller.lexicalAnalysis(res, patterns);

        Assert.assertEquals(null, dfa.getEndingPatterns("a"));
        Assert.assertEquals(patterns, dfa.getEndingPatterns("aa"));
        Assert.assertEquals(patterns, dfa.getEndingPatterns("aaa"));
    }

    /**
     * Method: isValid(String s)
     */
    @Test
    public void testIsValid2() throws Exception {
        List<String> res = new LinkedList<>();
        res.add("aa+");
        res.add("b*");


        List<String> patterns = new LinkedList<>();
        patterns.add("ID");
        patterns.add("A");

        FA_Controller controller = new FA_Controller();
        DFA dfa = controller.lexicalAnalysis(res, patterns);

        List<String> result1 = new LinkedList<>();
        result1.add("ID");

        List<String> result2 = new LinkedList<>();
        result2.add("A");

        Assert.assertEquals(null, dfa.getEndingPatterns("a"));
        Assert.assertEquals(result1, dfa.getEndingPatterns("aa"));
        Assert.assertEquals(result1, dfa.getEndingPatterns("aaa"));
        Assert.assertEquals(result2, dfa.getEndingPatterns("b"));
        Assert.assertEquals(result2, dfa.getEndingPatterns("bb"));
        Assert.assertEquals(result2, dfa.getEndingPatterns("bbb"));
    }

}
