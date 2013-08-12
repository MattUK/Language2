package com.sky.mattca.language2.tokenizer2;

/**
 * User: Matt
 * Date: 07/10/12
 * Time: 22:33
 */
public class Token {

    public TokenType type;
    public String contents;
    public int line, position;

    public Token(TokenType type, String contents, int line, int position) {
        this.type = type;
        this.contents = contents;
        this.line = line;
        this.position = position;
    }

    public Token(TokenType type) {
        this.type = type;
    }

    public Token() {
        this.type = TokenType.NONE;
    }

}
