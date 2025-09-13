package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;

import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ControladorListaDePresentes {

    private ArquivoLista arqListas;
    
    public ControladorListaDePresentes() throws Exception {
        this.arqListas = new ArquivoLista();
    }

    public void criarNovaLista(Scanner scanner, Usuario usuario) throws Exception {
        
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
            
            String codigo = NanoID.nanoId();
            
            int idUsuario = usuario.getID();
            LocalDate dataCriacao = LocalDate.now();
            Lista novaLista = new Lista(codigo, nome, descricao, dataCriacao, dataLimite, idUsuario);
            int id = arqListas.create(novaLista);

            System.out.println("\n-- Lista criado com sucesso! (Código compartilhável: " + codigo + ") --\n");
        }
        
        catch(Exception e){
            System.err.println("\nOcorreu um erro ao criar a Lista: " + e.getMessage() + "\n");
        }
    }

    public void mostrarLista() throws Exception{

    }

    public void buscarListaPorCodigo(Scanner scanner, ArquivoLista arqListas) {

        System.out.println("\n--- Buscar Lista ---");

        try {
            System.out.print("\nDigite o código da lista: ");
            String codigo = scanner.nextLine();

            Lista lista = arqListas.readByCodigo(codigo);

            if (lista == null) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
            } else {
                System.out.println("\n-- Lista encontrada! --");
                ClearConsole.clearScreen();
                System.out.println("\n--- Dados da Lista ---");
                System.out.println("Nome: " + lista.getNome());
                System.out.println("Descrição: " + lista.getDescricao());
                System.out.println("Data de criação: " + lista.getDataCriacao());
                System.out.println("Data limite: " + lista.getDataLimite());
                System.out.println("ID do Usuário dono: " + lista.getIdUsuario());
                System.out.println("Código compartilhável: " + lista.getCodigo());
                System.out.println("------------------------------\n");
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
    }

    public ArquivoLista getArquivoLista() {
        return this.arqListas;
    }


}
