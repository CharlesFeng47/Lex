package finiteAutomata;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilties.ExtendedMark;

import java.lang.reflect.Method;

/**
 * RegularExpressionHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 25, 2017</pre>
 */
public class RegularExpressionHandlerTest {

    private static final Logger logger = Logger.getLogger(RegularExpressionHandlerTest.class);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: standardizeRE(String re)
     */
    @Test
    public void testStandardizeRE() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();
        logger.debug(re.standardizeRE("a+") + "\n");
        logger.debug(re.standardizeRE("aba?a+abb+cc") + "\n");
        logger.debug(re.standardizeRE("(a|b)+") + "\n");
        logger.debug(re.standardizeRE("(a*|b*)*") + "\n");
        logger.debug(re.standardizeRE("((ε|a)b*)*") + "\n");
        logger.debug(re.standardizeRE("(a|b)*abb(a|b)*") + "\n");
        logger.debug(re.standardizeRE(",|;") + "\n");

    }

    /**
     * Method: convertInfixToPostfix(String re)
     */
    @Test
    public void testConvertInfixToPostfix() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();
        logger.debug(re.convertInfixToPostfix("(a|b)*") + "\n");
        logger.debug(re.convertInfixToPostfix("(a*|b*)*") + "\n");
        logger.debug(re.convertInfixToPostfix("((ε|a)·b*)*") + "\n");
        logger.debug(re.convertInfixToPostfix("(a|b)*·a·b·b·(a|b)*") + "\n");
        logger.debug(re.convertInfixToPostfix("a·b·(ε|a)·a·a*·a·b·b·b*·c·c") + "\n");
        logger.debug(re.convertInfixToPostfix(",|;") + "\n");

        /*
        ab|*a·b·b·ab|*·
        ab·εa|·a·a*·a·b·b·b*·c·c·
         */
    }

    @Test
    public void testStandardizeExtendedMark() throws Exception {
        RegularExpressionHandler regularExpressionHandler = new RegularExpressionHandler();

        Method method = RegularExpressionHandler.class.getDeclaredMethod("standardizeExtendedMark", StringBuffer.class, int.class, ExtendedMark.class);
        method.setAccessible(true);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cc(ab)+aaa");

        method.invoke(regularExpressionHandler, stringBuffer, 6, ExtendedMark.PLUS_MARK);

        /*
        检测：
        "a?", 1, ExtendedMark.QUESTION_MARK: (ε|a)
        "aba?a", 3, ExtendedMark.QUESTION_MARK: ab(ε|a)a
        "cc(ab)?a", 6, ExtendedMark.QUESTION_MARK: cc(ε|(ab))a
        "aba+a", 3, ExtendedMark.PLUS_MARK: abaa*a
        "cc(ab)+aaa", 6, ExtendedMark.PLUS_MARK: cc(ab)(ab)*aaa
         */
    }


    /**
     * Method: comparePriority(char curChar, char top)
     */
    @Test
    public void testComparePriority() throws Exception {

        try {
            Method method = RegularExpressionHandler.class.getDeclaredMethod("comparePriority", char.class, char.class);
            method.setAccessible(true);
            method.invoke(new RegularExpressionHandler(), '*', '*');

            /*
            测试：
            '|', '*':true
            '·', '*':true
            '·', '(':false
            '*', '*':true
             */
        } catch (NoSuchMethodException e) {
        }
    }
} 