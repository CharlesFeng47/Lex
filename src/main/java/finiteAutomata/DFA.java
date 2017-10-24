package finiteAutomata;

import utilties.FA_StateComparator;
import utilties.FA_StatesList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * Deterministic FA，确定的有穷自动机
 */
public class DFA extends NFA {

    /**
     * @param nfa 需要转变的NFA
     * @return 与输入NFA一致的DFA
     */
    public DFA getFromNFA(NFA nfa) {
        // DStates为<闭包, 已标记>
        Map<List<FA_State>, Boolean> DStates = new HashMap<>();
        DStates.put(closure(nfa.getStart()), false);

        while (true) {
            // DStates中是否还有未标记的状态，并对未标记的状态进行处理
            boolean hasStopped = true;
            List<FA_State> unhandled = null;
            for (Map.Entry<List<FA_State>, Boolean> entry : DStates.entrySet()) {
                if (!entry.getValue()) {
                    hasStopped = false;
                    entry.setValue(true);
                    unhandled = entry.getKey();
                    break;
                }
            }

            // 循环的终止条件
            if (hasStopped) break;

            // 处理此时的标记
            for (char c : nfa.getAlphabet()) {
                List<FA_State> curFollowing = move(unhandled, c);
                int curFollowingSize = curFollowing.size();

                if (curFollowingSize != 0) {
                    // 遍历后继的核，得到核的闭包
                    // 不采用foreach的形式，因为curFollowing在过程中添加新的后继状态
                    for (int i = 0; i < curFollowingSize; i++) {
                        FA_State tempState = curFollowing.get(i);
                        List<FA_State> tempClosure = closure(tempState);

                        for (FA_State state : tempClosure) {
                            System.out.println(curFollowing.contains(state));
                            if (!curFollowing.contains(state)) curFollowing.add(state);
                        }
                    }

                    curFollowing.sort(new FA_StateComparator());

                    if (!isInDSates(DStates, curFollowing)) {
                        DStates.put(curFollowing, false);
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param dfa 需要被优化的DFA
     * @return 具有最少状态的DFA
     */
    public DFA optimize(DFA dfa) {
        return null;
    }


    /**
     * 计算当前节点的ε闭包 ε-closure
     */
    public List<FA_State> closure(FA_State nowState) {
        List<FA_State> result = new FA_StatesList();
        result.add(nowState);

        // 遍历当前节点的每一个后续节点
        for (FA_Edge tempEdge : nowState.getFollows()) {
            if (tempEdge.getLabel() == 'ε') {
                // 若结果集中不包含此节点，则将此节点加入结果集
                if (!result.contains(tempEdge.getPointTo())) result.addAll(closure(tempEdge.getPointTo()));
            }
        }

        result.sort(new FA_StateComparator());
        return result;
    }

    /**
     * 将此状态以label后移
     */
    private List<FA_State> move(List<FA_State> cur, char label) {
        List<FA_State> result = new FA_StatesList();

        for (FA_State tempState : cur) {
            for (FA_Edge tempEdge : tempState.getFollows()) {
                if (tempEdge.getLabel() == label) {
                    result.add(tempEdge.getPointTo());
                }
            }
        }

        result.sort(new FA_StateComparator());
        return result;
    }

    /**
     * 判断states是否已经在DSates中了
     */
    private boolean isInDSates(Map<List<FA_State>, Boolean> DStates, List<FA_State> states) {
        for (Map.Entry<List<FA_State>, Boolean> entry : DStates.entrySet()) {
            if (entry.getKey().size() == states.size()) {
                for (int i = 0; i < states.size(); i++) {
                    boolean allEqual = true;
                    if (states.get(i).getStateID() != entry.getKey().get(i).getStateID()) allEqual = false;

                    // 找到已经存在的状态
                    if (allEqual) return true;
                }
            }
        }
        return false;
    }
}
