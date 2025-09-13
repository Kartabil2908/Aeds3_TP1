package src.presenteFacil.view;
import src.presenteFacil.utils.ClearConsole;

import java.util.Scanner;

import src.presenteFacil.controller.ControladorUsuario;
import src.presenteFacil.controller.ControladorListaDePresentes;
import src.presenteFacil.model.Usuario;

/**
 * Classe responsável por exibir o menu principal do sistema
 * PresenteFácil 1.0, acessível após o login do usuário.
 * 
 * Nela o usuário pode:
 * - Ver seus dados pessoais
 * - Gerenciar listas
 * - Desativar ou excluir sua conta
 * - Efetuar logout
 * 
 * @author Yasmin
 * @version 1.0
 */
public class MenuUsuario {

    private ControladorUsuario userController;
    private ControladorListaDePresentes giftListController;
    private Usuario usuarioLogado;

    public MenuUsuario(ControladorUsuario userController, Usuario usuarioLogado) throws Exception {
        this.userController = userController;
        this.usuarioLogado = usuarioLogado;
        this.giftListController = new ControladorListaDePresentes();
    }

    /**
     * Exibe o menu principal para o usuário logado.
     */
    public void exibir(Scanner scanner) throws Exception{

        int opcao = 0;

        while (true) {
            System.out.println("------- Menu Principal -------");
            System.out.println("------------------------------");
            System.out.println("\n> Início\n");
            System.out.println("(1) Meus dados................");
            System.out.println("(2) Minhas listas.............");
            System.out.println("(3) Produtos..................");
            System.out.println("(4) Buscar lista..............");
            System.out.println("(5) Desativar minha conta.....");
            System.out.println("(6) Excluir minha conta.......");

            System.out.println("\n(S) Sair....................");

            System.out.print("\nOpção: ");

            String entrada = scanner.nextLine().toUpperCase();
            ClearConsole.clearScreen();

            try {

                if (entrada.equals("S")) {
                    userController.logout();
                    System.out.println("\n-- Você foi desconectado. --\n");
                    return;
                }

                opcao = Integer.parseInt(entrada);

                switch (opcao) {

                    case 1:
                        userController.exibirDadosDoUsuarioLogado();
                        break;

                    case 2:
                        MenuMinhasListas menuListas = new MenuMinhasListas(giftListController, usuarioLogado);
                        menuListas.exibir(scanner);
                        break;

                    case 3:
                        System.out.println("\n[Funcionalidade Produtos será implementada no TP2]\n");
                        break;

                    case 4:
                        //System.out.println("\n[Buscar lista por código NanoID - implementar aqui]\n");
                        giftListController.buscarListaPorCodigo(scanner, giftListController.getArquivoLista());
                        break;

                    case 5:
                        boolean foiDesativado = userController.desativarPropriaConta(scanner);
                        if (foiDesativado) return;
                        break;

                    case 6:
                        boolean foiExcluido = userController.excluirPropriaConta(scanner);
                        if (foiExcluido) return;
                        break;

                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Digite um número ou 'S'.\n");
            }
        }
    }
}
