/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venky.csfj.solver.stigmergy;

import com.venky.csfj.solver.Problem;
import com.venky.csfj.solver.Solution;
import com.venky.csfj.solver.Solver;
import com.venky.csfj.solver.variable.Variable;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.venky.csfj.util.Domain;
import com.venky.csfj.util.SortedList;


/**
 *
 * @author venky
 */
public class Population<V extends Variable<DT>,DT> {
    private final Random generator = new Random();
    private final Problem<DT> problem ;
    private int populationSize; 
    private SortedList<Solution<V,DT>> members ;
    public Population(final Problem<DT> problem,int populationSize){
        this.problem = problem;
        this.populationSize = populationSize;
        this.members = new SortedList<Solution<V,DT>>();
    }

    public int getPopulationSize() {
        return populationSize;
    }
    
    @SuppressWarnings("unchecked")
	public int loadMembers(ObjectInputStream in) throws IOException,ClassNotFoundException{
        Map<String,DT> assignmentMap = null;
        try {
            do {
                assignmentMap = (Map<String,DT>)in.readObject();
                Solution<V,DT> member = new Solution<V, DT>(problem);
                member.merge(assignmentMap);
                members.add(member);
            }while (assignmentMap != null && in.available() >= 0);
        }catch (EOFException ex){
            //
        }finally {
            in.close();
        }
        int numMembersLoaded = members.size(); 
        if (numMembersLoaded > 0 ){
            System.out.println("Num Members loaded:" + numMembersLoaded);
            computeStatistics();
        }
        return numMembersLoaded;
    }
    
    public void writeMembers(ObjectOutputStream out) throws IOException{
        for (Solution<V,DT> solution : members){
            out.writeObject(solution.getAssignmentMap());
        }
    }
    
    public void writeBestMember(ObjectOutputStream out) throws IOException {
        out.writeObject(bestSolution().getAssignmentMap());
    }
    public Solution<V,DT> bestSolution(){ 
        return members.isEmpty() ? null : members.first();
    }
    
    public Population<V,DT> evolve(int minNumOfGenerations){
        int numPralayas = 0 ;
        do {
            double avgCostBeforeEvolve = avgCost;
            double bestCostBeforeEvolve = minCost;
            seedPopulationWithMembers();
            for (int i = 0 ; i < minNumOfGenerations ; i++){
                evolveOneGeneraton();
                printStatistics();
            }
            double improvementInAverage = (avgCostBeforeEvolve - avgCost)/avgCostBeforeEvolve;
            double improvementInBest = (bestCostBeforeEvolve - minCost)/bestCostBeforeEvolve;
            
            System.out.println("Avg old: " + avgCostBeforeEvolve + "Avg :" + avgCost + " Avg Cost Improvement:" + improvementInAverage + " Best Cost improvement:" + improvementInBest);
            if ( improvementInAverage >= 0  && improvementInAverage < getAverageImprovementThreshold()){
                numPralayas ++; 
                createPralaya();
            }
        }while (numPralayas < getMaxPralayas()); 
        
        
        return this;
    }
    
    @SuppressWarnings("unchecked")
	private Population<V,DT> evolveOneGeneraton(){
        int populationBeforeEvolution = members.size();
        List<Solution<V,DT>> nextGeneration = new ArrayList<Solution<V, DT>>();
        do {
            Solver<DT> solver = new Solver<DT>(problem);
            Solution<V,DT> newMember = solver.nextSolution(selectFitMember(),selectFitMember());
            if (newMember != null){
                nextGeneration.add(newMember);
            }
        }while (nextGeneration.size() < getPopulationChangePerGeneration() * populationBeforeEvolution);
        
        members.addAll(nextGeneration);
        for (int i = members.size() -1 ; i >= populationSize ; i --) {
            members.remove(i);
        }
        computeStatistics();
        return this;
    }
    
    public void createPralaya(){ 
        Set<Solution<V,DT>> retained = new HashSet<Solution<V, DT>>();
        for (int i = 0 ; i < members.size() && retained.size() < getNumMembersToRetainDuringPralaya() ; i ++){
            retained.add(members.get(i));
        }
        System.out.println("Num members retained in pralaya:" + retained.size() + " as opposed to " + getNumMembersToRetainDuringPralaya());
        members.clear();
        members.addAll(retained);
                
        computeStatistics();
    }
    
    
    private void seedPopulationWithMembers(){ 
        List<Solution<V,DT>> heuristicSolutions = null;
        do {
            heuristicSolutions = createHeuristicSolutions(populationSize - members.size());
            members.addAll(heuristicSolutions);
        }while (!heuristicSolutions.isEmpty() && members.size() < populationSize);
        
        Solution<V,DT> member = null; 
        while (members.size() < populationSize){
            member = createMember();
            if (member != null){ 
                members.add(member);
            }
        }
        computeStatistics();
    }
    
