package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.TokenString;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 30/08/13
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */

@FunctionalInterface
public interface StatementParser {

    public boolean doParse(TokenString string);

    public static boolean parseExpression(TokenString string) {
        return false;
    }

    public static boolean parseConditional(TokenString string) {
        return false;
    }

}
