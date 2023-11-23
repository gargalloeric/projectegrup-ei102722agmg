package es.uji.ei1027.clubesportiu.dao;


import es.uji.ei1027.clubesportiu.model.Initiative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
@Repository
public class InitiativeDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el action a la base de dades */
    public Integer addInitative(Initiative initiative) {
        return jdbcTemplate.queryForObject("INSERT INTO Initiative (name, description, startDate, endDate, status, lastModified, id_sdg, id_participant, classification) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_initiative",
                new Object[] {initiative.getName(), initiative.getDescription(), initiative.getStartDate(),
                initiative.getEndDate(), initiative.getStatus().toString(), initiative.getLastModified(), initiative.getIdSdg(), initiative.getIdUjiParticipant(), initiative.getClassification().toString()},
                Integer.class);
    }

    /* Esborra el nadador de la base de dades */
    public void deleteInitiative(int idInitiative) {
        jdbcTemplate.update("DELETE FROM Initiative WHERE id_initiative=?", idInitiative);
    }

    /* Actualitza els atributs del nadador
       (excepte el nom, que és la clau primària) */
    public void updateInitiative(Initiative initiative) {
        jdbcTemplate.update(
                "UPDATE Initiative SET name=?, description=?,startDate=?,endDate = ?, status=?, lastModified=?,id_sdg=?, id_participant= ? , classification= ? WHERE id_initiative = ?"
                , initiative.getName(), initiative.getDescription(), initiative.getStartDate(), initiative.getEndDate(), initiative.getStatus().toString()
                , initiative.getLastModified(), initiative.getIdSdg(), initiative.getIdUjiParticipant(), initiative.getClassification().toString(),initiative.getIdInitiative());
    }

    /* Obté el nadador amb el nom donat. Torna null si no existeix. */
    public Initiative getInitative(int idInitiative) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Initiative WHERE id_initiative = ?", new InitiativeRowMapper(), idInitiative);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Initiative> getListInitiativesActive() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM initiative WHERE status='ACTIVE'",
                    new InitiativeRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }

    public List<Initiative> getListInitiativesForFeed(List<Integer> ids) {
        if (ids.isEmpty()) {
            return new LinkedList<>();
        }
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        try {
            return jdbcTemplate.query(
                    String.format("SELECT * FROM initiative WHERE id_sdg IN (%s) AND status != 'PENDING'", inSql),
                    new InitiativeRowMapper(),
                    ids.toArray()
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }


    public List<Initiative> getListInitiativesFromUser(int idUser) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM initiative WHERE id_participant = ?",
                    new InitiativeRowMapper(),
                    idUser
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }

    /* Obté tots els nadadors. Torna una llista buida si no n'hi ha cap. */
    public List<Initiative> getListInitiatives() {
        try {
            return jdbcTemplate.query("SELECT * FROM Initiative", new InitiativeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }
}
