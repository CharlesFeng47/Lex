package lex;

import lex.entity.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by cuihua on 2017/11/1.
 * <p>
 * 与使用词法分析器的用户进行交互
 */
public class UserInteractionController {

    /**
     * 读取用户输入并进行简单处理，返回所有的词素 lexemes
     */
    public List<String> readUserContent() {
        Scanner sc = new Scanner(System.in);

        List<String> lexemes = new LinkedList<>();
        String line;
        while (!(line = sc.nextLine()).equals("###")) {
            String[] parts = line.split(" ");
            for (String lexeme: parts) {
                if (!lexeme.equals(""))
                lexemes.add(lexeme);
            }
        }

        return lexemes;
    }

    /**
     * 向用户展示所有的词法单元结果
     */
    public void showAllTokens(List<Token> tokens) {
        System.out.println("-------------------");
        for (Token token : tokens) {
            System.out.print("<");
            System.out.print(token.getPatternType());
            if (token.getAttribute() != null) {
                System.out.print(", ");
                System.out.print(token.getAttribute());
            }
            System.out.println(">");
        }
        System.out.println("-------------------");
    }
}
