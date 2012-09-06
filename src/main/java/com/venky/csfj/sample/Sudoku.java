/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.sample;

import com.venky.csfj.solver.Constraint;
import com.venky.csfj.solver.ConstraintViolationException;
import com.venky.csfj.solver.Problem;
import com.venky.csfj.solver.variable.Variable;
import com.venky.csfj.solver.variable.VariableAssignment;
import java.util.Iterator;
import java.util.List;
import com.venky.csfj.util.IntegerEnumeratedDomain;

/**
 *
 * @author Venky
 */
public class Sudoku extends Problem<Sudoku.CellVariable,Integer>{

    private CellVariable[][] cell = new CellVariable[9][9];
    public Sudoku() {
        super();

        for (int row = 0 ; row < 9 ; row ++){
            for (int col = 0; col < 9 ; col ++){
                cell[row][col] = new CellVariable(row, col);
                getVariables().add(cell[row][col]);
            }
        }
        addConstraint(new RowColumnConstraint());
    }
    public void register_initial_values(int[][] values){
      for (int r = 0 ; r < 9 ; r ++ ){
          for (int c = 0; c < 9 ; c ++ ){
              if (values[r][c] != 0) {
                  for (Iterator<Integer> i = cell[r][c].getDomain().iterator(); i.hasNext();){
                      int value = i.next();
                      if (value != values[r][c]){
                          i.remove();
                      }
                  }
              }
          }
      }
    }


    public static class CellVariable extends Variable<Integer>{
        /**
		 * 
		 */
		private static final long serialVersionUID = -8078816158937529209L;
		public final int row, col ;
        public CellVariable(int row, int col){
            super(row + "," + col,new IntegerEnumeratedDomain(1,9));
            this.row = row; this.col = col;
        }
    }

    public class RowColumnConstraint implements Constraint<CellVariable,Integer>{
        public void propagate(VariableAssignment<CellVariable,Integer> workingAssignment, List<VariableAssignment<CellVariable,Integer>> assigned, List<VariableAssignment<CellVariable,Integer>> unassigned) throws ConstraintViolationException {
            CellVariable c1 = (CellVariable)workingAssignment.getVariable();
            for (VariableAssignment<CellVariable,Integer> uv : unassigned){
                CellVariable c2 = (CellVariable)uv.getVariable();
                if ((c1.row == c2.row) || (c1.col == c2.col) || isSameGrid(c1, c2)){
                    uv.getDomain().remove(workingAssignment.getValue());
                }
            }
        }
        private boolean isSameGrid(CellVariable c1,CellVariable c2){
            if ((c1.row/3 == c2.row/3)  &&
                    (c1.col / 3 ==  c2.col /3)){
                return true;
            }
            return false;
        }
        
    }

}
