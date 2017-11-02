package utilties;

import finiteAutomata.entity.FA_State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuihua on 2017/11/2.
 * <p>
 * 统一控制 NFA 中每个状态与其对应模式的映射
 */
public class NFA_StatePatternMappingController {

    private static Map<FA_State, String> map = new HashMap<>();

    private NFA_StatePatternMappingController() {
    }

    public static Map<FA_State, String> getMap() {
        return map;
    }

    /**
     * 对终止态 state 添加对应的模式 pattern
     */
    public static boolean add(FA_State state, String pattern) {
        map.put(state, pattern);
        return true;
    }
}
