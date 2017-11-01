package lex;

/**
 * Created by cuihua on 2017/11/1.
 *
 * 主程序
 * 输入：用户输入程序
 * 输出：根据已有的 .l 文件输出 Token 序列
 */
public class Main {

    public static void main(String[] args) {
        UserInputReader reader = new UserInputReader();
        String lexeme = reader.getUserContent();
    }
}
