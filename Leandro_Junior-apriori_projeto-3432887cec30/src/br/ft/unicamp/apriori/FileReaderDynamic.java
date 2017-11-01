package br.ft.unicamp.apriori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Possui como responsabilidade executar a leitura do arquivo de dados .txt e
 * gerar uma matriz de listas, sendo que cada lista representa um dos parametros
 * analisados no algoritmo
 *
 */
public class FileReaderDynamic {

//=========================== ATRIBUTOS =======================================
    private String FILE_NAME = null;
    public  int LINE_NUMBER = 0;  
    public  int COLUMNS_NUMBER = 0;
    private Matriz m = new Matriz();
    
//===========================CONSTRUTOR E METODOS GET E SET DA CLASSE ==========
    public FileReaderDynamic(String NAME_FILE, int LINE_NUMBER, int COLUMNS_NUMBER) throws IOException {
        this.FILE_NAME = NAME_FILE;
        this.LINE_NUMBER = LINE_NUMBER;
        this.COLUMNS_NUMBER = COLUMNS_NUMBER;
        
        instanceMatriz();
        extractDataFromFile();
    }

     private void instanceMatriz() {
        for (int i = 0; i < this.COLUMNS_NUMBER; i++) {
           this.m.addColumn(new ArrayList<Integer>());
        }
    }
    
    public Matriz getMatrizData() {
        return this.m;
    }

//========= METODO PRIVADO QUE REALIZA EXTRAÇAO DOS DADOS DO ARQUIVO PARA A MATRIZ ==========
    private void extractDataFromFile() throws IOException {
        String line;
        BufferedReader in = null;
        int i = 0;

        System.out.println("Obtendo arquivo local em " + FILE_NAME);
        try {
            in = new BufferedReader(new FileReader(FILE_NAME));

            while ((line = in.readLine()) != null && (i < LINE_NUMBER)) {
                String lineData[] = line.split("\t");
                dataFixer(lineData);
                i++;

            }
        } catch (IOException e) {
            System.err.println("Não foi possível acessar o arquivo.");
        } finally {
            if (in != null) {
                in.close();
            }

        }
    }
    
    private void dataFixer(String[] vectorData) {
        Integer[] lineForAdd = new Integer[vectorData.length];
        
        for (int i = 0; i < vectorData.length; i++) {
            
            if (i == 1 || i == 0) {     
                lineForAdd[i] = Integer.parseInt(vectorData[i]);
            } else if ("-".equals(vectorData[i].substring(0, 1))) {
                lineForAdd[i] = 0;

            } else {
                 lineForAdd[i] = Integer.parseInt(vectorData[i].substring(0, 1));
            }
        }
        
        this.m.addLine(lineForAdd); 
    }

// =============================================================================
    public static void main(String[] args) throws IOException {
        FileReaderDynamic file = new FileReaderDynamic("./Arquivos/data.txt", 8, 4);
        
        Matriz matriz = file.getMatrizData();
        matriz.print("MATRIZ");
        
    }
}
