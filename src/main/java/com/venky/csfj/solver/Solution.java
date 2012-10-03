/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author venky
 */
public class Solution<V extends Variable<DT>,DT> extends HashMap<V,DT> implements Comparable<Solution<V,DT>>, Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8121089109097870863L;
	private transient Problem<V,DT> problem = null;
    private transient Double cost = null; 
    
    
    public Solution(Problem<V,DT> problem){
        super();
        this.problem = problem;
    }

    
    public Problem<V, DT> getProblem() {
        return problem;
    }

    public void setProblem(Problem<V, DT> problem) {
        this.problem = problem;
    }
    
    public Solution<V,DT> merge (Collection<VariableAssignment<V,DT>> variableAssignments){
        for (VariableAssignment<V,DT> assignment: variableAssignments){
            put(assignment.getVariable(), assignment.getValue());
        }
        return this;
    }
    public Solution<V,DT> merge (VariableAssignment<V,DT>... variableAssignment){
        for (int i = 0 ;  i < variableAssignment.length ;  i ++ ){ 
            put(variableAssignment[i].getVariable(), variableAssignment[i].getValue());
        }
        return this;
    }

    
    public Solution<V,DT> merge (Map<String,DT> assignmentMap){
        for (String name:assignmentMap.keySet()){ 
            put(problem.getVariable(name),assignmentMap.get(name));
        }
        return this;
    }
    
    public Map<String,DT> getAssignmentMap(){
        Map<String,DT> assignmentMap = new HashMap<String, DT>();
        for (V v :keySet()){
            assignmentMap.put(v.getName(), get(v));
        }
        return assignmentMap;
    }
    
    public int compareTo(Solution<V, DT> o) {
        int ret = 0;
        if (ret == 0){
            ret = (int)Math.signum(getCost() - o.getCost());
        }
        
        if (ret == 0) {
            Set<V> keys = new TreeSet<V>();
            keys.addAll(keySet());
            keys.addAll(o.keySet());
            for (V v:keys){
                DT v1 = get(v);
                DT v2 = o.get(v);
                int index1 = v.getDomain().indexOf(v1); 
                int index2 = v.getDomain().indexOf(v2);
                ret = index2 - index1 ; // As domain is a stack, need to sort based on index from top of the stack. hence 2 - 1
                if (ret != 0){
                    break;
                }
            }
        }
        return ret;
    }
    
    public double getCost(){
        if (cost == null){
            cost = 0.0;  
        }
        return cost;
    }
    
    public void setCost(double cost){
    	this.cost = cost;
    }
    
    private void resetComputedFields(){ 
        cost = null;
    }
    
    @Override
    public void clear() {
        resetComputedFields();
        super.clear();
    }

    @Override
    public DT put(V key, DT value) {
        resetComputedFields();
        return super.put(key, value);
    }

    @Override
    public DT remove(Object key) {
        resetComputedFields();
        return super.remove((V)key);
    }

}
