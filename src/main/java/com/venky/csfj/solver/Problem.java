/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Venky
 */
public class Problem<V extends Variable<DT>,DT> {
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
    
    private Stack<V> variables;
    private List<Constraint<V,DT>> constraints;

    public Problem(List<V> variables,List<Constraint<V,DT>> constraints){
        this();
        this.variables.addAll(variables);
        this.constraints.addAll(constraints);
    }
    public Problem(){
        this.variables = new Stack<V>();
        this.constraints = new ArrayList<Constraint<V,DT>>();
    }

    public List<Constraint<V,DT>> getConstraints() {
        return constraints;
    }
    public Stack<V> getVariables() {
        return variables;
    }

    public void addConstraint(Constraint<V,DT> c){
        constraints.add(c);
    }

    public void sortUnassignedVariables(List<VariableAssignment<V,DT>> assigned, List<VariableAssignment<V,DT>> unassigned){
        Collections.sort(unassigned,new Comparator <VariableAssignment<V,DT>>(){

            @Override
            public int compare(VariableAssignment<V,DT> o1, VariableAssignment<V,DT> o2) {
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

    public double getCost(Solution<V,DT> solution){
        return 0.0;
    }

    private boolean costToBeMinimized = false;

    public boolean isCostToBeMinimized() {
        return costToBeMinimized;
    }

    public void setCostToBeMinimized(boolean costToBeMinimized) {
        this.costToBeMinimized = costToBeMinimized;
    }
    
    private Map<String,V> variableMap = new HashMap<String, V>();
    private void loadVariableMap(){ 
        variableMap.clear(); 
        for (V v : variables){
            variableMap.put(v.getName(),v);
        }
        return;
    }
    public V getVariable(String name){
        V var = variableMap.get(name); 
        if (variableMap.isEmpty() || var == null){
            loadVariableMap();
            var = variableMap.get(name);
        }
        return var;
    }

}
