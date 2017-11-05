package lex;

import exceptions.NotMatchingException;
import finiteAutomata.entity.DFA;
import lex.entity.Token;
import utilties.DFA_StatePatternMappingController;

import java.util.List;

/**
 * Created by cuihua on 2017/11/2.
 * <p>
 * 词法分析器
 */
public class LexicalAnalyzer {

    /**
     * 由当前 .l 文件生成的最小 DFA
     */
    private List<DFA> allDFAs;

    public LexicalAnalyzer(List<DFA> allDFAs) {
        this.allDFAs = allDFAs;
    }

    /**
     * 对每一个词素都进行分析
     *
     * @param lexeme 要分析的词素
     * @return 分析结束之后的的结果词法单元
     */
    public Token analyze(String lexeme) throws NotMatchingException {
        for (DFA curDFA : allDFAs) {
            // 按优先级顺序依次对比，满足了就返回
            if (curDFA.isValid(lexeme))
                return new Token(DFA_StatePatternMappingController.getMap().get(curDFA), lexeme);
        }
        throw new NotMatchingException(lexeme);

    }
}
