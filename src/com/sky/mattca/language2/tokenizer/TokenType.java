package com.sky.mattca.language2.tokenizer;

/**
 * List of possible token types, these are used by the parser and interpreter to ascertain the context of a line or statement.
 */
public enum TokenType {
    WHITESPACE, // spaces, tabs, etc
    OP_ADD, // +
    OP_SUB, // -
    OP_MUL, // *
    OP_DIV, // /
    OP_MOD, // %
    OP_POW, // ^
    OP_ASSIGNMENT, // :
    OP_GREATER_THAN, // >
    OP_LESS_THAN, // <
    OP_GREATER_EQ_TO, // >=
    OP_LESS_EQ_TO, // <=
    OP_DOUBLE_EQUALS, // ==
    OP_LOGICAL_AND, // &
    OP_LOGICAL_OR, // |
    OP_CONCATENATE, // ~
    IDENTIFIER, // if, func, myVar, etc
    STRING_LITERAL, // "hello, world!"
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    BOOL_LITERAL,
    SEPARATOR, // ,
    OPEN_BRACKET, // (
    CLOSE_BRACKET, // )
    OPEN_SQUARE_BRACKET, // [
    CLOSE_SQUARE_BRACKET, // ]
    OPEN_CURVY_BRACE, // {
    CLOSED_CURVY_BRACE, // }
    END_STATEMENT, // ;
    PERIOD, // .
    NONE
}
