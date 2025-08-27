package src.java;

import java.util.Scanner;
import src.java.util.ControladorUsuario;
import src.java.ui.Terminal;

/**
 * Classe principal do sistema PresenteFácil 1.0.
 * 
 * Responsável por iniciar a aplicação, instanciar o controlador de usuários,
 * a interface de terminal e iniciar a interação com o usuário.
 * 
 * Exemplo de execução:
 * <pre>
 * java src.java.Main
 * </pre>
 * 
 * Fecha o {@link Scanner} ao finalizar para evitar vazamento de recursos.
 * 
 * @author Bernardo
 * @version 1.0
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 1. Instancia o controlador, que gerencia usuários e arquivos.
            ControladorUsuario controlador = new ControladorUsuario();

            // 2. Instancia o terminal, injetando o controlador.
            Terminal terminal = new Terminal(controlador);

            // 3. Exibe a tela inicial e interage com o usuário.
            terminal.exibirTelaInicial(scanner);

        } catch (Exception e) {
            System.err.println("ERRO FATAL: Não foi possível iniciar o sistema.");
            e.printStackTrace();
        } finally {
            // Fecha o scanner para liberar recursos do sistema
            scanner.close();
        }
    }
}
