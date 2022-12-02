package br.edu.univille.service;

import br.edu.univille.dao.TarefaDao;
import br.edu.univille.model.Tarefa;

import javax.swing.*;
import java.time.LocalDate;

public class TarefaService {

    private final TarefaDao tarefaDao;

    public TarefaService (TarefaDao tarefaDao) {
        this.tarefaDao = tarefaDao;
    }

    public void adicionarTarefaEmUmaLista(Tarefa tarefa) {

        if (tarefa.getTitulo().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um título para a tarefa");
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio");
        }

        if(tarefa.isConcluida()){
            tarefa.setDataConclusao(LocalDate.now());
        }

        tarefa.setId(tarefaDao.getProximoId());
        tarefaDao.create(tarefa);
    }

    public void concluirTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.readOne(idTarefa);
        tarefa.setConcluida(true);
        tarefa.setDataConclusao(LocalDate.now());
        tarefaDao.update(tarefa);
    }

    public void desfazerTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.readOne(idTarefa);
        tarefa.setConcluida(false);
        tarefa.setDataConclusao(null);
        tarefaDao.update(tarefa);
    }

    public void excluirTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.readOne(idTarefa);
        tarefaDao.delete(tarefa.getId());
    }

    public void atualizarTarefa(int idTarefaOld, Tarefa tarefaNew) {
        Tarefa tarefaOld = tarefaDao.readOne(idTarefaOld);
        tarefaDao.update(tarefaNew);
    }

    public Tarefa listarUmaTarefa(int idTarefa) {
        Tarefa tarefa = tarefaDao.readOne(idTarefa);
        return tarefa;
    }
}
