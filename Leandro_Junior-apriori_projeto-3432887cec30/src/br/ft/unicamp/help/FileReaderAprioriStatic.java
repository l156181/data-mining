/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ft.unicamp.help;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author l156181
 */
public class FileReaderAprioriStatic {
    //====================================================== ATRIBUTOS =======================================================================================
    private static String NAME_FILE = null;
    public static int NUMBER_LINE = 0;  // 4
    public static int NUMBER_COLUNS = 0; // 2
    private int[][] matrizData;

//====================================================== CONSTRUTOR E METODOS GET E SET DA CLASSE =======================================================================================
    public FileReaderAprioriStatic(String NAME_FILE, int NUMBER_LINE, int NUMBER_COLUNS) throws IOException {
        FileReaderAprioriStatic.NAME_FILE = NAME_FILE;
        FileReaderAprioriStatic.NUMBER_COLUNS = NUMBER_COLUNS;
        FileReaderAprioriStatic.NUMBER_LINE = NUMBER_LINE;

        extractDataFromFile();
    }

    public int[][] getMatrizData() {
        return matrizData;
    }

//====================================================== METODO PRIVADO QUE REALIZA EXTRAÇAO DOS DADOS DO ARQUIVO PARA A MATRIZ =================================================
    private void extractDataFromFile() throws IOException {
        String line = null;
        BufferedReader in = null;
        this.matrizData = new int[NUMBER_LINE][];
        int i = 0;

        System.out.println("Obtendo arquivo local em " + NAME_FILE);
        try {
            in = new BufferedReader(new FileReader(NAME_FILE));

            while ((line = in.readLine()) != null && (i < NUMBER_LINE)) {
                String lineData[] = line.split("\t");
                this.matrizData[i] = dataFixer(lineData);
                i++;
            }
        } catch (IOException e) {
            System.err.println("Não foi possível acessar o arquivo.");
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }

        }
    }
 

    private int[] dataFixer(String[] vectorData) {
        int[] array = new int[vectorData.length];

        for (int i = 0; i < vectorData.length; i++) {

            if (i == 1 || i == 0) {
                array[i] = Integer.parseInt(vectorData[i]);
            } else if ("-".equals(vectorData[i].substring(0, 1))) {
                array[i] = 0;
            } else {
                array[i] = Integer.parseInt(vectorData[i].substring(0, 1));
            }

        }
        return array;
    }
    
//====================================================================================================================================================

    public static void main(String args[]) throws IOException {

        FileReaderAprioriStatic file = new FileReaderAprioriStatic("./Arquivos/Arquivo-Dados.txt", 5565, 10);
        int[][] arrayfile = file.getMatrizData();

        for (int i = 0; i < arrayfile.length; i++) {
            for (int j = 0; j < FileReaderAprioriStatic.NUMBER_COLUNS; j++) {
                System.out.print(arrayfile[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
