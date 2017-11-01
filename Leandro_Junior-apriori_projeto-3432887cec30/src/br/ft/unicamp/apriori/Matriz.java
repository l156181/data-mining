package br.ft.unicamp.apriori;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Matriz {

    private List<List<Integer>> matriz;
    private int numColumns;
    private int numLines;
    private int numLinesBegin;
    
    public Matriz() {
        this.matriz = new ArrayList<>();
        this.numLines = 0;
        this.numColumns = 0;
        this.numLinesBegin = 0;
    }

    public Matriz(List<List<Integer>> matriz) {
        this();
        this.matriz = matriz;
        updateNumColumns();
        updateNumLines();
        this.numLinesBegin = getNumLines();
    }
    
   @Override
   public Object clone() throws CloneNotSupportedException{
        Object object = this;
        return object;
   }

    public void addColumn(List<Integer> column) {
        this.matriz.add(column);
        updateNumColumns();
    }

    private void addElement(int numColumn, int element) {
        List<Integer> list = this.matriz.get(numColumn);
        list.add(element);
        setColumnMatriz(list, numColumn);
    }

    public void addLine(Integer[] listAdd) {

        for (int i = 0; i < listAdd.length; i++) {
            addElement(i, listAdd[i]);
        }
        updateNumLines();
    }

    private void deletePosition(int numLine, int numColumn) {
        List<Integer> list = this.matriz.get(numColumn);
        list.remove(numLine);
    }

    public void deleteLine(int numLine) {
        for (int i = 0; i < getNumColumns(); i++) {
            deletePosition(numLine, i);
        }
        updateNumLines();
    }

    private void setColumnMatriz(List<Integer> column, int numColumn) {
        this.matriz.set(numColumn, column);
    }

    public void setMatriz(List<List<Integer>> matriz) {
        this.matriz = matriz;
    }
    
    private void updateNumColumns() {
        this.numColumns = this.matriz.size();
    }

    private void updateNumLines() {
        this.numLines = this.matriz.get(getNumColumns() - 1).size();
    }

    public int getValueMatriz(int indexColumn, int indexLine) {
        int x = this.matriz.get(indexColumn).get(indexLine);
        return x;
    }

    public boolean checkMatrizIfNull() {
        Iterator<List<Integer>> iterator = matriz.iterator();

        while (iterator.hasNext()) {
            if (!iterator.next().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public boolean checkIfLineEqualValue(Integer value,Integer numColumn, Integer numLine) {
        return value == getValueMatriz(numColumn, numLine);
    }
    
    public void deleteLinesEqualsValues(Integer[] value, Integer[] numColumn) {        
        for (int i = 0; i < numColumn.length; i++) {
            for(int j = 0; j < getNumLines(); j++) {
                if(checkIfLineEqualValue(value[i], numColumn[i], j)){
                    System.out.println("LINHA DELETADA: " + getLine(j));
                    deleteLine(j);
                }
            }    
        }       
    }
    
    public String getLine(int numLine){
        String line = "";
        
        for(int i = 0; i < getNumColumns(); i++){
            line += getValueMatriz(i, numLine)  + "\t";
        }

        return line;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumLines() {
        return numLines;
    }

     public int getNumLinesBegin(){
         return this.numLinesBegin;
    }
    
    public List<List<Integer>> getMatriz() {
        return matriz;
    }

    public void print(String messageLog) throws IndexOutOfBoundsException{
         try{
            if (messageLog != null) {
                System.out.println("============================== " + messageLog.toUpperCase() + "=====================");
            }
            for (int i = 0; i < getNumLines(); i++) {
                for (int j = 0; j < getNumColumns(); j++) {
                    System.out.print(matriz.get(j).get(i) + "  ");
                }
                System.out.println();
            }

            System.out.println("======================================================== INFORMACAO MATRIZ =================================");

            System.out.println("NUMERO DE COLUNAS: " + getNumColumns());
            System.out.println("NUMERO DE LINHAS: " + getNumLines());

            System.out.println("========================================================");
         }catch(IndexOutOfBoundsException e){
             System.out.println("MATRIZ VAZIA");
         
         }   
            
    }

    public static void main(String[] args) {
        List<List<Integer>> listMatriz = new ArrayList<List<Integer>>();
        List<Integer> listaColumns = null;
        int value = 0;

        for (int i = 0; i < 10; i++) {
            listaColumns = new ArrayList<Integer>();
            for (int j = 0; j < 10; j++, value++) {
                listaColumns.add(value);
            }

            listMatriz.add(listaColumns);
        }
//        System.out.println(listMatriz);

        Matriz matriz = new Matriz(listMatriz);

        matriz.print("matriz antes");
        
        System.out.println("BOOLEAN = " + matriz.checkMatrizIfNull());

//        matriz.deleteLine(2);
//        matriz.deleteLine(2);
//        matriz.deleteLine(7);
        listMatriz = new ArrayList<List<Integer>>();
        matriz.setMatriz(listMatriz);
        
        System.out.println("BOOLEAN = " + matriz.checkMatrizIfNull());

        matriz.print("matriz depois");

    }

}
