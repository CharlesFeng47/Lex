package exceptions;

/**
 * Created by cuihua on 2017/11/2.
 * <p>
 * 用户要分析的字符串和 .l 正则定义不匹配
 */
public class NotMatchingException extends Exception {
    @Override
    public String getMessage() {
        return "DFA 中无匹配状态";
    }
}
