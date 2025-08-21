package src.java.models;

public interface Registro {
    public int getID();           // todo registro tem um ID único
    public void setID(int id);

    // Converte o objeto para bytes (para salvar no arquivo)
    public byte[] toByteArray() throws Exception;

    // Reconstrói o objeto a partir dos bytes (quando ler do arquivo)
    public void fromByteArray(byte[] ba) throws Exception;
}