package Evaluator;

import ErrorHandling.RuntimeError;
import Lexical.Token;

import java.util.Hashtable;

public class Environment {
    private final Hashtable<String, Object> lookupTable;
    private final Environment parent;

    public Environment(){
        lookupTable = new Hashtable<>();
        this.parent = null;
    }

    public Environment(Environment parent){
        lookupTable = new Hashtable<>();
        this.parent = parent;
    }

    public void define(String name, Object value){
        lookupTable.put(name, value);
    }

    public Object get(Token name){
        if(lookupTable.containsKey(name.getText())){
            return lookupTable.get(name.getText());
        }
        // If the name does not exist in the current scope, find it on upper scope
        if(parent != null) return parent.get(name);
        throw new RuntimeError(String.format("Undefined variable %s", name.getText()), name);
    }

    public void assign(Token name, Object value){
        if(lookupTable.containsKey(name.getText())){
            lookupTable.put(name.getText(), value);
            return;
        }
        if(parent != null) assign(name, value);
        throw new RuntimeError(String.format("Undefined variable %s", name.getText()), name);
    }
}
