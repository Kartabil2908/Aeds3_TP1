package src.presenteFacil.model;

import java.util.ArrayList;
import src.presenteFacil.aeds3.*;

public class ArquivoLista extends Arquivo<Lista> {

    HashExtensivel<ParIDEndereco> indiceDiretoID;
    HashExtensivel<ParCodigoID> indiceDiretoCodigo;
    ArvoreBMais<ParIntInt> usuarioLista;

    public ArquivoLista() throws Exception {
        super("listas", Lista.class.getConstructor());

        indiceDiretoID = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(),
            4,
            "./data/listas/lista.id.d.db",
            "./data/listas/lista.id.c.db"
        );

        indiceDiretoCodigo = new HashExtensivel<>(
            ParCodigoID.class.getConstructor(),
            4,
            "./data/listas/lista.codigo.d.db",
            "./data/listas/lista.codigo.c.db"
        );

        usuarioLista = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listas/lista.usuario.db"
        );
    }

    @Override
    public int create(Lista l) throws Exception {
        int id = super.create(l);

        // índice direto por ID
        indiceDiretoID.create(new ParIDEndereco(id, id));

        // índice direto por Código
        indiceDiretoCodigo.create(new ParCodigoID(l.getCodigo(), id));

        // Relacionamento Usuario -> Lista (1:N)
        usuarioLista.create(new ParIntInt(l.getIdUsuario(), id));

        return id;
    }

    public Lista readByCodigo(String codigo) throws Exception {
        ParCodigoID pci = this.indiceDiretoCodigo.read(ParCodigoID.hash(codigo));
        if(pci == null) {
            System.out.println("Lista não encontrada."+ pci.getIDLista());
            return null;
        }
        System.out.println("ID da lista: " + pci.getIDLista());
        return super.read(pci.getIDLista());
    }

    public Lista[] readByUsuario(int idUsuario) throws Exception {
        ArrayList<ParIntInt> pares = usuarioLista.read(new ParIntInt(idUsuario, -1));
        Lista[] listas = new Lista[pares.size()];
        int i = 0;
        for(ParIntInt p : pares) {
            listas[i++] = super.read(p.getId2());
        }
        return listas;
    }

    public boolean update(Lista novaLista) throws Exception {
        Lista listaAntiga = super.read(novaLista.getId());
        if(listaAntiga == null){
            return false;
        }

        if(super.update(novaLista)) {
            // Se o código mudou, atualiza o índice direto por código
            if(!listaAntiga.getCodigo().equals(novaLista.getCodigo())) {
                indiceDiretoCodigo.delete(new ParCodigoID(listaAntiga.getCodigo(), listaAntiga.getId()));
                indiceDiretoCodigo.create(new ParCodigoID(novaLista.getCodigo(), novaLista.getId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int idLista) throws Exception {
        Lista lista = super.read(idLista);
        
        if(lista == null) return false;

        if(super.delete(idLista)) {
            return indiceDiretoID.delete(new ParIDEndereco(lista.getId(), idLista))
                && indiceDiretoCodigo.delete(new ParCodigoID(lista.getCodigo(), idLista))
                && usuarioLista.delete(new ParIntInt(lista.getIdUsuario(), idLista));
        }
        return false;
    }
}
