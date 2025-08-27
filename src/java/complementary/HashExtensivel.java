package src.java.complementary;

import java.io.*;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

/**
 * Classe HashExtensivel<T>
 * -------------------------
 * Implementação de um índice baseado em Hash Extensível com armazenamento em disco.
 * 
 * Estrutura:
 *  - Diretório (armazena ponteiros para cestos, controlando profundidade global).
 *  - Cestos (blocos que armazenam elementos com limite fixo).
 * 
 * Cada operação (create, read, update, delete) acessa o diretório em disco,
 * localiza o cesto correspondente e realiza a operação.
 *
 * @param <T> Classe que implementa RegistroHashExtensivel<T>, garantindo métodos
 *            de serialização e comparação.
 */
public class HashExtensivel<T extends RegistroHashExtensivel<T>> {

  // Nomes dos arquivos de armazenamento
  String nomeArquivoDiretorio;
  String nomeArquivoCestos;

  // Arquivos físicos
  RandomAccessFile arqDiretorio;
  RandomAccessFile arqCestos;

  // Configuração
  int quantidadeDadosPorCesto;
  Diretorio diretorio;
  Constructor<T> construtor;

  /**
   * Classe interna Cesto
   * --------------------
   * Representa um "balde" que armazena elementos com profundidade local própria.
   */
  public class Cesto {

    Constructor<T> construtor;
    short quantidadeMaxima;   // capacidade máxima de elementos no cesto
    short bytesPorElemento;   // tamanho fixo de cada elemento em bytes
    short bytesPorCesto;      // tamanho total fixo do cesto em bytes

    byte profundidadeLocal;   // profundidade local do cesto
    short quantidade;         // quantidade atual de elementos
    ArrayList<T> elementos;   // lista de elementos armazenados

    // Construtores
    public Cesto(Constructor<T> ct, int qtdmax) throws Exception {
      this(ct, qtdmax, 0);
    }

    public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
      construtor = ct;

      if (qtdmax > 32767)
        throw new Exception("Quantidade máxima de 32.767 elementos");
      if (pl > 127)
        throw new Exception("Profundidade local máxima de 127 bits");

      profundidadeLocal = (byte) pl;
      quantidade = 0;
      quantidadeMaxima = (short) qtdmax;
      elementos = new ArrayList<>(quantidadeMaxima);

      bytesPorElemento = ct.newInstance().size();
      bytesPorCesto = (short) (bytesPorElemento * quantidadeMaxima + 3);
    }

    /** Serializa o cesto em um array de bytes. */
    public byte[] toByteArray() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.writeByte(profundidadeLocal);
      dos.writeShort(quantidade);

      int i = 0;
      while (i < quantidade) {
        dos.write(elementos.get(i).toByteArray());
        i++;
      }

      // Preenche espaço vazio com bytes nulos
      byte[] vazio = new byte[bytesPorElemento];
      while (i < quantidadeMaxima) {
        dos.write(vazio);
        i++;
      }

