package br.com.fanconnect.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private String titulo;
    private LocalDateTime dataHora;
    private String local;
    private String organizador;
    private Categoria categoria;
    private boolean lembreteAtivo;

    public Evento(String titulo, LocalDateTime dataHora, String local, String organizador, Categoria categoria) {
        this.titulo = titulo;
        this.dataHora = dataHora;
        this.local = local;
        this.organizador = organizador;
        this.categoria = categoria;
        this.lembreteAtivo = false;
    }

    public String getTitulo() { return titulo; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getLocal() { return local; }
    public String getOrganizador() { return organizador; }
    public Categoria getCategoria() { return categoria; }
    public boolean isLembreteAtivo() { return lembreteAtivo; }
    public void setLembreteAtivo(boolean lembreteAtivo) { this.lembreteAtivo = lembreteAtivo; }

    public void exibirDetalhes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm");
        System.out.println("\n========================================");
        System.out.println("          DETALHES DO EVENTO            ");
        System.out.println("========================================");
        System.out.println("Título: " + titulo);
        System.out.println("Categoria: " + categoria.getDescricao());
        System.out.println("Data e Hora: " + dataHora.format(formatter));
        System.out.println("Local: " + local);
        System.out.println("Organizador: " + organizador);
        System.out.println("----------------------------------------");
        System.out.println("Mensagens do Sistema:");
        if (lembreteAtivo) {
            System.out.println("[✓] Lembrete ativado! Notificaremos você 24h antes.");
        } else {
            System.out.println("[ ] Lembrete desativado.");
        }
        System.out.println("[!] Evento adicionado ao calendário de todos os alunos.");
        System.out.println("========================================\n");
    }
}