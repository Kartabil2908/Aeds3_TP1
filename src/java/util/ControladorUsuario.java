// src/java/util/ControladorUsuario.java
package src.java.util;

import java.util.Scanner;
import src.java.complementary.ArquivoUsuario;
import src.java.models.Usuario;

/**
 * Controlador responsável por gerenciar a lógica de usuários no sistema PresenteFácil 1.0.
 * 
 * Ele fornece métodos para:
 * <ul>
 *   <li>Criar novos usuários;</li>
 *   <li>Efetuar login;</li>
 *   <li>Logout;</li>
 *   <li>Deletar a própria conta do usuário logado.</li>
 * </ul>
 * 
 * Mantém internamente o estado do usuário atualmente logado.
 * 
 * @author Bernardo
 * @version 1.0
 */
public class ControladorUsuario {

    /** Arquivo persistente de usuários. */
    private ArquivoUsuario arqUsuarios;

    /** Usuário atualmente logado na sessão; null se ninguém estiver logado. */
    private Usuario usuarioLogado;

    /**
     * Construtor do controlador.
     * Inicializa o arquivo de usuários e garante que nenhum usuário esteja logado.
     * 
     * @throws Exception caso ocorra algum problema ao abrir o arquivo de usuários
     */
    public ControladorUsuario() throws Exception {
        this.arqUsuarios = new ArquivoUsuario();
        this.usuarioLogado = null;
    }

    /**
     * Cria um novo usuário solicitando dados via terminal.
     * Realiza verificações para impedir a criação de usuários com e-mail duplicado.
     * 
     * @param scanner Scanner para leitura de entradas do usuário
     */
    public void criarNovoUsuario(Scanner scanner) {
        System.out.println("\n--- Novo Usuário ---");
        try {
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine();
            System.out.print("E-mail: ");
            String email = scanner.nextLine();

            if (arqUsuarios.read(email) != null) {
                System.out.println("\n-- ERRO: O e-mail informado já está em uso! --\n");
                return;
            }

            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            int hashSenha = senha.hashCode();

            System.out.print("Pergunta secreta: ");
            String pergunta = scanner.nextLine();
            System.out.print("Resposta secreta: ");
            String resposta = scanner.nextLine();

            Usuario novoUsuario = new Usuario(nome, email, hashSenha, pergunta, resposta);
            int id = arqUsuarios.create(novoUsuario);

            System.out.println("\n-- Usuário criado com sucesso! (ID: " + id + ") --\n");

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao criar o usuário: " + e.getMessage() + "\n");
        }
    }

    /**
     * Efetua o login de um usuário solicitando e-mail e senha via terminal.
     * 
     * @param scanner Scanner para leitura de entradas do usuário
     * @return o {@link Usuario} logado se sucesso, ou null caso e-mail/senha estejam incorretos
     */
    public Usuario loginUsuario(Scanner scanner) {
        System.out.println("\n--- Login ---");
        try {
            System.out.print("E-mail: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Usuario usuario = arqUsuarios.read(email);

            if (usuario != null) {
                int hashSenhaDigitada = senha.hashCode();
                if (hashSenhaDigitada == usuario.getHashSenha()) {
                    this.usuarioLogado = usuario;
                    return this.usuarioLogado;
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro durante o login: " + e.getMessage() + "\n");
        }
        
        this.usuarioLogado = null;
        return null;
    }

    /**
     * Desconecta o usuário logado, limpando o estado da sessão.
     */
    public void logout() {
        this.usuarioLogado = null;
    }

    /**
     * Permite ao usuário logado deletar sua própria conta.
     * Confirmação via terminal é solicitada antes da exclusão.
     * 
     * @param scanner Scanner para leitura da confirmação do usuário
     * @return true se a exclusão foi confirmada e realizada com sucesso, false caso contrário
     */
    public boolean deletarProprioUsuario(Scanner scanner) {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- ERRO: Não há um usuário logado para deletar. --\n");
            return false;
        }

        System.out.println("\n--- Deletar Minha Conta ---");
        try {
            System.out.println("Atenção! Esta ação é permanente e não pode ser desfeita.");
            System.out.println(" > Nome: " + this.usuarioLogado.getNome());
            System.out.println(" > E-mail: " + this.usuarioLogado.getEmail());
            System.out.print("\nVocê tem CERTEZA de que deseja deletar sua conta? (S/N): ");
            
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                boolean sucesso = arqUsuarios.delete(this.usuarioLogado.getEmail());
                if (sucesso) {
                    System.out.println("\n-- Conta deletada com sucesso! Você será desconectado. --\n");
                    logout();
                    return true;
                } else {
                    System.out.println("\n-- Falha ao deletar a conta. --\n");
                }
            } else {
                System.out.println("\n-- Operação cancelada. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao tentar deletar a conta: " + e.getMessage() + "\n");
        }
        return false;
    }
}
