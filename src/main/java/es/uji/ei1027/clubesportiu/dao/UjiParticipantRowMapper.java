package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.UjiType;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UjiParticipantRowMapper implements RowMapper {
    @Override
    public UjiParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {
        UjiParticipant ujiParticipant=new UjiParticipant();
        ujiParticipant.setIdParticipant(rs.getInt("id_participant"));
        ujiParticipant.setName(rs.getString("name"));
        ujiParticipant.setType(UjiType.valueOf(rs.getString("type")));
        ujiParticipant.setEmailAddress(rs.getString("email_address"));
        ujiParticipant.setPhoneNumber(rs.getString("phone_number"));
        ujiParticipant.setAdmin(rs.getBoolean("admin"));
        ujiParticipant.setPassword(rs.getString("password"));

        return ujiParticipant;
    }
}
