package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.UjiType;

public class UjiParticipant {
    private int idParticipant;
    private String name;
    private UjiType type;
    private String emailAddress;
    private String phoneNumber;
    private boolean admin;
    private String password;

    @Override
    public String toString() {
        return "UjiParticipant{" +
                "idParticipant=" + idParticipant +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", admin=" + admin +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public String getName() {
        return name;
    }

    public UjiType getType() {
        return type;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(UjiType type) {
        this.type = type;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
