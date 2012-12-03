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

    /**
	 * 
	 */
	private static final long serialVersionUID = 6854658242123492986L;
	public NoMoreValuesToTryException(String message){ 
        this(message,null);
    }
    public NoMoreValuesToTryException(String message,VariableAssignment<?> culprit) {
        super(message);
        this.culprit = culprit;
    }

    private final VariableAssignment<?> culprit ; 
    public NoMoreValuesToTryException() {
        super();
        culprit = null;
    }

    @SuppressWarnings("unchecked")
	public <DT> VariableAssignment<DT> getCulprit() {
        return (VariableAssignment<DT>) culprit;
    }
    
    
}
