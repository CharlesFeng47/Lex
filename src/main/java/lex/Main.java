package lex;

import finiteAutomata.entity.DFA;
import lex.generator.LexInputHandler;
import lex.generator.LexInputReader;

/**
 * Created by cuihua on 2017/11/1.
 *
 * 主程序
 * 输入：用户输入程序
 * 输出：根据已有的 .l 文件输出 Token 序列
 */
public class Main {

    public static void main(String[] args) {
        UserInputReader userInputReader = new UserInputReader();
        String lexeme = userInputReader.getUserContent();


        // .l 文件代表的 DFA
        LexInputReader lexInputReader = new LexInputReader();
        LexInputHandler lexInputHandler = new LexInputHandler();
        DFA dfa = lexInputHandler.convert(lexInputReader.readREs());

    }
}
