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
    public IntegerEnumeratedVariable(String name,int min, int max){
        super(name,new IntegerEnumeratedDomain(min,max));
    }
}
