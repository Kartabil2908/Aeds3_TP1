// src/java/ui/Terminal.java
package src.java.ui;

import java.util.Scanner;

import src.java.util.ControladorLista;
import src.java.util.ControladorUsuario;
import src.java.models.Usuario;

/**
 * Classe responsável por exibir e controlar a interface de linha de comando (terminal)
 * do sistema PresenteFácil 1.0.
 *
 * Através desta interface, o usuário pode:
 * <ul>
 * <li>Realizar login;</li>
 * <li>Criar uma nova conta de usuário;</li>
 * <li>Acessar o menu principal após autenticação;</li>
 * <li>Consultar seus dados pessoais;</li>
 * <li>Desativar ou Excluir a própria conta;</li>
 * <li>Efetuar logout.</li>
 * </ul>
 *
 * O Terminal depende de um {@link ControladorUsuario} para realizar as operações
 * relacionadas à autenticação e ao gerenciamento de usuários.
 *
 * @author Bernardo
 * @version 1.3
 */
public class Terminal {

    /** Controlador responsável pela lógica de usuários (injeção de dependência). */
    private ControladorUsuario controlador;
    private ControladorLista controladorLista;
    Usuario usuarioLogado;
    /**
     * Construtor da classe Terminal.
     *
     * @param controlador instância de {@link ControladorUsuario} responsável pelas operações de usuário
     */
    public Terminal(ControladorUsuario controlador) {
        this.controlador = controlador;
    }

    // ... (o método exibirTelaInicial permanece o mesmo) ...
        /**
     * Exibe a tela inicial do sistema, onde o usuário pode escolher entre:
     * <ul>
     * <li>Fazer login;</li>
     * <li>Criar um novo usuário;</li>
     * <li>Encerrar o programa.</li>
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
                        usuarioLogado = controlador.loginUsuario(scanner);
                        if (usuarioLogado != null) {
                            System.out.println("\n-- Login efetuado com sucesso! Bem-vindo(a), " + usuarioLogado.getNome() + ". --\n");
                            exibirMenuPrincipal(scanner);
                        } else {
                            System.out.println("\n-- E-mail ou senha inválidos, ou conta desativada. Tente novamente. --\n");
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
     * <li>Acessar seus dados pessoais;</li>
     * <li>Gerenciar listas (a implementar);</li>
     * <li>Desativar ou Excluir a própria conta;</li>
     * <li>Efetuar logout e retornar à tela inicial.</li>
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
            System.out.println("(3) Desativar minha conta");
            System.out.println("(4) Excluir minha conta"); // Nova opção
            System.out.println("\n(5) Desconectar (Logout)");
            System.out.print("\nOpção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        controlador.exibirDadosDoUsuarioLogado();
                        break;
                    case 2:
                        exibirMinhasListas(scanner);
                        break;
                    case 3:
                        boolean foiDesativado = controlador.desativarPropriaConta(scanner);
                        if (foiDesativado) {
                            return; // Retorna para a tela inicial
                        }
                        break;
                    case 4: // Novo case
                        boolean foiExcluido = controlador.excluirPropriaConta(scanner);
                        if (foiExcluido) {
                            return; // Retorna para a tela inicial
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

    public void exibirMinhasListas(Scanner scanner){
        String opcao;
        boolean continua = true;

        while(continua){ 
            System.out.println("PresenteFácil 1.0");
            System.out.println("-----------------");
            System.out.println("> Inicio > Minhas Listas");
            System.out.println();
            System.out.println("LISTAS");

            System.out.println();
            System.out.println("(N) Nova Lista");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.println();
            System.out.print("Opção: ");

            try {
                opcao = scanner.nextLine().toUpperCase();

                switch (opcao) {
                    case "N":
                        controladorLista = new ControladorLista();
                        controladorLista.criarNovaLista(scanner, usuarioLogado);
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        exibirMenuPrincipal(scanner);
                        break;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }
            }catch (Exception e) {
                System.out.println("\nErro ao ler entrada. Tente novamente.\n");
            }
        }
    }
}