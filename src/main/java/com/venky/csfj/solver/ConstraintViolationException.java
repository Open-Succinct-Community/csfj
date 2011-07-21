/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;

/**
 *
 * @author Venky
 */
public class ConstraintViolationException extends RuntimeException{


    public ConstraintViolationException(String message){
        this(message,null);
    }
    
    public ConstraintViolationException(String message, VariableAssignment culprit) {
        super(message);
        this.culprit = culprit;
    }
    private final VariableAssignment culprit ;
    public ConstraintViolationException() {
        this.culprit = null;
    }

    public VariableAssignment getCulprit() {
        return culprit;
    }

    

}
