package src.java.models;

import java.io.*;

public class Usuario implements Registro
{

    private int id;
    private String nome;
    private String email;
    private String HashSenha;
    private String PerguntaSecreta;
    private String RespostaSecreta;
    

     // ---------- Construtores ----------
    public Usuario() {
        this.id = -1;
        this.nome = "";
        this.email = "";
        this.HashSenha = "";
        this.PerguntaSecreta = "";
        this.RespostaSecreta = "";

    }

     public Usuario(String nome, String email, String senha, String pergunta, String resposta) {
        this.id = -1; // será definido pelo Arquivo<T>
        this.nome = nome;
        this.email = email;
        this.HashSenha = senha;
        this.PerguntaSecreta = pergunta;
        this.RespostaSecreta = resposta;

    }

     // ---------- Implementação da interface Registro ----------
    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    /*@Override
    public byte[] toByteArray() throws Exception {} // falta implementar

    @Override
    public void fromByteArray(byte[] ba) throws Exception {} // falta implementar 

    */



    // ---------- Getters e setters ----------

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getEmail() { return email; }

    public String getHashSenha() { return HashSenha; }

    public String getPerguntaSecreta() { return PerguntaSecreta; }

    public String getRespostaSecreta() { return RespostaSecreta; }

    

}