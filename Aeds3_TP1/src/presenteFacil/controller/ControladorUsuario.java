package src.presenteFacil.controller;

import java.util.Scanner;

import src.presenteFacil.model.*;

/**
 * Controlador responsável por gerenciar a lógica de usuários no sistema PresenteFácil 1.0.
 *
 * Ele fornece métodos para:
 * <ul>
 * <li>Criar novos usuários;</li>
 * <li>Efetuar login;</li>
 * <li>Logout;</li>
 * <li>Desativar (soft delete) e Excluir (hard delete) a conta do usuário logado;</li>
 * <li>Exibir os dados do usuário logado.</li>
 * </ul>
 *
 * Mantém internamente o estado do usuário atualmente logado.
 *
 * @author Bernardo
 * @version 1.3
 */
public class ControladorUsuario {

    /** Arquivo persistente de usuários. */
    private ArquivoUsuario arqUsuarios;

    /** Arquivo persistente de lista. */
    private ArquivoLista arqListas;


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
        this.arqListas = new ArquivoLista();
        this.usuarioLogado = null;
    }

    /**
     * Cria um novo usuário solicitando dados via terminal.
     * Realiza verificações para impedir a criação de usuários com e-mail duplicado.
     *
     * @param scanner Scanner para leitura de entradas do usuário
     */
    public void criarNovoUsuario(Scanner scanner) {

        System.out.println("\n------- Novo Usuário -------");
        
        try {
            System.out.print("\nNome completo: ");
            String nome = scanner.nextLine();
            System.out.print("\nE-mail: ");
            String email = scanner.nextLine();

            if (arqUsuarios.read(email) != null) {
                System.out.println("\n-- ERRO: O e-mail informado já está em uso! --\n");
                return;
            }

            System.out.print("\nSenha: ");
            String senha = scanner.nextLine();
            int hashSenha = senha.hashCode();

            System.out.print("\nPergunta secreta: ");
            String pergunta = scanner.nextLine();
            System.out.print("\nResposta secreta: ");
            String resposta = scanner.nextLine();

            // O construtor já define o usuário como 'ativo'
            Usuario novoUsuario = new Usuario(nome, email, hashSenha, pergunta, resposta);
            int id = arqUsuarios.create(novoUsuario);

            System.out.println("\n-- Usuário criado com sucesso! (ID: " + id + ") --\n");

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao criar o usuário: " + e.getMessage() + "\n");
        }
    }

    /**
     * Efetua o login de um usuário solicitando e-mail e senha via terminal.
     * Verifica também se a conta está ativa.
     *
     * @param scanner Scanner para leitura de entradas do usuário
     * @return o {@link Usuario} logado se sucesso, ou null caso e-mail/senha estejam incorretos ou a conta inativa
     */
    public Usuario loginUsuario(Scanner scanner) {

        System.out.println("\n----------- Login ------------");
        try {
            
            System.out.print("\nE-mail: ");
            String email = scanner.nextLine();
            
            System.out.print("\nSenha: ");
            String senha = scanner.nextLine();
            System.out.println("\n");

            Usuario usuario = arqUsuarios.read(email);

            if (usuario != null) {

                int hashSenhaDigitada = senha.hashCode();

                if (hashSenhaDigitada == usuario.getHashSenha()) {
                    if (usuario.isAtivo()) {
                        this.usuarioLogado = usuario;
                        return this.usuarioLogado;
                    }
                    
                    else {
                        System.out.println("\n-- ERRO: Esta conta foi desativada. --\n");
                        return null;
                    }
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
     * Permite ao usuário logado desativar sua própria conta (soft delete).
     * A operação atualiza o status do usuário para inativo.
     *
     * @param scanner Scanner para leitura da confirmação do usuário
     * @return true se a desativação foi confirmada e realizada com sucesso, false caso contrário
     */
    public boolean desativarPropriaConta(Scanner scanner) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Desativar Conta\n");
        
        if (this.usuarioLogado == null) {
            System.out.println("\n-- ERRO: Não há um usuário logado para desativar. --\n");
            return false;
        }

        //System.out.println("\n------ Desativar Minha Conta ------");
        try {
            System.out.println("Atenção! Esta ação fará com que você não possa mais acessar sua conta.");
            System.out.println(" > Nome: " + this.usuarioLogado.getNome());
            System.out.println(" > E-mail: " + this.usuarioLogado.getEmail());
            System.out.print("\nVocê tem CERTEZA de que deseja desativar sua conta? (S/N): ");

            String confirmacao = scanner.nextLine();
            String resp = "N";

            if (confirmacao.equalsIgnoreCase("S")) {
                Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());
                
                if(listas.length > 0){
                    System.out.print("Ainda existem listas vinculadas a sua conta. Deseja desativar sua conta mesmo assim? (S/N): ");
                    resp = scanner.nextLine();
                    if(resp.equalsIgnoreCase("N")){
                        System.out.println("\n------- Operação cancelada. ------\n");
                        return false;
                    }
                }

                if((listas.length == 0 || resp.equalsIgnoreCase("S")) && confirmacao.equalsIgnoreCase("S")){
                    this.arqListas.desativarLista(usuarioLogado.getId());
                    this.usuarioLogado.setAtivo(false);
                    boolean sucesso = arqUsuarios.update(this.usuarioLogado);
                    if (sucesso) {
                        System.out.println("\n-- Conta desativada com sucesso! Você será desconectado. --\n");
                        logout();
                        return true;
                    } else {
                        this.usuarioLogado.setAtivo(true);
                        System.out.println("\n-- Falha ao desativar a conta. --\n");
                    }
                }
            } else {
                System.out.println("\n-- Operação cancelada. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao tentar desativar a conta: " + e.getMessage() + "\n");
        }
        return false;
    }

    /**
     * Permite ao usuário logado excluir permanentemente sua própria conta (hard delete).
     * A operação remove o registro do arquivo e dos índices.
     *
     * @param scanner Scanner para leitura da confirmação do usuário
     * @return true se a exclusão foi confirmada e realizada com sucesso, false caso contrário
     */
    public boolean excluirPropriaConta(Scanner scanner) {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Excluir Minha Conta\n");

        if (this.usuarioLogado == null) {
            System.out.println("\n-- ERRO: Não há um usuário logado para excluir. --\n");
            return false;
        }

        //System.out.println("\n--- Excluir Minha Conta Permanentemente ---");
        try {
            System.out.println("ATENÇÃO! Esta ação é PERMANENTE e IRREVERSÍVEL.");
            System.out.println("Todos os seus dados serão apagados para sempre.");
            System.out.println(" > Nome: " + this.usuarioLogado.getNome());
            System.out.println(" > E-mail: " + this.usuarioLogado.getEmail());
            System.out.print("\nDigite 'EXCLUIR' para confirmar a exclusão permanente de sua conta: ");

            String confirmacao = scanner.nextLine();

            if (confirmacao.equals("EXCLUIR")) {
                Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());

                if(listas.length > 0){
                    System.out.print("Ainda existem listas vinculadas a sua conta. Deseja apagalas permanentimente? (S/N): ");
                    String resp = scanner.nextLine();

                    if(!resp.equalsIgnoreCase("S")){
                        System.out.println("\n------- Operação cancelada. ------\n");
                        return false;
                    }else{
                        for(Lista lista : listas){
                            arqListas.desativarLista(lista.getIdUsuario());
                            arqListas.delete(lista.getId());
                        }
                    }  
                }
                boolean sucesso = arqUsuarios.delete(this.usuarioLogado.getId());
                if (sucesso) {
                    System.out.println("\n-- Conta excluída permanentemente com sucesso! Você será desconectado. --\n");
                    logout();
                    return true;
                } else {
                    System.out.println("\n-- Falha ao excluir a conta. --\n");
                }
            } else {
                System.out.println("\n-- A confirmação não foi digitada corretamente. Operação cancelada. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao tentar excluir a conta: " + e.getMessage() + "\n");
        }
        return false;
    }


    /**
     * Exibe os dados do usuário que está logado na sessão atual.
     * Utiliza o método toString() da classe Usuario.
     */
    public void exibirDadosDoUsuarioLogado() {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > Meus Dados\n");
        if (this.usuarioLogado != null) {
            // O método toString() já formata os dados para nós
            System.out.println(this.usuarioLogado.toString());
        } else {
            
            System.out.println("Nenhum usuário está logado no momento.\n");
        }
    }
}