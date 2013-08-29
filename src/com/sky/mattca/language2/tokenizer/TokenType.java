package com.sky.mattca.language2.tokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 12/08/13
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */
public enum TokenType {
    WHITESPACE,
    OP_ADD("+"),
    OP_SUB("-"),
    OP_MUL("*"),
    OP_DIV("/"),
    OP_MOD("%"),
    OP_POW("^"),
    OP_ASSIGN(":"),
    OP_GREATER_THAN(">"),
    OP_LESS_THAN("<"),
    OP_GREATER_EQ_TO(">="),
    OP_LESS_EQ_TO("<="),
    OP_IS_EQ_TO("=="),
    OP_LOGICAL_AND("&&"),
    OP_LOGICAL_OR("||"),
    OP_CONCATENATE("~"),
    IDENTIFIER,
    STRING_LITERAL,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    BOOLEAN_TRUE("true"),
    BOOLEAN_FALSE("false"),
    COMMA(","),
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    OPEN_SQUARE_BRACKET("["),
    CLOSE_SQUARE_BRACKET("]"),
    OPEN_CURVY_BRACKET("{"),
    CLOSE_CURVY_BRACKET("}"),
    END_STATEMENT(";"),
    PERIOD("."),
    RANGE("..."),
    NONE,

    // Keywords
    MODULE("module"),
    CLASS("class"),
    FUNCTION("function"),

    VARIABLE("var"),

    IF("if"),
    ELSE_IF("elseif"),
    ELSE("else"),

    FOR("for"),
    FOREACH("foreach"),

    WHILE("while"),
    REPEAT("repeat"),

    CONTINUE("continue"),
    BREAK("break"),

    PRIVATE("private"),
    PUBLIC("public");

    public String tokenValue;
    public boolean allowMatching;

    TokenType() {
        allowMatching = false;
    }

    TokenType(String tokenValue) {
        this.tokenValue = tokenValue;
        allowMatching = true;
    }

    public boolean match(String value) {
        if (allowMatching && value.length() >= length()) {
            String toMatch = value.substring(0, length());
            return (tokenValue.equals(toMatch));
        }
        return false;
    }

    public int length() {
        if (allowMatching) {
            return tokenValue.length();
        }
        return 0;
    }

    public boolean isBooleanLiteral() {
        return (this == TokenType.BOOLEAN_TRUE) || (this == TokenType.BOOLEAN_FALSE);
    }

}
