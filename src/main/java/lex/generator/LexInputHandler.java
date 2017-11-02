package lex.generator;

import finiteAutomata.FA_Controller;
import finiteAutomata.entity.DFA;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cuihua on 2017/11/1.
 * <p>
 * 处理 Lex .l 文件中的数据（只含有正则定义）
 */
public class LexInputHandler {

    /**
     * .l 文件内容对应的 DFA
     */
    public DFA convert(List<String> content) {
        // 处理正则定义
        List<String> res = new LinkedList<>();
        List<String> patternTypes = new LinkedList<>();

        for (int i = 0; i < content.size(); i++) {
            String[] parts = content.get(i).split(" ");
            res.add(parts[0]);
            patternTypes.add(parts[1]);
        }

        FA_Controller controller = new FA_Controller();
        return controller.lexicalAnalysis(res, patternTypes);
    }

}
