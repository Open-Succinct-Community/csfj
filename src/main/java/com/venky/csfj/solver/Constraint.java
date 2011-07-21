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
 * @author Venky
 */
public interface Constraint<V extends Variable<DT>,DT> {
    public  void propagate(VariableAssignment<V,DT> workingAssignment, List<VariableAssignment<V,DT>> assigned, List<VariableAssignment<V,DT>> unassigned) throws ConstraintViolationException ;
}