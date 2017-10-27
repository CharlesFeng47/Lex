package finiteAutomata;

import exceptions.UnexpectedRegularExprRuleException;
import finiteAutomata.entity.DFA;
import finiteAutomata.entity.NFA;

import java.util.List;
import java.util.Stack;

/**
 * Created by cuihua on 2017/10/27.
 * <p>
 * 控制将输入的所有 RE 转换为拥有最少数目状态的 DFA
 */
public class FA_Controller {

    public DFA lexicalAnalysis(List<String> res) {

        RegularExpressionHandler rgHandler = new RegularExpressionHandler();
        NFA_Handler nfaHandler = new NFA_Handler();
        DFA_Handler dfaHandler = new DFA_Handler();

        Stack<NFA> convertedNFA = new Stack<>();
        for (String re : res) {
            // 处理当前 RE
            try {
                re = rgHandler.convertInfixToPostfix(rgHandler.standardizeRE(re));
                System.out.println("-------------- " + re + " --------------");
            } catch (UnexpectedRegularExprRuleException e) {
                e.printStackTrace();
            }

            // RE => NFA
            NFA nfa = nfaHandler.getFromRE(re);
            convertedNFA.push(nfa);
            System.out.println("*************** finish convert " + re + " to NFA ***************");
        }

        // 合并所有 NFA 为一个 NFA
        NFA finalNFA = nfaHandler.combine(convertedNFA);
        System.out.println("-------------- combine all NFA to ONE --------------");

        // 转化为最小DFA
        // TODO 暂未实现最小DFA
//        DFA dfa = dfaHandler.optimize(dfaHandler.getFromNFA(finalNFA));
        DFA dfa = dfaHandler.getFromNFA(finalNFA);
        return dfa;
    }
}
