/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver;

/**
 *
 * @author venky
 */
public class TimeOutException extends NoMoreValuesToTryException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7076319830113631375L;

	public TimeOutException() {
    }

    public TimeOutException(String message) {
        super(message,null);
    }

    
}
