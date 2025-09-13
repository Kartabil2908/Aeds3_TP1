package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ControladorListaDePresentes {

    private ArquivoLista arqListas;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ControladorListaDePresentes() throws Exception {
        this.arqListas = new ArquivoLista();
    }

    public void criarNovaLista(Scanner scanner, Usuario usuario) {
        
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > Nova Lista\n");
        try{
            System.out.print("Nome da Lista: ");
            String nome = scanner.nextLine();

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            LocalDate dataLimite = null;
            while(dataLimite == null) {
                System.out.print("Data limite (dd/MM/yyyy): ");
                String dataLimiteStr = scanner.nextLine();
                try {
                    dataLimite = LocalDate.parse(dataLimiteStr, formato);
                } catch(DateTimeParseException e) {
                    System.out.println("\n-- Data em formato inválido. Tente novamente. --\n");
                }
            }
            
            String codigo = NanoID.nanoId();
            
            int idUsuario = usuario.getID();
            LocalDate dataCriacao = LocalDate.now();
            Lista novaLista = new Lista(codigo, nome, descricao, dataCriacao, dataLimite, idUsuario);
            arqListas.create(novaLista);

            System.out.println("\n-- Lista criada com sucesso! (Código compartilhável: " + codigo + ") --\n");
        }
        
        catch(Exception e){
            System.err.println("\nOcorreu um erro ao criar a Lista: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public Lista[] mostrarMinhasListas(Usuario usuario) throws Exception{
        try{
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas\n");
            System.out.println("LISTAS");
            Lista[] listas = arqListas.readByUsuario(usuario.getId());

            if (listas == null || listas.length == 0) {
                System.out.println("\n-- Nenhuma lista cadastrada. --\n");
                return null;
            }

            Arrays.sort(listas, Comparator.comparing(Lista::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < listas.length; i++) {
                System.out.println("(" + (i + 1) + ") " + listas[i].getNome() + " - "
                    + listas[i].getDataCriacao().format(formato));
            }
            System.out.println();
            return listas;
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
        return null;
    }

    public void buscarListaPorCodigo(Scanner scanner, ArquivoLista arqListas) {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Buscar Lista\n");

        try {
            System.out.print("\nDigite o código da lista: ");
            String codigo = scanner.nextLine();
            Lista lista = arqListas.readByCodigo(codigo);
            
            if (lista == null) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
            } else {
                System.out.println("\n-- Lista encontrada! --");
                ClearConsole.clearScreen();
                System.out.println("-------- PresenteFácil 1.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + "\n");
                System.out.println("Nome: " + lista.getNome());
                System.out.println("Descrição: " + lista.getDescricao());
                System.out.println("Data de criação: " + lista.getDataCriacao().format(formato));
                System.out.println("Data limite: " + lista.getDataLimite().format(formato));
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
                return;
            }
            
            ClearConsole.clearScreen();
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas > " + lista.getNome() + "\n");
            System.out.println("Nome: " + lista.getNome());
            System.out.println("Descrição: " + lista.getDescricao());
            System.out.println("Data de criação: " + lista.getDataCriacao().format(formato));
            System.out.println("Data limite: " + lista.getDataLimite().format(formato));
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

                switch (opcao) {
                    case "1":
                        System.out.println("\n[Funcionalidade Produtos será implementada no TP2]\n");
                        break;
                    case "2":
                        alterarDadosLista(scanner, lista);
                        continua = false; 
                        break;
                    case "3":
                        boolean foiDeletado = deletarLista(scanner, lista.getId(), lista.getNome());
                        if (foiDeletado) {
                            continua = false;
                        }
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    default:
                        System.out.println("\nOpção Inválida. Tente novamente.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao mostrar lista: " + e.getMessage() + "\n");
        }
    }

    public void alterarDadosLista(Scanner scanner, Lista lista) {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Alterar Dados da Lista\n");
        System.out.println("\n----- Alterar Dados da Lista ------");
        System.out.println("[Deixe o campo em branco para manter a informação atual.]\n");

        try {
            System.out.println("Nome atual: " + lista.getNome());
            System.out.print("Novo nome: ");
            String novoNome = scanner.nextLine();
            if (!novoNome.trim().isEmpty()) {
                lista.setNome(novoNome);
            }

            System.out.println("\nDescrição atual: " + lista.getDescricao());
            System.out.print("Nova descrição: ");
            String novaDescricao = scanner.nextLine();
            if (!novaDescricao.trim().isEmpty()) {
                lista.setDescricao(novaDescricao);
            }

            boolean dataValida = false;
            while(!dataValida) {
                System.out.println("\nData limite atual: " + lista.getDataLimite().format(formato));
                System.out.print("Nova data limite (dd/MM/yyyy): ");
                String novaDataStr = scanner.nextLine();
                if (novaDataStr.trim().isEmpty()) {
                    dataValida = true;
                } else {
                    try {
                        LocalDate novaData = LocalDate.parse(novaDataStr, formato);
                        lista.setDataLimite(novaData);
                        dataValida = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("\n-- Formato de data inválido. Tente novamente. --\n");
                    }
                }
            }
            
            if (arqListas.update(lista)) {
                System.out.println("\n-- Lista alterada com sucesso! --\n");
            } else {
                System.out.println("\n-- Erro ao alterar a lista. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao alterar a lista: " + e.getMessage() + "\n");
        }
    }

    public ArquivoLista getArquivoLista() {
        return this.arqListas;
    }

    public boolean deletarLista(Scanner scanner, int idLista, String nome) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + nome + " > Deletar Lista\n");
        System.out.println("\n-------- Deletar Lista ----------");
        System.out.print("Você tem certeza que deseja deletar esta lista? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();

        if (!confirmacao.equals("S")) {
            System.out.println("\n-- Operação cancelada. --\n");
            return false;
        }
        
        try {
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