package finiteAutomata;

import exceptions.UnexpectedRegularExprRuleException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
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
        Assert.assertEquals("a·a*", re.standardizeRE("a+"));
        Assert.assertEquals("a·b·(ε|a)·a·a*·a·b·b·b*·c·c", re.standardizeRE("aba?a+abb+cc"));
        Assert.assertEquals("(a|b)·(a|b)*", re.standardizeRE("(a|b)+"));
        Assert.assertEquals("(a*|b*)*", re.standardizeRE("(a*|b*)*"));
        Assert.assertEquals("((ε|a)·b*)*", re.standardizeRE("((ε|a)b*)*"));
        Assert.assertEquals("(a|b)*·a·b·b·(a|b)*", re.standardizeRE("(a|b)*abb(a|b)*"));

        Assert.assertEquals("c·c·(a·b)·(a·b)·(ε|(a·b))·a·a·a", re.standardizeRE("cc(ab){2, 3}aaa"));
        Assert.assertEquals("c·c·c·c*·a·a·a", re.standardizeRE("cc{2, }aaa"));
        Assert.assertEquals("c·c·(ε|(a·b))·(ε|(a·b))·(ε|(a·b))·a·a·a", re.standardizeRE("cc(ab){, 3}aaa"));
        Assert.assertEquals("c·c·c·a·a·a", re.standardizeRE("cc{2}aaa"));
        Assert.assertEquals("c·(ε|c)·(ε|c)·(ε|c)·a·a·a", re.standardizeRE("cc{, 3}aaa"));

        Assert.assertEquals("c·(ε|c)·(ε|c)·(ε|(ε|c))·a·a·a", re.standardizeRE("c·(ε|c)·(ε|c){1,  2}aa·a"));
        Assert.assertEquals("c·(ε|a)·(ε|a)·(ε|a)·(ε|c)·(ε|(ε|c))·a·a·a", re.standardizeRE("ca{0,3}·(ε|c){1,  2}aa·a"));
        Assert.assertEquals("c·c·(a·b)·(ε|((0|1|2|3|4|5|6|7|8|9)|(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)))·a·a·a",
                re.standardizeRE("cc(ab)[0-9a-z]?aaa"));
        Assert.assertEquals("c·c·(a·b)·((0|1|2|3|4|5|6|7|8|9)|(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z))·((0|1|2|3|4|5|6|7|8|9)|(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z))*·a·a·a",
                re.standardizeRE("cc(ab)[0-9a-z]+aaa"));
        Assert.assertEquals("c·c·(a·b|(c·d)*)·(a·b|(c·d)*)·(ε|(a·b|(c·d)*))·a·a·a", re.standardizeRE("cc(ab|(cd)*){2,3}aaa"));
        Assert.assertEquals("c·c·(a·b)·(a|b|c)·a·a·a", re.standardizeRE("cc(ab)[abc]aaa"));
        Assert.assertEquals("c·c·(a·b)·(a|b|(c|d|e|f)|x|y)·a·a·a", re.standardizeRE("cc(ab)[abc-fxy]aaa"));
    }

    /**
     * Method: standardizeRE(String re)
     */
    @Test(expected = UnexpectedRegularExprRuleException.class)
    public void testStandardizeRE2() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();
//        logger.debug(re.standardizeRE("aa(*)") + "\n");
        re.standardizeRE("aa[");
    }

    /**
     * Method: standardizeRE(String re)
     */
    @Test
    public void testStandardizeRE3() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();

        Assert.assertEquals("a·a·\\|", re.standardizeRE("aa\\|"));
        Assert.assertEquals("a·a·\\{", re.standardizeRE("aa\\{"));
        Assert.assertEquals("a·a·\\{·\\}", re.standardizeRE("aa\\{\\}"));
        Assert.assertEquals("a·a·\\+", re.standardizeRE("aa\\+"));
        Assert.assertEquals("a·a·\\?", re.standardizeRE("aa\\?"));
        Assert.assertEquals("a·a·\\[", re.standardizeRE("aa\\["));
        Assert.assertEquals("a·a·\\[·\\]", re.standardizeRE("aa\\[\\]"));
        Assert.assertEquals("a·a·\\-", re.standardizeRE("aa\\-"));
        Assert.assertEquals("a·b·c·\\{·(ε|\\{)·(ε|\\{)", re.standardizeRE("abc\\{{1,3}"));
        Assert.assertEquals("a·b·\\[·0·\\-·9·\\]", re.standardizeRE("ab\\[0\\-9\\]"));

        // TODO 对输入的RE进行报错
//        Assert.assertEquals("", re.standardizeRE("ab\\[0-9\\]"));
    }

    /**
     * Method: convertInfixToPostfix(String re)
     */
    @Test
    public void testConvertInfixToPostfix() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();

        Assert.assertEquals("ab|*", re.convertInfixToPostfix("(a|b)*"));
        Assert.assertEquals("a*b*|*", re.convertInfixToPostfix("(a*|b*)*"));
        Assert.assertEquals("εa|b*·*", re.convertInfixToPostfix("((ε|a)·b*)*"));
        Assert.assertEquals("ab|*a·b·b·ab|*·", re.convertInfixToPostfix("(a|b)*·a·b·b·(a|b)*"));
        Assert.assertEquals("ab·εa|·a·a*·a·b·b·b*·c·c·", re.convertInfixToPostfix("a·b·(ε|a)·a·a*·a·b·b·b*·c·c"));

        Assert.assertEquals("cc·ab··ab··εab·|·a·a·a·", re.convertInfixToPostfix("c·c·(a·b)·(a·b)·(ε|(a·b))·a·a·a"));
        Assert.assertEquals("cc·c·c*·a·a·a·", re.convertInfixToPostfix("c·c·c·c*·a·a·a"));
        Assert.assertEquals("cc·εab·|·εab·|·εab·|·a·a·a·", re.convertInfixToPostfix("c·c·(ε|(a·b))·(ε|(a·b))·(ε|(a·b))·a·a·a"));
        Assert.assertEquals("cc·c·a·a·a·", re.convertInfixToPostfix("c·c·c·a·a·a"));
        Assert.assertEquals("cεc|·εc|·εc|·a·a·a·", re.convertInfixToPostfix("c·(ε|c)·(ε|c)·(ε|c)·a·a·a"));

    }

    /**
     * Method: convertInfixToPostfix(String re)
     */
    @Test
    public void testConvertInfixToPostfix2() throws Exception {
        RegularExpressionHandler re = new RegularExpressionHandler();
        Assert.assertEquals("\\,;|", re.convertInfixToPostfix("\\,|;"));
        Assert.assertEquals("ab·c·\\{·ε\\{|·ε\\{|·", re.convertInfixToPostfix("a·b·c·\\{·(ε|\\{)·(ε|\\{)"));
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