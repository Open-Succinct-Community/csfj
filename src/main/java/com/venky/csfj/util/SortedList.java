/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author venky
 */
public class SortedList<C> extends ArrayList<C> implements Comparator<C>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1012502806760345149L;
	private final Comparator<? super C> comparator; 
    public SortedList(Comparator<? super C> comparator){
        this.comparator = comparator;
    }
    public Comparator<? super C> getComparator(){ 
        if (comparator == null){
            return this;
        }
        return comparator;
    }
    public SortedList(Collection<? extends C> c) {
        super(c);
        this.comparator = null;
    }

    public SortedList() {
        this.comparator = null;
    }

    public SortedList(int initialCapacity) {
        super(initialCapacity);
        this.comparator = null;
    }

    @Override
    public boolean add(C e) {
        int index = search(e);
        if (index < 0){
            index = (-1 * (index + 1));
        }
        super.add(index, e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends C> c) {
        boolean ret = false; 
        Iterator<? extends C> i = c.iterator(); 
        while (i.hasNext()){
            ret = add(i.next()) || ret;
        }
        
        return ret;
    }
    @Override
    public boolean contains(Object o) {
        return search(o) > 0;
    }
    
    private int search(Object o){
        return Collections.binarySearch(this, (C)o, getComparator());
    }

    @Override
    public int indexOf(Object o) {
        int index = search(o);
        if (index < 0){
            return -1; 
        }else {
            int ret = index; 
            for (int i = index -1  ; i >= 0 ; i--){
                if (getComparator().compare(get(i),(C)o) == 0 ){
                    ret = i ;
                }else{
                    break;
                }
            }
            return ret;
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = indexOf(o); 
        int ret = index; 
        if (index >= 0){
            for (int i = index + 1; i < size() ; i++){
                if (getComparator().compare(get(i),(C)o) == 0 ){
                    ret = i ;
                }else {
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        boolean ret = false;
        if (index >= 0) {
            super.remove(index);
            ret = true;
        }
        return ret;
    }

    public C first(){
        return get(0);
    }
    
    public C last(){ 
        return get(size() - 1);
    }
    
    @Override
    public void add(int index, C element) {
        throw new UnsupportedOperationException("Cannot add to specific index of Sorted List");
    }

    @Override
    public boolean addAll(int index, Collection<? extends C> c) {
        throw new UnsupportedOperationException("Cannot add to specific index of Sorted List");
    }


    @Override
    public C set(int index, C element) {
        throw new UnsupportedOperationException("Cannot add to specific index of Sorted List");
    }

    public int compare(C o1, C o2) {
        @SuppressWarnings("unchecked")
		Comparable<? super C> c1 = (Comparable<? super C>)o1; 
        return c1.compareTo(o2);
    }
    
}
