package src.java.test;

import src.java.complementary.ArquivoUsuario; 
import src.java.models.Usuario;
import java.text.Normalizer; // Usado para remover acentos dos nomes ao criar e-mails

/**
 * Classe de teste responsável pela criação automatizada de 20 usuários 
 * fictícios porém verídicos, para popular o sistema com dados de exemplo.
 * <p>
 * O processo inclui:
 * <ul>
 *   <li>Geração de nomes completos a partir de listas de nomes e sobrenomes comuns;</li>
 *   <li>Criação de e-mails normalizados (sem acentos e em minúsculas);</li>
 *   <li>Definição de senhas, perguntas e respostas de segurança padronizadas;</li>
 *   <li>Verificação de duplicidade de usuários por e-mail antes da inserção;</li>
 *   <li>Armazenamento no arquivo de usuários por meio da classe {@link ArquivoUsuario}.</li>
 * </ul>
 * </p>
 */
public class TesteCargaUsuarios {

    /**
     * Método principal que executa o processo de criação e inserção de
     * 20 usuários no arquivo de usuários.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {

        System.out.println("--- INICIANDO CRIAÇÃO AUTOMATIZADA DE 20 USUÁRIOS VERÍDICOS ---");

        // --- DADOS DE TESTE COM NOMES E SOBRENOMES COMUNS ---
        String[] nomes = {
            "Ana", "Bruno", "Carla", "Daniel", "Eduarda", "Felipe", "Gabriela", "Heitor", "Isabela", "João",
            "Larissa", "Marcos", "Natália", "Otávio", "Patrícia", "Rafael", "Sofia", "Thiago", "Valentina", "William"
        };

        String[] sobrenomes = {
            "Silva", "Costa", "Dias", "Alves", "Ferreira", "Gomes", "Oliveira", "Martins", "Pereira", "Rodrigues",
            "Lima", "Souza", "Nunes", "Andrade", "Barbosa", "Mendes", "Ribeiro", "Carvalho", "Lopes", "Araújo"
        };

        ArquivoUsuario arqUsuarios = null;
        try {
            // 1. Instancia o arquivo de usuários
            arqUsuarios = new ArquivoUsuario();

            // 2. Laço de repetição para criar 20 usuários
            for (int i = 0; i < 20; i++) {
                
                // --- GERAÇÃO DINÂMICA DE DADOS VERÍDICOS ---
                String nomeCompleto = nomes[i] + " " + sobrenomes[i];
                
                // Cria um e-mail a partir do nome, em minúsculas e sem acentos
                String email = normalizarString(nomes[i]) + "." + normalizarString(sobrenomes[i]) + "@email.com";
                
                String senha = "senha" + (i + 1);
                String pergunta = "Qual o nome do seu primeiro pet?";
                String resposta = "Rex" + (i + 1);

                System.out.println("\n-> Tentando criar usuário: " + nomeCompleto + " (" + email + ")");

                // 3. VERIFICAÇÃO: Checa se um usuário com este e-mail já existe
                Usuario usuarioExistente = arqUsuarios.read(email);
                if (usuarioExistente != null) {
                    System.out.println("[AVISO] Usuário com e-mail '" + email + "' já existe. Pulando...");
                    continue; 
                }

                // 4. Se não existe, prossegue com a criação
                int hashSenha = senha.hashCode();
                Usuario novoUsuario = new Usuario(nomeCompleto, email, hashSenha, pergunta, resposta);

                int novoID = arqUsuarios.create(novoUsuario);

                System.out.println("[SUCESSO] Usuário '" + nomeCompleto + "' criado com sucesso! ID: " + novoID);
            }

        } catch (Exception e) {
            System.err.println("\n[ERRO FATAL] Ocorreu um erro durante a execução dos testes.");
            e.printStackTrace();
        }

        System.out.println("\n--- TESTES FINALIZADOS ---");
    }

    /**
     * Função auxiliar que remove acentos e converte um texto para minúsculas,
     * ideal para criar identificadores de usuário ou e-mails a partir de nomes.
     *
     * @param texto texto original a ser normalizado
     * @return texto sem acentos e em letras minúsculas
     */
    private static String normalizarString(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }
}
