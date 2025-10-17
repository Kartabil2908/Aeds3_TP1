package src.presenteFacil.utils;

import src.presenteFacil.model.ArquivoProduto;
import src.presenteFacil.model.Produto;

public class GeradorProdutosTeste {

    public static void main(String[] args) throws Exception {
        ArquivoProduto arq = new ArquivoProduto();

        String[][] itens = new String[][]{
                {"Arroz branco 1kg", "Arroz tipo 1, grãos selecionados, pacote de 1kg."},
                {"Arroz integral 1kg", "Arroz integral longo fino, rico em fibras, pacote de 1kg."},
                {"Feijão carioca 1kg", "Feijão tipo carioca selecionado, pacote de 1kg."},
                {"Feijão preto 1kg", "Feijão preto tradicional, ideal para feijoada, pacote de 1kg."},
                {"Macarrão espaguete 500g", "Massa de sêmola de trigo duro, formato espaguete, 500g."},
                {"Macarrão penne 500g", "Massa de sêmola de trigo duro, formato penne, 500g."},
                {"Farinha de trigo 1kg", "Farinha de trigo especial para pães e bolos, 1kg."},
                {"Açúcar refinado 1kg", "Açúcar branco refinado para uso culinário, 1kg."},
                {"Açúcar mascavo 1kg", "Açúcar mascavo natural, sem refinamento químico, 1kg."},
                {"Sal refinado 1kg", "Sal marinho iodado, refinado, pacote de 1kg."},
                {"Óleo de soja 900ml", "Óleo de soja refinado, garrafa PET 900ml."},
                {"Azeite de oliva extra virgem 500ml", "Azeite de oliva extra virgem, acidez máxima 0,5%, 500ml."},
                {"Café torrado e moído 500g", "Café extraforte, embalagem a vácuo, 500g."},
                {"Chá verde 10 sachês", "Chá verde natural, caixa com 10 sachês."},
                {"Leite UHT integral 1L", "Leite longa vida integral, embalagem Tetra Pak 1L."},
                {"Leite UHT desnatado 1L", "Leite longa vida desnatado, embalagem Tetra Pak 1L."},
                {"Biscoito cream cracker 400g", "Biscoito salgado tipo cream cracker, pacote 400g."},
                {"Biscoito recheado chocolate 140g", "Biscoito recheado sabor chocolate, pacote 140g."},
                {"Achocolatado em pó 400g", "Bebida em pó sabor chocolate, lata 400g."},
                {"Manteiga sem sal 200g", "Manteiga de primeira qualidade, sem sal, 200g."},
                {"Margarina 500g", "Margarina cremosa com sal, pote 500g."},
                {"Queijo mussarela fatiado 150g", "Queijo mussarela fatiado, embalagem 150g."},
                {"Presunto cozido fatiado 150g", "Presunto cozido fatiado, livre de glúten, 150g."},
                {"Iogurte natural 170g", "Iogurte natural integral, sem adição de açúcar, 170g."},
                {"Molho de tomate tradicional 340g", "Molho de tomate pronto, receita tradicional, 340g."},
                {"Ketchup 380g", "Ketchup tradicional, frasco squeeze, 380g."},
                {"Maionese 500g", "Maionese cremosa, frasco 500g."},
                {"Mostarda 200g", "Mostarda amarela tradicional, frasco 200g."},
                {"Papel toalha 2 rolos", "Papel toalha de alta absorção, pacote com 2 rolos."},
                {"Papel higiênico 12 rolos", "Papel higiênico folha dupla, pacote com 12 rolos."},
                {"Sabonete em barra 90g", "Sabonete perfumado em barra, 90g."},
                {"Sabonete líquido 250ml", "Sabonete líquido hidratante para mãos, 250ml."},
                {"Shampoo 350ml", "Shampoo para cabelos normais, 350ml."},
                {"Condicionador 350ml", "Condicionador desembaraçante para cabelos normais, 350ml."},
                {"Creme dental 90g", "Creme dental com flúor, proteção anticáries, 90g."},
                {"Escova de dentes macia", "Escova dental cerdas macias com cabo ergonômico."},
                {"Desodorante aerosol 150ml", "Desodorante antitranspirante aerosol, 48h de proteção, 150ml."},
                {"Água mineral sem gás 1,5L", "Água mineral natural sem gás, garrafa 1,5L."},
                {"Refrigerante cola 2L", "Refrigerante sabor cola, garrafa 2 litros."},
                {"Suco de laranja 1L", "Suco de laranja integral, 100% fruta, 1L."},
                {"Cerveja pilsner lata 350ml", "Cerveja tipo pilsen, lata 350ml, gelada é melhor."},
                {"Vinho tinto seco 750ml", "Vinho tinto seco, uvas selecionadas, garrafa 750ml."},
                {"Arroz parboilizado 1kg", "Arroz parboilizado tipo 1, pacote de 1kg."},
                {"Aveia em flocos 200g", "Aveia em flocos finos, fonte de fibras, 200g."},
                {"Granola 1kg", "Granola tradicional com mel e castanhas, pacote 1kg."},
                {"Barra de cereal chocolate 25g", "Barra de cereal sabor chocolate, unidade 25g."},
                {"Chocolate ao leite 90g", "Chocolate ao leite cremoso, tablete 90g."},
                {"Bombom sortido 300g", "Caixa de bombons sortidos, 300g."},
                {"Gelatina sabor morango 25g", "Gelatina em pó sabor morango, 25g."},
                {"Leite condensado 395g", "Leite condensado integral, lata 395g."},
                {"Detergente líquido neutro 500ml", "Detergente líquido neutro para louças, 500ml."},
                {"Sabão em pó 1kg", "Sabão em pó para roupas, limpeza profunda, 1kg."},
                {"Amaciante de roupas 2L", "Amaciante concentrado, perfume intenso, frasco 2L."},
                {"Desinfetante floral 1L", "Desinfetante perfumado fragrância floral, 1L."},
                {"Água sanitária 1L", "Água sanitária com cloro ativo, 1L."},
                {"Esponja multiuso", "Esponja dupla face para limpeza geral."},
                {"Saco para lixo 50L 30 unidades", "Sacos para lixo 50 litros, pacote com 30 unidades."},
                {"Alvejante sem cloro 1L", "Alvejante sem cloro para roupas coloridas, 1L."},
                {"Limpador multiuso 500ml", "Limpador multiuso para diversas superfícies, 500ml."},
                {"Limpador de vidros 500ml", "Limpador para vidros e espelhos, 500ml."},
                {"Álcool em gel 70% 500ml", "Antisséptico para mãos, 70% INPM, 500ml."},
                {"Algodão 50g", "Algodão hidrófilo em bolas, pacote 50g."},
                {"Curativo adesivo 10 unidades", "Curativos adesivos estéreis, caixa com 10 unidades."},
                {"Protetor solar FPS 50 200ml", "Protetor solar corporal, FPS 50, 200ml."},
                {"Repelente de insetos 200ml", "Repelente contra mosquitos e pernilongos, 200ml."},
                {"Ração para cães adultos 1kg", "Ração seca para cães adultos, sabor carne, 1kg."},
                {"Areia higiênica para gatos 4kg", "Areia higiênica granulada para gatos, 4kg."},
                {"Fraldas descartáveis M 30 unidades", "Fraldas tamanho M, alto poder de absorção, 30 un."},
                {"Panela antiaderente 24cm", "Panela de alumínio com revestimento antiaderente, 24cm."},
                {"Jogo de talheres inox 24 peças", "Conjunto de talheres em aço inox, 24 peças."},
                {"Copo de vidro 300ml 6 unidades", "Conjunto com 6 copos de vidro de 300ml."},
                {"Garrafa térmica 1L", "Garrafa térmica inox com ampola de vidro, 1L."},
                {"Filtro de café n°103 30 unidades", "Filtros de papel para café tamanho 103, 30 un."},
                {"Fone de ouvido intra-auricular", "Fone de ouvido com microfone, conexão P2, intra-auricular."},
                {"Carregador USB 2.1A", "Carregador USB de tomada, saída 5V 2.1A."},
                {"Cabo HDMI 2.0 2m", "Cabo HDMI 2.0 de alta velocidade, 2 metros."},
                {"Mouse óptico sem fio", "Mouse sem fio 2.4GHz com receptor nano USB."},
                {"Teclado mecânico ABNT2", "Teclado mecânico layout ABNT2 com teclas iluminadas."},
                {"SSD NVMe 500GB", "SSD NVMe PCIe 3.0 x4, 500GB, leitura até 3.5GB/s."},
                {"HD externo 1TB", "Disco rígido externo USB 3.0, 1TB."},
                {"Pendrive 32GB USB 3.0", "Pendrive 32GB com interface USB 3.0."},
                {"Cartão microSD 64GB", "Cartão de memória microSD 64GB classe 10."},
                {"Roteador Wi‑Fi AC1200", "Roteador dual band AC1200 com 4 antenas."},
                {"Lâmpada LED 9W", "Lâmpada LED 9W bocal E27, luz branca."},
                {"Extensão elétrica 3 tomadas 1,5m", "Extensão elétrica com 3 tomadas, cabo 1,5m."},
                {"Pilhas alcalinas AA 4 unidades", "Pacote com 4 pilhas alcalinas AA."},
                {"Caderno universitário 200 folhas", "Caderno espiral universitário, 200 folhas pautadas."},
                {"Caneta esferográfica azul 1.0mm", "Caneta esferográfica azul ponta 1.0mm, escrita suave."},
                {"Lápis grafite HB n°2", "Lápis grafite HB nº2 com borracha na ponta."},
                {"Borracha branca", "Borracha branca macia, sem PVC."},
                {"Marca-texto amarelo", "Marca-texto cor amarela, ponta chanfrada."},
                {"Post-it 76x76mm 100 folhas", "Bloco autoadesivo 76x76mm, 100 folhas."},
                {"Travesseiro antialérgico", "Travesseiro com tratamento antiácaro e antialérgico."},
                {"Jogo de lençol casal 3 peças", "Jogo de lençol casal 3 peças, 100% algodão."},
                {"Toalha de banho 70x140cm", "Toalha de banho felpuda 70x140cm, 100% algodão."},
                {"Tapete antiderrapante 60x40cm", "Tapete para banheiro 60x40cm com base antiderrapante."},
                {"Vaso decorativo cerâmica", "Vaso decorativo em cerâmica esmaltada."},
                {"Vela aromática lavanda 200g", "Vela aromática fragrância lavanda, 200g."},
                {"Garrafa squeeze 750ml", "Garrafa squeeze esportiva 750ml, livre de BPA."},
                {"Colchonete para exercício", "Colchonete para exercícios, espuma densa, dobrável."},
                {"Faixa elástica resistência média", "Faixa elástica para treino, resistência média."},
                {"Adoçante líquido 100ml", "Adoçante líquido sucralose, 100ml."},
                {"Vinagre de álcool 750ml", "Vinagre de álcool transparente, garrafa 750ml."},
                {"Molho shoyu 150ml", "Molho de soja tipo shoyu tradicional, 150ml."},
                {"Pimenta-do-reino moída 50g", "Pimenta-do-reino moída na hora, frasco 50g."},
                {"Orégano 10g", "Tempero orégano desidratado, sachê 10g."},
                {"Milho verde em conserva 170g", "Milho verde em conserva, lata 170g."},
                {"Ervilha em conserva 170g", "Ervilha em conserva, lata 170g."}
        };

        int criados = 0;
        long base = 789123000000L; // 12 dígitos base para GTIN-13

        int limite = Math.min(100, itens.length);
        for (int i = 0; i < limite; i++) {
            String nome = itens[i][0];
            String desc = itens[i][1];

            // Gera GTIN-13 válido (EAN-13) a partir de 12 dígitos base + sequencial
            String corpo12 = String.format("%012d", base + i);
            char dv = calculaDigitoEAN13(corpo12);
            String gtin = corpo12 + dv;

            // Garante unicidade de GTIN; em caso de colisão, avança sequencial
            int tentativas = 0;
            while (tentativas < 1000 && arq.read(gtin) != null) {
                long proximo = (Long.parseLong(corpo12) + 1000 + tentativas);
                String novo12 = String.format("%012d", proximo);
                dv = calculaDigitoEAN13(novo12);
                gtin = novo12 + dv;
                tentativas++;
            }

            Produto p = new Produto(gtin, nome, desc);
            try {
                arq.create(p);
                criados++;
            } catch (Exception e) {
                System.err.println("Falha ao criar produto '" + nome + "': " + e.getMessage());
            }
        }

        System.out.println("Produtos criados: " + criados + "/" + limite);
    }

    private static char calculaDigitoEAN13(String dozeDigitos) {
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int d = dozeDigitos.charAt(i) - '0';
            soma += (i % 2 == 0) ? d : d * 3; // posições pares*1, ímpares*3 (0-based)
        }
        int resto = soma % 10;
        int dv = (resto == 0) ? 0 : 10 - resto;
        return (char) ('0' + dv);
    }
}
