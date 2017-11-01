package lex.entity;

import utilties.PatternType;

/**
 * Created by cuihua on 2017/10/31.
 *
 * 词法单元
 */
public class Token {

    /**
     * 模式
     */
    private PatternType patternType;

    /**
     * 属性值
     */
    private String attribute;

    public PatternType getPatternType() {
        return patternType;
    }

    public void setPatternType(PatternType patternType) {
        this.patternType = patternType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
