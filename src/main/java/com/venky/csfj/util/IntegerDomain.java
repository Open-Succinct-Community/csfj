/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author venky
 */
public class IntegerDomain implements Domain<Integer>{
    private int min; 
    private int max;
    public IntegerDomain(){
        this(0,-1);
    }
    public IntegerDomain(int min,int max){
        this.min = min; 
        this.max = max;
        assert min <= (max + 1); 
        
    }
    public boolean contains(Object value) {
        return value != null &&  min <= (Integer)value  && (Integer)value <= max;
    }

    public Integer peek() {
        return max;
    }

    public Integer pop() {
        int ret = max; 
        max = max - 1;
        if ( ret < min) {
            throw new NoSuchElementException();
        }
        return ret;
    }
    public boolean isEmpty(){
        return max < min;
    }

    public Integer push(Integer value) {
        if (isEmpty()) {
            min = value; 
            max = value;
        }else if (value == max +1){
            max = value;
        }else {
            throw new RuntimeException("Cannot push any element other than " + (max+1));
        }
        return value;
    }

    public Iterator<Integer> iterator() {
        return new IDIterator(min,max);
    }

    public int size() {
        return isEmpty()? 0 : max - min + 1;
    }

    public Integer get(int i) {
        if (isEmpty()) { 
           throw new NoSuchElementException(); 
        }else {
            return min + i;
        }
    }

    public int indexOf(Object value) {
        if (contains(value)){
            return (Integer)value - min;
        }else {
            return -1;
        }
    }
	@Override
	public int lastIndexOf(Object value) {
		return indexOf(value);
	} 

    public boolean remove(Object value) {
        Integer v =  (Integer)value; 
        if (v == max){
            pop();
            return true;
        }else { 
            throw new RuntimeException("Cannot remove out of sequence from IntegerDomain");
        }
    }

    public void clear() {
        max = min -1 ;
    }
    
    private class IDIterator implements Iterator<Integer>{
        int min, max ; 
        int current ;
        public IDIterator(int min, int max){
            this.min = min; 
            this.max = max; 
            this.current = min;
        }
        public boolean hasNext() {
            return max >= min && current <= max ;
        }

        public Integer next() {
            if (min > current || current > max ) {
                throw new NoSuchElementException();
            }
            int ret = current; 
            current ++; 
            return ret;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }

    
}
