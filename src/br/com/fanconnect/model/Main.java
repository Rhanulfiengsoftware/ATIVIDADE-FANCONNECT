package br.com.fanconnect;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Esse comando faz a mágica de abrir a janela visual com segurança no Java
        SwingUtilities.invokeLater(() -> {
            AgendaInterface janela = new AgendaInterface();
            janela.setVisible(true); // Faz a janela aparecer!
        });
    }
}