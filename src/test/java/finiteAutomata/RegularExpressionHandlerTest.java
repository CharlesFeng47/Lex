package finiteAutomata;

import exceptions.UnexpectedRegularExprRuleException;
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

        logger.debug(re.standardizeRE("cc(ab){2, 3}aaa") + "\n");
        logger.debug(re.standardizeRE("cc{2, }aaa") + "\n");
        logger.debug(re.standardizeRE("cc(ab){, 3}aaa") + "\n");
        logger.debug(re.standardizeRE("cc{2}aaa") + "\n");
        logger.debug(re.standardizeRE("cc{, 3}aaa") + "\n");

        logger.debug(re.standardizeRE("c·(ε|c)·(ε|c){1,  2}aa·a") + "\n");
        logger.debug(re.standardizeRE("ca{0,3}·(ε|c){1,  2}aa·a") + "\n");
        logger.debug(re.standardizeRE("cc(ab)[0-9a-z]?aaa") + "\n");
        logger.debug(re.standardizeRE("cc(ab)[0-9a-z]+aaa") + "\n");
        logger.debug(re.standardizeRE("cc(ab|(cd)*){2,3}aaa") + "\n");
        logger.debug(re.standardizeRE("cc(ab)[abc]aaa") + "\n");
        logger.debug(re.standardizeRE("cc(ab)[abc-fxy]aaa") + "\n");
    }

    /**
     * Method: standardizeRE(String re)
     */
    @Test(expected = UnexpectedRegularExprRuleException.class)
    public void testStandardizeRE2() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();
        logger.debug(re.standardizeRE("aa(*)") + "\n");
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

        logger.debug(re.convertInfixToPostfix("c·c·(a·b)·(a·b)·(ε|(a·b))·a·a·a") + "\n");
        logger.debug(re.convertInfixToPostfix("c·c·c·c*·a·a·a") + "\n");
        logger.debug(re.convertInfixToPostfix("c·c·(ε|(a·b))·(ε|(a·b))·(ε|(a·b))·a·a·a") + "\n");
        logger.debug(re.convertInfixToPostfix(" c·c·c·a·a·a") + "\n");
        logger.debug(re.convertInfixToPostfix("c·(ε|c)·(ε|c)·(ε|c)·a·a·a") + "\n");

        // TODO 只支持数字字母
//        logger.debug(re.convertInfixToPostfix(",|;") + "\n");

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

    @Test
    public void testStandardizeExtendedMark2() throws Exception {
        RegularExpressionHandler regularExpressionHandler = new RegularExpressionHandler();

        Method method = RegularExpressionHandler.class.getDeclaredMethod("standardizeExtendedMark", StringBuffer.class, int.class, ExtendedMark.class);
        method.setAccessible(true);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cc(ab){,3}aaa");

        method.invoke(regularExpressionHandler, stringBuffer, 6, ExtendedMark.BRACE_MARK);

        /*
        检测：
        "cc(ab){2,3}aaa", 6, ExtendedMark.PLUS_MARK: cc(ab)(ab)(ε|(ab))aaa
        "cc{2, 3}aaa", 2, ExtendedMark.PLUS_MARK: ccc(ε|c)aaa

        "cc(ab){2}aaa", 6, ExtendedMark.PLUS_MARK: cc(ab)(ab)aaa
        "cc{2}aaa", 2, ExtendedMark.PLUS_MARK: cccaaa

        "cc(ab){2,}aaa", 6, ExtendedMark.PLUS_MARK: cc(ab)(ab)(ab)*aaa
        "cc{2,}aaa", 2, ExtendedMark.PLUS_MARK: cccc*aaa

        "cc(ab){,3}aaa", 6, ExtendedMark.PLUS_MARK: cc(ε|(ab))(ε|(ab))(ε|(ab))aaa
        "cc{,3}aaa", 2, ExtendedMark.PLUS_MARK: c(ε|c)(ε|c)(ε|c)aaa

         */
    }

    @Test
    public void testStandardizeSquareBracketMark() throws Exception {
        RegularExpressionHandler regularExpressionHandler = new RegularExpressionHandler();

        Method method = RegularExpressionHandler.class.getDeclaredMethod("standardizeSquareBracketMark", StringBuffer.class, int.class);
        method.setAccessible(true);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cc(ab)[0-9a-z]aaa");

        method.invoke(regularExpressionHandler, stringBuffer, 6);
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