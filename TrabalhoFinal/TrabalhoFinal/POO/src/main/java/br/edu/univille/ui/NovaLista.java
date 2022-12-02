package br.edu.univille.ui;

import br.edu.univille.model.Lista;
import br.edu.univille.service.ListaService;
import br.edu.univille.service.TarefaService;

import javax.swing.*;

public class NovaLista extends JFrame {

    private final ListaService listaService;
    private final TarefaService tarefaService;
    private JTextArea txtTitulo;
    private JCheckBox checkExcluir;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public NovaLista(ListaService listaService, TarefaService tarefaService) {

        this.listaService = listaService;
        this.tarefaService = tarefaService;

        setTitle("Nova lista");
        setSize(500, 200);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.txtTitulo = UtilsMethods.newJtextArea(50, 50, 25, 400, "Titulo", this);
        this.checkExcluir = UtilsMethods.newCheckBox(50, 100, 10, 15, "Concluida", this);
        this.btnCancelar = UtilsMethods.newBtn(225, 100, 20, 100, "Cancelar", this);
        this.btnSalvar = UtilsMethods.newBtn(350, 100, 20, 100, "Salvar", this);

        this.cancelarLista();
        this.salvarLista();

    }

    private void cancelarLista() {
        btnCancelar.addActionListener(e -> {
            this.dispose();
            new TelaInicial(listaService, tarefaService);
        });
    }

    private void salvarLista() {
        btnSalvar.addActionListener(e -> {
            Lista lista = new Lista();
            lista.setTitulo(txtTitulo.getText());
            lista.setExcluida(checkExcluir.isSelected());
            listaService.criarLista(lista);

            this.dispose();
            new TelaInicial(listaService, tarefaService);
        });
    }
}
