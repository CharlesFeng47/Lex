package lex.entity;

import utilties.PatternType;

/**
 * Created by cuihua on 2017/10/31.
 *
 * 词法单元 Token 的模式
 */
public class Pattern {

    /**
     * 模式类型
     */
    private PatternType type;

    public Pattern(PatternType type) {
        this.type = type;
    }

    public PatternType getType() {
        return type;
    }
}
