package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Sdg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Repository
public class SdgDao {

    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el sdg a la base de dades */
    public void addSdg(Sdg sdg) {
        jdbcTemplate.update("INSERT INTO sdg (name, number, relevance) VALUES(?, ?, ?)",
                sdg.getName(), sdg.getNumber(), sdg.getRelevance().toString());
    }

    /* Esborra el sdg de la base de dades */
    public void deleteSdg(int idSdg) {
        jdbcTemplate.update("DELETE FROM sdg WHERE id_sdg = ?", idSdg);
    }

    /* Actualitza els atributs del nadador */
    public void updateSdg(Sdg sdg) {
        jdbcTemplate.update("UPDATE sdg SET name = ?, number = ?, relevance = ? WHERE id_sdg = ?"
                ,sdg.getName(), sdg.getNumber(), sdg.getRelevance().toString(), sdg.getIdSdg());
    }

    public String getSdgName(int idSdg) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT name FROM sdg WHERE id_sdg = ?",
                    String.class,
                    idSdg
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté el nadador amb el nom donat. Torna null si no existeix. */
    public Sdg getSdg(int idSdg) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM sdg WHERE id_sdg = ?", new SdgRowMapper(), idSdg);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els nadadors. Torna una llista buida si no n'hi ha cap. */
    public List<Sdg> getSdgs() {
        try {
            return jdbcTemplate.query("SELECT * FROM sdg", new SdgRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Sdg>();
        }
    }
}
