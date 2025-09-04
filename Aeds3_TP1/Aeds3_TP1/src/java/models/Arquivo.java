package src.java.models;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import src.java.complementary.*;

/**
 * Classe genérica que implementa um CRUD em arquivo binário
 * com suporte a índice direto (Hash Extensível).
 *
 * @param <T> Tipo de registro que estende {@link Registro}.
 */
public class Arquivo<T extends Registro> {
    
    /** Tamanho fixo do cabeçalho do arquivo (último ID + lista de excluídos). */
    final int TAM_CABECALHO = 12;

    private RandomAccessFile arquivo;         // Arquivo principal de dados
    private String nomeArquivo;               // Caminho do arquivo
    private Constructor<T> construtor;        // Construtor do tipo T
    private HashExtensivel<ParIDEndereco> indiceDireto; // Índice primário (ID → endereço no arquivo)

    /**
     * Construtor da classe Arquivo.
     *
     * @param na Nome base do arquivo (ex: "usuarios").
     * @param c Construtor do tipo T (usado para criar instâncias dinamicamente).
     */
    public Arquivo(String na, Constructor<T> c) throws Exception {
        // Cria pasta raiz "data"
        File d = new File(".\\data");
        if (!d.exists()) d.mkdir();

        // Cria subpasta para o tipo de entidade (ex: data/usuarios)
        d = new File(".\\data\\" + na);
        if (!d.exists()) d.mkdir();

        // Nome do arquivo principal
        this.nomeArquivo = ".\\data\\" + na + "\\" + na + ".db";
        this.construtor = c;
        this.arquivo = new RandomAccessFile(this.nomeArquivo, "rw");

        // Inicializa o cabeçalho caso o arquivo esteja vazio
        if (arquivo.length() < TAM_CABECALHO) {
            arquivo.writeInt(0);    // último ID gerado
            arquivo.writeLong(-1);  // lista encadeada de espaços excluídos
        }

        // Criação do índice direto (Hash Extensível)
        indiceDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(),
            4, // profundidade inicial
            ".\\data\\" + na + "\\" + na + ".d.db", // arquivo do diretório
            ".\\data\\" + na + "\\" + na + ".c.db"  // arquivo dos cestos
        );
    }

    /**
     * Cria um novo registro no arquivo.
     *
     * @param obj objeto a ser persistido.
     * @return ID atribuído ao objeto.
     */
    public int create(T obj) throws Exception {
        // Atualiza último ID
        arquivo.seek(0);
        int proximoID = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(proximoID);
        obj.setID(proximoID);

        // Serializa objeto
        byte[] b = obj.toByteArray();

        // Tenta reutilizar espaço de registros excluídos
        long endereco = getDeleted(b.length);
        if (endereco == -1) {
            // Nenhum espaço livre: escreve no final do arquivo
            arquivo.seek(arquivo.length());
            endereco = arquivo.getFilePointer();
            arquivo.writeByte(' ');       // lápide (ativo)
            arquivo.writeShort(b.length); // tamanho
            arquivo.write(b);             // conteúdo
        } else {
            // Reutiliza espaço
            arquivo.seek(endereco);
            arquivo.writeByte(' ');       // remove lápide
            arquivo.skipBytes(2);         // mantém tamanho antigo
            arquivo.write(b);
        }

        // Atualiza índice direto
        indiceDireto.create(new ParIDEndereco(proximoID, endereco));

        return obj.getID();
    }

    /**
     * Lê um registro pelo ID.
     */
    public T read(int id) throws Exception {
        // Busca no índice direto
        ParIDEndereco pid = indiceDireto.read(id);
        if (pid != null) {
            arquivo.seek(pid.getEndereco());
            T obj = construtor.newInstance();
            byte lapide = arquivo.readByte();

            if (lapide == ' ') { // ativo
                short tam = arquivo.readShort();
                byte[] b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getID() == id) return obj;
            }
        }
        return null;
    }

    /**
     * Exclui um registro pelo ID.
     */
    public boolean delete(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            T obj = construtor.newInstance();
            byte lapide = arquivo.readByte();

            if (lapide == ' ') { // ativo
                short tam = arquivo.readShort();
                byte[] b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);

                if (obj.getID() == id) {
                    if (indiceDireto.delete(id)) {
                        // Marca como excluído
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*'); 
                        addDeleted(tam, pie.getEndereco()); // adiciona à lista de espaços livres
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Atualiza um registro existente.
     */
    public boolean update(T novoObj) throws Exception {
        ParIDEndereco pie = indiceDireto.read(novoObj.getID());
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            T obj = construtor.newInstance();
            byte lapide = arquivo.readByte();

            if (lapide == ' ') {
                short tam = arquivo.readShort();
                byte[] b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);

                if (obj.getID() == novoObj.getID()) {
                    byte[] b2 = novoObj.toByteArray();
                    short tam2 = (short) b2.length;

                    // Caso caiba no mesmo espaço
                    if (tam2 <= tam) {
                        arquivo.seek(pie.getEndereco() + 3);
                        arquivo.write(b2);
                    } else {
                        // Marca como excluído
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        addDeleted(tam, pie.getEndereco());

                        // Grava no fim do arquivo ou reutiliza espaço
                        long novoEndereco = getDeleted(b.length);
                        if (novoEndereco == -1) {
                            arquivo.seek(arquivo.length());
                            novoEndereco = arquivo.getFilePointer();
                            arquivo.writeByte(' ');
                            arquivo.writeShort(tam2);
                            arquivo.write(b2);
                        } else {
                            arquivo.seek(novoEndereco);
                            arquivo.writeByte(' ');
                            arquivo.skipBytes(2);
                            arquivo.write(b2);
                        }

                        // Atualiza índice
                        indiceDireto.update(new ParIDEndereco(novoObj.getID(), novoEndereco));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // ----------------------
    // Gestão da lista de espaços livres
    // ----------------------

    /**
     * Adiciona espaço excluído à lista de reaproveitamento.
     */
    public void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        long anterior = 4; // início da lista no cabeçalho
        arquivo.seek(anterior);
        long endereco = arquivo.readLong(); // primeiro da lista
        long proximo;
        int tamanho;

        if (endereco == -1) {  // lista vazia
            arquivo.seek(4);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco+3);
            arquivo.writeLong(-1);
        } else {
            do {
                arquivo.seek(endereco+1);
                tamanho = arquivo.readShort();
                proximo = arquivo.readLong();

                if (tamanho >= tamanhoEspaco) {  // insere antes
                    if (anterior == 4) arquivo.seek(anterior);
                    else arquivo.seek(anterior+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(endereco);
                    break;
                }
                if (proximo == -1) {  // fim da lista
                    arquivo.seek(endereco+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(-1);
                    break;
                }
                anterior = endereco;
                endereco = proximo;
            } while (endereco != -1);
        }
    }

    /**
     * Recupera espaço excluído reutilizável.
     */
    public long getDeleted(int tamanhoNecessario) throws Exception {
        long anterior = 4; // início da lista
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        long proximo;
        int tamanho;

        while (endereco != -1) {
            arquivo.seek(endereco+1);
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();

            if (tamanho >= tamanhoNecessario) {
                if (anterior == 4) arquivo.seek(anterior);
                else arquivo.seek(anterior+3);
                arquivo.writeLong(proximo);
                break;
            }
            anterior = endereco;
            endereco = proximo;
        }
        return endereco;
    }

    /**
     * Fecha o arquivo.
     */
    public void close() throws Exception {
        arquivo.close();
    }
}
