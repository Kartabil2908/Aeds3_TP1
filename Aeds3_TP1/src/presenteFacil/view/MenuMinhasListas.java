package src.presenteFacil.view;

import java.util.Scanner;

import src.presenteFacil.controller.ControladorListaDePresentes;
import src.presenteFacil.model.*;


public class MenuMinhasListas {

    private ControladorListaDePresentes giftListController;
    private Usuario usuarioLogado;

    public MenuMinhasListas(ControladorListaDePresentes giftListController, Usuario usuarioLogado) {
        this.giftListController = giftListController;
        this.usuarioLogado = usuarioLogado;
    }

    public void exibirMenu(Scanner scanner) throws Exception {
        String opcao;
        boolean continua = true;

        while (continua) {
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas\n"); 
            //System.out.println("LISTAS"); 

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

