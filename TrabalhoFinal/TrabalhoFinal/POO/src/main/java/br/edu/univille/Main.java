package br.edu.univille;

import br.edu.univille.dao.ListaDao;
import br.edu.univille.dao.TarefaDao;
import br.edu.univille.service.ListaService;
import br.edu.univille.service.TarefaService;
import br.edu.univille.ui.TelaInicial;

import java.awt.*;

public class Main {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaInicial frame = new TelaInicial(new ListaService(new ListaDao()), new TarefaService(new TarefaDao()));
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
