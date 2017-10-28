package finiteAutomata;

import finiteAutomata.entity.*;
import utilties.ClosureRecursionHandler;
import utilties.FA_NodesList;
import utilties.FA_StateComparator;
import utilties.FA_StatesList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/10/27.
 * <p>
 * 对 NFA 进行处理
 * NFA => DFA
 * optimize DFA
 */
public class DFA_Handler {

    private static FA_StateComparator comparator = new FA_StateComparator();

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
                    curFollowing.sort(comparator);
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

        // 标记子集构造法中形成的等价节点和现在简化的节点之间的映射
        Map<List<FA_State>, FA_State> faStatesConvertTable = new HashMap<>();

        // dStates 顺序压入，重新更换为简单 FA_State 也是顺序
        int curIndex = 0;
        for (List<FA_State> nowConvertedNFAStates : dStates.keySet()) {
            FA_State state = new FA_State(curIndex);
            curStates.add(state);
            curIndex++;

            faStatesConvertTable.put(nowConvertedNFAStates, state);

            // 含有原NFA终止态的即为现终止态
            if (isTerminatedState(preTerminatedStates, nowConvertedNFAStates)) {
                curTerminatedStates.add(state);
            }
        }

        // 把 dTrans 上的连接加入现在 DFA，并存入 DFA 成员变量 move
        Map<FA_State, Map<Character, FA_State>> move = new HashMap<>();
        for (DTran dTran : dTrans) {
            FA_State curStart = faStatesConvertTable.get(dTran.getFrom());
            FA_State curTo = faStatesConvertTable.get(dTran.getTo());
            char label = dTran.getLabel();

            FA_Edge curEdge = new FA_Edge(label, curTo);
            curStart.getFollows().add(curEdge);

            Map<Character, FA_State> curMove = move.get(curStart);
            if (curMove != null) {
                curMove.put(label, curTo);
            } else {
                curMove = new HashMap<>();
                curMove.put(label, curTo);
                move.put(curStart, curMove);
            }
        }

        DFA dfa = new DFA();
        dfa.setStart(curStates.get(0));
        dfa.setAlphabet(nfa.getAlphabet());
        dfa.setStates(curStates);
        dfa.setTerminatedStates(curTerminatedStates);
        dfa.setMove(move);
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

        result.sort(comparator);
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

        result.sort(comparator);
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
        // 深度拷贝复制 toTest，保证 retainAll 之后 toTest 不会被修改
        List<FA_State> newList = new FA_StatesList();
        newList.addAll(toTest);
        newList.retainAll(pre);

        if (newList.size() != 0) return true;
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

        // 构造初始两个集合 终结状态／非终结状态
        nonTerminatedStates.addAll(dfa.getStates());
        nonTerminatedStates.removeAll(terminatedStates);

        nonTerminatedStates.sort(comparator);
        terminatedStates.sort(comparator);

        FA_Node node1 = new FA_Node(nonTerminatedStates);
        FA_Node node2 = new FA_Node(terminatedStates);

        // 第一次分的这两个集合手动排序，让程序先处理非终结状态
        List<FA_Node> nodes = new FA_NodesList();
        nodes.add(node2);
        nodes.add(node1);

        while (true) {

            // 所有叶节点内部的 FA_State 都是等价的
            boolean isWeakEqual = true;
            for (int i = 0; i < nodes.size(); ) {
                // 子集分化
                for (char c : alphabet) {
                    List<FA_Node> tempResult = optimizeOneNodeOneChar(dfa, nodes, nodes.get(i), c);

                    nodes.remove(i);
                    nodes.addAll(i, tempResult);

                    if (tempResult.size() > 1) {
                        isWeakEqual = false;
                    }

                    // 下个节点 i++ 偏差 tempResult.size() - 1
                    i += tempResult.size();
                }

            }

            // 全都弱等价，结束算法
            if (isWeakEqual) break;
        }

        // 重构 DFA
        Map<FA_State, FA_State> deleteTran = new HashMap<>();
        for (FA_Node node : nodes) {
            // 只需要第一个状态作为代表，从后面向前删除
            List<FA_State> division = node.getStates();
            while (division.size() > 1) {
                int deleteIndex = division.size() - 1;
                deleteTran.put(division.get(deleteIndex), division.get(0));

                node.getStates().remove(deleteIndex);
            }
        }

        // 移除这些状态
        List<FA_State> needDeleteStates = new FA_StatesList(deleteTran.keySet());
        dfa.getStates().removeAll(needDeleteStates);
        dfa.getTerminatedStates().removeAll(needDeleteStates);

        // 转移链接关系
        // 需移除节点 指向 其他节点
        for (FA_State state : needDeleteStates) {
            dfa.getMove().remove(state);
        }

        // 其他节点 指向 需移除节点
        for (Map.Entry<FA_State, Map<Character, FA_State>> curMove : dfa.getMove().entrySet()) {
            FA_State curStart = curMove.getKey();
            // 转换表
            for (Map.Entry<Character, FA_State> curEdge : curMove.getValue().entrySet()) {
                if (needDeleteStates.contains(curEdge.getValue())) {
                    char label = curEdge.getKey();
                    FA_State deleteState = curEdge.getValue();

                    curMove.getValue().remove(label);
                    curMove.getValue().put(label, deleteTran.get(deleteState));

                }
            }

            // 状态链接
            for (FA_Edge curEdge : curStart.getFollows()) {
                if (needDeleteStates.contains(curEdge.getPointTo())) {
                    curEdge.setPointTo(deleteTran.get(curEdge.getPointTo()));
                    ;
                }
            }
        }

        return dfa;
    }

    /**
     * 在特定字母下优化一个 FA_Node 节点
     *
     * @param dfa         当前 DFA
     * @param curDivision 目前的分化
     * @param node        要优化的叶节点
     * @param c           分化基于的条件
     */
    private List<FA_Node> optimizeOneNodeOneChar(final DFA dfa, List<FA_Node> curDivision, FA_Node node, char c) {
        List<FA_Node> result = new FA_NodesList();

        // 子集分化，保存结果
        List<FA_State> parentToNull = new FA_StatesList();
        List<FA_State> parentNotToNull = new FA_StatesList();
        List<FA_State> following = new FA_StatesList();
        for (FA_State parentState : node.getStates()) {
            // 该节点在该映射条件下的后继
            Map<Character, FA_State> curEdges = dfa.getMove().get(parentState);
            if (curEdges != null) {
                FA_State sonState = curEdges.get(c);
                parentNotToNull.add(parentState);
                following.add(sonState);
            } else {
                parentToNull.add(parentState);
            }
        }

        if (parentToNull.size() != 0) {
            result.add(new FA_Node(parentToNull));
        }

        // 判断 following 是不是在同一叶节点中
        Map<FA_Node, List<FA_State>> judge = new HashMap<>();
        for (FA_State state : following) {
            FA_Node belongingNode = getBelongingNode(curDivision, state);
            if (judge.get(belongingNode) == null) {
                List<FA_State> temp = new FA_StatesList();
                temp.add(state);
                judge.put(belongingNode, temp);
            } else {
                judge.get(belongingNode).add(state);
            }
        }

        if (judge.size() > 1) {
            // 形成了不同的分化
            for (List<FA_State> states : judge.values()) {
                result.add(new FA_Node(states));
            }
        }

        return result;
    }

    /**
     * 找到当前状态所在的节点
     */
    private FA_Node getBelongingNode(List<FA_Node> curDivision, FA_State state) {
        for (FA_Node node : curDivision) {
            if (node.getStates().contains(state)) return node;
        }
        return null;
    }
}
