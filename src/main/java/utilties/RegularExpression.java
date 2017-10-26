package utilties;

import exceptions.UnexpectedRegularExprRuleException;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cuihua on 2017/10/25.
 * <p>
 * 输入的正则表达式
 */
public class RegularExpression {

    private List<String> unexpectedRERules;

    public RegularExpression() {
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
    }

    /**
     * 默认re不含有连接符
     * 将 +、? 用基本符号代替
     * 添加省略的连接符'·'（对所有操作符画出所有的可能情况）
     *
     * @param re 输入的正则表达式
     * @return 标准的没有扩展语法如[], +, ?
     */
    public String standardizeRE(final String re) throws UnexpectedRegularExprRuleException {
        // 替换扩展符号，result存储替换后的字符串，differ表示替换前后的对当前处理字符的Index差
        StringBuffer result = new StringBuffer().append(re);
        int differ = 0;
        for (int i = 0; i < re.length() - 1; i++) {
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
     * @param joinIndex 需要在两个字符中间添加连接符号，第一个字符的index
     */
    private StringBuffer standardizeJoinMark(final StringBuffer re, int joinIndex) {
        StringBuffer sb = new StringBuffer();
        sb.append(re.substring(0, joinIndex+1)).append('·').append(re.substring(joinIndex + 1));
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
     * 将正则定义的中缀表达式改为后缀表达式
     * 注：暂只考虑并、或、闭包，括号，问号，加号，未考虑[]等
     */
    public String convertInfixToPostfix(String re) {
        // 存储结果的后缀字符串
        StringBuffer sb = new StringBuffer(re.length());

        // 操作符的栈、元素的栈
        Stack<Character> operandStack = new Stack<>();
        Stack<Character> charStack = new Stack<>();

        for (char c : re.toCharArray()) {
            switch (c) {
                case '(':
                    operandStack.push('(');
                    break;
                case ')':
                    if (operandStack.peek() == '(') {
                        operandStack.pop();
                    }
                    break;
                case '·':
                    handleTwoOperand(operandStack, '·');
                    break;
                case '|':
                    handleTwoOperand(operandStack, '|');
                    break;
                case '*':
                    sb.append(handleClosure(charStack));
                    break;

                default:
                    sb.append(handleCommonChar(operandStack, charStack, c));
                    break;

            }
        }

        return sb.toString();
    }


    /**
     * 处理RE中的二元操作符（连接运算'·'／或运算'|'）
     */
    private void handleTwoOperand(Stack<Character> stack, char operand) {
        // 压栈
        stack.push(operand);
    }

    /**
     * 处理RE中的闭包符号'*'
     */
    private StringBuffer handleClosure(Stack<Character> charStack) {
        // 获取字符栈重的上一个元素，如果栈中没有元素就表示之前存在括号，已处理，直接添加'*'就好
        StringBuffer sb = new StringBuffer();

        if (charStack.empty()) sb.append('*');
        else {
            char preChar = charStack.pop();
            sb.append(preChar).append('*');
        }
        return sb;
    }

    /**
     * 处理RE中的普通符号
     */
    private StringBuffer handleCommonChar(Stack<Character> operandStack, Stack<Character> charStack, char toHandle) {
        // 栈中为空就直接连接'·'，否则需要弹出栈顶元素，连接栈顶元素
        StringBuffer sb = new StringBuffer();
        if (operandStack.empty()) {
            sb.append(toHandle).append('·');
        } else {
            char topOperand = operandStack.peek();
            if (topOperand == '(') {
                charStack.push(toHandle);
            } else {
                char preChar = charStack.pop();
                sb.append(preChar).append(toHandle).append(topOperand);
            }
        }
        return sb;
    }

}
