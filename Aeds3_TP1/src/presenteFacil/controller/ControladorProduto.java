package src.presenteFacil.controller;

import src.presenteFacil.model.ArquivoProduto;
import src.presenteFacil.model.Produto;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ControladorProduto {

    private ArquivoProduto arqProdutos;

    public ControladorProduto() throws Exception {
        this.arqProdutos = new ArquivoProduto();
    }

    public void cadastrarNovoProduto(Scanner scanner) {
        System.out.println("-------- PresenteFácil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Produtos > Cadastrar\n");
        try {
            System.out.print("GTIN-13: ");
            String gtin13 = scanner.nextLine();

            if (arqProdutos.read(gtin13) != null) {
                System.out.println("\n-- ERRO: Já existe um produto com o GTIN-13 informado. --\n");
                return;
            }

            System.out.print("Nome do Produto: ");
            String nome = scanner.nextLine();
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            Produto novoProduto = new Produto(gtin13, nome, descricao);
            arqProdutos.create(novoProduto);

            System.out.println("\n-- Produto cadastrado com sucesso! --\n");
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao cadastrar o produto: " + e.getMessage() + "\n");
        }
    }

    public void buscarProdutoPorGtin(Scanner scanner) {
        System.out.println("-------- PresenteFácil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Produtos > Buscar por GTIN\n");
        try {
            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine();
            Produto produto = arqProdutos.read(gtin13);

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                exibirDetalhesProduto(scanner, produto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
        }
    }
    
    public void listarTodosOsProdutos(Scanner scanner) {
        try {
            List<Produto> produtos = arqProdutos.listarTodos();
            if (produtos.isEmpty()) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return;
            }
    
            produtos.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));
    
            int paginaAtual = 0;
            final int ITENS_POR_PAGINA = 10;
            boolean sair = false;
    
            while (!sair) {
                System.out.println("-------- PresenteFácil 1.0 --------");
                System.out.println("-----------------------------------");
                System.out.println("> Início > Produtos > Listagem\n");
    
                int totalPaginas = (int) Math.ceil((double) produtos.size() / ITENS_POR_PAGINA);
                System.out.println("Página " + (paginaAtual + 1) + " de " + totalPaginas);
                System.out.println("----------------------------------\n");
    
                int inicio = paginaAtual * ITENS_POR_PAGINA;
                int fim = Math.min(inicio + ITENS_POR_PAGINA, produtos.size());
    
                for (int i = inicio; i < fim; i++) {
                    System.out.println("(" + (i + 1) + ") " + produtos.get(i).getNome());
                }
    
                System.out.println("\n(P) Próxima página");
                System.out.println("(A) Página anterior");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.print("\nOpção: ");
                String opcao = scanner.nextLine().trim().toUpperCase();
    
                switch (opcao) {
                    case "P":
                        if (paginaAtual < totalPaginas - 1) paginaAtual++;
                        break;
                    case "A":
                        if (paginaAtual > 0) paginaAtual--;
                        break;
                    case "R":
                        sair = true;
                        break;
                    default:
                        System.out.println("\n-- Opção inválida. --\n");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao listar produtos: " + e.getMessage() + "\n");
        }
    }

    private void exibirDetalhesProduto(Scanner scanner, Produto produto) throws Exception {
         while (true) {
            System.out.println("\n-------- Detalhes do Produto --------");
            System.out.println(produto.toString());
            System.out.println("-------------------------------------\n");
            System.out.println("(1) Alterar dados do produto");
            System.out.println("(2) Inativar o produto");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            String opcao = scanner.nextLine().trim().toUpperCase();
            switch (opcao) {
                case "1":
                    System.out.println("\n[Funcionalidade de Alteração não implementada neste TP]\n");
                    break;
                case "2":
                    System.out.println("\n[Funcionalidade de Inativação não implementada neste TP]\n");
                    break;
                case "R":
                    return;
                default:
                    System.out.println("\n-- Opção inválida. --\n");
            }
        }
    }
}