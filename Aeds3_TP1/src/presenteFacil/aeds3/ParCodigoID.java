package src.presenteFacil.aeds3;

import src.presenteFacil.aeds3.*;

import java.io.*;

public class ParCodigoID implements RegistroHashExtensivel<ParCodigoID> {

    private String codigo;
    private int idLista;

    private final short TAMANHO = 24;

    public ParCodigoID() {
        this("", -1);
    }

    public ParCodigoID(String codigo, int idLista) {
        this.codigo = codigo;
        this.idLista = idLista;
    }

    public String getCodigo() {
        return codigo; 
    }
    
    public int getIDLista() { 
        return idLista; 
    }

    public static int hash(String codigo) {
        return Math.abs(codigo.hashCode());
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    /*@Override
    public byte[] toByteArray() throws IOException {
        return (codigo + ";" + idLista).getBytes();
    }*/

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(codigo);   // escreve string com tamanho
        dos.writeInt(idLista);  // escreve int fixo

        return baos.toByteArray();
    }

    /*@Override
    public void fromByteArray(byte[] ba) throws IOException {
        String[] s = new String(ba).split(";");

        if (s.length >= 2) { // garante que s[1] existe
            this.codigo = s[0];
            this.idLista = Integer.parseInt(s[1]);
        } else {
            throw new IOException("Linha inválida ou incompleta: " + new String(ba));
        }
    }*/

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.codigo = dis.readUTF();
        this.idLista = dis.readInt();
    }
}
