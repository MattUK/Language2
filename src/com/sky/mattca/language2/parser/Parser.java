package com.sky.mattca.language2.parser;

import com.sky.mattca.language2.tokenizer.TokenString;
import com.sky.mattca.language2.tokenizer.TokenType;

import static com.sky.mattca.language2.parser.StatementParser.*;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 28/08/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    static {
        ParserHelper parser = ParserHelper.start()
                .matchConsume(TokenType.FUNCTION)
                .matchConsume(TokenType.OPEN_BRACKET)

    }

}
