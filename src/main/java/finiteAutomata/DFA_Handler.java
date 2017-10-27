package finiteAutomata;

import finiteAutomata.entity.*;
import finiteAutomata.entity.DTran;
import utilties.ClosureRecursionHandler;
import utilties.FA_StateComparator;
import utilties.FA_StatesList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/10/27.
 *
 * 对 NFA 进行处理
 * NFA => DFA
 * optimize DFA
 */
public class DFA_Handler {

    /**
     * @param nfa 需要转变的NFA
     * @return 与输入NFA一致的DFA
     */
    public DFA getFromNFA(NFA nfa) {
        List<DTran> dTrans = new LinkedList<>();

        // dStates为<闭包, 已标记>
        Map<List<FA_State>, Boolean> dStates = new HashMap<>();
        dStates.put(closure(nfa.getStart()), false);

        // 清理当前节点计算 closure 时的递归现场
        ClosureRecursionHandler.reset();

        while (true) {
            // dStates中是否还有未标记的状态，并对未标记的状态进行处理
            boolean hasStopped = true;
            List<FA_State> unhandled = null;
            for (Map.Entry<List<FA_State>, Boolean> entry : dStates.entrySet()) {
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
                    // 否则此等价状态在此字符上无后继状态，标记为空

                    // 遍历后继的核，得到核的闭包
                    // 不采用foreach的形式，因为curFollowing在过程中添加新的后继状态
                    for (int i = 0; i < curFollowingSize; i++) {
                        FA_State tempState = curFollowing.get(i);
                        List<FA_State> tempClosure = closure(tempState);

                        // 清理当前节点计算 closure 时的递归现场
                        ClosureRecursionHandler.reset();

                        // 在 curFollowing 中加入所有 tempClosure 没有的元素
                        curFollowing.removeAll(tempClosure);
                        curFollowing.addAll(tempClosure);
                    }

                    // 排序后对比，判断此集合是都在dStates中
                    curFollowing.sort(new FA_StateComparator());
                    if (!isInDSates(dStates, curFollowing)) {
                        dStates.put(curFollowing, false);
                    }

                    // 标记dTrans转换表
                    dTrans.add(new DTran(unhandled, curFollowing, c));
                }
            }
        }

        for (DTran dTran : dTrans) {
            dTran.show();
        }


        // 子集构造法结束，根据dStates、dTrans构造相对应的DFA（dStates从后往前即为现等价状态的产生顺序）
        // pre代表原NFA，cur代表对应的DFA
        List<FA_State> preTerminatedStates = nfa.getTerminatedStates();

        List<FA_State> curStates = new LinkedList<>();
        List<FA_State> curTerminatedStates = new FA_StatesList();

        // 因为dStates是逐步压入的，所以形成的状态序号是倒序
        int curIndex = dStates.size() - 1;
        for (List<FA_State> nowConvertedNFAStates : dStates.keySet()) {
            FA_State state = new FA_State(curIndex);
            curStates.add(state);
            curIndex--;

            // 含有原NFA终止态的即为现终止态
            if (isTerminatedState(preTerminatedStates, nowConvertedNFAStates)) {
                curTerminatedStates.add(state);
            }
        }

        DFA dfa = new DFA();
        dfa.setStart(curStates.get(curStates.size() - 1));
        dfa.setAlphabet(nfa.getAlphabet());
        dfa.setStates(curStates);
        dfa.setTerminatedStates(curTerminatedStates);
        return dfa;
    }

    /**
     * 计算当前节点的ε闭包 ε-closure
     */
    private List<FA_State> closure(FA_State nowState) {
        List<FA_State> result = new FA_StatesList();
        result.add(nowState);
        ClosureRecursionHandler.addState(nowState);

        // 遍历当前节点的每一个后续节点
        for (FA_Edge tempEdge : nowState.getFollows()) {
            if (tempEdge.getLabel() == 'ε') {
                // 若递归 closure 结果集中不包含此节点，则将此节点加入结果集
                FA_State nextState = tempEdge.getPointTo();
                if (!ClosureRecursionHandler.contain(nextState)) {
                    List<FA_State> temp = closure(nextState);
                    result.addAll(temp);
                    ClosureRecursionHandler.addAllState(temp);
                }
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

    /**
     * 判断toTest是否与pre有交集
     * 有交集，现等价状态即为现DFA的终止态
     */
    private boolean isTerminatedState(final List<FA_State> pre, final List<FA_State> toTest) {
        // 取交集无并集
        // 注意：retainAll之后 toTest 会被修改，但是因为在调用方法中不会再使用，所以就不再深度拷贝复制了
        toTest.retainAll(pre);
        if (toTest.size() != 0) return true;
        else return true;
    }


    /**
     * @param dfa 需要被优化的DFA
     * @return 具有最少状态的DFA
     */
    public DFA optimize(DFA dfa) {
        List<FA_State> nonTerminatedStates = new FA_StatesList();
        List<FA_State> terminatedStates = dfa.getTerminatedStates();
        List<Character> alphabet = dfa.getAlphabet();

        nonTerminatedStates.addAll(dfa.getStates());
        nonTerminatedStates.removeAll(terminatedStates);

        FA_Node node1 = new FA_Node(nonTerminatedStates);
        FA_Node node2 = new FA_Node(terminatedStates);

        while (true) {

            for (char c : alphabet) {

            }


            break;
        }


        return null;
    }
}
