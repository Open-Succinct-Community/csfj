/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver;

import java.util.List;

import com.venky.core.util.Bucket;
import com.venky.csfj.solver.variable.VariableAssignment;

/**
 *
 * @author venky
 */
public class GenericCostConstraint<DT> implements Constraint<DT>{
    private final Problem<DT> problem; 
    public GenericCostConstraint(Problem<DT> problem){
        this.problem = problem;
    }
    private Double minCost = null; 
    
    public void setMinCost(double minCost){
        this.minCost = minCost;
    }
    
    public Double getMinCost(){
    	return minCost;
    }

    public void propagate(VariableAssignment<DT> workingAssignment, List<VariableAssignment<DT>> assigned, List<VariableAssignment<DT>> unassigned) throws ConstraintViolationException {
        VariableAssignment<DT> lastAssignment = null; 
        Bucket costSoFar = null ;
        if (!assigned.isEmpty()) {
        	lastAssignment = assigned.get(assigned.size()-1);
        	costSoFar = (Bucket)lastAssignment.getAttribute("costSoFar");
    		costSoFar = costSoFar.clone();
        }else {
        	costSoFar = new Bucket(0);
        }
        
        costSoFar.increment(problem.getCost(workingAssignment,assigned,unassigned));
        if (minCost != null && costSoFar.value() >= minCost){
            throw new ConstraintViolationException("Cost must be less than minimum Cost");
        }
        workingAssignment.setAttribute("costSoFar", costSoFar);
        if (unassigned.isEmpty()){
        	minCost = costSoFar.doubleValue();
        }
    }
    
}
