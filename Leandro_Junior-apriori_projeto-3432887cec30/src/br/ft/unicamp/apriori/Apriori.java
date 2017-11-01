package br.ft.unicamp.apriori;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Classe gerenciadora do algoritmo Apriori
 *
 * @author i155822
 */
public class Apriori {

    /**
     * @param args the command line arguments
     */
    private Matriz matrizOld;
    private Matriz matrizCurrent;
    private final Map<String, Ocurrencies> map = new HashMap<>();
    List<Integer> listNumberColumns = new ArrayList<>();
    private final double MIN_SUPPORT;
    private final double MIN_CONFIDENCE;
    private int[] arrayNumberColumnsMatriz;
    private int k_itemSet;
    private int numberOfCombination;
    private Integer[] arrayExclusionIndicators = null;

    public Apriori(List<List<Integer>> matriz, double minSupport, FileReaderDynamic fileReaderDynamic) {
        this.MIN_SUPPORT = minSupport;
        this.MIN_CONFIDENCE = 0; //temporario
    }

    public Apriori(Matriz matriz, double minSupport, double minConfidence) {
        this.matrizCurrent = matriz;
        this.MIN_SUPPORT = minSupport;
        this.MIN_CONFIDENCE = minConfidence; //temporario        
        this.k_itemSet = 1;
        this.numberOfCombination = 0;

        for (int i = 1; i <= this.matrizCurrent.getNumColumns() - 1; i++) {
            this.listNumberColumns.add(i);
        }
    }

    public Matriz getMatrizNew() {
        return this.matrizCurrent;
    }

    public Matriz getMatrizOld() {
        return this.matrizOld;
    }

//================== METODO QUE EXTRAI VALORES DA MATRIZ=======================
    private void analyzingColumns(List<Integer> analyzedColumns) {
        Integer[] values;
        Ocurrencies ocurrencies;

        for (int i = 0; i < this.matrizCurrent.getNumLines(); i++) {
            ocurrencies = new Ocurrencies();
            values = new Integer[analyzedColumns.size()];

            for (int j = 0; j < analyzedColumns.size(); j++) {
                values[j] = this.matrizCurrent.getValueMatriz(analyzedColumns.get(j), i);
            }

            writeMapOcurrencies(values, analyzedColumns, i);
        }
    }

//METODO QUE CONTA QUANTIDADE DE OCORRENCIAS EM UMA COLUNA E E ATUALIZA O MAP DE FORMA PADRONIZADA
    private void writeMapOcurrencies(Integer[] values, List<Integer> analyzedColumns, int numLine) {
        String keyMap = new String();
        Ocurrencies ocurrencies = new Ocurrencies();

        ocurrencies.setColumnsOfValues(analyzedColumns.toArray(new Integer[analyzedColumns.size()]));

        for (int i = 0; i < values.length; i++) {
            if (i == (values.length - 1)) {
                keyMap = keyMap + analyzedColumns.get(i) + "." + values[i];
            } else {
                keyMap = keyMap + analyzedColumns.get(i) + "." + values[i] + " - ";
            }
        }
        if (map.containsKey(keyMap)) {
            ocurrencies = map.get(keyMap);
        } else {
            ocurrencies.setValues(Arrays.asList(values));
        }
        ocurrencies.addLinesOcurrencies(numLine);
        ocurrencies.addNumberOcurrencies();
        map.put(keyMap, ocurrencies);
    }

    private void copyMatrizCurrent() throws CloneNotSupportedException {
        this.matrizOld = (Matriz) this.matrizCurrent.clone();
    }

    public void calcSupportConfidence() throws IOException {
        double support;
        int numberLinesMatrizPrimary = this.matrizCurrent.getNumLinesBegin();
        Ocurrencies ocurrencies;
        List<Ocurrencies> listOcurrencies;

        for (String key : map.keySet()) {
            ocurrencies = map.get(key);
            ocurrencies.calcSupport(numberLinesMatrizPrimary);
            if (ocurrencies.getK_itemSet() != 1 && ocurrencies.getK_itemSet() == this.k_itemSet) {
                listOcurrencies = getOcurrenciesForCalcConfidence(ocurrencies.createListKeysForSeachMap());
                ocurrencies.calcConfidences(listOcurrencies);
            }
 
            map.put(key, ocurrencies);
        }
       // printSupports();
        //printConfidences();
    }

    public List<Ocurrencies> getOcurrenciesForCalcConfidence(List<String> listKeys) {
        List<Ocurrencies> listOcurrencies = new ArrayList<>();
        Iterator<String> iteratorListKeys = listKeys.iterator();
        Ocurrencies ocurrencies;

        while (iteratorListKeys.hasNext()) {
            String key = iteratorListKeys.next();
            ocurrencies = map.get(key);
            listOcurrencies.add(ocurrencies);
        }

        return listOcurrencies;
    }

    public void scannerMapSupportConfidence() throws IOException {
        Ocurrencies ocurrencies;
        
        for(String key : map.keySet()){
            ocurrencies = map.get(key);
            if(ocurrencies.getK_itemSet() == k_itemSet){
                if (ocurrencies.getK_itemSet() != 1) {            
                    if (ocurrencies.verifyDeleteSupportConfidence(MIN_CONFIDENCE, MIN_SUPPORT)) {
                        addArrayExcluisonIndicators(ocurrencies.getLinesOcurrencies());
                    }
                }else if (ocurrencies.verifyDeleteSupport(MIN_SUPPORT)) {   
//                     System.out.println("valor = "+ Arrays.toString(ocurrencies.getValues().toArray()) + "COLUNA = " 
//                                        + Arrays.toString(ocurrencies.getColumnsOfValues())
//                                        + " lines " + Arrays.toString(ocurrencies.getLinesOcurrencies().toArray()));
                     addArrayExcluisonIndicators(ocurrencies.getLinesOcurrencies());
                }
               // System.out.println("arrayExclusion "+Arrays.toString(this.arrayExclusionIndicators));
            }  
        }
    }
    
