package finiteAutomata;

import utilties.FA_StateComparator;

import java.util.LinkedList;
import java.util.List;

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
        List<FA_State> result = new LinkedList<FA_State>();
        result.add(nowState);

        // 遍历当前节点的每一个后续节点
        for (FA_Edge tempEdge : nowState.getFollows()) {
            if (tempEdge.getLabel() == 'ε'){
                result.addAll(closure(tempEdge.getPointTo()));
            }
        }

        result.sort(new FA_StateComparator());
        return result;
    }
}
