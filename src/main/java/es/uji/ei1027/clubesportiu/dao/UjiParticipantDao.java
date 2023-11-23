package es.uji.ei1027.clubesportiu.dao;


import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UjiParticipantDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el target a la base de dades */
    public void addUjiParticipant(UjiParticipant ujiParticipant) {
        jdbcTemplate.update("INSERT INTO UjiParticipant (name, type, email_address, phone_number, admin, password) VALUES(?, ?, ?, ?, ?, ?)",
                ujiParticipant.getName(),
                ujiParticipant.getType().toString(),
                ujiParticipant.getEmailAddress(),
                ujiParticipant.getPhoneNumber(),
                ujiParticipant.isAdmin(),
                ujiParticipant.getPassword());
    }

    /* Esborra el target de la base de dades */
    public void deleteUjiParticipant(String id_participant) {
        jdbcTemplate.update("DELETE FROM UjiParticipant WHERE id_participant=?", id_participant);
    }

    /* Actualitza els atributs del target
       (excepte el nom, que és la clau primària) */
    public void updateUjiParticipant(UjiParticipant ujiParticipant) {
        jdbcTemplate.update("UPDATE UjiParticipant SET  number=?,description=?,relevance=?,id_sgd=? WHERE id_target = ?",
                 ujiParticipant.getName(), ujiParticipant.getType(),ujiParticipant.getEmailAddress(),ujiParticipant.getPhoneNumber(),ujiParticipant.getIdParticipant());
    }

    /* Obté el target amb el id donat. Torna null si no existeix. */
    public UjiParticipant getUjiParticipant(int id_participant) {
        try {
            return (UjiParticipant) jdbcTemplate.queryForObject("SELECT * FROM UjiParticipant WHERE id_participant = ?",new UjiParticipantRowMapper(), id_participant);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UjiParticipant getUjiParticipantByEmail(String email) {
        try {
            return (UjiParticipant) jdbcTemplate.queryForObject(
                    "SELECT * FROM UjiParticipant WHERE email_address = ?",
                    new UjiParticipantRowMapper(),
                    email
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els target. Torna una llista buida si no n'hi ha cap. */
    public List<UjiParticipant> getUjiParticipants() {
        try {
            return jdbcTemplate.query("SELECT * FROM UjiParticipant", new UjiParticipantRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
