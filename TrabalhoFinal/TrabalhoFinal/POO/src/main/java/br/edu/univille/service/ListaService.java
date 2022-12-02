package br.edu.univille.service;

import br.edu.univille.dao.ListaDao;
import br.edu.univille.model.Lista;

import javax.swing.*;
import java.util.ArrayList;

public class ListaService {

    private final ListaDao listaDao;

    public ListaService (ListaDao listaDao) {
        this.listaDao = listaDao;
    }

    public void criarLista(Lista lista) {
        try{
            listaDao.create(lista);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao criar lista (por favor, tente novamente)");
        }
    }

    public void excluirLista(int idLista) {
        Lista lista = listaDao.readOne(idLista);
        listaDao.delete(lista.getId());
    }

    public void excluirTodasAsListas(){
        listaDao.deleteAll();
    }

    public void atualizarLista(Lista listaNew) {
        listaDao.update(listaNew);
    }

    public ArrayList<Lista> listarTodasAsListas() {
        return listaDao.readAll();
    }

    public Lista listarUmaListaPeloId(int idLista){
        return listaDao.readOne(idLista);
    }

}
