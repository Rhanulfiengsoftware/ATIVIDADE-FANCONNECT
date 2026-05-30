package br.com.fanconnect;

import br.com.fanconnect.model.Categoria;
import br.com.fanconnect.model.Evento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaInterface extends JFrame {
    private List<Evento> eventos = new ArrayList<>();
    private JTextArea txtDetalhes;
    private JButton btnSalvar;
    private Evento eventoSelecionado;

    // Memória do programa para salvar suas anotações separadas por Mês e por Evento
    // Chave exemplo: "Janeiro-Prova de Cálculo II", Valor: "Texto que você digitou"
    private Map<String, String> bancoDeDadosAnotacoes = new HashMap<>();

    private JComboBox<String> comboMeses;
    private JLabel lblMesAno;
    private String mesSelecionado = "Maio";

    // Cores fiéis à imagem do FanConnect
    private final Color LARANJA = new Color(235, 94, 40);
    private final Color AZUL_ESCURO = new Color(20, 53, 105);
    private final Color FUNDO_CLARO = new Color(248, 249, 250);

    private final String[] MESES = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public AgendaInterface() {
        inicializarDados();
        inicializarTextosPadrao();

        // Configurações da Janela (Visual de 3 colunas permanente)
        setTitle("FanConnect - Agenda Acadêmica Interativa");
        setSize(980, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(FUNDO_CLARO);

        // 1. TOPO: Menu de Seleção de Meses + Nome do Usuário
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelTopo.setBackground(Color.WHITE);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel lblSelecione = new JLabel("Selecione o Mês:");
        lblSelecione.setFont(new Font("Arial", Font.BOLD, 12));

        comboMeses = new JComboBox<>(MESES);
        comboMeses.setSelectedItem("Maio");
        comboMeses.addActionListener(e -> {
            mesSelecionado = (String) comboMeses.getSelectedItem();
            lblMesAno.setText(mesSelecionado + " 2026");

            // Se houver algum evento selecionado na hora da troca de mês, carrega o texto daquele novo mês
            if (eventoSelecionado != null) {
                carregarTextoDoEvento(eventoSelecionado);
            } else {
                txtDetalhes.setText("\n Mês alterado para " + mesSelecionado + ". Clique em um evento ao lado para escrever nele...");
                txtDetalhes.setEnabled(false);
                btnSalvar.setEnabled(false);
            }
        });

        lblMesAno = new JLabel("Maio 2026");
        lblMesAno.setFont(new Font("Arial", Font.BOLD, 14));
        lblMesAno.setForeground(AZUL_ESCURO);

        JLabel lblUsuario = new JLabel("    |    Usuário: RHANULFI DE OLIVEIRA SANTOS [✓ Conectado]");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setForeground(Color.GRAY);

        painelTopo.add(lblSelecione);
        painelTopo.add(comboMeses);
        painelTopo.add(lblMesAno);
        painelTopo.add(lblUsuario);
        add(painelTopo, BorderLayout.NORTH);

        // 2. PAINEL CENTRAL (As 3 colunas que você gostou)
        JPanel painelCentral = new JPanel(new GridLayout(1, 3, 15, 15));
        painelCentral.setBackground(FUNDO_CLARO);

        // COLUNA 1: Desenho do Calendário Acadêmico
        JPanel painelCalendario = new JPanel(new BorderLayout(5, 5));
        painelCalendario.setBackground(Color.WHITE);
        painelCalendario.setBorder(BorderFactory.createTitledBorder("Calendário Acadêmico"));

        JPanel painelDias = new JPanel(new GridLayout(6, 7, 2, 2));
        painelDias.setBackground(Color.WHITE);
        String[] diasSemana = {"D", "S", "T", "Q", "Q", "S", "S"};
        for (String d : diasSemana) {
            JLabel lblDia = new JLabel(d, JLabel.CENTER);
            lblDia.setFont(new Font("Arial", Font.BOLD, 12));
            painelDias.add(lblDia);
        }

        String[] diasMes = {
                "", "", "", "", "", "1", "2",
                "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23",
                "24", "25", "26", "27", "28", "29", "30",
                "31", "", "", "", "", "", ""
        };

        for (String dia : diasMes) {
            JLabel lblDia = new JLabel(dia, JLabel.CENTER);
            lblDia.setFont(new Font("Arial", Font.PLAIN, 12));
            if (dia.equals("20") || dia.equals("25")) {
                lblDia.setForeground(LARANJA);
                lblDia.setFont(new Font("Arial", Font.BOLD, 13));
            } else if (dia.equals("22") || dia.equals("28")) {
                lblDia.setForeground(AZUL_ESCURO);
                lblDia.setFont(new Font("Arial", Font.BOLD, 13));
            }
            painelDias.add(lblDia);
        }
        painelCalendario.add(painelDias, BorderLayout.CENTER);

        JLabel lblLegenda = new JLabel("<html><font color='orange'>●</font> Burocracia/Provas &nbsp;&nbsp; <font color='blue'>●</font> Palestras/Sociais</html>", JLabel.CENTER);
        lblLegenda.setFont(new Font("Arial", Font.PLAIN, 11));
        painelCalendario.add(lblLegenda, BorderLayout.SOUTH);


        // COLUNA 2: Lista de Próximos Eventos (Fica VISÍVEL PARA SEMPRE em todos os meses)
        JPanel painelListaEventos = new JPanel();
        painelListaEventos.setLayout(new BoxLayout(painelListaEventos, BoxLayout.Y_AXIS));
        painelListaEventos.setBackground(Color.WHITE);
        painelListaEventos.setBorder(BorderFactory.createTitledBorder("Painel de Compromissos"));

        for (Evento ev : eventos) {
            JButton btnEvento = new JButton(ev.getTitulo());
            btnEvento.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnEvento.setMaximumSize(new Dimension(280, 45));
            btnEvento.setFont(new Font("Arial", Font.BOLD, 12));
            btnEvento.setForeground(Color.WHITE);

            if (ev.getCategoria() == Categoria.PROVAS || ev.getCategoria() == Categoria.BUROCRACIA) {
                btnEvento.setBackground(LARANJA);
            } else {
                btnEvento.setBackground(AZUL_ESCURO);
            }

            // Ao clicar, carrega o texto específico deste evento para o mês selecionado
            btnEvento.addActionListener(e -> carregarTextoDoFolder(ev));
            painelListaEventos.add(Box.createRigidArea(new Dimension(0, 10)));
            painelListaEventos.add(btnEvento);
        }


        // COLUNA 3: Bloco de Notas Livre para Escrever e Salvar
        JPanel painelDetalhes = new JPanel(new BorderLayout(10, 10));
        painelDetalhes.setBackground(Color.WHITE);
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Anotações Gerais do Mês Escolhido"));

        txtDetalhes = new JTextArea("\n Selecione um mês no topo e clique em uma categoria ao lado para escrever...");
        txtDetalhes.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDetalhes.setLineWrap(true);
        txtDetalhes.setWrapStyleWord(true);
        txtDetalhes.setMargin(new Insets(10, 10, 10, 10));
        txtDetalhes.setEnabled(false);
        painelDetalhes.add(new JScrollPane(txtDetalhes), BorderLayout.CENTER);

        btnSalvar = new JButton("Salvar Notas deste Mês");
        btnSalvar.setBackground(AZUL_ESCURO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 13));
        btnSalvar.setEnabled(false);

        // Ação de salvar o texto modificado
        btnSalvar.addActionListener(e -> {
            if (eventoSelecionado != null) {
                String chaveUnica = mesSelecionado + "-" + eventoSelecionado.getTitulo();
                bancoDeDadosAnotacoes.put(chaveUnica, txtDetalhes.getText());

                JOptionPane.showMessageDialog(this,
                        "✓ Suas anotações de " + mesSelecionado + " foram salvas!",
                        "Agenda Atualizada",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        painelDetalhes.add(btnSalvar, BorderLayout.SOUTH);

        // Adiciona as 3 colunas estruturadas ao painel central
        painelCentral.add(painelCalendario);
        painelCentral.add(painelListaEventos);
        painelCentral.add(painelDetalhes);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void inicializarDados() {
        // As 4 opções fixas baseadas na imagem
        eventos.add(new Evento("Provas", LocalDateTime.now(), "", "", Categoria.PROVAS));
        eventos.add(new Evento("Palestras", LocalDateTime.now(), "", "", Categoria.PALESTRAS));
        eventos.add(new Evento("Lembretes", LocalDateTime.now(), "", "", Categoria.BUROCRACIA));
        eventos.add(new Evento("Festa e Eventos Acadêmica", LocalDateTime.now(), "", "", Categoria.SOCIAIS));
    }

    private void inicializarTextosPadrao() {
        // Deixa os textos originais salvos automaticamente no mês de Maio para manter o padrão da foto
        bancoDeDadosAnotacoes.put("Maio-Prova de Cálculo II", "Título: Prova de Cálculo II\nLocal: Bloco A, Sala 302\nOrganizador: Dept. Matemática\n\n[Escreva mais anotações aqui...]");
        bancoDeDadosAnotacoes.put("Maio-Palestra: IA na Educação", "Título: Palestra: IA na Educação\nLocal: Auditório Principal\nOrganizador: Coordenação de TI\n\n[Escreva mais anotações aqui...]");
        bancoDeDadosAnotacoes.put("Maio-Matrícula 2026.2", "Título: Matrícula 2026.2\nLocal: Portal do Aluno / Online\nOrganizador: Secretaria Acadêmica\n\n[Escreva mais anotações aqui...]");
        bancoDeDadosAnotacoes.put("Maio-Festa Junina Acadêmica", "Título: Festa Junina Acadêmica\nLocal: Pátio Central\nOrganizador: Diretório Central dos Estudantes\n\n[Escreva mais anotações aqui...]");
    }

    private void carregarTextoDoFolder(Evento ev) {
        this.eventoSelecionado = ev;
        txtDetalhes.setEnabled(true);
        btnSalvar.setEnabled(true);
        carregarTextoDoEvento(ev);
    }

    private void carregarTextoDoEvento(Evento ev) {
        String chaveUnica = mesSelecionado + "-" + ev.getTitulo();

        // Se já tiver algo escrito no mês para essa categoria, carrega. Se não, deixa em branco para você digitar do zero!
        String textoSalvo = bancoDeDadosAnotacoes.getOrDefault(chaveUnica,
                "Anotações para [" + ev.getTitulo() + "] no mês de " + mesSelecionado + ":\n\n- ");

        txtDetalhes.setText(textoSalvo);
        txtDetalhes.requestFocus();
    }
}