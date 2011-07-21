/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.util;

import com.venky.csfj.util.SortedList;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author venky
 */
public class SortedListTest {
    
    public SortedListTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class SortedList.
     */
    Random r = new Random();
    @Test
    public void testAdd_GenericType() {
        System.out.println("add");
        
        SortedList<Integer> instance = new SortedList<Integer>();
        addRandomIntegers(instance);
        assertEquals(instance.size(), 10);
        ensureSorted(instance);
    }
    
    private <C extends Comparable<C>> void ensureSorted(SortedList<C> instance){ 
        List<C> another = new ArrayList<C>();
        another.addAll(instance);
        Collections.sort(another);
        assertEquals(another, instance);
        System.out.println(instance.toString());
    }
    
    private void addRandomIntegers(Collection<Integer> collection){
        addRandomIntegers(collection, 10);
    }
    private void addRandomIntegers(Collection<Integer> collection,int howMany){
        for (int i = 0; i < howMany ; i ++){
            collection.add(r.nextInt(howMany));
        }
    }

    /**
     * Test of addAll method, of class SortedList.
     */
    @Test
    public void testAddAll_Collection() {
        System.out.println("addAll");
        Collection<Integer> c = new HashSet<Integer>();
        SortedList instance = new SortedList();

        addRandomIntegers(c) ;
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        
        ensureSorted(instance);
    }

    /**
     * Test of contains method, of class SortedList.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        
        SortedList<Integer> instance = new SortedList<Integer>();
        addRandomIntegers(instance,100);
        boolean result = false; 

        instance.add(75);
        result = instance.contains(75);
        assertEquals(true, result);
        
        instance.add(75);
        result = instance.contains(75);
        assertEquals(true, result);
        
        result = instance.remove(Integer.valueOf(75));
        assertEquals(true, result);
        
        result = instance.remove(Integer.valueOf(75));
        assertEquals(true, result);
        
        while (instance.contains(Integer.valueOf(75))){ 
            instance.remove(Integer.valueOf(75));
        }

        result = instance.remove(Integer.valueOf(75));
        assertEquals(false, result);
        
    }

    /**
     * Test of indexOf method, of class SortedList.
     */
    @Test
    public void testIndexOf() {
        System.out.println("indexOf");
        SortedList instance = new SortedList();
        for (int i = 10 ; i >=-10 ; i--){
            instance.add(i);
        }
        assertEquals(instance.size(), 21); // including 0 
        int j = 1;
        for (int i = 10 ; i >=-10 ; i--){
            int result = instance.indexOf(i); 
            assertEquals(result, 21 - j);
            result = instance.lastIndexOf(i); 
            assertEquals(result, 21 - j);
            j++;
        }
        
    }


    /**
     * Test of remove method, of class SortedList.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        SortedList<Integer> instance = new SortedList<Integer>();
        
        boolean result = instance.remove(Integer.valueOf(1));
        assertEquals(false, result);
        result = instance.add(Integer.valueOf(1));
        assertEquals(true, result);

        result = instance.remove(Integer.valueOf(1));
        assertEquals(true, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of add method, of class SortedList.
     */
    @Test
    public void testAdd_int_GenericType() {
        System.out.println("add");
        int index = 0;
        SortedList<Integer> instance = new SortedList<Integer>();
        try {
            instance.add(index, Integer.valueOf(10));
            fail("This Should have thrown unsupported operation exception");
        }catch (UnsupportedOperationException ex){
            assertEquals(true, true);
        }catch (Throwable th) {
            fail("This Should have thrown unsupported operation exception");
        }
        
    }

    /**
     * Test of addAll method, of class SortedList.
     */
    @Test
    public void testAddAll_int_Collection() {
        System.out.println("addAll");
        int index = 0;
        Collection<Integer> c = new HashSet<Integer>();
        addRandomIntegers(c);
        
        SortedList<Integer> instance = new SortedList<Integer>();
        try {
            instance.addAll(index, c);
            fail("This Should have thrown unsupported operation exception");
        }catch (UnsupportedOperationException ex){
            assertEquals(true, true);
        }catch (Throwable th) {
            fail("This Should have thrown unsupported operation exception");
        }

        
    }

    /**
     * Test of set method, of class SortedList.
     */
    @Test
    public void testSet() {
        System.out.println("set");
        int index =0 ;
        SortedList<Integer> instance = new SortedList<Integer>();
        try {
            instance.set(index, 75);
            fail("This Should have thrown unsupported operation exception");
        }catch (UnsupportedOperationException ex){
            assertEquals(true, true);
        }catch (Throwable th) {
            fail("This Should have thrown unsupported operation exception");
        }
    }
    
    @Test
    public void testIndexOf_1(){
        SortedList<Integer> s = new SortedList<Integer>();
        s.add(1);
        s.add(3);
        s.add(2);
        s.add(2);
        s.add(1);
        assertEquals(s.indexOf(1),0);
        assertEquals(s.lastIndexOf(1),1);
        assertEquals(s.indexOf(2),2);
        assertEquals(s.lastIndexOf(2),3);
        assertEquals(s.indexOf(3),4);
        assertEquals(s.lastIndexOf(3),4);

        
    }       
}
