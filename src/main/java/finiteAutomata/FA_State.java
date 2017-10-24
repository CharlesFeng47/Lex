package finiteAutomata;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cuihua on 2017/10/24.
 * <p>
 * 有穷自动机中的节点
 */
public class FA_State {

    /**
     * 此节点对应的ID编号
     */
    private int stateID;

    /**
     * 此节点对应的后续状态链接
     */
    private List<FA_State> follows;


    public FA_State(int stateID) {
        this.stateID = stateID;
        this.follows = new LinkedList<FA_State>();
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public List<FA_State> getFollows() {
        return follows;
    }

    public void setFollows(List<FA_State> follows) {
        this.follows = follows;
    }
}