      return baos.toByteArray();
    }

    /** Reconstrói o cesto a partir de um array de bytes. */
    public void fromByteArray(byte[] ba) throws Exception {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);

      profundidadeLocal = dis.readByte();
      quantidade = dis.readShort();

      elementos = new ArrayList<>(quantidadeMaxima);
      byte[] dados = new byte[bytesPorElemento];
      T elem;

      for (int i = 0; i < quantidadeMaxima; i++) {
        dis.read(dados);
        elem = construtor.newInstance();
        elem.fromByteArray(dados);
        elementos.add(elem);
      }
    }

    /** Insere um elemento no cesto, mantendo ordenação. */
    public boolean create(T elem) {
      if (full())
        return false;

      int i = quantidade - 1;
      while (i >= 0 && elem.hashCode() < elementos.get(i).hashCode())
        i--;

      elementos.add(i + 1, elem);
      quantidade++;
      return true;
    }

    /** Busca um elemento pelo hash (chave). */
    public T read(int chave) {
      if (empty())
        return null;

      int i = 0;
      while (i < quantidade && chave > elementos.get(i).hashCode())
        i++;

      if (i < quantidade && chave == elementos.get(i).hashCode())
        return elementos.get(i);
      else
        return null;
    }

    /** Atualiza um elemento existente. */
    public boolean update(T elem) {
      if (empty())
        return false;

      int i = 0;
      while (i < quantidade && elem.hashCode() > elementos.get(i).hashCode())
        i++;

      if (i < quantidade && elem.hashCode() == elementos.get(i).hashCode()) {
        elementos.set(i, elem);
        return true;
      }
      return false;
    }

    /** Remove um elemento pela chave. */
    public boolean delete(int chave) {
      if (empty())
        return false;

      int i = 0;
      while (i < quantidade && chave > elementos.get(i).hashCode())
        i++;

      if (i < quantidade && chave == elementos.get(i).hashCode()) {
        elementos.remove(i);
        quantidade--;
        return true;
      }
      return false;
    }

    public boolean empty() {
      return quantidade == 0;
    }

    public boolean full() {
      return quantidade == quantidadeMaxima;
    }

    public String toString() {
      String s = "Profundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
      int i = 0;
      while (i < quantidade) {
        s += elementos.get(i).toString() + " | ";
        i++;
      }
      while (i < quantidadeMaxima) {
        s += "- | ";
        i++;
      }
      return s;
    }

    /** Retorna o tamanho em bytes de um cesto. */
    public int size() {
      return bytesPorCesto;
    }
  }

  /**
   * Classe interna Diretorio
   * ------------------------
   * Representa o diretório de ponteiros para os cestos.
   * Controla a profundidade global e redimensionamento (duplicação).
   */
  protected class Diretorio {

    byte profundidadeGlobal;
    long[] enderecos;

    public Diretorio() {
      profundidadeGlobal = 0;
      enderecos = new long[1];
      enderecos[0] = 0;
    }

    /** Atualiza o endereço de um cesto no diretório. */
    public boolean atualizaEndereco(int p, long e) {
      if (p > Math.pow(2, profundidadeGlobal))
        return false;
      enderecos[p] = e;
      return true;
    }

    /** Serializa o diretório. */
    public byte[] toByteArray() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      dos.writeByte(profundidadeGlobal);
      int quantidade = (int) Math.pow(2, profundidadeGlobal);

      for (int i = 0; i < quantidade; i++) {
        dos.writeLong(enderecos[i]);
      }

      return baos.toByteArray();
    }

    /** Reconstrói o diretório a partir de bytes. */
    public void fromByteArray(byte[] ba) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);

      profundidadeGlobal = dis.readByte();
      int quantidade = (int) Math.pow(2, profundidadeGlobal);

      enderecos = new long[quantidade];
      for (int i = 0; i < quantidade; i++) {
        enderecos[i] = dis.readLong();
      }
    }

    public String toString() {
      String s = "\nProfundidade global: " + profundidadeGlobal;
      int quantidade = (int) Math.pow(2, profundidadeGlobal);

      for (int i = 0; i < quantidade; i++) {
        s += "\n" + i + ": " + enderecos[i];
      }
      return s;
    }

    protected long endereço(int p) {
      if (p > Math.pow(2, profundidadeGlobal))
        return -1;
      return enderecos[p];
    }

    /** Duplica o diretório, aumentando a profundidade global em 1. */
    protected boolean duplica() {
      if (profundidadeGlobal == 127)
        return false;

      profundidadeGlobal++;
      int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
      int q2 = (int) Math.pow(2, profundidadeGlobal);

      long[] novosEnderecos = new long[q2];

      // primeira metade
      for (int i = 0; i < q1; i++)
        novosEnderecos[i] = enderecos[i];

      // segunda metade
      for (int i = q1; i < q2; i++)
        novosEnderecos[i] = enderecos[i - q1];

      enderecos = novosEnderecos;
      return true;
    }

    /** Hash considerando profundidade global. */
    protected int hash(int chave) {
      return Math.abs(chave) % (int) Math.pow(2, profundidadeGlobal);
    }

    /** Hash auxiliar para uma profundidade local específica. */
    protected int hash2(int chave, int pl) {
      return Math.abs(chave) % (int) Math.pow(2, pl);
    }
  }

  /**
   * Construtor HashExtensivel
   *
   * @param ct  Construtor da classe T
   * @param n   Quantidade de dados por cesto
   * @param nd  Nome do arquivo do diretório
   * @param nc  Nome do arquivo dos cestos
   */
  public HashExtensivel(Constructor<T> ct, int n, String nd, String nc) throws Exception {
    construtor = ct;
    quantidadeDadosPorCesto = n;
    nomeArquivoDiretorio = nd;
    nomeArquivoCestos = nc;

    arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
    arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

    // Se os arquivos estão vazios, inicializa o diretório e um cesto
    if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {
      diretorio = new Diretorio();
      byte[] bd = diretorio.toByteArray();
      arqDiretorio.write(bd);

      Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
      bd = c.toByteArray();
      arqCestos.seek(0);
      arqCestos.write(bd);
    }
  }

  // ---------------------------
  // Métodos principais
  // ---------------------------

  /** Insere um elemento no hash extensível. */
  public boolean create(T elem) throws Exception {
    return true;
  }

  /** Busca um elemento pela chave (hashCode). */
  public T read(int chave) throws Exception {

    return null;
  }

  /** Atualiza um elemento existente. */
  public boolean update(T elem) throws Exception {
    return false;
  }

  /** Remove um elemento pela chave. */
  public boolean delete(int chave) throws Exception {
    return false;
  }

  /** Imprime o conteúdo do diretório e dos cestos. */
  public void print() {
  }
}
