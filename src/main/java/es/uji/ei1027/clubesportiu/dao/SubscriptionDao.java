package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Repository
public class SubscriptionDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el action a la base de dades */
    public void addSubscription(Subscription subscription) {
        jdbcTemplate.update("INSERT INTO Subscription (id_participant, id_sdg, startDate, endDate) VALUES(?, ?, ?, ?)",
                subscription.getIdUjiParticipant(), subscription.getIdSdg(), subscription.getStartDate(), subscription.getEndDate());

    }

    /* Esborra el nadador de la base de dades */
    public void deleteSubscription(String subscription) {
        jdbcTemplate.update("DELETE FROM Subscription WHERE name=?", subscription);
    }

    /* Actualitza els atributs del nadador
       (excepte el nom, que és la clau primària) */
    public void updateSubscription(Subscription subscription) {
        jdbcTemplate.update("UPDATE Subscription SET startDate=?,endDate=? WHERE id_sdg = ? AND id_participant = ?"
                ,subscription.getStartDate(), subscription.getEndDate()
                ,subscription.getIdSdg(), subscription.getIdUjiParticipant());
    }

    /* Obté el nadador amb el nom donat. Torna null si no existeix. */
    public Subscription getSubscription(int idSdg, int idParticipant) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Subscription WHERE id_sdg = ? AND id_participant = ?",
                    new SubscriptionRowMapper(),
                    idSdg,
                    idParticipant
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Subscription> getSubscriptionsFromUser(int idUser) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM subscription WHERE id_participant = ?",
                    new SubscriptionRowMapper(),
                    idUser
            );
        } catch (EmptyResultDataAccessException e) {
            return new LinkedList<>();
        }
    }

    /* Obté tots els nadadors. Torna una llista buida si no n'hi ha cap. */
    public List<Subscription> getListSubscription() {
        try {
            return jdbcTemplate.query("SELECT * FROM Subscription", new SubscriptionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Subscription>();
        }
    }
}