package lex;

import exceptions.NotMatchingException;
import finiteAutomata.entity.DFA;
import lex.entity.Token;
import lex.generator.LexInputHandler;
import lex.generator.LexInputReader;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cuihua on 2017/11/1.
 * <p>
 * 主程序
 * 输入：用户输入程序
 * 输出：根据已有的 .l 文件输出 Token 序列
 */
public class Main {

    public static void main(String[] args) throws NotMatchingException {
        UserInteractionController userInteractionController = new UserInteractionController();
        List<String> lexemes = userInteractionController.readUserContent();


        // 解析 .l 文件代表的 DFA
        LexInputReader lexInputReader = new LexInputReader();
        List<String> lexContent = lexInputReader.readREs();

        LexInputHandler lexInputHandler = new LexInputHandler(lexContent);
        List<DFA> allDFAs = lexInputHandler.convert();

        // 生成词法分析器
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(allDFAs);
        List<Token> resultTokens = new LinkedList<>();
        for (String lexeme : lexemes) {
            resultTokens.add(lexicalAnalyzer.analyze(lexeme));
        }

        userInteractionController.showAllTokens(resultTokens);

    }
}
