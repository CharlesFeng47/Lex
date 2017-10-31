package lex.entity;

/**
 * Created by cuihua on 2017/10/31.
 *
 * 词法单元
 */
public class Token {

    /**
     * 模式
     */
    private Pattern pattern;

    /**
     * 属性值
     */
    private String attribute;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
