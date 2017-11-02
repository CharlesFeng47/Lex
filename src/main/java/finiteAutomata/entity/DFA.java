package finiteAutomata.entity;

import exceptions.NotMatchingException;
import utilties.DFA_StatePatternMappingController;

import java.util.List;
import java.util.Map;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * Deterministic FA，确定的有穷自动机
 */
public class DFA extends FA {

    /**
     * DFA 中各状态之间的转换关系
     * 第一个 state(FA_State) 通过 label(Character) 到达第二个 state(FA_State)
     */
    private Map<FA_State, Map<Character, FA_State>> move;


    public Map<FA_State, Map<Character, FA_State>> getMove() {
        return move;
    }

    public void setMove(Map<FA_State, Map<Character, FA_State>> move) {
        this.move = move;
    }

    public List<String> getEndingPatterns(String s) throws NotMatchingException {
        FA_State curState = getStart();

        for (char c : s.toCharArray()) {
            boolean canFind = false;
            for (FA_Edge curEdge : curState.getFollows()) {
                if (curEdge.getLabel() == c) {
                    curState = curEdge.getPointTo();
                    canFind = true;
                    break;
                }
            }

            if (!canFind) throw new NotMatchingException();
        }

        // 字符串结束后在终止态即为合法的结束
        boolean isValid = getTerminatedStates().contains(curState);
        if (isValid) return DFA_StatePatternMappingController.getMap().get(curState);
        else return null;
    }

}
