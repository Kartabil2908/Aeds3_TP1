package src.java.ui;

import java.util.Scanner;

public class Terminal {


    public void exibirTelaInicial(Scanner scanner) {
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("PresenteFácil 1.0");
            System.out.println("-----------------");
            System.out.println();
            System.out.println("(1) Login");
            System.out.println("(2) Novo usuário");
            System.out.println();
            System.out.println("(5) Sair");
            System.out.println();
            System.out.print("Opção: ");

            // Tratamento de exceção para entrada não numérica
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        // Simula um login bem-sucedido e vai para o menu principal
                        System.out.println("\n-- Login efetuado com sucesso! --\n");
                        exibirMenuPrincipal(scanner);
                        break;
                    case 2:
                        // Apenas exibe uma mensagem, pois a lógica de criação de usuário não foi solicitada
                        System.out.println("\n-- Função 'Novo usuário' a ser implementada. --\n");
                        break;
                    case 5:
                        System.out.println("\nSaindo do sistema...");
                        break;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Por favor, digite um número.\n");
            }
        }
    }

    public void exibirMenuPrincipal(Scanner scanner) {
        int opcao = 0;
        // O loop continua até que o usuário decida voltar (neste caso, qualquer número diferente das opções válidas ou o 5 para sair do programa)
        while (opcao != 5) {
            System.out.println("PresenteFácil 1.0");
            System.out.println("-----------------");
            System.out.println("> Início");
            System.out.println();
            System.out.println("(1) Meus dados");
            System.out.println("(2) Minhas listas");
            System.out.println("(3) Produtos");
            System.out.println("(4) Buscar lista");
            System.out.println();
            System.out.println("(5) Sair");
            System.out.println();
            System.out.print("Opção: ");

             // Tratamento de exceção para entrada não numérica
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.println("\n-- Exibindo 'Meus dados'... (Função a ser implementada) --\n");
                        break;
                    case 2:
                        System.out.println("\n-- Exibindo 'Minhas listas'... (Função a ser implementada) --\n");
                        break;
                    case 3:
                        System.out.println("\n-- Exibindo 'Produtos'... (Função a ser implementada) --\n");
                        break;
                    case 4:
                        System.out.println("\n-- Executando 'Buscar lista'... (Função a ser implementada) --\n");
                        break;
                    case 5:
                        System.out.println("\nSaindo do sistema...");
                        // System.exit(0) encerra a aplicação completamente
                        System.exit(0);
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }
            } catch (NumberFormatException e) {
                 System.out.println("\nEntrada inválida. Por favor, digite um número.\n");
            }
        }
    }
}