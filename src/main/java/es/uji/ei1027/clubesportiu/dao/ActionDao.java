package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Repository
public class ActionDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el action a la base de dades */
    public void addAction(Action action) {
        jdbcTemplate.update("INSERT INTO Action (name, startDate, endDate, lastModified, description, progress, id_target, id_initiative) VALUES(?, ?, ?, ?, ?,?,?,?)",
                action.getName(), action.getStartDate(), action.getEndDate(), action.getLastModified(),
                action.getDescription(), action.getProgress().toString(), action.getIdTarget(), action.getIdInitiative());
    }

    /* Esborra el nadador de la base de dades */
    public void deleteAction(String action) {
        jdbcTemplate.update("DELETE FROM Action WHERE name=?", action);
    }

    /* Actualitza els atributs del nadador
       (excepte el nom, que és la clau primària) */
    public void updateAction(Action action) {
        jdbcTemplate.update("UPDATE Action SET name=?, startDate=?,endDate=?,description = ?, progress=?, id_target=?,id_initiative=? WHERE id_action = ?"
                , action.getName(), action.getStartDate(), action.getEndDate(), action.getDescription(), action.getProgress().toString()
                , action.getIdTarget(), action.getIdInitiative(), action.getIdAction());
    }

    public List<Action> getActionsByInitiative(int idAction) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM action WHERE id_initiative = ?",
                    new ActionRowMapper(),
                    idAction
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }
    public Action getAction(int idAction) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Action WHERE id_action = ?", new ActionRowMapper(), idAction);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Action> getAction() {
        try {
            return jdbcTemplate.query("SELECT * FROM Action", new ActionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Action>();
        }
    }
}

