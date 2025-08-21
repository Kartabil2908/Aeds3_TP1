package src.java;

import java.util.Scanner;

import src.java.ui.Terminal;

public class main {
    public static void main(String[] args){
        
        Scanner scanner = new Scanner(System.in);
        Terminal terminal = new Terminal();

        terminal.exibirTelaInicial(scanner);
    }
}
