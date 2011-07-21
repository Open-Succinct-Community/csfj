/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver.variable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.venky.csfj.util.Domain;

/**
 *
 * @author Venky
 */
public class Variable<DT> implements Comparable<Variable<DT>>,Serializable{
    
    public static final String NAME = "NAME"; 
    private Domain<DT> domain;
    public Variable(String name, Domain<DT> domain) {
        this.domain = domain;
        setAttribute(NAME, name);
    }
    public String getName(){
        return getAttribute(NAME);
    }
    public Domain<DT> getDomain(){
        return domain;
    }

    @Override
    public int compareTo(Variable<DT> o) {
        return getName().compareTo(o.getName());
    }

    private final Map<String,String> attributes = new HashMap<String, String>();
    
    public final void setAttribute(String name, String value){
        attributes.put(name, value);
    }
    public final String getAttribute(String name){
        return attributes.get(name);
    }
    
    public final Set<String> getAttributeNames(){ 
        return attributes.keySet();
    }
}
