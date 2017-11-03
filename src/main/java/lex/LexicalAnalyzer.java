package lex;

import exceptions.NotMatchingException;
import finiteAutomata.FA_Controller;
import finiteAutomata.entity.DFA;
import lex.entity.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/11/2.
 * <p>
 * 词法分析器
 */
public class LexicalAnalyzer {

    /**
     * 由当前 .l 文件生成的最小 DFA
     */
    private DFA dfa;

    /**
     * 由当前用户输入产生的 模式-正则定义 映射表
     */
    private Map<String, String> patternREMap;

    /**
     * 由当前用户输入产生的 模式优先级 映射表（整数0表示优先级最高，越优先匹配）
     */
    private Map<String, Integer> patternPriorityMap;

    /**
     * 对每一种模式的小个 DFA
     */
    private Map<String, DFA> patternDFAMap;

    /**
     * 控制生成 DFA
     */
    private FA_Controller faController;

    public LexicalAnalyzer(DFA dfa, Map<String, String> patternREMap, Map<String, Integer> patternPriorityMap) {
        this.dfa = dfa;
        this.patternREMap = patternREMap;
        this.patternPriorityMap = patternPriorityMap;
        this.patternDFAMap = new HashMap<>();
        this.faController = new FA_Controller();
    }

    /**
     * 对每一个词素都进行分析
     *
     * @param lexeme 要分析的词素
     * @return 分析结束之后的的结果词法单元
     */
    public Token analyze(String lexeme) throws NotMatchingException {
        // 合法的所有模式，再一个一个比对
        List<String> dfaValidPatterns = dfa.getEndingPatterns(lexeme);

        List<String> validPatterns = new LinkedList<>();
        for (String pattern : dfaValidPatterns) {
            DFA curDFA = patternDFAMap.get(pattern);

            if (curDFA == null) {
                // 第一次出现，重新构建小 DFA
                List<String> res = new LinkedList<>();
                res.add(patternREMap.get(pattern));

                List<String> patterns = new LinkedList<>();
                patterns.add(pattern);

                curDFA = faController.lexicalAnalysis(res, patterns);
                patternDFAMap.put(pattern, curDFA);
            }

            // 使用当前的 DFA 进行测试
            if (curDFA.isValid(lexeme)) {
                validPatterns.add(pattern);
            }
        }

        // 在 Valid Pattern 中挑选优先级最高的模式作为当前词素的模式
        String pattern = getMaxPriorityPattern(validPatterns);
        return new Token(pattern, lexeme);
    }

    private String getMaxPriorityPattern(List<String> patterns) {
        if (patterns.size() == 1) {
            return patterns.get(0);
        }

        int maxPriority = patternREMap.size();
        String maxPriorityPattern = patterns.get(0);

        for (String compared : patterns) {
            if (patternPriorityMap.get(compared) < maxPriority) {
                maxPriority = patternPriorityMap.get(compared);
                maxPriorityPattern = compared;
            }
        }
        return maxPriorityPattern;
    }
}
