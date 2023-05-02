package BusinessLogic;

import Model.Client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
public class FileManager {
    private FileWriter fileWriter;
    private PrintWriter printWriter;

    public FileManager(List<Client> listaClienti) {
        try {
            fileWriter = new FileWriter("simulation_results.txt");
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            System.err.println("Error creating output file: " + e.getMessage());
        }
        printWriter.println("Generated clients: ");
        for (Client client : listaClienti)
            printWriter.println(client.toString());
    }
    public void print(String print){
        printWriter.println(print);
    }
    public void printWriterClose(){
        printWriter.close();
    }
}
