package finiteAutomata;

import exceptions.UnexpectedRegularExprRuleException;
import finiteAutomata.entity.DFA;
import finiteAutomata.entity.NFA;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Stack;

/**
 * Created by cuihua on 2017/10/27.
 * <p>
 * 控制将输入的所有 RE 转换为拥有最少数目状态的 DFA
 */
public class FA_Controller {

    private static final Logger logger = Logger.getLogger(FA_Controller.class);

    public DFA lexicalAnalysis(List<String> res) {

        RegularExpressionHandler rgHandler = new RegularExpressionHandler();
        NFA_Handler nfaHandler = new NFA_Handler();
        DFA_Handler dfaHandler = new DFA_Handler();

        Stack<NFA> convertedNFA = new Stack<>();
        for (String re : res) {
            // 处理当前 RE
            try {
                re = rgHandler.convertInfixToPostfix(rgHandler.standardizeRE(re));
                logger.debug("---------------- " + re + " ----------------");
            } catch (UnexpectedRegularExprRuleException e) {
                e.printStackTrace();
            }

            // RE => NFA
            // TODO 补上pattern
            NFA nfa = nfaHandler.getFromRE(re, null);
            convertedNFA.push(nfa);
            logger.debug("**************** finish convert " + re + " to NFA ****************");
        }

        // 合并所有 NFA 为一个 NFA
        NFA finalNFA = nfaHandler.combine(convertedNFA);
        logger.debug("---------------- combine all NFA to ONE ----------------");

        // 转化为最小DFA
        // TODO 暂未实现最小DFA
//        DFA dfa = dfaHandler.optimize(dfaHandler.getFromNFA(finalNFA));
        DFA dfa = dfaHandler.getFromNFA(finalNFA);
        return dfa;
    }
}
