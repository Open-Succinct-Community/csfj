/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;
import com.venky.csfj.util.Domain;

/**
 *
 * @author Venky
 */
public class Solver<DT> {

    private Problem<DT> problem;
    private Stack<VariableAssignment<DT>> assignedVariables;
    private Stack<VariableAssignment<DT>> unAssignedVariables;
    private final GenericCostConstraint<DT> costConstraint; 
            

    public Solver(Problem<DT> problem) {
        this.problem = problem;
        this.assignedVariables = new Stack<VariableAssignment<DT>>();
        this.unAssignedVariables = new Stack<VariableAssignment<DT>>();
        for (Variable<DT> variable : problem.getVariables()) {
            pushUnAssignedVariable(new VariableAssignment<DT>(variable));
        }
        if (problem.isCostToBeMinimized()){
            this.costConstraint = new GenericCostConstraint<DT>(problem);
        }else {
            this.costConstraint = null;
        }
        createCheckPoints();
    }

    public final void createCheckPoints() {
        for (VariableAssignment<DT> unAssignedVariable : unAssignedVariables) {
            unAssignedVariable.createCheckpoint();
        }
    }

    @SuppressWarnings("unchecked")
	public <V extends Variable<DT>> Solution<V, DT> nextSolution(Solution<V,DT>... bias) {
        Solution<V,DT> solution =  solve(bias);
        if (solution != null && bias != null && bias.length > 0 && isSolutionInBias(solution, bias)) {
            solution = solve();
        }
        return solution;
    }
    
    private <V extends Variable<DT>> boolean isSolutionInBias(Solution<V,DT> solution, Solution<V,DT>... bias){
        for (Solution<V,DT> aBias: bias){
            if (aBias.equals(solution)){
                return true;
            }
        }
        return false;
    }
    
    public <V extends Variable<DT>> Solution<V, DT> solve(Solution<V,DT>... bias) {
        try {
            backTrack();
            Solution<V,DT> solution = solveVariables(bias);
            return solution;
        } catch (NoMoreValuesToTryException e) {
            return null;
        }
    }
    
    public void setMinCost(double... cost){
        if (costConstraint == null){
            return ;
        }
        double minCost = Double.MAX_VALUE; 
        for (int i = 0 ; i < cost.length ; i++ ){
            if (minCost > cost[i]){
                minCost = cost[i];
            }
        }
        // Try to get better than the best cost passed.
        costConstraint.setMinCost(minCost);
    }

    private void backTrack() {
        backTrack(null);
    }

    private void checkTimeOut(long startTime) {
        if (problem.getTimeOut() < 0) {
            return;
        }
        long now = System.currentTimeMillis();
        long elapsedTime = now - startTime;
        if (elapsedTime > problem.getTimeOut()) {
            problem.println("No Solution Yet. Elapsed Time :" + elapsedTime + ". Timing Out!!");
            throw new TimeOutException();
        }
    }
    @SuppressWarnings("unchecked")
	private <V extends Variable<DT>> Solution<V,DT>[] cloneBias(Solution<V,DT>... bias){
        if (bias == null || bias.length == 0 ){
            return null;
        }
        Solution<V,DT>[] clone = (Solution<V,DT>[])Array.newInstance(bias[0].getClass(), bias.length);
        for (int i = 0 ; i <bias.length ; i++){
            clone[i] = (Solution<V,DT>)bias[i].clone();
        }
        return clone;
    }

    private <V extends Variable<DT>> Solution<V,DT> solveVariables(Solution<V,DT>... bias) {
        long start = System.currentTimeMillis();
        Solution<V,DT>[] biasClone = cloneBias(bias);
        while (!unAssignedVariables.empty()) {
            checkTimeOut(start);
            sortUnassignedVariables();
            VariableAssignment<DT> next_variable_to_solve = popUnAssignedVariable();
            try {
                solveVariable(next_variable_to_solve,biasClone);
                assignedVariables.push(next_variable_to_solve);
            } catch (NoMoreValuesToTryException e) {
                pushUnAssignedVariable(next_variable_to_solve);
                backTrack(e);
            }
            //problem.println(next_variable_to_solve.getVariable().getName()+"="+next_variable_to_solve.getValue());
        }
        long now = System.currentTimeMillis();
        long elapsedTime = now - start;
        Solution<V,DT> solution = new Solution<V, DT>(problem).merge(assignedVariables);
        if (costConstraint != null){
        	solution.setCost(costConstraint.getMinCost());
        }
        if (!isSolutionInBias(solution, bias)){
            problem.println("Solution of cost " + (int)solution.getCost() + " found. Elapsed Time :" + elapsedTime);
        }
        return solution;
    }

