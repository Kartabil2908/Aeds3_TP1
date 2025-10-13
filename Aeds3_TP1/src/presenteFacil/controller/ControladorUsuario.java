package src.presenteFacil.controller;

import java.util.Scanner;

import src.presenteFacil.model.*;

public class ControladorUsuario {

    private ArquivoUsuario arqUsuarios;
    private ArquivoLista arqListas;
    public Usuario usuarioLogado;

    public ControladorUsuario() throws Exception {
        this.arqUsuarios = new ArquivoUsuario();
        this.arqListas = new ArquivoLista();
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    public void criarNovoUsuario(Scanner scanner) {
        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n---------- Novo Usuario -----------");
        try {
            System.out.print("\nNome completo: ");
            String nome = scanner.nextLine();
            System.out.print("\nE-mail: ");
            String email = scanner.nextLine();
            if (arqUsuarios.read(email) != null) {
                System.out.println("\n-- ERRO: O e-mail informado ja esta em uso! --\n");
                return;
            }
            System.out.print("\nSenha: ");
            String senha = scanner.nextLine();
            int hashSenha = senha.hashCode();
            System.out.print("\nPergunta secreta: ");
            String pergunta = scanner.nextLine();
            System.out.print("\nResposta secreta: ");
            String resposta = scanner.nextLine();
            Usuario novoUsuario = new Usuario(nome, email, hashSenha, pergunta, resposta);
            arqUsuarios.create(novoUsuario);
            System.out.println("\n-- Usuario criado com sucesso! --\n");
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao criar o usuario: " + e.getMessage() + "\n");
        }
    }

    public Usuario loginUsuario(Scanner scanner) {
        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n------------- Login ---------------");
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
                    } else {
                        System.out.println("\n-- ERRO: Esta conta foi desativada. --\n");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro no login: " + e.getMessage() + "\n");
        }
        System.out.println("\n-- E-mail ou senha invalidos. --\n");
        return null;
    }

    public void logout() { this.usuarioLogado = null; }

