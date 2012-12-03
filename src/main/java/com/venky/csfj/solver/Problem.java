/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;

/**
 *
 * @author Venky
 */
public class Problem<DT> {
    private boolean sysOutEnabled  = false;

    public boolean isSysOutEnabled() {
        return sysOutEnabled;
    }

    public void setSysOutEnabled(boolean sysOutEnabled) {
        this.sysOutEnabled = sysOutEnabled;
    }
    
    public void println(String message){
        if (isSysOutEnabled()){
            System.out.println(message);
            System.out.flush();
        }
    }
    
    private Stack<Variable<DT>> variables;
    private List<Constraint<DT>> constraints;

    public Problem(List<Variable<DT>> variables,List<Constraint<DT>> constraints){
        this();
        this.variables.addAll(variables);
        this.constraints.addAll(constraints);
    }
    public Problem(){
        this.variables = new Stack<Variable<DT>>();
        this.constraints = new ArrayList<Constraint<DT>>();
    }

    public List<Constraint<DT>> getConstraints() {
        return constraints;
    }
    public Stack<Variable<DT>> getVariables() {
        return variables;
    }

    public void addConstraint(Constraint<DT> c){
        constraints.add(c);
    }

    public void sortUnassignedVariables(List<VariableAssignment<DT>> assigned, List<VariableAssignment<DT>> unassigned){
        Collections.sort(unassigned,new Comparator <VariableAssignment<DT>>(){

            public int compare(VariableAssignment<DT> o1, VariableAssignment<DT> o2) {
                int ret = o2.getDomain().size() - o1.getDomain().size(); // sort decending as we are using stack 
                if (ret == 0) {
                    ret = o2.getVariable().getName().compareTo(o1.getVariable().getName());
                }
                return ret;
            }

        });
    }
    
    private long timeOut = -1;

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public double getCost(VariableAssignment<DT> workingAssignment,List<VariableAssignment<DT>> assigned, List<VariableAssignment<DT>> unassigned){
    	return 0.0;
    }

    private boolean costToBeMinimized = false;

    public boolean isCostToBeMinimized() {
        return costToBeMinimized;
    }

    public void setCostToBeMinimized(boolean costToBeMinimized) {
        this.costToBeMinimized = costToBeMinimized;
    }
    
    private Map<String,Variable<DT>> variableMap = new HashMap<String, Variable<DT>>();
    private void loadVariableMap(){ 
        variableMap.clear(); 
        for (Variable<DT> v : variables){
            variableMap.put(v.getName(),v);
        }
        return;
    }
    @SuppressWarnings("unchecked")
	public <V extends Variable<DT>> V getVariable(String name){
    	Variable<DT> var = variableMap.get(name); 
        if (variableMap.isEmpty() || var == null){
            loadVariableMap();
            var = variableMap.get(name);
        }
        return (V)var;
    }

}
