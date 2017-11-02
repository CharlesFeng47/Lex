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

    public DFA lexicalAnalysis(List<String> res, List<String> patternType) {
        RegularExpressionHandler rgHandler = new RegularExpressionHandler();
        NFA_Handler nfaHandler = new NFA_Handler();
        DFA_Handler dfaHandler = new DFA_Handler();

        Stack<NFA> convertedNFA = new Stack<>();
        for (int i = 0; i < res.size(); i++) {
            // 处理当前 RE
            String re = res.get(i);
            try {
                re = rgHandler.convertInfixToPostfix(rgHandler.standardizeRE(re));
                logger.debug("正在处理正则定义 " + re);
            } catch (UnexpectedRegularExprRuleException e) {
                e.printStackTrace();
            }

            // RE => NFA
            NFA nfa = nfaHandler.getFromRE(re, patternType.get(i));
            convertedNFA.push(nfa);
            logger.debug("将正则定义 " + re + " 成功转化为 NFA");
        }

        // 合并所有 NFA 为一个 NFA
        NFA finalNFA = nfaHandler.combine(convertedNFA);
        logger.debug("所有 NFA 已被合并为一个");

        // 转化为最小DFA
        DFA dfa = dfaHandler.optimize(dfaHandler.getFromNFA(finalNFA));
        logger.debug("all states size: " + dfa.getStates().size());

        return dfa;
    }
}