    public boolean desativarPropriaConta(Scanner scanner) throws Exception {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nao ha usuario logado para desativar. --\n");
            return false;
        }
        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n------ Desativar Minha Conta ------");
        System.out.print("Tem certeza que deseja desativar sua conta? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();
        if (!confirmacao.equals("S")) {
            System.out.println("\n-- Operacao cancelada. --\n");
            return false;
        }
        this.usuarioLogado.setAtivo(false);
        boolean sucesso = arqUsuarios.update(this.usuarioLogado);
        if (sucesso) {
            Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());
            for (Lista l : listas) arqListas.disableList(l.getId());
            System.out.println("\n-- Conta desativada com sucesso! Voce sera desconectado. --\n");
            logout();
        }
        return sucesso;
    }

    public boolean excluirPropriaConta(Scanner scanner) throws Exception {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nao ha usuario logado para excluir. --\n");
            return false;
        }
        System.out.println("ATENCAO! Esta acao e PERMANENTE e IRREVERSIVEL.");
        System.out.println(" > Nome: " + this.usuarioLogado.getNome());
        System.out.println(" > E-mail: " + this.usuarioLogado.getEmail());
        System.out.print("\nDigite 'EXCLUIR' para confirmar a exclusao permanente de sua conta: ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equals("EXCLUIR")) {
            System.out.println("\n-- Operacao cancelada. --\n");
            return false;
        }
        Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());
        for (Lista l : listas) arqListas.delete(l.getId());
        boolean sucesso = arqUsuarios.delete(this.usuarioLogado.getId());
        if (sucesso) {
            System.out.println("\n-- Conta excluida permanentemente! --\n");
            logout();
        }
        return sucesso;
    }

    public void reativarUsuario(Scanner scanner) throws Exception {
        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n--------- Reativar Usuario --------");
        System.out.print("\nE-mail: ");
        String email = scanner.nextLine();
        System.out.print("\nSenha: ");
        String senha = scanner.nextLine();
        Usuario usuario = arqUsuarios.read(email);
        if (usuario == null) { System.out.println("\n-- ERRO: Conta nao encontrada. --\n"); return; }
        if (senha.hashCode() != usuario.getHashSenha()) { System.out.println("\n-- Senha incorreta. --\n"); return; }
        if (usuario.isAtivo()) { System.out.println("\n-- Conta ja ativa. --\n"); return; }
        System.out.print("\nPergunta secreta: " + usuario.getPerguntaSecreta() + "\n");
        System.out.print("\nResposta secreta: ");
        String resp = scanner.nextLine();
        if (!resp.equals(usuario.getRespostaSecreta())) { System.out.println("\n-- Resposta secreta incorreta. --\n"); return; }
        usuario.setAtivo(true);
        if (arqUsuarios.update(usuario)) {
            Lista[] listas = arqListas.readByUsuario(usuario.getId());
            for (Lista l : listas) arqListas.activeList(l.getId());
            System.out.println("\n-- Conta reativada com sucesso! --\n");
        } else {
            System.out.println("\n-- Falha ao reativar a conta. --\n");
        }
    }

    // Meus dados + edição
    public void exibirDadosDoUsuarioLogado() {
        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Inicio > Meus Dados\n");
        if (this.usuarioLogado != null) {
            System.out.println(this.usuarioLogado.toString());
        } else {
            System.out.println("Nenhum usuario esta logado no momento.\n");
        }
    }

    public void exibirMeusDados(Scanner scanner) {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nenhum usuario logado. --\n");
            return;
        }
        boolean sair = false;
        while (!sair) {
            System.out.println("-------- PresenteFacil 1.0 --------");
            System.out.println("-----------------------------------");
            System.out.println("> Inicio > Meus Dados\n");
            System.out.println(this.usuarioLogado.toString());
            System.out.println("(1) Alterar meus dados");
            System.out.println("(R) Retornar");
            System.out.print("\nOpcao: ");
            String op = scanner.nextLine().trim().toUpperCase();
            switch (op) {
                case "1":
                    alterarDadosUsuario(scanner);
                    break;
                case "R":
                    sair = true;
                    break;
                default:
                    System.out.println("\n-- Opcao invalida. --\n");
            }
        }
    }

    private void alterarDadosUsuario(Scanner scanner) {
        if (this.usuarioLogado == null) return;
        boolean sair = false;
        while (!sair) {
            System.out.println("\n-------- Alterar Meus Dados --------");
            System.out.println("(1) Alterar nome");
            System.out.println("(2) Alterar e-mail");
            System.out.println("(3) Alterar senha");
            System.out.println("(4) Alterar pergunta secreta");
            System.out.println("(5) Alterar resposta secreta");
            System.out.println("(R) Retornar");
            System.out.print("\nOpcao: ");
            String op = scanner.nextLine().trim().toUpperCase();
            try {
                switch (op) {
                    case "1": {
                        System.out.println("Nome atual: " + usuarioLogado.getNome());
                        System.out.print("Novo nome: ");
                        String novo = scanner.nextLine();
                        if (!novo.trim().isEmpty()) {
                            usuarioLogado.setNome(novo);
                            salvarUsuarioLogado();
                            System.out.println("\n-- Nome atualizado! --\n");
                        } else {
                            System.out.println("\n-- Nenhuma alteracao realizada. --\n");
                        }
                        break;
                    }
                    case "2": {
                        System.out.println("Email atual: " + usuarioLogado.getEmail());
                        System.out.print("Novo e-mail: ");
                        String novo = scanner.nextLine().trim();
                        if (novo.isEmpty()) { System.out.println("\n-- Nenhuma alteracao realizada. --\n"); break; }
                        Usuario existente = arqUsuarios.read(novo);
                        if (existente != null && existente.getId() != usuarioLogado.getId()) {
                            System.out.println("\n-- Ja existe conta com esse e-mail. --\n");
                            break;
                        }
                        usuarioLogado.setEmail(novo);
                        salvarUsuarioLogado();
                        System.out.println("\n-- E-mail atualizado! --\n");
                        break;
                    }
                    case "3": {
                        System.out.print("Nova senha: ");
                        String s = scanner.nextLine();
                        if (s.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteracao realizada. --\n"); break; }
                        usuarioLogado.setHashSenha(s.hashCode());
                        salvarUsuarioLogado();
                        System.out.println("\n-- Senha atualizada! --\n");
                        break;
                    }
                    case "4": {
                        System.out.println("Pergunta atual: " + usuarioLogado.getPerguntaSecreta());
                        System.out.print("Nova pergunta: ");
                        String p = scanner.nextLine();
                        if (p.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteracao realizada. --\n"); break; }
                        usuarioLogado.setPerguntaSecreta(p);
                        salvarUsuarioLogado();
                        System.out.println("\n-- Pergunta secreta atualizada! --\n");
                        break;
                    }
                    case "5": {
                        System.out.print("Nova resposta secreta: ");
                        String r = scanner.nextLine();
                        if (r.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteracao realizada. --\n"); break; }
                        usuarioLogado.setRespostaSecreta(r);
                        salvarUsuarioLogado();
                        System.out.println("\n-- Resposta secreta atualizada! --\n");
                        break;
                    }
                    case "R":
                        sair = true;
                        break;
                    default:
                        System.out.println("\n-- Opcao invalida. --\n");
                }
            } catch (Exception e) {
                System.err.println("\nErro ao alterar dados do usuario: " + e.getMessage() + "\n");
                return;
            }
        }
    }

    private void salvarUsuarioLogado() throws Exception {
        arqUsuarios.update(usuarioLogado);
    }
}

