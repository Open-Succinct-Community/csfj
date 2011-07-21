/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

/**
 *
 * @author venky
 */
public class IntegerEnumeratedDomain extends Stk<Integer> implements Domain<Integer>{
    public IntegerEnumeratedDomain(){
    }
    public IntegerEnumeratedDomain(int min, int max){
        assert min <= max ;
        for (int i = min ; i<= max; i++ ){
            push(i);
        }
        
    }
    
}
