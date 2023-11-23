package es.uji.ei1027.clubesportiu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import es.uji.ei1027.clubesportiu.enums.ClassificationType;
import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.Initiative;
import org.springframework.jdbc.core.RowMapper;

public final class InitiativeRowMapper implements RowMapper<Initiative> {

    @Override
    public Initiative mapRow(ResultSet rs, int rowNum) throws SQLException {
        Initiative initiative = new Initiative();
        initiative.setIdInitiative(rs.getInt("id_initiative"));
        initiative.setName(rs.getString("name"));
        initiative.setDescription(rs.getString("description"));
        initiative.setStartDate(rs.getObject("startDate", LocalDate.class));
        initiative.setEndDate(rs.getObject("endDate",LocalDate.class));
        initiative.setStatus(StatusType.valueOf(rs.getString("status")));
        initiative.setLastModified(rs.getObject("lastModified", LocalDateTime.class));
        initiative.setIdSdg(rs.getInt("id_sdg"));
        initiative.setIdUjiParticipant(rs.getInt("id_participant"));
        initiative.setClassification(ClassificationType.valueOf(rs.getString("classification")));

        return initiative;
    }
}
