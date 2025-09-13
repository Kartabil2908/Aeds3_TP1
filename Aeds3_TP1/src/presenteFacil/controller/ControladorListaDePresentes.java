package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;

import java.util.ArrayList;
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

    public Lista[] mostrarMinhasListas(Usuario usuario) throws Exception{
        try{
            System.out.println("\n--- Listas Cadastradas ---\n");
            Lista[] listas = arqListas.readByUsuario(usuario.getId());

            if (listas.length == 0) {
                System.out.println("\n-- Nenhuma lista cadastrada. --\n");
                return null;
            }
            for (int i = 0; i < listas.length; i++) {
                System.out.println("(" + (i + 1) + ") " + listas[i].getNome() + " - "
                    + listas[i].getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            System.out.println();
            return listas;
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
        return null;
    }

    public void buscarListaPorCodigo(Scanner scanner, ArquivoLista arqListas) {

        System.out.println("\n--- Buscar Lista ---");

        try {
            System.out.print("\nDigite o código da lista: ");
            String codigo = scanner.nextLine();
            System.out.println("Código digitado: " + codigo); // Debug
            Lista lista = arqListas.readByCodigo(codigo);
            System.out.println("ok"); // Debug
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

    public void MostrarLista(Scanner scanner, Lista lista) throws Exception{
        try {
            if (lista == null) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
            } else {
                System.out.println("\n-- Lista encontrada! --");
                ClearConsole.clearScreen();
                System.out.println("\n--- Dados da Lista ---\n");
                System.out.println("Nome: " + lista.getNome());
                System.out.println("Descrição: " + lista.getDescricao());
                System.out.println("Data de criação: " + lista.getDataCriacao());
                System.out.println("Data limite: " + lista.getDataLimite());
                System.out.println("ID do Usuário dono: " + lista.getIdUsuario());
                System.out.println("Código compartilhável: " + lista.getCodigo());
                System.out.println();

                String opcao;
                boolean continua = true;

                while(continua) {
                    System.out.println("(1) Gerenciar produtos da lista");
                    System.out.println("(2) Alterar dados da lista");
                    System.out.println("(3) Excluir lista");
                    System.out.println();
                    System.out.println("(R) Retornar ao menu anterior");
                    System.out.println();
                    System.out.print("\nOpção: ");

                    opcao = scanner.nextLine().trim().toUpperCase();
                    System.out.println(opcao);

                    switch (opcao) {
                        case "1":
                            System.out.println("\n[Funcionalidade Produtos será implementada no TP2]\n");
                            break;
                        case "2":
                            System.out.println("\n-- implententar!!!!. --\n");
                            continua = false;
                            break;
                        case "3":
                            boolean foiDeletado = deletarLista(scanner);
                            if (foiDeletado) {
                                continua = false;
                            }
                            break;
                        case "R":
                            System.out.println("\n-- Retornando ao menu anterior. --\n");
                            continua = false;
                            break;
                        default:
                            System.out.println("\nOpição Invalida. Tente novamente.\n");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
    }

    public ArquivoLista getArquivoLista() {
        return this.arqListas;
    }

    public boolean deletarLista(Scanner scanner) throws Exception {
        System.out.println("\n--- Deletar Lista ---");
        try {
            System.out.print("Digite o ID da lista a ser deletada: ");
            int idLista = Integer.parseInt(scanner.nextLine());

            boolean sucesso = arqListas.delete(idLista);
            if (sucesso) {
                System.out.println("\n-- Lista deletada com sucesso! --\n");
            } else {
                System.out.println("\n-- Nenhuma lista encontrada com esse ID. --\n");
            }
            return sucesso;
        } catch (Exception e) {
            System.err.println("\nErro ao deletar lista: " + e.getMessage() + "\n");
            return false;
        }

    }

}
