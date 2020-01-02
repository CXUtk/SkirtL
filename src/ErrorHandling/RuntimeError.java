package ErrorHandling;

import Lexical.Token;

public class RuntimeError extends RuntimeException {
    private Token token;
    public RuntimeError(String errormessage, Token token) {
        super(errormessage);
        this.token = token;
    }
    public Token getToken(){return this.token;}
}