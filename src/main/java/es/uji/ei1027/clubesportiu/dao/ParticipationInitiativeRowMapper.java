package es.uji.ei1027.clubesportiu.dao;


import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.ParticipationInitiative;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
public class ParticipationInitiativeRowMapper implements RowMapper<ParticipationInitiative> {
    @Override
    public ParticipationInitiative mapRow(ResultSet rs, int rowNum) throws SQLException {
        ParticipationInitiative participationInitiative = new ParticipationInitiative();
        participationInitiative.setIdParticipant(rs.getInt("id_participant"));
        participationInitiative.setIdInitiative(rs.getInt("id_initiative"));
        participationInitiative.setStartDate(rs.getObject("startDate", LocalDate.class));
        participationInitiative.setEndDate(rs.getObject("endDate", LocalDate.class));
        participationInitiative.setStatus(StatusType.valueOf(rs.getString("status")));

        return participationInitiative;
    }
}
