/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver.variable;

import com.venky.csfj.util.IntegerEnumeratedDomain;

/**
 *
 * @author venky
 */
public class IntegerEnumeratedVariable extends Variable<Integer> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3007898661653649310L;

	public IntegerEnumeratedVariable(String name,int min, int max){
        super(name,new IntegerEnumeratedDomain(min,max));
    }
}
