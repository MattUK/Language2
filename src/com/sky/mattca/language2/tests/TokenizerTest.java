package com.sky.mattca.language2.tests;

import com.sky.mattca.language2.tokenizer.Token;
import com.sky.mattca.language2.tokenizer.TokenString;
import com.sky.mattca.language2.tokenizer.Tokenizer;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 27/07/2013
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class TokenizerTest extends TestCase {
    public void testStart() throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        String identifierLine = "function helloWorld";
        String integerLine = "1024";
        String floatLine = "3.14159265";
        String stringLine = "\"Hello, World!\" + - / * . ^ % : == >=";
        String typeParamLine = "public class TestClass<T...>";

        tokenizer.addSourceLine(identifierLine);
        tokenizer.addSourceLine(integerLine);
        tokenizer.addSourceLine(floatLine);
        tokenizer.addSourceLine(stringLine);
        tokenizer.addSourceLine(typeParamLine);

        List<TokenString> result = tokenizer.start();
        for (TokenString str : result) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~");
            for (Token tk : str.toList()) {
                System.out.println("Type = " + tk.type + ", Contents = " + tk.contents);
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }
}
