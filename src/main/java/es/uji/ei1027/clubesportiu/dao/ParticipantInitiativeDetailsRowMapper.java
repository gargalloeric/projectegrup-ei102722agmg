package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.enums.UjiType;
import es.uji.ei1027.clubesportiu.model.ParticipantInitiativeDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ParticipantInitiativeDetailsRowMapper implements RowMapper<ParticipantInitiativeDetails> {
    @Override
    public ParticipantInitiativeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        ParticipantInitiativeDetails i = new ParticipantInitiativeDetails();
        i.setName(rs.getString("name"));
        i.setIdParticipant(rs.getInt("id_participant"));
        i.setType(UjiType.valueOf(rs.getString("type")));
        i.setStatus(StatusType.valueOf(rs.getString("status")));
        i.setStartDate(rs.getObject("startDate", LocalDate.class));
        i.setEndDate(rs.getObject("endDate", LocalDate.class));
        return i;
    }
}
