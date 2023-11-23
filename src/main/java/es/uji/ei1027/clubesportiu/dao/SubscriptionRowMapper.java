package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Subscription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class SubscriptionRowMapper implements RowMapper<Subscription> {
    @Override
    public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setIdSdg(rs.getInt("id_sdg"));
        subscription.setStartDate(rs.getObject("startDate", LocalDate.class));
        subscription.setEndDate(rs.getObject("endDate", LocalDate.class));
        subscription.setIdUjiParticipant(rs.getInt("id_participant"));

        return subscription;
    }
}
