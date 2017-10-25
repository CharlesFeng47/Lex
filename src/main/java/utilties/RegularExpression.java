package utilties;

import java.util.Stack;

/**
 * Created by cuihua on 2017/10/25.
 * <p>
 * 输入的正则表达式
 */
public class RegularExpression {

    /**
     * 添加省略的连接符'·'，将 +、? 用基本符号代替
     *
     * @param re 输入的正则表达式
     * @return 标准的没有扩展语法如[], +, ?
     */
    public String standardizeRE(String re) {
        // 替换扩展符号
        for (int i = 0; i < re.length() - 1; i++) {
            char c = re.charAt(i);
            if (c == '?') re = standardizeExtendedMark(re, i, ExtendedMark.QUESTION_MARK);
            if (c == '+') re = standardizeExtendedMark(re, i, ExtendedMark.PLUS_MARK);
        }

        StringBuffer result = new StringBuffer();

        // 补充连接符
        for (int i = 0; i < re.length() - 2; ) {
            char before = re.charAt(i);
            char after = re.charAt(i + 1);
            if (before == ')' && after == '(') {
                result.append(')').append('·').append('(');
                i += 2;
            }

        }

        return re;
    }

    /**
     * 处理特殊符号（+／?）
     */
    private String standardizeExtendedMark(final String re, int markIndex, ExtendedMark mark) {
        StringBuffer result = new StringBuffer();

        // ? 前面是括号，需要找到核
        String content;
        int contentStartIndex;

        if (re.charAt(markIndex - 1) == ')') {
            String pre = re.substring(0, markIndex);
            contentStartIndex = re.lastIndexOf('(');
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
        if (markIndex != re.length() - 1) result.append(re.charAt(markIndex + 1));
        System.out.println(result);
        return result.toString();
    }

    /**
     * 将正则定义的中缀表达式改为后缀表达式
     * 注：暂只考虑并、或、闭包，括号，未考虑[]，+，?等
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
                case '+':
                    break;
                case '?':
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
