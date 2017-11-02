package finiteAutomata.entity;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * Nondeterministic FA，不确定的有穷自动机
 */
public class NFA extends FA {

    @Override
    public boolean isValid(String s) {
        FA_State start = getStart();

        return false;
    }
}
