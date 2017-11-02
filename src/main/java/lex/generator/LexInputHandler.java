package lex.generator;

import finiteAutomata.FA_Controller;
import finiteAutomata.entity.DFA;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/11/1.
 * <p>
 * 处理 Lex .l 文件中的数据（只含有正则定义）
 */
public class LexInputHandler {

    /**
     * .l 文件的内容（模式 pattern + 正则定义 re）
     */
    private List<String> content;

    /**
     * 模式 与 正则定义 的一一映射
     */
    private Map<String, String> patternREMap;

    /**
     * 模式 与 优先级 的一一映射（整数0表示优先级最高，越优先匹配）
     */
    private Map<String, Integer> patternPriorityMap;

    public LexInputHandler(List<String> content) {
        this.content = content;
        initMap();
    }

    /**
     * 根据 .l 文件初始化映射表
     */
    private void initMap() {
        patternREMap = new HashMap<>();
        patternPriorityMap = new HashMap<>();
        for (int i = 0; i < content.size(); i++) {
            String[] parts = content.get(i).split(" ");
            patternREMap.put(parts[0], parts[1]);
            patternPriorityMap.put(parts[0], i);
        }
    }

    public Map<String, String> getPatternREMap() {
        return patternREMap;
    }

    public Map<String, Integer> getPatternPriorityMap() {
        return patternPriorityMap;
    }

    /**
     * .l 文件内容对应的 DFA
     */
    public DFA convert() {
        // 处理正则定义
        List<String> res = new LinkedList<>(patternREMap.values());
        List<String> patternTypes = new LinkedList<>(patternREMap.keySet());

        FA_Controller controller = new FA_Controller();
        return controller.lexicalAnalysis(res, patternTypes);
    }

}
