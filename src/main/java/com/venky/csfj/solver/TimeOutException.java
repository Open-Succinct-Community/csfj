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

    public TimeOutException() {
    }

    public TimeOutException(String message) {
        super(message,null);
    }

    
}
