package utilties;

import finiteAutomata.entity.FA_State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/11/2.
 * <p>
 * 统一控制 DFA 中每个状态与其对应模式列表的映射
 */
public class DFA_StatePatternMappingController {

    private static Map<FA_State, List<String>> map = new HashMap<>();

    private DFA_StatePatternMappingController() {
    }

    public static Map<FA_State, List<String>> getMap() {
        return map;
    }

    /**
     * 对终止态 state 添加对应的模式列表 patterns
     */
    public static boolean add(FA_State state, List<String> patterns) {
        List<String> prePatterns = map.get(state);
        if (prePatterns == null) {
            map.put(state, patterns);
        } else {
            prePatterns.addAll(patterns);
        }
        return true;
    }
}
