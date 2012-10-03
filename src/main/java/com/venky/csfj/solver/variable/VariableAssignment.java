/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver.variable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.venky.csfj.util.Domain;

/**
 *
 * @author Venky
 */
public class VariableAssignment<V extends Variable<DT>, DT> {

    private V variable;
    private boolean assigned = false; 
    private Stack<Checkpoint<DT>> checkpoints;

    public VariableAssignment(V variable) {
        this.variable = variable;
        this.checkpoints = new Stack<Checkpoint<DT>>();
    }

    public V getVariable() {
        return variable;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
       
    public void createCheckpoint() {
        Checkpoint<DT> checkpoint = null;
        if (this.checkpoints.empty()) {
            checkpoint = new Checkpoint<DT>(variable.getDomain());
        } else {
            checkpoint = new Checkpoint<DT>(checkpoints.peek());
        }
        this.checkpoints.push(checkpoint);
    }

    public void clearCheckpoint() {
        this.checkpoints.pop();
    }

    public DT getValue() {
        return getDomain().peek();
    }

    public Domain<DT> getDomain() {
        return checkpoints.peek().getDomain();
    }

    public void inValidate() {
        getDomain().pop();
    }

    public void setAttribute(String name,Object value){
        checkpoints.peek().setAttribute(name, value);
    }
    
    public Object getAttribute(String name){
        return checkpoints.peek().getAttribute(name);
    }

    private class Checkpoint<T> {

        private Domain<T> domain;
        private Map<String,Object> attributes = null ;
        public void setAttribute(String name,Object value){
            if (attributes == null){
                attributes = new HashMap<String, Object>(5);
            }
            attributes.put(name, value);
        }
        public Object getAttribute(String name){
            if (attributes == null){
                return null; 
            }else {
                return attributes.get(name);
            }
        }

        public Checkpoint(Checkpoint<T> another){
            this(another.getDomain());
        }
        @SuppressWarnings("unchecked")
		public Checkpoint(Domain<T> domain) {
            try {
                this.domain = domain.getClass().newInstance();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
            Iterator<T> i = domain.iterator(); 
            while (i.hasNext()){
                this.domain.push(i.next());
            }
        }

        public Domain<T> getDomain() {
            return domain;
        }
    }
}
