/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;
import java.util.List;

/**
 *
 * @author venky
 */
public class GenericCostConstraint<V extends Variable<DT>,DT> implements Constraint<V,DT>{
    private final Problem<V,DT> problem; 
    public GenericCostConstraint(Problem<V,DT> problem){
        this.problem = problem;
    }
    private Double minCost = null; 
    
    public void setMinCost(double minCost){
        this.minCost = minCost;
    }
    

    
    public void propagate(VariableAssignment<V, DT> workingAssignment, List<VariableAssignment<V, DT>> assigned, List<VariableAssignment<V, DT>> unassigned) throws ConstraintViolationException {
        if (minCost == null){
            return;
        }
        Solution<V,DT> partial = new Solution<V, DT>(this.problem); 
        partial.merge(assigned);
        partial.merge(workingAssignment);
        double costSoFar = problem.getCost(partial);
        if (costSoFar >= minCost){
            throw new ConstraintViolationException("Cost must be less than minimum Cost");
        }
        
    }
    
}
