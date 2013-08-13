package com.sky.mattca.language2.tests

import com.sky.mattca.language2.tokenizer.TokenString

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 13/08/13
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    private fun TokenString.reverse(): TokenString {
        return this;
    }

    fun testParserFunction() {
        var ts = TokenString();
        ts.reverse();
    }

}