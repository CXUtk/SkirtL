package Evaluator;

import ErrorHandling.RuntimeError;
import Lexical.Token;

import java.util.Hashtable;

public class Environment {
    private final Hashtable<String, Object> lookupTable;

    public Environment(){
        lookupTable = new Hashtable<>();
    }

    public void define(String name, Object value){
        lookupTable.put(name, value);
    }

    public Object get(Token name){
        if(lookupTable.containsKey(name.getText())){
            return lookupTable.get(name.getText());
        }
        throw new RuntimeError(String.format("Undefined variable %s", name.getText()), name);
    }

    public void assign(Token name, Object value){
        if(lookupTable.containsKey(name.getText())){
            lookupTable.put(name.getText(), value);
            return;
        }
        throw new RuntimeError(String.format("Undefined variable %s", name.getText()), name);
    }
}
