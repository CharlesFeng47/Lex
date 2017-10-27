package finiteAutomata;

import finiteAutomata.entity.FA_Edge;
import finiteAutomata.entity.FA_State;
import finiteAutomata.entity.NFA;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * DFA_Handler Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 24, 2017</pre>
 */
public class DFA_HandlerTest {

    private NFA nfa;

    @Before
    public void before() throws Exception {
        FA_State state1 = new FA_State(1);
        FA_State state2 = new FA_State(2);
        FA_State state3 = new FA_State(3);
        FA_State state4 = new FA_State(4);
        FA_State state5 = new FA_State(5);
        FA_State state6 = new FA_State(6);
        FA_State state7 = new FA_State(7);
        FA_State state8 = new FA_State(8);
        FA_State state9 = new FA_State(9);
        FA_State state10 = new FA_State(10);

        FA_Edge edge1 = new FA_Edge('ε', state2);
        FA_Edge edge2 = new FA_Edge('ε', state3);
        FA_Edge edge3 = new FA_Edge('a', state4);
        FA_Edge edge4 = new FA_Edge('ε', state6);
        FA_Edge edge5 = new FA_Edge('ε', state7);
        FA_Edge edge6 = new FA_Edge('ε', state8);
        FA_Edge edge7 = new FA_Edge('ε', state5);
        FA_Edge edge8 = new FA_Edge('ε', state9);
        FA_Edge edge9 = new FA_Edge('a', state10);

        List<FA_Edge> follow1 = new LinkedList<>();
        follow1.add(edge1);
        follow1.add(edge2);
        state1.setFollows(follow1);
        List<FA_Edge> follow2 = new LinkedList<>();
        follow2.add(edge3);
        follow2.add(edge4);
        state2.setFollows(follow2);
        List<FA_Edge> follow3 = new LinkedList<>();
        follow3.add(edge5);
        follow3.add(edge6);
        state3.setFollows(follow3);
        List<FA_Edge> follow4 = new LinkedList<>();
        follow4.add(edge7);
        state4.setFollows(follow4);
        List<FA_Edge> follow5 = new LinkedList<>();
        follow5.add(edge8);
        state5.setFollows(follow5);
        List<FA_Edge> follow7 = new LinkedList<>();
        follow7.add(edge9);
        state7.setFollows(follow7);

        List<Character> alphabet = new LinkedList<>();
        alphabet.add('a');

        List<FA_State> terminatedStates = new LinkedList<>();
        terminatedStates.add(state6);
        terminatedStates.add(state8);
        terminatedStates.add(state9);
        terminatedStates.add(state10);

        List<FA_State> allStates = new LinkedList<>();
        allStates.add(state1);
        allStates.add(state2);
        allStates.add(state3);
        allStates.add(state4);
        allStates.add(state5);
        allStates.add(state6);
        allStates.add(state7);
        allStates.add(state8);
        allStates.add(state9);
        allStates.add(state10);

        nfa = new NFA();
        nfa.setAlphabet(alphabet);
        nfa.setStart(state1);
        nfa.setTerminatedStates(terminatedStates);
        nfa.setStates(allStates);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFromNFA(NFA nfa)
     */
    @Test
    public void testGetFromNFA() throws Exception {
        DFA_Handler dfaHandler = new DFA_Handler();
        dfaHandler.getFromNFA(nfa);

    }

    /**
     * Method: optimize(DFA nfa)
     */
    @Test
    public void testOptimize() throws Exception {
    }


    /**
     * Method: closure(FA_State nowState)
     */
    @Test
    public void testClosure() throws Exception {
        DFA_Handler dfaHandler = new DFA_Handler();

        Method method = DFA_Handler.class.getDeclaredMethod("closure", FA_State.class);
        method.setAccessible(true);
        method.invoke(dfaHandler, nfa.getStart());

        // 需比对的答案
        // state: closure
        // 1: 1, 2, 3, 6, 7, 8
        // 2: 2, 6
        // 3: 3, 7, 8
        // 4: 4, 5, 9
        // 7: 7
//        for (FA_State temp : result) {
//            System.out.println(temp.getStateID());
//        }
    }

}
