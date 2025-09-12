package src.java.util;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import src.java.models.*;
import src.java.util.*;
import src.java.complementary.*;

public class ControladorLista {

    private ArquivoLista arqListas;

    public void criarNovaLista(Scanner scanner, Usuario usuario){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n--- Nova Lista ---");

        try{
            System.out.print("Nome da Lista: ");
            String nome = scanner.nextLine();

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            System.out.print("Data limite (dd/MM/yyyy): ");
            String dataLimiteStr = scanner.nextLine();

            
            LocalDate dataLimite = LocalDate.parse(dataLimiteStr, formato);
            String codigo = NanoID.nonoId();
            
            int idUsuario = usuario.getID();
            LocalDate dataCriacao = LocalDate.now();
            Lista novaLista = new Lista(codigo, nome, descricao, dataCriacao, dataLimite, idUsuario);
            int id = arqListas.create(novaLista);

            System.out.println("\n-- Usuário criado com sucesso! (Código compartilhável: " + codigo + ") --\n");
        }catch(Exception e){
            System.err.println("\nOcorreu um erro ao criar a Lista: " + e.getMessage() + "\n");
        }
    }

    public void mostrarLista(){

    }
}
