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
    public int create(Lista lista) throws Exception {
        int id = super.create(lista);

        // índice direto por ID
        indiceDiretoID.create(new ParIDEndereco(lista.getId(), id));

        // índice direto por Código
        indiceDiretoCodigo.create(new ParCodigoID(lista.getCodigo(), id));

        // Relacionamento Usuario -> Lista (1:N)
        usuarioLista.create(new ParIntInt(lista.getIdUsuario(), id));

        return id;
    }

    public Lista readByCodigo(String codigo) throws Exception {
        ParCodigoID pci = indiceDiretoCodigo.read(ParCodigoID.hash(codigo));
        if(pci == null) return null;
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

    @Override
    public boolean delete(int idLista) throws Exception {
        
        Lista lista = super.read(idLista);
        
        if(lista == null) return false;

        if(super.delete(idLista)) {
            return indiceDiretoID.delete(lista.getId()/*new ParIDEndereco(lista.getID(), idLista)*/)
                && indiceDiretoCodigo.delete(lista.getId()/*new ParCodigoID(lista.getCodigo(), idLista)*/)
                && usuarioLista.delete(new ParIntInt(lista.getIdUsuario(), idLista));
        }
        return false;
    }
}
