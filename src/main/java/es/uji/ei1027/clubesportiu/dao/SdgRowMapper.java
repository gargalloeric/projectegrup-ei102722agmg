package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.Sdg;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SdgRowMapper implements RowMapper<Sdg> {
    @Override
    public Sdg mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sdg sdg = new Sdg();
        sdg.setIdSdg(rs.getInt("id_sdg"));
        sdg.setName(rs.getString("name"));
        sdg.setNumber(rs.getInt("number"));
        sdg.setRelevance(RelevanceType.valueOf(rs.getString("relevance")));

        return sdg;
    }
}
