package main.java.sol;

import main.java.writer.DataWriter;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = "test1.txt";
        Company ft = new Company();
        ft.setOperationMode("PRODUCT"); // "PRODUCT" or "DEBUG"
//        ft.setReader(new BufferedReader(new BufferedReader(new InputStreamReader(System.in))));
        ft.setReader(new BufferedReader(new BufferedReader(new FileReader(new File("./data/" + filename)))));
//        ft.setWriter(new DataWriter(new BufferedWriter(new OutputStreamWriter(System.out))));
        ft.setWriter(new DataWriter(new BufferedWriter(new FileWriter(new File("./data/" + filename.substring(0, filename.length() - 4) + "_out" + ".txt")))));
        ft.execute();
    }
}
