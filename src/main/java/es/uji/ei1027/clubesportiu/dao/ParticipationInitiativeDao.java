package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.ParticipantInitiativeDetails;
import es.uji.ei1027.clubesportiu.model.ParticipationInitiative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Repository
public class ParticipationInitiativeDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el action a la base de dades */
    public void addparticipationInitiative(ParticipationInitiative participationInitiative) {
        jdbcTemplate.update("INSERT INTO ParticipationInitiative (id_participant, id_initiative, startDate, endDate, status) VALUES(?, ?, ?, ?, ?)",
                participationInitiative.getIdParticipant(), participationInitiative.getIdInitiative(), participationInitiative.getStartDate(), participationInitiative.getEndDate(),
                participationInitiative.getStatus().toString());
    }

    /* Esborra el nadador de la base de dades */
    public void deleteparticipationInitiative(String participationInitiative) {
        jdbcTemplate.update("DELETE FROM ParticipationInitiative WHERE name=?", participationInitiative);
    }

    /* Actualitza els atributs del nadador
       (excepte el nom, que és la clau primària) */
    public void updateParticipationInitiative(ParticipationInitiative participationInitiative) {
        jdbcTemplate.update("UPDATE ParticipationInitiative SET startDate=?,endDate=?,status=? WHERE id_participant = ? AND id_initiative = ?"
                , participationInitiative.getStartDate(), participationInitiative.getEndDate(), participationInitiative.getStatus().toString()
                , participationInitiative.getIdParticipant(), participationInitiative.getIdInitiative());
    }

    /* Obté el nadador amb el nom donat. Torna null si no existeix. */
    public ParticipationInitiative getParticipationInitiative(int idInitiative, int idParticipant) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ParticipationInitiative WHERE id_initiative = ? AND id_participant = ?", new ParticipationInitiativeRowMapper(), idInitiative, idParticipant);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ParticipationInitiative> getListParticipationsIninitiative(int idInitiative) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM participationinitiative WHERE id_initiative = ?",
                    new ParticipationInitiativeRowMapper(),
                    idInitiative
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ParticipantInitiativeDetails> getListParticipantDetails(int idInitiative) {
        try {
            return jdbcTemplate.query(
                    "SELECT UjiParticipant.name,UjiParticipant.id_participant,UjiParticipant.type,Initiative.name,ParticipationInitiative.startDate, ParticipationInitiative.endDate, ParticipationInitiative.status\n" +
                            "FROM ParticipationInitiative\n" +
                            "INNER JOIN UjiParticipant\n" +
                            "ON ParticipationInitiative.id_participant=UjiParticipant.id_participant\n" +
                            "INNER JOIN Initiative\n" +
                            "ON ParticipationInitiative.id_initiative=Initiative.id_initiative\n" +
                            "WHERE ParticipationInitiative.id_initiative=? AND ParticipationInitiative.status != 'PENDING'",
                    new ParticipantInitiativeDetailsRowMapper(),
                    idInitiative
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }

    public List<ParticipantInitiativeDetails> getListParticipantsPending(int idInitiative) {
        try {
            return jdbcTemplate.query(
                    "SELECT UjiParticipant.name,UjiParticipant.id_participant,UjiParticipant.type,Initiative.name,ParticipationInitiative.startDate, ParticipationInitiative.endDate, ParticipationInitiative.status\n" +
                            "FROM ParticipationInitiative\n" +
                            "INNER JOIN UjiParticipant\n" +
                            "ON ParticipationInitiative.id_participant=UjiParticipant.id_participant\n" +
                            "INNER JOIN Initiative\n" +
                            "ON ParticipationInitiative.id_initiative=Initiative.id_initiative\n" +
                            "WHERE ParticipationInitiative.id_initiative=? AND ParticipationInitiative.status = 'PENDING'",
                    new ParticipantInitiativeDetailsRowMapper(),
                    idInitiative
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }



    /* Obté tots els nadadors. Torna una llista buida si no n'hi ha cap. */
    public List<ParticipationInitiative> getListParticipationInitiative() {
        try {
            return jdbcTemplate.query("SELECT * FROM ParticipationInitiative", new ParticipationInitiativeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ParticipationInitiative>();
        }
    }
}