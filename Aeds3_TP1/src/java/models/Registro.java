package src.java.models;

/**
 * Interface que define o contrato básico para registros que serão
 * persistidos em arquivos. 
 * 
 * Todo registro precisa ter um identificador único (ID) e métodos
 * para serialização e desserialização em vetores de bytes.
 */
public interface Registro {

    /**
     * Retorna o identificador único (ID) do registro.
     *
     * @return o ID do registro
     */
    public int getID();

    /**
     * Define o identificador único (ID) do registro.
     *
     * @param id novo ID do registro
     */
    public void setID(int id);

    /**
     * Converte o registro em um vetor de bytes para armazenamento
     * em arquivo.
     *
     * @return vetor de bytes que representa o registro
     * @throws Exception caso ocorra algum erro na serialização
     */
    public byte[] toByteArray() throws Exception;

    /**
     * Reconstrói o registro a partir de um vetor de bytes.
     *
     * @param ba vetor de bytes contendo os dados do registro
     * @throws Exception caso ocorra algum erro na desserialização
     */
    public void fromByteArray(byte[] ba) throws Exception;
}
