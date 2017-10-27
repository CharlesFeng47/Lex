package finiteAutomata;

import exceptions.UnexpectedRegularExprRuleException;
import utilties.ExtendedMark;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cuihua on 2017/10/25.
 * <p>
 * 输入的正则表达式
 */
public class RegularExpressionHandler {

    /**
     * 不可能存在的正则定义
     */
    private static List<String> unexpectedRERules;

    /**
     * 标准化的正则表达式中优先级序列
     * 优先级越高，越靠后
     */
    private static List<Character> priority;

    public RegularExpressionHandler() {
        unexpectedRERules = new LinkedList<>();
        unexpectedRERules.add("(*");
        unexpectedRERules.add("(|");
        unexpectedRERules.add("|)");
        unexpectedRERules.add("|*");
        unexpectedRERules.add("||");
        unexpectedRERules.add("·)");
        unexpectedRERules.add("·*");
        unexpectedRERules.add("·|");
        unexpectedRERules.add("(·");
        unexpectedRERules.add("|·");
        unexpectedRERules.add("··");

        priority = new LinkedList<>();
        priority.add(0, '(');
        priority.add(1, '·');
        priority.add(2, '|');
        priority.add(3, '*');
    }

    /**
     * 默认re不含有连接符
     * 将 +、? 用基本符号代替
     * 添加省略的连接符'·'（对所有操作符画出所有的可能情况）
     * TODO 增加对 {m, n} 的实现
     *
     * @param re 输入的正则表达式
     * @return 标准的没有扩展语法如[], +, ?
     */
    public String standardizeRE(final String re) throws UnexpectedRegularExprRuleException {
        // 替换扩展符号，result存储替换后的字符串，differ表示替换前后的对当前处理字符的Index差
        StringBuffer result = new StringBuffer().append(re);
        int differ = 0;
        for (int i = 0; i < re.length(); i++) {
            char c = re.charAt(i);
            if (c == '?') {
                int preLength = result.length();
                result = standardizeExtendedMark(result, i + differ, ExtendedMark.QUESTION_MARK);
                differ += result.length() - preLength;
            }
            if (c == '+') {
                int preLength = result.length();
                result = standardizeExtendedMark(result, i + differ, ExtendedMark.PLUS_MARK);
                differ += result.length() - preLength;
            }
        }

        // 补充连接符，joinCount表示连接前后的对当前处理字符的Index差
        String tempResult = result.toString();
        int joinCount = 0;
        for (int i = 0; i < tempResult.length() - 1; i++) {
            char before = tempResult.charAt(i);
            char after = tempResult.charAt(i + 1);

            // 输入RE不合法
            String temp = before + "" + after;
            if (unexpectedRERules.contains(temp)) {
                throw new UnexpectedRegularExprRuleException(temp);
            }

            // 合法情况下含有连接符号的都不需要处理
            if (before == '·' || after == '·') {
                continue;
            }

            if (after == '(' || isValidChar(after)) {
                if (before == ')' || before == '*' || isValidChar(before)) {
                    result = standardizeJoinMark(result, i + joinCount);
                    joinCount++;
                }
            }
        }
        return result.toString();
    }

    /**
     * 处理特殊符号（+／?）
     */
    private StringBuffer standardizeExtendedMark(final StringBuffer re, int markIndex, ExtendedMark mark) {
        StringBuffer result = new StringBuffer();

        // ? 前面是括号，需要找到核
        String content;
        int contentStartIndex;

        if (re.charAt(markIndex - 1) == ')') {
            // 核为非单字符
            contentStartIndex = re.lastIndexOf("(");
            content = re.substring(contentStartIndex, markIndex);
        } else {
            // 核直接是前面的单个字符
            contentStartIndex = markIndex - 1;
            content = String.valueOf(re.charAt(markIndex - 1));
        }

        result.append(re.substring(0, contentStartIndex));

        if (mark == ExtendedMark.QUESTION_MARK) result.append("(ε|").append(content).append(')');
        else if (mark == ExtendedMark.PLUS_MARK) result.append(content).append(content).append('*');

        // 如果 ? 不是最后一个字符，加上后续字符
        if (markIndex != re.length() - 1) result.append(re.substring(markIndex + 1));
        System.out.println(result);
        return result;
    }

    /**
     * 增加省略的连接符（·）
     *
     * @param joinIndex 需要在两个字符中间添加连接符号，第一个字符的index
     */
    private StringBuffer standardizeJoinMark(final StringBuffer re, int joinIndex) {
        StringBuffer sb = new StringBuffer();
        sb.append(re.substring(0, joinIndex + 1)).append('·').append(re.substring(joinIndex + 1));
        return sb;
    }

    /**
     * 判断re中的输入字符是否合法
     * 只支持数字和字母
     */
    private boolean isValidChar(char c) {
        if (Character.isDigit(c)) return true;
        if (Character.isLetter(c)) return true;
        return false;
    }


    /**
     * 将标准化后的正则定义的中缀表达式改为后缀表达式
     * 注：暂只考虑并、或、闭包，括号
     */
    public String convertInfixToPostfix(String re) {
        // 存储结果的后缀字符串
        StringBuffer sb = new StringBuffer(re.length());

        // 操作符的栈
        Stack<Character> operandStack = new Stack<>();

        for (char c : re.toCharArray()) {
            // 非操作符
            if (isValidChar(c)) {
                sb.append(c);
                continue;
            }

            // 操作符
            if (c == '(') operandStack.push('(');
            else if (c == ')') {
                // 退栈至匹配的'('
                char top;
                while ((top = operandStack.pop()) != '(') {
                    sb.append(top);
                }
            } else {
                if (!operandStack.empty()) {
                    char top = operandStack.peek();

                    while (true) {
                        // 退栈高优先级的操作符，最后再压栈当前操作符
                        // 没有优先级更高的操作符时跳出
                        if (comparePriority(c, top)){
                            operandStack.pop();
                            sb.append(top);
                        } else break;

                        // 操作栈不为空时继续比较，否则跳出
                        if (!operandStack.empty()) {
                            top = operandStack.peek();
                        } else break;
                    }

                    operandStack.push(c);
                } else {
                    // 操作符栈中之前无堆栈，将此操作符压栈
                    operandStack.push(c);
                }

            }
        }

        // 栈中剩余操作符
        while (!operandStack.empty()) {
            char top = operandStack.pop();
            sb.append(top);
        }

        return sb.toString();
    }


    /**
     * @param curChar 当前读取的操作符
     * @param top     当前符号栈的栈顶操作符
     * @return true 如果 curChar 优先级小于等于 top 优先级，top 需要被弹出。false otherwise
     */
    private boolean comparePriority(char curChar, char top) {
        int curCharIndex = priority.indexOf(curChar);
        int topCharIndex = priority.indexOf(top);
        System.out.println((curCharIndex - topCharIndex) <= 0);
        return (curCharIndex - topCharIndex) <= 0;
    }

}
