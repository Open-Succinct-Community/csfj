/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver.variable;

import com.venky.csfj.solver.ConstraintViolationException;

/**
 *
 * @author venky
 */
public class Counter implements Cloneable{
    private int counter;
    private int max;
    private int min; 
    public Counter(){
        this(Integer.MAX_VALUE);
    }
    public Counter(int max){
        this(0,max);
    }
    public Counter(int min,int max){
        this(min,max,min);
    }
    public Counter(int min,int max,int start){
        this.counter = start;
        this.max = max;
        this.min = min; 
        if (min > max){ 
            throw new ConstraintViolationException("Min cannot be more than max");
        }
        increment(0);
    }
    public void increment(){
        increment(1);
    }
    public void decrement(){ 
        increment(-1);
    }
    public void decrement(int by){
        increment(-1 * by);
    }
    public final void increment(int by){
        synchronized (this){
            int newValue = counter + by;
            if (newValue <= max  && newValue >= min ) {
                counter += by; 
            }else { 
                throw new ConstraintViolationException("Attempting to stretch beyond bounds.");
            }
        }
    }
    public int value() {
        return counter;
    }

    @Override
    public String toString() {
        return "min: " + min + " max: " + max + "value: " + counter;
    }
    
    @Override
    public Counter clone() {
        Object clone; 
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        return (Counter)clone;
    }
           
}
