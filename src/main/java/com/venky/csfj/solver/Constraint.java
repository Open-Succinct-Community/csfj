/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.solver;

import java.util.List;

import com.venky.csfj.solver.variable.VariableAssignment;

/**
 *
 * @author Venky
 */
public interface Constraint<DT> {
    public  void propagate(VariableAssignment<DT> workingAssignment, List<VariableAssignment<DT>> assigned, List<VariableAssignment<DT>> unassigned) throws ConstraintViolationException ;
}