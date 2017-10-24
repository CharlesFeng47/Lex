package finiteAutomata;

import java.util.List;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * Nondeterministic FA，不确定的有穷自动机
 */
public class NFA {

    /**
     * 开始状态
     */
    private FA_State start;

    /**
     * 所有状态
     */
    private List<FA_State> states;

    /**
     * 终止／接受态
     */
    private List<FA_Edge> terminatedStates;

    /**
     * @param s 要检查的词素
     * @return 词素是否合法
     */
    protected boolean isVAlid(String s) {
        return true;
    }


    /**
     * @param re 需转换的正则定义
     * @return 此正则定义对应的NFA
     */
    public NFA getFromRE(String re) {
        return null;
    }


    /**
     * 将正则定义的中缀表达式改为后缀表达式
     * 注：暂只考虑并、或、闭包，未考虑[]等
     */
    private String convertInfixToPostfix(String re) {
        return null;
    }

}
