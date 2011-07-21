/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver;

import com.venky.csfj.solver.variable.VariableAssignment;

/**
 *
 * @author Venky
 */
public class NoMoreValuesToTryException extends RuntimeException{

    public NoMoreValuesToTryException(String message){ 
        this(message,null);
    }
    public NoMoreValuesToTryException(String message,VariableAssignment culprit) {
        super(message);
        this.culprit = culprit;
    }

    private final VariableAssignment culprit ; 
    public NoMoreValuesToTryException() {
        super();
        culprit = null;
    }

    public VariableAssignment getCulprit() {
        return culprit;
    }
    
    
}
