/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.venky.csfj.sample;

import com.venky.csfj.solver.variable.IntegerEnumeratedVariable;
import com.venky.csfj.solver.Constraint;
import com.venky.csfj.solver.ConstraintViolationException;
import com.venky.csfj.solver.Problem;
import com.venky.csfj.solver.Solution;
import com.venky.csfj.solver.Solver;
import com.venky.csfj.solver.variable.IntegerVariable;
import com.venky.csfj.solver.variable.VariableAssignment;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Venky
 */
public class CartesianTest {

    public CartesianTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void testCartesian(){
        Problem<IntegerVariable,Integer> p = new Problem<IntegerVariable,Integer>();
        IntegerVariable x = new IntegerVariable("x", 0, 5);
        IntegerVariable y = new IntegerVariable("y", 0, 5);
        p.getVariables().add(x);
        p.getVariables().add(y);
        Solver<IntegerVariable,Integer> s = new Solver<IntegerVariable,Integer>(p);
        HashMap<IntegerVariable, Integer> h = s.nextSolution() ;
        int solNo = 0;
        while (h != null){
            solNo ++;
            System.out.println("" + solNo + ":" + h.get(x) +"," + h.get(y));
            h = s.nextSolution();
        }
        assertEquals(solNo, 36);
    }

    @Test
    public void testxplusyequal6(){
        Problem<IntegerEnumeratedVariable,Integer> p = new Problem<IntegerEnumeratedVariable,Integer>();
        IntegerEnumeratedVariable x = new IntegerEnumeratedVariable("x", 0, 5);
        IntegerEnumeratedVariable y = new IntegerEnumeratedVariable("y", 0, 5);
        p.getVariables().add(x);
        p.getVariables().add(y);

        Solution<IntegerEnumeratedVariable,Integer> seed = new Solution<IntegerEnumeratedVariable, Integer>(p);
        seed.put(x, 4);
        seed.put(y, 2);
        
        p.addConstraint(new Constraint<IntegerEnumeratedVariable,Integer>() {
            public void propagate(VariableAssignment<IntegerEnumeratedVariable,Integer> workingAssignment, List<VariableAssignment<IntegerEnumeratedVariable,Integer>> assigned, List<VariableAssignment<IntegerEnumeratedVariable,Integer>> unassigned) throws ConstraintViolationException {
                int sum = 6 ; 
                sum -= workingAssignment.getValue();

                for (VariableAssignment<IntegerEnumeratedVariable,Integer> v : assigned){
                    sum -= v.getValue();
                }
                
                if (sum < 0){
                    throw new ConstraintViolationException();
                }else if (sum > 0 && unassigned.isEmpty()){
                    throw new ConstraintViolationException();
                }else if (sum > 0){
                    for (VariableAssignment<IntegerEnumeratedVariable,Integer> uv : unassigned){
                        for (Iterator<Integer> di = uv.getDomain().iterator() ; di.hasNext(); ){
                            int value = di.next();
                            if (value > sum){
                                di.remove();
                            }
                        }
                    }
                }
            }
        });
        Solver<IntegerEnumeratedVariable,Integer> s = new Solver<IntegerEnumeratedVariable,Integer>(p);
        Solution<IntegerEnumeratedVariable, Integer> h = s.solve((Solution<IntegerEnumeratedVariable, Integer>)seed) ;
        assertTrue(!seed.isEmpty());
        int solNo = 0;
        while (h != null){
            solNo ++;
            System.out.println("" + solNo + ":" + h.get(x) +"," + h.get(y));
            h = s.nextSolution();
        }
        assertEquals(solNo, 4);

    }

}