    private void backTrack(NoMoreValuesToTryException e) {
        VariableAssignment<DT> culprit = null; 
        if (e != null){
            culprit = e.getCulprit();
        }

        VariableAssignment<DT> last_solved_variable = assignedVariables.empty() ? null : assignedVariables.peek();
        if (culprit == null || 
                (!unAssignedVariables.isEmpty() && 
                culprit == unAssignedVariables.peek())){
            culprit = last_solved_variable;
        }
        
        if (culprit != null){
            boolean culpritFound = false; 
            while (!assignedVariables.isEmpty() && !culpritFound){
                last_solved_variable = assignedVariables.pop();
                if (last_solved_variable == culprit) {
                    culpritFound = true; 
                }
                last_solved_variable.inValidate();
                clearCheckPoints();
                pushUnAssignedVariable(last_solved_variable);
            }
        }else if (e != null) {
            throw e;
        }
    }
    
    private void pushUnAssignedVariable(VariableAssignment<DT> va){
        unAssignedVariables.push(va);
        va.setAssigned(false);
    }
    
    private VariableAssignment<DT> popUnAssignedVariable(){
        VariableAssignment<DT> ret = unAssignedVariables.pop(); 
        ret.setAssigned(true);
        return ret;
    }
    

    private void tryNextValue(VariableAssignment<DT> variableAssignmentToSolve) {
        for (Constraint<DT> c : problem.getConstraints()) {
            c.propagate(variableAssignmentToSolve, assignedVariables, unAssignedVariables);
        }
        if (costConstraint != null){
            costConstraint.propagate(variableAssignmentToSolve, assignedVariables, unAssignedVariables);
        }
    }

    private Random generator = new Random();
    private <V extends Variable<DT>> void biasAssignmentDomain(VariableAssignment<DT> variableAssignmentToSolve, Solution<V,DT>... bias) {
        if (bias == null || bias.length == 0) {
            return;
        }
        Domain<DT> domain = variableAssignmentToSolve.getDomain();
        V v = variableAssignmentToSolve.getVariable();
        List<Solution<V,DT>> biasList = new ArrayList<Solution<V,DT>>();
        for (int i = 0 ; i < bias.length ; i++ ){
            if (bias[i].containsKey(v)){
                biasList.add(bias[i]);
            }
        }
        if (biasList.isEmpty()){
            return;
        }
        Solution<V,DT> biasAddressed = biasList.get(generator.nextInt(biasList.size()));
        
        DT seedValue = biasAddressed.get(v);
        if (domain.contains(seedValue)) {
            while(!domain.peek().equals(seedValue)) {
                domain.pop();
            }
        }
        biasAddressed.remove(v);
    }

    private <V extends Variable<DT>> void solveVariable(VariableAssignment<DT> variableAssignmentToSolve,Solution<V,DT>... bias) {
        biasAssignmentDomain(variableAssignmentToSolve,bias);
        VariableAssignment<DT>  culprit = null;
        long start = System.currentTimeMillis();
        while (!variableAssignmentToSolve.getDomain().isEmpty()) {
            try {
                checkTimeOut(start);
                createCheckPoints();
                tryNextValue(variableAssignmentToSolve);
                return;
            } catch (ConstraintViolationException e) {
                culprit = e.getCulprit();
                variableAssignmentToSolve.inValidate();
                clearCheckPoints();
                if (culprit != null && culprit != variableAssignmentToSolve){
                    break;
                }
            }
        }

        throw new NoMoreValuesToTryException(variableAssignmentToSolve.getVariable().getName(),culprit);
    }

    private void clearCheckPoints() {
        for (VariableAssignment<DT> var : unAssignedVariables) {
            var.clearCheckpoint();
        }
    }

    private void sortUnassignedVariables() {
        problem.sortUnassignedVariables(assignedVariables, unAssignedVariables);
    }
    
}
