package es.uji.ei1027.clubesportiu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import es.uji.ei1027.clubesportiu.enums.ProgressType;
import es.uji.ei1027.clubesportiu.model.Action;
import org.springframework.jdbc.core.RowMapper;

public final class ActionRowMapper implements RowMapper<Action> {
    public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
        Action action = new Action();
        action.setIdAction(rs.getInt("id_action"));
        action.setName(rs.getString("name"));
        action.setStartDate(rs.getObject("startDate", LocalDate.class));
        action.setEndDate(rs.getObject("endDate",LocalDate.class));
        action.setDescription(rs.getString("description"));
        action.setProgress(ProgressType.valueOf(rs.getString("progress")));
        action.setLastModified(rs.getObject("lastModified", LocalDate.class));
        action.setIdTarget(rs.getInt("id_target"));
        action.setIdInitiative(rs.getInt("id_initiative"));

        return action;
    }


}
