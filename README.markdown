# A simple framework to solve constraint satisfaction problems
This framework includes what you need to solve constraint satisfaction problems. 
To learn more about constraint satisfaction problems see link:http://en.wikipedia.org/wiki/Constraint_satisfaction_problem 

## How it works (an analogy)
Think of a combination lock with n digits, how would you guess the lock sequence? You would pretty much need  to go from 0 0 0 to 9 9 9. however if you knew that the sum of the digits is 7, You can easily exclude several of the choices as invalid. This is the basic principle followed. 

* You define your problem.
* register variables (with their domain) and constraints. 
* Instantiate a solver for your problem
* solver.nextSolution() can be called as many times as the number of solutions you need. 
 
## examples
Examples may be found by browsing the tests folder. 




