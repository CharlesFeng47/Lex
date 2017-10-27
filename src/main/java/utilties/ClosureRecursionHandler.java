package utilties;

import finiteAutomata.entity.FA_State;

import java.util.List;

/**
 * Created by cuihua on 2017/10/27.
 * <p>
 * 解决递归 closure 时循环处理的问题
 */
public class ClosureRecursionHandler {

    private static List<FA_State> states = new FA_StatesList();
    private static FA_StateComparator comparator = new FA_StateComparator();

    private ClosureRecursionHandler() {
    }

    /**
     * 清理当前处理的现场
     */
    public static void reset() {
        states = new FA_StatesList();
    }

    /**
     * 增加一个 state，需保证整个 list 是排好序的，才能被复写的二分法找到
     */
    public static void addState(FA_State state) {
        states.add(state);
        states.sort(comparator);
    }

    /**
     * 增加一堆 state list，需保证整个 list 是排好序的，才能被复写的二分法找到
     */
    public static void addAllState(List<FA_State> newStates) {
        states.addAll(newStates);
        states.sort(comparator);
    }

    /**
     * 检测 states 中是否含有参数 state
     */
    public static boolean contain(FA_State state) {
        System.out.println("State " + state.getStateID() + " is contained: " + states.contains(state));
        return states.contains(state);
    }
}
