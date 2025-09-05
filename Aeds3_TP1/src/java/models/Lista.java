package src.java.models;

import java.io.*;
import java.time.LocalDate;

public class Lista{
    private int id;
    private String codigo; 
    private String nome;
    private String descricao;
    private LocalDate dataCriacao;
    private LocalDate dataLimite;
    private int idUsuario; //Chave Estrangeira 

    public Lista(){
        this.id = -1;
        this.codigo = "";
        this.nome = "";
        this.descricao = "";
        this.dataCriacao = LocalDate.now();
        this.dataLimite = LocalDate.now();
        this.idUsuario = -1;
    }

    public Lista(String codigo, String nome, String descricao, LocalDate dataCriacao, LocalDate dataLimite, int idUsuario){
        this.id = -1;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataCriacao(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataLimite(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public byte[] toByteArray() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.codigo);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int)this.dataCriacao.toEpochDay());
        return baos.toByteArray();
    }

    /*public void fromByteArray(byte[] vb) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.dataPublicacao = LocalDate.ofEpochDay(dis.readInt());
    }*/

    @Override
    public String toString() {
        return 
            "\nID................: " + this.id +
            "\nCódigo............: " + this.codigo +
            "\nNome..............: " + this.nome+ 
            "\nDescrição.........: " + this.descricao +
            "\nData de criação...: " + this.dataCriacao +
            "\nData de limite....: " + this.dataLimite +
            "\nID Usuario........: " + this.idUsuario
        ;
    }
}