    @SuppressWarnings("unchecked")
	private List<Solution<V,DT>> createHeuristicSolutions(int maxHeuristicSolutions){
        List<Solution<V,DT>>  heuristicSolutions = new ArrayList<Solution<V, DT>>();
        
        if (maxHeuristicSolutions <= 0){
            return heuristicSolutions;
        }

        boolean costToBeMinimized = problem.isCostToBeMinimized(); 
        long timeOut = problem.getTimeOut(); 
        
        problem.setCostToBeMinimized(true);
        //problem.setTimeOut(180 * 1000);
        Solution<V,DT> heuristicSolution =  null;
        System.out.println("Creating heuristic solutions");
        if (members.isEmpty()){
            Solver<DT> solver = new Solver<DT>(problem);
            while (((heuristicSolution = solver.nextSolution()) != null) && heuristicSolutions.size() < maxHeuristicSolutions){
                heuristicSolutions.add(heuristicSolution);
                System.out.println(heuristicSolutions.size());
            }
        }else{
            boolean [] solverFlagsForSortingUnassigedVariables = new boolean[] { true , false} ; 
            for (int i = 0 ; i < solverFlagsForSortingUnassigedVariables.length ; i ++ ){
                Solver<DT> solver = new Solver<DT>(problem);
                solver.solve(bestSolution()) ;
                while((heuristicSolution = solver.nextSolution()) != null && heuristicSolutions.size() < maxHeuristicSolutions){
                    heuristicSolutions.add(heuristicSolution);
                    System.out.println(heuristicSolutions.size());
                }
            }
        }
        

        problem.setCostToBeMinimized(costToBeMinimized);
        problem.setTimeOut(timeOut);
        
        return heuristicSolutions; 
    }


    @SuppressWarnings("unchecked")
	private Solution<V,DT> createMember(){ 
        
        Solution<V,DT> partialSolution = new Solution<V, DT>(problem);
        for (Variable<DT> v :problem.getVariables()){ 
            Domain<DT> domain = v.getDomain();
            int index = generator.nextInt(domain.size());
            partialSolution.put((V)v, domain.get(index));
        }
        Solver<DT> solver = new Solver<DT>(problem);
        Solution<V,DT> solution = solver.solve(partialSolution); 
        
        solver = null;
        return solution;
    }
    
    
    
    private Map<Solution<V,DT>,Double> fitness = new HashMap<Solution<V, DT>, Double>();
    private double sumCost = 0; 
    private double avgCost = 0; 
    private double maxCost = 0;
    private double minCost = 0;
    private void computeStatistics(){ 
        sumCost = 0 ; 
        avgCost = 0 ; 
        maxCost = 0;
        for (Solution<V,DT> member : members){
            sumCost += member.getCost();
        }
        avgCost  = (sumCost/members.size());
        maxCost = members.get(members.size()-1).getCost();
        minCost = members.first().getCost();
        computeFitness();
    }
    private void computeFitness(){ 
        fitness.clear();
        double scale = 1.0 / (maxCost - minCost);
        //double scaledFitness = scale * (maxCost - member.getCost());
        double sumScaledFitness = scale * (maxCost * members.size() - sumCost) ;
        for (Solution<V,DT> member :members){
            double scaledFitness = scale * (maxCost - member.getCost());
            fitness.put(member, scaledFitness/sumScaledFitness);
        }
    }

    public void printStatistics(){
        System.out.println (" Avg Cost: " + (int)avgCost + " Best :" + (int)minCost + " NumMembers: " + members.size());
        System.out.flush();
    }
    public Solution<V,DT> selectFitMember(){ 
        return members.get(selectIndexByFitness());
    }
    
    public Solution<V,DT> selectUnfitMember(){
        return members.get(selectIndexByCost());
    }
    private int selectIndexByCost(){ 
        double r = generator.nextDouble();
        double sumc = 0; 
        for (int i = 0 ; i < members.size() ; i ++){
            Solution<V,DT> member = members.get(i);
            sumc += member.getCost() / sumCost ;
            if (sumc > r){
                return i;
            }
        }
        return members.size()-1;
        
    }
    
    private int selectIndexByFitness(){
        double r = generator.nextDouble();
        double sumf = 0; 
        for (int i = 0 ; i < members.size() ; i ++){
            sumf += fitness.get(members.get(i));
            if (sumf > r){
                return i;
            }
        }
        return members.size()-1;
    }
    private double populationChangePerGeneration = 0.1;
    public void setPopulationChangePerGeneration(double populationChangePerGeneration){ 
        assert ((populationChangePerGeneration < 1) && (populationChangePerGeneration > 0));
        this.populationChangePerGeneration = populationChangePerGeneration;
    }

    public double getPopulationChangePerGeneration() {
        return populationChangePerGeneration;
    }
    
    private double percentMembersToRetainDuringPralaya = 10.0;

    public double getPercentMembersToRetainDuringPralaya() {
        return percentMembersToRetainDuringPralaya;
    }
    
    public void setPercentMembersToRetainDuringPralaya(double percentMembersToRetainDuringPralaya) {
        this.percentMembersToRetainDuringPralaya = percentMembersToRetainDuringPralaya;
    }
    private int getNumMembersToRetainDuringPralaya() { 
        return (int)((getPercentMembersToRetainDuringPralaya() / 100) * populationSize);
    }

    private double averageImprovementThreshold = (9.0E-6 ) ;

    public double getAverageImprovementThreshold() {
        return averageImprovementThreshold;
    }

    public void setAverageImprovementThreshold(double averageImprovementThreshold) {
        this.averageImprovementThreshold = averageImprovementThreshold;
    }
    
    private int maxPralayas = 5 ;

    public int getMaxPralayas() {
        return maxPralayas;
    }

    public void setMaxPralayas(int maxPralayas) {
        this.maxPralayas = maxPralayas;
    }
    
    
    
    
}
