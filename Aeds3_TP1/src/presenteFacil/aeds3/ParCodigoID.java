package src.presenteFacil.aeds3;

import src.presenteFacil.aeds3.*;

import java.io.IOException;

public class ParCodigoID implements RegistroHashExtensivel<ParCodigoID> {

    private String codigo;
    private int idLista;

    public ParCodigoID() {
        this("", -1);
    }

    public ParCodigoID(String codigo, int idLista) {
        this.codigo = codigo;
        this.idLista = idLista;
    }

    public String getCodigo() { return codigo; }
    public int getIDLista() { return idLista; }

    public static short hash(String codigo) {
        return (short)Math.abs(codigo.hashCode());
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public short size() {
        return (short)(codigo.getBytes().length + 4);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return (codigo + ";" + idLista).getBytes();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        String[] s = new String(ba).split(";");

        if (s.length >= 2) { // garante que s[1] existe
            this.codigo = s[0];
            this.idLista = Integer.parseInt(s[1]);
        } else {
            throw new IOException("Linha inválida ou incompleta: " + new String(ba));
        }
    }

}
