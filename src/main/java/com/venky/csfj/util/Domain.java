/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

import java.util.Iterator;

/**
 *
 * @author venky
 */
public interface Domain<T> {
    public boolean contains(Object value);
    public T peek();
    public T pop();
    public T push(T value);
    public boolean isEmpty();
    public Iterator<T> iterator();
    public int size();
    public T get(int i);
    public int indexOf(Object value);
    public boolean remove(Object value);
    public void clear();
}
