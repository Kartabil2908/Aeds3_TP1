package src.presenteFacil.model;

import java.util.*;
import src.presenteFacil.aeds3.*;

public class ArquivoListaProduto extends Arquivo<ListaProduto>{
    ArvoreBMais<ParIntInt> listaListaProduto; // Par(idLista; idListaProduto)
    ArvoreBMais<ParIntInt> produtoListaProduto; // Par(idProduto; idListaProduto)

    private ArquivoProduto arqProduto;

    public ArquivoListaProduto() throws Exception {
        super("listaProduto", ListaProduto.class.getConstructor());

        this.arqProduto = new ArquivoProduto();

        listaListaProduto = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listaProduto/listaId.listaProdutoId.db"
        );

        produtoListaProduto = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listaProduto/produtoId.listaProdutoId.db"
        );
    }

    @Override
    public int create(ListaProduto lp) throws Exception {
        int id = super.create(lp);

        listaListaProduto.create(new ParIntInt(lp.getIdLista(), id));
        produtoListaProduto.create(new ParIntInt(lp.getIdProduto(), id));

        return id;
    }

    @Override
    public boolean delete(int idListaProduto) throws Exception {
        ListaProduto listaProduto = super.read(idListaProduto);
        
        if(listaProduto == null) return false;

        if(super.delete(idListaProduto)) {
            return listaListaProduto.delete(new ParIntInt(listaProduto.getIdLista(), idListaProduto))
                && produtoListaProduto.delete(new ParIntInt(listaProduto.getIdProduto(), idListaProduto));

        }

        return false;
    }

    public Produto[] getProdutosByListaId(int listaId) throws Exception {
        ArrayList<ParIntInt> pares = listaListaProduto.read(new ParIntInt(listaId, -1));
        ArrayList<Produto> produtos = new ArrayList<>();

        for (ParIntInt x : pares) {
            ListaProduto lp = super.read(x.getId2());
            Produto p = arqProduto.read(lp.getIdProduto());
            if (p != null)
                produtos.add(p);
        }

        return produtos.toArray(new Produto[0]);
    }

    public ListaProduto[] readByListaId(int listaId) throws Exception {
        ArrayList<ParIntInt> pares = listaListaProduto.read(new ParIntInt(listaId, -1));
        ArrayList<ListaProduto> listaProdutos = new ArrayList<>();

        for (ParIntInt x : pares) {
            ListaProduto lp = super.read(x.getId2());
            if (lp != null) {
                listaProdutos.add(lp);
            }
        }

        return listaProdutos.toArray(new ListaProduto[0]);
    }
}
