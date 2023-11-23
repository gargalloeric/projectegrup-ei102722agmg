package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.Target;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TargetRowMapper implements RowMapper<Target> {
    @Override
    public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
        Target target=new Target();
        target.setIdTarget(rs.getInt("id_target"));
        target.setNumber(rs.getString("number"));
        target.setDescription(rs.getString("description"));
        target.setRelevance(RelevanceType.valueOf(rs.getString("relevance")));
        target.setIdSdg(rs.getInt("id_sdg"));

        return target;
    }
}
