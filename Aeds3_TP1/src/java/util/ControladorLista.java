package src.java.util;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import src.java.models.Lista;
import src.java.models.NanoID;

public class ControladorLista {
    public void criarNovaLista(Scanner scanner){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n--- Novo Usuário ---");
        try{
            System.out.print("Nome da Lista: ");
            String nome = scanner.nextLine();

            System.out.print("Descrição: ");
            String Descricao = scanner.nextLine();

            System.out.print("Data limite (dd/MM/yyyy): ");
            String dataLimiteStr = scanner.nextLine();

            try {
                LocalDate dataLimite = LocalDate.parse(dataLimiteStr, formato);
            } catch (Exception e) {
                System.out.println("Formato inválido! Use dd/MM/yyyy.");
            }
            
            String codigo = NanoID.nonoId();

            LocalDate dataCriacao = LocalDate.now();
            //Lista novaLista = new Lista();

            System.out.println("\n-- Usuário criado com sucesso! (Código compartilhável: " + codigo + ") --\n");
        }catch(Exception e){
            System.err.println("\nOcorreu um erro ao criar a Lista: " + e.getMessage() + "\n");
        }
    }

    public void mostrarLista(){

    }
}
