/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ft.unicamp.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Junior <jgjgjgjg@gmail.com>
 */
public class Combinations {
    
    private List<Integer> arrayPosibilities = new ArrayList<>();
    private List<Integer> arrayResult = new ArrayList<>();
    private List<Integer> arrayBegin = new ArrayList<>();
    private final List<List<Integer>> arraysIteracoes = new ArrayList<>();
    
    
    public Combinations(int quantNumCombinados, List<Integer> arrayPosibilities){
        this.arrayPosibilities = arrayPosibilities;
        
        if(quantNumCombinados <= this.arrayPosibilities.size()){
            this.arrayResult = new ArrayList<> (this.arrayPosibilities.subList(this.arrayPosibilities.size() - quantNumCombinados, this.arrayPosibilities.size()));
            this.arrayBegin = new ArrayList<> (this.arrayPosibilities.subList(0, quantNumCombinados));
        }
    }
    
    public List<List<Integer>> realizarTodasCombinacoes(){
        this.arraysIteracoes.add(new ArrayList<Integer>(arrayBegin));
        
        while(!arrayResult.equals(arrayBegin)){
            iteracao(getSizeArrayInicio() - 1);
            this.arraysIteracoes.add(new ArrayList<Integer>(arrayBegin));
        }
        
        return this.arraysIteracoes;
    } 
    
    private int iteracao(int posArrayAnalizada){
       int retornoMetodo = 0;
       int posArrayInicio = this.arrayBegin.get(posArrayAnalizada);
       int posArrayResult = this.arrayResult.get(posArrayAnalizada);
       
       if(posArrayInicio == posArrayResult){
            retornoMetodo =  iteracao(posArrayAnalizada - 1);
            this.arrayBegin.set(posArrayAnalizada, retornoMetodo);
            
            return retornoMetodo + 1;
       }else{  
           posArrayInicio++;
           this.arrayBegin.set(posArrayAnalizada, posArrayInicio);
           
           return posArrayInicio + 1;
       }
    }
    
    private void printArrays(){
        System.out.println("ArrayOpcoes = " + getArrayOpcoes() + "ArrayResult = " + getArrayResult() + "ArrayInicio = " + getArrayInicio());
    }

    private List<Integer> getArrayOpcoes() {
        return arrayPosibilities;
    }

    private List<Integer> getArrayResult() {
        return arrayResult;
    }

    private List<Integer> getArrayInicio() {
        return arrayBegin;
    }
   
     private int getSizeArrayInicio() {
        return arrayBegin.size();
    }
     
    public static void main(String[] args){
        Integer[] arrayPossibilities = new Integer[] {1,2,3,4,5};
        Integer quantNumCombinados = 3;
        Combinations teste = new Combinations(quantNumCombinados,Arrays.asList(arrayPossibilities));
        
        teste.printArrays();
        teste.realizarTodasCombinacoes();
        teste.printArrays();
    }    
}
