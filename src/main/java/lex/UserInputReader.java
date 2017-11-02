package lex;

import java.util.Scanner;

/**
 * Created by cuihua on 2017/11/1.
 *
 * 读取用户输入并进行简单处理，返回所有的词素 lexeme
 */
public class UserInputReader {

    public String getUserContent(){
        Scanner sc = new Scanner(System.in);

        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).equals("###")) {
            line = line.trim();
            sb.append(line + " ");
        }

        System.out.println(sb.toString());
        return sb.toString();
    }
}
