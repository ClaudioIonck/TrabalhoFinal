package br.edu.univille.dao;

import br.edu.univille.factory.ConnectionFactory;
import br.edu.univille.model.Lista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ListaDao {

    private Connection connection;

    public ListaDao() {
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            this.createTableIfExists();
        } catch (Exception e) {
            System.out.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void create(Lista lista) {
        if(!isValidoTituloLista(lista)){
            throw new RuntimeException("Título da lista inválido");
        }

        String sql = "INSERT INTO lista(titulo, dataCriacao, excluida) VALUES (?, ?, ?);";
        try {
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, lista.getTitulo());
            stmt.setString(2, lista.getDataCriacao().toString());
            stmt.setInt(3, lista.isExcluida() ? 1 : 0);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Falha ao inserir lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Lista readOne(int idLista){
        String sql = "SELECT * FROM lista WHERE id = ?;";
        try {
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idLista);
            ResultSet rs = stmt.executeQuery();

            Lista lista = new Lista();
            if (rs.next()) {
                lista.setId(rs.getInt("id"));
                lista.setTitulo(rs.getString("titulo"));
                lista.setDataCriacao(LocalDate.parse(rs.getString("dataCriacao")));
                lista.setExcluida(rs.getInt("excluida") == 1);
                lista.setTarefas(new TarefaDao().readAllByIdLista(lista.getId()));
                lista.getTarefas().stream().forEach(t -> t.setLista(lista));
            }

            stmt.close();
            return lista;
        } catch (Exception e) {
            System.err.println("Falha ao buscar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<Lista> readAll() {
        String sql = "SELECT * FROM lista;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Lista> listas = new ArrayList<>();
            while (rs.next()) {
                Lista lista = new Lista();
                lista.setId(rs.getInt("id"));
                lista.setTitulo(rs.getString("titulo"));
                lista.setDataCriacao(LocalDate.parse(rs.getString("dataCriacao")));
                lista.setExcluida(rs.getInt("excluida") == 1);
                lista.setTarefas(new TarefaDao().readAllByIdLista(lista.getId()));
                lista.getTarefas().stream().forEach(t -> t.setLista(lista));
                listas.add(lista);
            }

            stmt.close();
            return listas;

        } catch (SQLException e) {
            System.err.println("Falha ao ler lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Lista lista) {
        if(!isValidoTituloLista(lista)){
            throw new RuntimeException("Título da lista inválido");
        }

        String sql = "UPDATE lista SET titulo = ?, excluida = ? WHERE id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, lista.getTitulo());
            stmt.setInt(2, lista.isExcluida() ? 1 : 0);
            stmt.setInt(3, lista.getId());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Falha ao atualizar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(int idLista) {
        String sqlTarefasByList = "DELETE FROM tarefa WHERE idLista = ?;";
        String sqlDeleteList = "DELETE FROM lista WHERE id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlTarefasByList);
            stmt.setInt(1, idLista);
            stmt.execute();
            stmt.close();

            stmt = connection.prepareStatement(sqlDeleteList);
            stmt.setInt(1, idLista);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Falha ao deletar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAll() {
        String sqlTarefas = "DELETE FROM tarefa;";
        String sqlListas = "DELETE FROM lista;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlTarefas);
            stmt.execute();
            stmt.close();

            stmt = connection.prepareStatement(sqlListas);
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Falha ao deletar lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getIdByTitulo(String titulo) {
        String sql = "SELECT id FROM lista WHERE titulo = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            stmt.close();
            return id;
        } catch (SQLException e) {
            System.err.println("Falha ao buscar id da lista: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createTableIfExists(){
        String sql = "CREATE TABLE IF NOT EXISTS lista(      " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "titulo TEXT NOT NULL,                  " +
                "dataCriacao INTEGER NOT NULL DEFAULT 0," +
                "excluida TEXT                        " +
                ");                                     ";
        try {
            this.connection.createStatement().execute(sql);
        } catch (Exception e) {
            System.err.println("Falha ao criar tabela: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isValidoTituloLista(Lista lista){
        ArrayList<Lista> listas = readAll();
        if(listas.stream().anyMatch(l -> l.getTitulo().equals(lista.getTitulo()) && l.getId() != lista.getId())){
            System.err.println("Título da lista já existe");
            return false;
        }

        if (lista.getTitulo().isEmpty()){
            System.err.println("Título da lista não pode ser vazio");
            return false;
        }

        return true;
    }
}
