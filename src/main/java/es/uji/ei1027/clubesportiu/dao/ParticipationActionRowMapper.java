package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.ParticipationAction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ParticipationActionRowMapper implements RowMapper<ParticipationAction>{
    @Override
    public ParticipationAction mapRow(ResultSet rs, int rowNum) throws SQLException {
        ParticipationAction participationAction = new ParticipationAction();
        participationAction.setIdAction(rs.getInt("id_action"));
        participationAction.setIdParticipant(rs.getInt("id_participant"));
        participationAction.setStartDate(rs.getObject("startDate", LocalDate.class));
        participationAction.setEndDate(rs.getObject("endDate", LocalDate.class));
        participationAction.setStatus(StatusType.valueOf(rs.getString("status")));

        return participationAction;
    }
}
