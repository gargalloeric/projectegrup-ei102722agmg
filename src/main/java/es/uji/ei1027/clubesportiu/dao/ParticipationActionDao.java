package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.ParticipationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ParticipationActionDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el action a la base de dades */
    public void addparticipationAction(ParticipationAction participationAction) {
        jdbcTemplate.update("INSERT INTO ParticipationAction VALUES(?, ?, ?, ?, ?)",
                participationAction.getIdAction(), participationAction.getIdParticipant(), participationAction.getStartDate(), participationAction.getEndDate(),
                participationAction.getStatus());
    }

    /* Esborra el nadador de la base de dades */
    public void deleteparticipationAction(String participationAction) {
        jdbcTemplate.update("DELETE FROM ParticipationAction WHERE name=?", participationAction);
    }

    /* Actualitza els atributs del nadador
       (excepte el nom, que és la clau primària) */
    public void updateparticipationAction(ParticipationAction participationAction) {
        jdbcTemplate.update("UPDATE ParticipationAction SET startDate=?,endDate=?,status=? WHERE id_action = ? and id_participant = ?"
                , participationAction.getStartDate(), participationAction.getEndDate(), participationAction.getStatus()
                , participationAction.getIdAction(), participationAction.getIdParticipant());
    }

    /* Obté el nadador amb el nom donat. Torna null si no existeix. */
    public ParticipationAction getparticipationAction(String nomAction) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ParticipationAction WHERE nom = ?", new ParticipationActionRowMapper(), nomAction);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els nadadors. Torna una llista buida si no n'hi ha cap. */
    public List<ParticipationAction> getListparticipationAction() {
        try {
            return jdbcTemplate.query("SELECT * FROM ParticipationAction", new ParticipationActionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ParticipationAction>();
        }
    }
}
