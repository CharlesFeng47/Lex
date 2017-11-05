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
    public void testIsValid1() throws Exception {
        List<String> res = new LinkedList<>();
        res.add("aa+");

        List<String> patterns = new LinkedList<>();
        patterns.add("ID");

        FA_Controller controller = new FA_Controller();
        DFA dfa = controller.lexicalAnalysis(res, patterns).get(0);

        Assert.assertEquals(true, dfa.isValid("aa"));
        Assert.assertEquals(true, dfa.isValid("aaa"));
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
        List<DFA> dfas = controller.lexicalAnalysis(res, patterns);
        DFA dfa1 = dfas.get(0);
        DFA dfa2 = dfas.get(1);

        Assert.assertEquals(true, dfa1.isValid("aa"));
        Assert.assertEquals(true, dfa1.isValid("aaa"));
        Assert.assertEquals(false, dfa1.isValid("b"));
        Assert.assertEquals(true, dfa2.isValid("b"));
        Assert.assertEquals(true, dfa2.isValid("bb"));
        Assert.assertEquals(true, dfa2.isValid("bbb"));
    }

    /**
     * Method: isValid(String s)
     */
    @Test
    public void testIsValid3() throws Exception {
        List<String> res = new LinkedList<>();
        res.add("aa+");


        List<String> patterns = new LinkedList<>();
        patterns.add("ID");

        FA_Controller controller = new FA_Controller();
        DFA dfa = controller.lexicalAnalysis(res, patterns).get(0);

        Assert.assertEquals(false, dfa.isValid("a"));
    }

    /**
     * Method: isValid(String s)
     */
    @Test
    public void testIsValid4() throws Exception {
        List<String> res = new LinkedList<>();
        res.add("aa+");


        List<String> patterns = new LinkedList<>();
        patterns.add("ID");

        FA_Controller controller = new FA_Controller();
        DFA dfa = controller.lexicalAnalysis(res, patterns).get(0);

        Assert.assertEquals(false, dfa.isValid("b"));
    }

}
