package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.UjiParticipant;

public interface UserLoginDao {
    UjiParticipant loadUserByEmail(String name, String password);
}
