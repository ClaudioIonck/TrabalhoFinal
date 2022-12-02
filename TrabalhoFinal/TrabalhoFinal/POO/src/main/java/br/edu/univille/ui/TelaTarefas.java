package br.edu.univille.ui;

import br.edu.univille.model.Lista;
import br.edu.univille.model.Tarefa;
import br.edu.univille.service.ListaService;
import br.edu.univille.service.TarefaService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TelaTarefas extends JFrame {
    private final ListaService listaService;
    private final TarefaService tarefaService;

    private JButton btnCancelar;

    public TelaTarefas(ListaService listaService, TarefaService tarefaService, Lista lista) {

        this.listaService = listaService;
        this.tarefaService = tarefaService;

        setTitle("Lista de tarefas: " + lista.getTitulo());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);

        this.btnCancelar = UtilsMethods.newBtn(50, 350, 20, 150, "Voltar", this);
        this.btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 15));

        ArrayList<Tarefa> tarefas = listaService.listarUmaListaPeloId(lista.getId()).getTarefas();
        List<Object> listObjects = UtilsMethods.newJTableByListTarefas(tarefas, listaService, tarefaService,this);

        JTable table = (JTable) listObjects.get(0);
        JScrollPane scrollPane = (JScrollPane) listObjects.get(1);
        table.setBounds(50, 50, 400, 250);


        JButton btnNewTarefa = UtilsMethods.newWindowPanel(350, 350, 20, 50, "+", this, new NovaTarefa(listaService, tarefaService, lista));
        btnNewTarefa.setFont(new java.awt.Font("Tahoma", 1, 18));

        this.labelJtable();
        this.cancelarTarefa();
    }

    private void cancelarTarefa() {
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new TelaInicial(listaService, tarefaService);
        });
    }

    public void labelJtable() {
        JLabel label1 = new JLabel("ID");
        JLabel label2 = new JLabel("Titulo");
        JLabel label3 = new JLabel("Tarefa");
        JLabel label4 = new JLabel("Inicio");
        JLabel label5 = new JLabel("Termino");

        label1.setBounds(50, 25, 50, 25);
        label2.setBounds(110, 25, 50, 25);
        label3.setBounds(160, 25, 150, 25);
        label4.setBounds(220, 25, 150, 25);
        label5.setBounds(285, 25, 150, 25);

        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);

    }
}
