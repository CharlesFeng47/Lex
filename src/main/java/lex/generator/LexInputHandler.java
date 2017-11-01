package lex.generator;

import finiteAutomata.FA_Controller;
import finiteAutomata.entity.DFA;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cuihua on 2017/11/1.
 * <p>
 * 处理 Lex .l 文件中的数据
 */
public class LexInputHandler {

    /**
     * .l 文件内容对应的 DFA
     * TODO 常量是否需要。。
     */
    public DFA convert(List<String> content) {
        int constantStartIndex = content.indexOf("%{");
        int constantEndIndex = content.indexOf("}%");

        // 处理常量


        // 处理正则定义
        List<String> res = new LinkedList<>();
        List<String> patternType = new LinkedList<>();

        for (int i = constantEndIndex + 1; i < content.size(); i++) {
            String[] parts = content.get(i).split(" ");
            res.add(parts[0]);
            patternType.add(parts[1]);
        }

        FA_Controller controller = new FA_Controller();
        return controller.lexicalAnalysis(res, patternType);
    }

}
