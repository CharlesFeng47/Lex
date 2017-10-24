package finiteAutomata;

/**
 * Created by cuihua on 2017/10/24.
 *
 * 有穷自动机中的链接
 */
public class FA_Edge {

    /**
     * 这条边上的标记
     */
    private char label;

    /**
     * 这条边指向的后记状态
     */
    private FA_State pointTo;


    public FA_Edge(char label, FA_State pointTo) {
        this.label = label;
        this.pointTo = pointTo;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public FA_State getPointTo() {
        return pointTo;
    }

    public void setPointTo(FA_State pointTo) {
        this.pointTo = pointTo;
    }
}
