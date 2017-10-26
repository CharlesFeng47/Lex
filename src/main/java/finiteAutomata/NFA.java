package finiteAutomata;

import utilties.FA_StateIDController;
import utilties.FA_StatesList;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * Nondeterministic FA，不确定的有穷自动机
 */
public class NFA extends FA {


    /**
     * @param re 标准化的正则定义后缀表达式
     * @return 此正则定义对应的NFA
     */
    public NFA getFromRE(String re) {
        // 栈中暂时保存处理过的NFA
        Stack<NFA> handling = new Stack<>();

        for (char c : re.toCharArray()) {
            switch (c) {
                case '·':
                    handling = join(handling);
                    break;
                case '|':
                    handling = or(handling);
                    break;
                case '*':
                    handling = closure(handling);
                    break;
                default:
                    handling = add(handling, c);
                    break;
            }
        }

        // 最终栈中剩下的唯一NFA即为所求
        return handling.get(0);
    }

    /**
     * 确定接口后再写，不知道 nfaList 使用 List 还是 Stack
     *
     * @param nfaList 需要被连接的所有NFA
     * @return 连接为一个NFA
     */
    public NFA combine(List<NFA> nfaList) {
        if (nfaList.size() == 1) return nfaList.get(0);
        else {
            for (int i = 0; i < nfaList.size() - 1; i++) {
                NFA nfa1 = nfaList.get(i);
                NFA nfa2 = nfaList.get(i + 1);
            }
            return null;
        }
    }

    /**
     * 将字符c转换为一个NFA
     */
    private Stack<NFA> add(Stack<NFA> handling, char c) {
        int nowID = FA_StateIDController.getID();

        FA_State start = new FA_State(nowID);
        FA_State end = new FA_State(++nowID);
        FA_StateIDController.setID(++nowID);

        FA_Edge edge = new FA_Edge(c, end);
        List<FA_Edge> follows = new LinkedList<>();
        follows.add(edge);
        start.setFollows(follows);


        // 构造NFA
        List<Character> alphabet = new LinkedList<>();
        alphabet.add(c);

        List<FA_State> terminatedStates = new FA_StatesList();
        terminatedStates.add(end);

        List<FA_State> states = new FA_StatesList();
        states.add(start);
        states.add(end);

        NFA newNFA = new NFA();
        newNFA.setStart(start);
        newNFA.setAlphabet(alphabet);
        newNFA.setTerminatedStates(terminatedStates);
        newNFA.setStates(states);

        // 得到结果压栈
        handling.push(newNFA);
        return handling;
    }

    /**
     * 根据连接符取栈顶两个NFA连接
     */
    private Stack<NFA> join(Stack<NFA> handling) {
        NFA after = handling.pop();
        NFA before = handling.pop();

        // 将after加到before后面
        FA_State joinStart = before.getTerminatedStates().get(0);
        FA_State joinEnd = after.getStart();

        FA_Edge joinEdge = new FA_Edge('ε', joinEnd);
        List<FA_Edge> follows = new LinkedList<>();
        follows.add(joinEdge);
        joinStart.setFollows(follows);

        // 连接之后字母表相加，所有状态相加，终止态变为after，起始态不变
        List<Character> beforeAlphabet = before.getAlphabet();
        List<FA_State> beforeStates = before.getStates();
        beforeAlphabet.addAll(after.getAlphabet());
        beforeStates.addAll(after.getStates());
        before.setTerminatedStates(after.getTerminatedStates());

        handling.push(before);
        return handling;
    }

    /**
     * 根据或符取栈顶两个NFA做或操作
     */
    private Stack<NFA> or(Stack<NFA> handling) {
        NFA nfa1 = handling.pop();
        NFA nfa2 = handling.pop();

        // 新增两个连接态
        int nowID = FA_StateIDController.getID();
        FA_State newStart = new FA_State(nowID);
        FA_State newEnd = new FA_State(++nowID);
        FA_StateIDController.setID(++nowID);

        // 将 nfa1 和 nfa2 并联
        FA_State preStart1 = nfa1.getStart();
        FA_State preStart2 = nfa2.getStart();
        FA_State preEnd1 = nfa1.getTerminatedStates().get(0);
        FA_State preEnd2 = nfa2.getTerminatedStates().get(0);

        FA_Edge orEdge1 = new FA_Edge('ε', preStart1);
        FA_Edge orEdge2 = new FA_Edge('ε', preStart2);
        FA_Edge orEdge3 = new FA_Edge('ε', newEnd);
        FA_Edge orEdge4 = new FA_Edge('ε', newEnd);

        // 完善开始态
        List<FA_Edge> startFollows = new LinkedList<>();
        startFollows.add(orEdge1);
        startFollows.add(orEdge2);
        newStart.setFollows(startFollows);

        // 修改原终止态
        List<FA_Edge> preEndFollows1 = new LinkedList<>();
        preEndFollows1.add(orEdge3);
        preEnd1.setFollows(preEndFollows1);
        List<FA_Edge> preEndFollows2 = new LinkedList<>();
        preEndFollows2.add(orEdge4);
        preEnd2.setFollows(preEndFollows2);

        // 重新构造NFA
        List<Character> alphabet = new LinkedList<>();
        alphabet.addAll(nfa1.getAlphabet());
        alphabet.addAll(nfa2.getAlphabet());

        List<FA_State> terminatedStates = new FA_StatesList();
        terminatedStates.add(newEnd);

        List<FA_State> states = new FA_StatesList();
        states.addAll(nfa1.getStates());
        states.addAll(nfa2.getStates());
        states.add(newStart);
        states.add(newEnd);

        NFA newNFA = new NFA();
        newNFA.setStart(newStart);
        newNFA.setAlphabet(alphabet);
        newNFA.setTerminatedStates(terminatedStates);
        newNFA.setStates(states);

        handling.push(newNFA);
        return handling;
    }

    /**
     * 取栈顶NFA做闭包操作
     */
    private Stack<NFA> closure(Stack<NFA> handling) {
        NFA nfa = handling.pop();

        // 新增两个连接态
        int nowID = FA_StateIDController.getID();
        FA_State newStart = new FA_State(nowID);
        FA_State newEnd = new FA_State(++nowID);
        FA_StateIDController.setID(++nowID);

        // 新增连接边
        FA_State preStart = nfa.getStart();
        FA_State preEnd = nfa.getTerminatedStates().get(0);

        FA_Edge newEdge1 = new FA_Edge('ε', preStart);
        FA_Edge newEdge2 = new FA_Edge('ε', newEnd);
        FA_Edge newEdge3 = new FA_Edge('ε', newEnd);
        FA_Edge newEdge4 = new FA_Edge('ε', preStart);

        List<FA_Edge> startFollows = new LinkedList<>();
        startFollows.add(newEdge1);
        startFollows.add(newEdge3);
        newStart.setFollows(startFollows);

        // 修改原终止态
        List<FA_Edge> preEndFollows = new LinkedList<>();
        preEndFollows.add(newEdge2);
        preEndFollows.add(newEdge4);
        preEnd.setFollows(preEndFollows);

        // 对闭包NFA，字母表不变，修改开始态、终止态、所有状态
        nfa.setStart(newStart);

        List<FA_State> terminatedStates = new FA_StatesList();
        terminatedStates.add(newEnd);
        nfa.setTerminatedStates(terminatedStates);

        List<FA_State> states = nfa.getStates();
        states.add(newStart);
        states.add(newEnd);

        handling.push(nfa);
        return handling;
    }

}
