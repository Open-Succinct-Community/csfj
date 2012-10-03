/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

import java.util.EmptyStackException;

import com.venky.core.collections.SequenceSet;

/**
 *
 * @author venky
 */
public class DomainImpl<T> extends SequenceSet<T> implements Domain<T>{

	@Override
	public T peek() {
		int len = size();
		if (len == 0){
			throw new EmptyStackException();
		}
		return super.get(len -1);
	}

	@Override
	public T pop() {
		int len = size();
		if (len == 0){
			throw new EmptyStackException();
		}
		return super.remove(len -1);
	}

	@Override
	public T push(T value) {
		if (contains(value)){
			remove(value);
		}
		add(value);
		return value;
	}

    
}
