package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Repository
public class TargetDao {
    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el target a la base de dades */
    public void addTarget(Target target) {
        jdbcTemplate.update("INSERT INTO target (number, description, relevance, id_sdg) VALUES(?, ?, ?, ?)",
                target.getNumber(), target.getDescription(), target.getRelevance().toString(), target.getIdSdg());
    }

    /* Esborra el target de la base de dades */
    public void deleteTarget(int id_target) {
        jdbcTemplate.update("DELETE FROM target WHERE id_target=?", id_target);
    }

    /* Actualitza els atributs del target
       (excepte el nom, que és la clau primària) */
    public void updateTarget(Target target) {
        jdbcTemplate.update("UPDATE target SET  number=?,description=?,relevance=?,id_sdg=? WHERE id_target = ?"
                ,target.getNumber(),target.getDescription(),target.getRelevance().toString(),target.getIdSdg(),target.getIdTarget());
    }

    /* Obté el target amb el id donat. Torna null si no existeix. */
    public Target getTarget(int id_target) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM target WHERE id_target = ?", new TargetRowMapper(), id_target);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Target> getTargetsFromSdg(int idSdg) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM target WHERE id_sdg = ?",
                    new TargetRowMapper(),
                    idSdg
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    /* Obté tots els target. Torna una llista buida si no n'hi ha cap. */
    public List<Target> getTargets() {
        try {
            return jdbcTemplate.query("SELECT * FROM target", new TargetRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Target>();
        }
    }
}
