package src.presenteFacil.view;

import java.util.Scanner;

import src.presenteFacil.controller.ControladorListaDePresentes;
import src.presenteFacil.model.*;

/**
 * Classe responsável por exibir e gerenciar o menu "Minhas Listas"
 * do sistema PresenteFácil 1.0.
 *
 * Aqui o usuário pode:
 * - Criar novas listas de presentes
 * - Consultar as listas existentes (a ser expandido no TP2)
 * - Retornar ao menu principal
 * 
 * As listas são vinculadas ao {@link Usuario} atualmente logado.
 * 
 * @author Yasmin
 * @version 1.0
 */

public class MenuMinhasListas {

    private ControladorListaDePresentes giftListController;
    private Usuario usuarioLogado;

    /**
     * Construtor do menu Minhas Listas.
     * 
     * @param giftListController controlador responsável pelas operações de listas
     * @param usuarioLogado usuário autenticado no sistema
     */
    public MenuMinhasListas(ControladorListaDePresentes giftListController, Usuario usuarioLogado) {
        this.giftListController = giftListController;
        this.usuarioLogado = usuarioLogado;
    }

    /**
     * Exibe o menu "Minhas Listas" e processa as opções do usuário.
     * 
     * @param scanner objeto Scanner para leitura de entrada do usuário
     * @throws Exception 
    */
    public void exibirMenu(Scanner scanner) throws Exception {
        String opcao;
        boolean continua = true;

        while (continua) {
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas\n"); 
            System.out.println("LISTAS"); 
            System.out.println("\n"); 

            Lista[] listas = giftListController.mostrarMinhasListas(usuarioLogado);
             
            System.out.println("(N) Nova Lista"); 
            System.out.println("(R) Retornar ao menu anterior"); 
            System.out.println();
            System.out.print("\nOpção: "); 

            opcao = scanner.nextLine().trim().toUpperCase(); 
            System.out.println(opcao);

            switch (opcao) {
                case "N":
                    giftListController.criarNovaLista(scanner, usuarioLogado);
                    break;
                case "R":
                    System.out.println("\n-- Retornando ao menu anterior. --\n");
                    continua = false;
                    break;
                default:
                    if(isNumber(opcao)){ 
                        int indice = Integer.parseInt(opcao); 
                        System.out.println("\n[Selecionou a lista " + indice + "]\n"); 

                        if(listas != null && indice > 0 && indice <= listas.length){ 
                            giftListController.MostrarLista(scanner, listas[indice - 1]); 
                        } else { 
                            System.out.println("\nOpição Invalida. Tente novamente.\n");
                        } 
                    }else{
                        System.out.println("\nOpição Invalida. Tente novamente.\n");
                    }
            }
        }
    }

    public boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

