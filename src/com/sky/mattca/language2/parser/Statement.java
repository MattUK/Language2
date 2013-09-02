package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.Token;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 02/09/13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class Statement {

    private Token value;

    public Statement() {
    }

    public Statement(Token value) {
        this.value = value;
    }


    public Token getValue() {
        return value;
    }

}
