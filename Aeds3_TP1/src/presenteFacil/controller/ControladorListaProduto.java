package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;
import src.presenteFacil.controller.*;
import src.presenteFacil.view.MenuMinhasListas;

import java.util.*;

public class ControladorListaProduto {
    private ArquivoListaProduto arqListaProduto; 
    private ArquivoProduto arqProduto;
    private ControladorProduto controladorProduto;

    public ControladorListaProduto() throws Exception{
        this.arqListaProduto = new ArquivoListaProduto();
        this.arqProduto = new ArquivoProduto();
        this.controladorProduto = new ControladorProduto();
    }

    public void gerenciarProdutoLista(Scanner scanner, Lista lista) throws Exception{
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos \n");

        String opcao;
        boolean continua = true;

        Produto[] produtos = mostrarProdutosPorListaProduto(lista);

        try{
            while(continua) {
                System.out.println();
                System.out.println("(A) Acrescentar produto");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    case "A":
                        System.out.println("\n-- vai ser acresentada. --\n");
                        acresentarProdutoLista(scanner, lista);
                        continua = false;
                        break;
                    default:
                        if(isNumber(opcao)){ 
                            int indice = Integer.parseInt(opcao); 
                            System.out.println("\n[Selecionou a lista " + indice + "]\n"); 

                            if(produtos != null && indice > 0 && indice <= produtos.length){ 
                                controladorProduto.mostrarDetalhesProduto(scanner, produtos[indice - 1]); 
                            } else { 
                                System.out.println("\nOpição Invalida. Tente novamente.\n");
                            } 
                        }else{
                            System.out.println("\nOpição Invalida. Tente novamente.\n");
                        }
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao Gerenciar os produtos da lista: " + e.getMessage() + "\n");
        }
    }

    public void buscarProdutorPorGtin(Scanner scanner, Lista lista) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Buscar por GTIN \n");

        try {
            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine();
            boolean estaNaLista = false;
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());

            for(int i = 0; i < produtos.length; i++){
                if(gtin13.equals(produtos[i].getGtin13())){
                    estaNaLista = true;
                    break;
                }
            }

            if(estaNaLista){
                System.out.println("\n--- Projduto já está cadastrado na lista! ---\n");
                return;
            }

            Produto produto = arqProduto.read(gtin13);

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                System.out.println("\n-------- Detalhes do Produto --------");
                System.out.println(produto);
                System.out.println("-------------------------------------\n");
                System.out.print("Digite a quantidade: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                boolean continua = true;
                while (continua) {
                    System.out.print("Deseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    if (opcao.equals("S")) {
                        System.out.println("Produto adicionado à lista com sucesso!");
                        continua = false;
                    } else if (opcao.equals("N")) {
                        System.out.println("Produto não foi adicionado à lista.");
                        return;
                    } else {
                        System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
        }
    }  

    public void acresentarProdutoLista(Scanner scanner, Lista lista) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Protudos > Acrescentar produto \n");

        String opcao;
        boolean continua = true;

        try{
            while(continua) {
                System.out.println("(1) Buscar produtos por GTIN");
                System.out.println("(2) Listar todos os produtos");
                System.out.println();
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "1":
                        buscarProdutorPorGtin(scanner, lista);
                        continua = false;
                        break;
                    case "2":
                        System.out.println("\n-- Há ser acrescentada. --\n");
                        //continua = false;
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    default:
                        System.out.println("\nOpição Invalida. Tente novamente.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao mostrar acresentar produto: " + e.getMessage() + "\n");
        }
    }

    public Produto[] mostrarProdutosPorListaProduto(Lista lista) throws Exception {
        try {
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());
            ListaProduto[] listaProdutos = arqListaProduto.readByListaId(lista.getId());

            if (produtos == null || produtos.length == 0) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return null;
            }

            Arrays.sort(produtos, Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < produtos.length; i++) {
                for (int j = 0; j < listaProdutos.length; j++) {
                    if (produtos[i].getId() == listaProdutos[j].getIdProduto()) {
                        System.out.println("(" + (i + 1) + ") " + produtos[i].getNome() + " " +
                            "(x" + listaProdutos[j].getQuantidade() + ")");
                    }
                }
            }

            return produtos;
        } catch (Exception e) {
            System.err.println("\nErro ao mostrar produtos por ListaProduto: " + e.getMessage() + "\n");
            return null;  
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
