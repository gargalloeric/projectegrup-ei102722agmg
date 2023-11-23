package es.uji.ei1027.clubesportiu.model;

import java.time.LocalDate;

public class UjiUserSubscriptionDetail {
    private int idSdg;
    private int idParticipant;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;

    public int getIdSdg() {
        return idSdg;
    }

    public void setIdSdg(int idSdg) {
        this.idSdg = idSdg;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
