// src/java/ui/Terminal.java
package src.java.ui;

import java.util.Scanner;
import src.java.util.ControladorUsuario; 
import src.java.models.Usuario;

/**
 * Classe responsável por exibir e controlar a interface de linha de comando (terminal) 
 * do sistema PresenteFácil 1.0. 
 * 
 * Através desta interface, o usuário pode:
 * <ul>
 *   <li>Realizar login;</li>
 *   <li>Criar uma nova conta de usuário;</li>
 *   <li>Acessar o menu principal após autenticação;</li>
 *   <li>Consultar e gerenciar dados pessoais e listas (futuras implementações);</li>
 *   <li>Deletar a própria conta;</li>
 *   <li>Efetuar logout.</li>
 * </ul>
 *
 * O Terminal depende de um {@link ControladorUsuario} para realizar as operações
 * relacionadas à autenticação e ao gerenciamento de usuários.
 * 
 * @author Bernardo
 * @version 1.0
 */
public class Terminal {

    /** Controlador responsável pela lógica de usuários (injeção de dependência). */
    private ControladorUsuario controlador;

    /**
     * Construtor da classe Terminal.
     * 
     * @param controlador instância de {@link ControladorUsuario} responsável pelas operações de usuário
     */
    public Terminal(ControladorUsuario controlador) {
        this.controlador = controlador;
    }

    /**
     * Exibe a tela inicial do sistema, onde o usuário pode escolher entre:
     * <ul>
     *   <li>Fazer login;</li>
     *   <li>Criar um novo usuário;</li>
     *   <li>Encerrar o programa.</li>
     * </ul>
     *
     * @param scanner objeto {@link Scanner} para leitura de entradas do usuário
     */
    public void exibirTelaInicial(Scanner scanner) {
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("PresenteFácil 1.0");
            System.out.println("-----------------");
            System.out.println("\n(1) Login");
            System.out.println("(2) Novo usuário");
            System.out.println("\n(5) Sair do Programa");
            System.out.print("\nOpção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        Usuario usuarioLogado = controlador.loginUsuario(scanner);
                        if (usuarioLogado != null) {
                            System.out.println("\n-- Login efetuado com sucesso! Bem-vindo(a), " + usuarioLogado.getNome() + ". --\n");
                            exibirMenuPrincipal(scanner);
                        } else {
                            System.out.println("\n-- E-mail ou senha inválidos. Tente novamente. --\n");
                        }
                        break;
                    case 2:
                        controlador.criarNovoUsuario(scanner);
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

    /**
     * Exibe o menu principal após o login do usuário.
     * 
     * Permite ao usuário:
     * <ul>
     *   <li>Acessar seus dados pessoais (a implementar);</li>
     *   <li>Gerenciar listas (a implementar);</li>
     *   <li>Deletar a própria conta;</li>
     *   <li>Efetuar logout e retornar à tela inicial.</li>
     * </ul>
     *
     * @param scanner objeto {@link Scanner} para leitura de entradas do usuário
     */
    public void exibirMenuPrincipal(Scanner scanner) {
        int opcao = 0;
        while (true) { 
            System.out.println("Menu Principal");
            System.out.println("-----------------");
            System.out.println("(1) Meus dados");
            System.out.println("(2) Minhas listas");
            System.out.println("(3) Deletar minha conta");
            System.out.println("\n(5) Desconectar (Logout)");
            System.out.print("\nOpção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.println("\n-- Função 'Meus dados' a ser implementada. --\n");
                        break;
                    case 2:
                        System.out.println("\n-- Função 'Minhas listas' a ser implementada. --\n");
                        break;
                    case 3:
                        boolean foiDeletado = controlador.deletarProprioUsuario(scanner);
                        if (foiDeletado) {
                            return;
                        }
                        break;
                    case 5:
                        controlador.logout();
                        System.out.println("\n-- Você foi desconectado. --\n");
                        return;
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
