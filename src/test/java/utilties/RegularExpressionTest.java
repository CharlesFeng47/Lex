package utilties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * RegularExpression Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 25, 2017</pre>
 */
public class RegularExpressionTest {

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
        RegularExpression re = new RegularExpression();

    }

    /**
     * Method: convertInfixToPostfix(String re)
     */
    @Test
    public void testConvertInfixToPostfix() throws Exception {
        RegularExpression re = new RegularExpression();
        System.out.println(re.convertInfixToPostfix("(a|b)*"));
        System.out.println(re.convertInfixToPostfix("(a*|b*)*"));
    }

    @Test
    public void testStandardizeExtendedMark() throws Exception {
        RegularExpression regularExpression = new RegularExpression();

        Method method = RegularExpression.class.getDeclaredMethod("standardizeExtendedMark", String.class, int.class, ExtendedMark.class);
        method.setAccessible(true);
        method.invoke(regularExpression, "cc(ab)+a", 6, ExtendedMark.PLUS_MARK);

        /*
        检测：
        "a?", 1, ExtendedMark.QUESTION_MARK: (ε|a)
        "aba?a", 3, ExtendedMark.QUESTION_MARK: ab(ε|a)a
        "cc(ab)?a", 6, ExtendedMark.QUESTION_MARK: cc(ε|(ab))a
        "aba+a", 3, ExtendedMark.PLUS_MARK: abaa*a
        "cc(ab)+a", 6, ExtendedMark.PLUS_MARK: cc(ab)(ab)*a
         */
    }

} 
