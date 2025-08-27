package src.java.models;

import java.io.*;

/**
 * Representa um usuário do sistema, contendo informações pessoais,
 * credenciais e dados de recuperação de conta. 
 * 
 * A classe implementa a interface {@link Registro}, possibilitando
 * que seus objetos sejam armazenados e recuperados de arquivos
 * binários por meio da serialização em bytes.
 */
public class Usuario implements Registro {

    /** Identificador único do usuário. */
    private int id;

    /** Nome completo do usuário. */
    private String nome;

    /** Endereço de e-mail do usuário. */
    private String email;

    /** Hash da senha do usuário. */
    private int HashSenha;

    /** Pergunta secreta para recuperação de conta. */
    private String PerguntaSecreta;

    /** Resposta da pergunta secreta. */
    private String RespostaSecreta;


    // ---------- Construtores ----------

    /**
     * Construtor padrão que inicializa um usuário vazio.
     */
    public Usuario() {
        this.id = -1;
        this.nome = "";
        this.email = "";
        this.HashSenha = -1;
        this.PerguntaSecreta = "";
        this.RespostaSecreta = "";
    }

    /**
     * Construtor que cria um usuário com os dados fornecidos.
     * O ID será atribuído posteriormente pelo gerenciador de arquivos.
     *
     * @param nome nome do usuário
     * @param email e-mail do usuário
     * @param senhaHash hash da senha
     * @param pergunta pergunta secreta
     * @param resposta resposta secreta
     */
    public Usuario(String nome, String email, int senhaHash, String pergunta, String resposta) {
        this.id = -1;
        this.nome = nome;
        this.email = email;
        this.HashSenha = senhaHash;
        this.PerguntaSecreta = pergunta;
        this.RespostaSecreta = resposta;
    }


    // ---------- Implementação da interface Registro ----------

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Serializa o usuário em um vetor de bytes.
     *
     * @return vetor de bytes representando o objeto
     * @throws IOException caso ocorra falha na escrita dos dados
     */
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeInt(this.HashSenha);
        dos.writeUTF(this.PerguntaSecreta);
        dos.writeUTF(this.RespostaSecreta);
        return baos.toByteArray();
    }

    /**
     * Reconstrói o usuário a partir de um vetor de bytes.
     *
     * @param ba vetor de bytes com os dados serializados
     * @throws Exception caso ocorra falha na leitura dos dados
     */
    @Override
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.HashSenha = dis.readInt();
        this.PerguntaSecreta = dis.readUTF();
        this.RespostaSecreta = dis.readUTF();
    }

    /**
     * Retorna uma representação textual do usuário.
     *
     * @return string com os atributos do usuário
     */
    public String toString() {
        return "ID: " + this.id + "\n" +
               "Nome: " + this.nome + "\n" +
               "Email: " + this.email + "\n" +
               "HashSenha: " + this.HashSenha + "\n" +
               "PerguntaSecreta: " + this.PerguntaSecreta + "\n" +
               "RespostaSecreta: " + this.RespostaSecreta + "\n";
    }


    // ---------- Getters ----------

    /** @return o identificador único do usuário */
    public int getId() { return id; }

    /** @return o nome do usuário */
    public String getNome() { return nome; }

    /** @return o e-mail do usuário */
    public String getEmail() { return email; }

    /** @return o hash da senha do usuário */
    public int getHashSenha() { return HashSenha; }

    /** @return a pergunta secreta cadastrada */
    public String getPerguntaSecreta() { return PerguntaSecreta; }

    /** @return a resposta da pergunta secreta */
    public String getRespostaSecreta() { return RespostaSecreta; }
}
