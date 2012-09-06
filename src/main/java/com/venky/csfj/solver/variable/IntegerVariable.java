/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver.variable;

import com.venky.csfj.util.IntegerDomain;

/**
 *
 * @author Venky
 */
public class IntegerVariable extends Variable<Integer>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1134746866846771089L;

	public IntegerVariable(String name, int min, int max){
        super(name,new IntegerDomain(min,max));
    }
}