    private void addArrayExcluisonIndicators(List<Integer> listLinesOcurrencies){
       Iterator<Integer> iterator = listLinesOcurrencies.iterator();
       
       while(iterator.hasNext()){
           this.arrayExclusionIndicators[iterator.next()]++;
       } 
    }

    private void printSupports() {
        split();
        System.out.println("CALCULO SUPORTE");
        for (Object object1 : this.map.keySet()) {
            if (this.map.get((String) object1).getK_itemSet() == this.k_itemSet) {
                System.out.println("{" + object1 + " = " + this.map.get((String) object1).getSupport() + " " + this.map.get((String) object1).getNumberOcurrencies());
            }
        }
    }

    private void printConfidences() {
        int k_itemSetOcurrencies = 0;
        split();
        System.out.println("CALCULO CONFIANÇA");
        for (Object object1 : this.map.keySet()) {
            k_itemSetOcurrencies = this.map.get((String) object1).getK_itemSet();

            if (k_itemSetOcurrencies == this.k_itemSet && k_itemSetOcurrencies != 1) {
                System.out.println("{" + object1 + " = " + Arrays.toString(this.map.get((String) object1).getConfidences().toArray()));
            }
        }
    }
    public void drowLowSupportConfidence() {
        int countDelete = 0;

        for (int i = 0; i < this.arrayExclusionIndicators.length; i++) {
            if (this.arrayExclusionIndicators[i] == this.numberOfCombination) {
                this.matrizCurrent.deleteLine(i - countDelete);
                countDelete++;
            }
        }
    }
    public void result() throws IOException {
        Ocurrencies ocurrencies;
        List<List<String>> listIdentifiers = new ArrayList<>();
        Iterator<List<String>> iteratorListIdentifiers = listIdentifiers.iterator();
        Iterator<String> iteratorIdentifiers;
        
        for (String key : map.keySet()) {
            ocurrencies = map.get(key);
            if(ocurrencies.getK_itemSet() == this.k_itemSet - 2 && !ocurrencies.verifyDeleteSupportConfidence(MIN_CONFIDENCE, MIN_SUPPORT)){
                listIdentifiers.add(ocurrencies.returnIdentifierConfidence(MIN_CONFIDENCE));
            }
        }
        
        for (int j = 0; j < listIdentifiers.size(); j++){
           for(int i = 0; i < listIdentifiers.get(j).size(); i++){
               System.out.println(listIdentifiers.get(j).get(i));
           }      
        }
    }

    public void iteration_K_ItemSet(int k) {
        Combinations combinations = new Combinations(k, this.listNumberColumns);
        List<List<Integer>> listCombinations = combinations.realizarTodasCombinacoes();
        Iterator<List<Integer>> iteratorListCombinations = listCombinations.iterator();
        this.arrayExclusionIndicators = new Integer[this.matrizCurrent.getNumLines()];
        Arrays.fill(this.arrayExclusionIndicators, 0);
        this.numberOfCombination = listCombinations.size();

        while (iteratorListCombinations.hasNext()) {
            analyzingColumns(iteratorListCombinations.next());
        }
    }

    public void executeAlgorithm() throws IOException, CloneNotSupportedException {
        while (!this.matrizCurrent.checkMatrizIfNull() && this.k_itemSet < this.matrizCurrent.getNumColumns()) {
            iteration_K_ItemSet(this.k_itemSet);
            calcSupportConfidence();
            scannerMapSupportConfidence();
            drowLowSupportConfidence();
            //System.out.println("numberCombination = " + this.numberOfCombination + "| arrayExclusion " + Arrays.toString(this.arrayExclusionIndicators));

            if (!this.matrizCurrent.checkMatrizIfNull()) {
                copyMatrizCurrent();
            }
            this.k_itemSet++;
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        try {
            FileReaderDynamic fileReaderDynamic = new FileReaderDynamic("./Arquivos/Arquivo-Dados.txt", 5565, 10);
            //FileReaderDynamic fileReaderDynamic = new FileReaderDynamic("./Arquivos/dadosP.txt", 30, 10);
            //FileReaderDynamic fileReaderDynamic = new FileReaderDynamic("./Arquivos/exemplo.txt", 100, 7);
            //FileReaderDynamic fileReaderDynamic = new FileReaderDynamic("./Arquivos/data.txt", 8, 4);
            split();

            Matriz matriz = new Matriz(fileReaderDynamic.getMatrizData().getMatriz());
            matriz.print("MATRIZ ORIGINAL");

            split();
            Apriori apriori = new Apriori(matriz, 0.2,0.4);
            apriori.executeAlgorithm();
            
            apriori.result();
            

        } catch (IOException ex) {
            System.out.println("Erro desconhecido na execucao do algoritmo \n"
                    + "ERRO:" + ex);
        }
    }

    private static void split() {
        System.out.println("================================================"
                + "===================");
    }

}
