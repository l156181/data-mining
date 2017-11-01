package br.ft.unicamp.apriori;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Registra o número de ocorrencias, as posicições onde ocorreram as ocorrencias
 * e o valor do suporte correspondente. Vale lembrar que arrayIndex.size() =
 * numberOcurrencies
 *
 * @author l156181
 */
public class Ocurrencies {

    private int numberOcurrencies;
    private double support;
    private List<Double> confidences;
    private Integer[] columnsOfValues;
    private List<Integer> values;
    private List<String> listKeys;
    private List<String> listKeysTemporary;
    private List<String> listidentifierConfidence;
    private List<Integer> linesOcurrencies;

    public Ocurrencies() {
        this.numberOcurrencies = 0;
        this.support = 0;
        this.confidences = new ArrayList<>();
        this.values = new ArrayList<>();
        this.listKeys = new ArrayList<>();
        this.listidentifierConfidence = new ArrayList<>();
        this.listKeysTemporary  = new ArrayList<>();
        this.linesOcurrencies = new ArrayList<>();
    }
    
    public void addLinesOcurrencies(Integer line){
        if(!linesOcurrencies.contains(line)){
           linesOcurrencies.add(line);
        }
    }
    public void addNumberOcurrencies() {
        this.numberOcurrencies++;
    }    
    public int getK_itemSet() {
        return this.values.size();
    }
    public List<Double> getConfidences() {
        return this.confidences;
    }
    public int getNumberOcurrencies() {
        return this.numberOcurrencies;
    }
    public List<Integer> getLinesOcurrencies(){
        return linesOcurrencies;    
    }
    public double getSupport() {
        return support;
    }
    public List<String> getListKeys() {
        return listKeys;
    }
     public List<Integer> getValues() {
        return values;
    }
    public Integer[] getColumnsOfValues() {
        return columnsOfValues;
    }
    public void setSupport(double support) {
        this.support = support;
    }
    public void setListKeys(List<String> listKeys) {
        this.listKeys = listKeys;
    }
    public void setColumnsOfValues(Integer[] columnsOfValues) {
        this.columnsOfValues = columnsOfValues;
    }
    public void setValues(List<Integer> values) {
        this.values = values;
    }
    public boolean verifyDeleteSupportConfidence(double MIN_CONFIDENCE, double MIN_SUPPORT) throws IOException {
        //System.out.println("verfifyDelete " + (!verifyConfidence(MIN_CONFIDENCE) || !verifySupport(MIN_SUPPORT)));
        return (!verifyConfidence(MIN_CONFIDENCE) || !verifySupport(MIN_SUPPORT));
    }
    public boolean verifyDeleteSupport(double MIN_SUPPORT) throws IOException {
        //System.out.println(" verifySupport " + !verifySupport(MIN_SUPPORT));
        return !verifySupport(MIN_SUPPORT);
    }
    // há alguma confiança positiva
    public boolean verifyConfidence(double MIN_CONFIDENCE) throws IOException {
        Iterator<Double> iteratorConfidences = this.confidences.iterator();
        double confidence;
        int contIfConfidence = 0;

        while (iteratorConfidences.hasNext()) {
            confidence = iteratorConfidences.next();
            if (confidence > MIN_CONFIDENCE) {
                contIfConfidence++;
            }
        }
        return contIfConfidence > 0;
    }
    public boolean verifySupport(double MIN_SUPPORT) throws IOException {
        //System.out.println(getSupport() + " "+ MIN_SUPPORT + " " +(getSupport() > MIN_SUPPORT));
        return (getSupport() > MIN_SUPPORT);
    }
    public void calcSupport(int numberLinesMatriz) {
        this.support = (double) this.numberOcurrencies / numberLinesMatriz;
    }
    public void calcConfidences(List<Ocurrencies> listOcurrencies) {
        Iterator<Ocurrencies> iteratorListOcurrencies = listOcurrencies.iterator();
        Double confidence;
        Ocurrencies ocurrencies;
        this.listKeysTemporary = new ArrayList<>(listKeys);

        while (iteratorListOcurrencies.hasNext()) {
            confidence = 0.0;
            ocurrencies = iteratorListOcurrencies.next();
            confidence = (double) this.getNumberOcurrencies() / ocurrencies.getNumberOcurrencies();
            this.confidences.add(confidence);
            this.listidentifierConfidence.add(createStringRepresentsConfidence());
        }
    }
    public List<String> createListKeysForSeachMap() {
        toStringKeys();
        List<String> listKeysForSearch = new ArrayList<>();

        String keysConcat;
        int i = 0;

        while (listKeysForSearch.size() < getK_itemSet()) {
            keysConcat = "";
            keysConcat = concatKeys(i);
            listKeysForSearch.add(keysConcat);
            i++;
        }

        return listKeysForSearch;
    }
    public String concatKeys(int posDeleteConcat) {
        String keysConcat = "";

        for (int i = 0; i < this.listKeys.size(); i++) {
            if (i == posDeleteConcat) {
                continue;
            }
            if (!keysConcat.isEmpty()) {
                keysConcat += " - ";
            }

            keysConcat += this.listKeys.get(i);
        }

        return keysConcat;
    }
    public void toStringKeys() {
        List<String> arrayKeysMap = new ArrayList<>();
        String keyString;

        for (int i = 0; i < this.values.size(); i++) {
            keyString = new String();
            keyString = this.columnsOfValues[i] + "." + this.values.get(i);
            arrayKeysMap.add(keyString);
        }

        this.listKeys = arrayKeysMap;
    }
    public String createStringRepresentsConfidence() {
        List<String> list = this.listKeysTemporary;
        String key = "";

        key = list.get(0);
        list.remove(0);
        list.add(key);
        key = "";
        
        for (int i = 0; i < list.size(); i++) {
            if(i == 0){
                key = key + list.get(i);
            }else if (i == list.size() - 1){
                key = key  +  ", " +  " -> "  + list.get(i);
            }else{
                key = key  +  ", " + list.get(i);
            }
        }
        return key;
    }
    public List<String> returnIdentifierConfidence(double MIN_CONFIDENCE){
        List<String> listIdentifierConfidence = new ArrayList<>();
        
        for(int i = 0; i < this.confidences.size(); i++){
            if(this.confidences.get(i) > MIN_CONFIDENCE){               
                listIdentifierConfidence.add(this.listidentifierConfidence.get(i));
            }
        }
        
        return listIdentifierConfidence;
    }
}
