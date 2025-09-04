package src.java.complementary;

import src.java.models.*;

/**
 * Classe ArquivoUsuario
 * ---------------------
 * Especialização da classe Arquivo para manipulação de objetos do tipo Usuario.
 * 
 * Além do arquivo principal (que armazena os usuários em si),
 * a classe mantém um índice indireto baseado no email do usuário,
 * utilizando Hash Extensível para permitir busca, atualização e exclusão por email.
 */
public class ArquivoUsuario extends Arquivo<Usuario> {

    // Índice indireto para localizar usuários a partir de seus emails
    private HashExtensivel<ParEmailID> indiceIndiretoEmail;

    /**
     * Construtor da classe ArquivoUsuario.
     * Cria o arquivo principal de usuários e inicializa o índice por email.
     */
    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());

        indiceIndiretoEmail = new HashExtensivel<>(
                ParEmailID.class.getConstructor(),
                4, // tamanho inicial
                ".\\data\\usuarios\\indiceEmail.d.db", // diretório do índice
                ".\\data\\usuarios\\indiceEmail.c.db"  // cestos do índice
        );
    }

    /**
     * Cria um novo usuário no arquivo principal e insere seu email no índice.
     *
     * @param u Usuário a ser criado
     * @return ID do usuário criado
     */
    @Override
    public int create(Usuario u) throws Exception {
        int id = super.create(u); // cria no arquivo principal
        indiceIndiretoEmail.create(new ParEmailID(u.getEmail(), id)); // cria no índice
        return id;
    }

    /**
     * Lê um usuário a partir de seu email.
     *
     * @param email Email do usuário
     * @return Objeto Usuario correspondente ou null se não existir
     */
    public Usuario read(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei == null) 
            return null;
        
        return read(pei.getId()); // usa o método read(int id) da classe pai
    }

    /**
     * Remove um usuário a partir de seu email.
     *
     * @param email Email do usuário
     * @return true se conseguiu remover, false caso contrário
     */
    public boolean delete(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei != null) {
            // remove do arquivo principal e do índice
            return delete(pei.getId());
        }
        return false;
    }

    /**
     * Remove um usuário a partir do ID.
     * Remove tanto do arquivo principal quanto do índice de emails.
     *
     * @param id ID do usuário
     * @return true se conseguiu remover, false caso contrário
     */
    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if (u != null) {
            if (super.delete(id)) {
                // remove também do índice de emails
                return indiceIndiretoEmail.delete(ParEmailID.hash(u.getEmail()));
            }
        }
        return false;
    }

    /**
     * Atualiza os dados de um usuário existente.
     * Se o email foi alterado, atualiza o índice correspondente.
     *
     * @param novoUsuario Usuário com os novos dados
     * @return true se conseguiu atualizar, false caso contrário
     */
    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioVelho = super.read(novoUsuario.getId());

        if (usuarioVelho == null) {
            return false; // não existe usuário com esse ID
        }

        if (super.update(novoUsuario)) {
            // Caso o email tenha mudado, atualiza o índice
            if (!novoUsuario.getEmail().equals(usuarioVelho.getEmail())) {
                indiceIndiretoEmail.delete(ParEmailID.hash(usuarioVelho.getEmail()));
                indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getId()));
            }
            return true;
        }
        return false;
    }

}
