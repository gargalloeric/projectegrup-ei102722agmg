package es.uji.ei1027.clubesportiu.model;

import java.time.LocalDate;

public class Subscription {
    private int idSdg;
    private LocalDate startDate;
    private LocalDate endDate;
    private int idUjiParticipant;

    @Override
    public String toString() {
        return "Subscription{" +
                "id_subscription='" + idSdg + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", id_ujiparticipant='" + idUjiParticipant + '\'' +
                '}';
    }

    public int getIdSdg() {
        return idSdg;
    }

    public void setIdSdg(int idSdg) {
        this.idSdg = idSdg;
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

    public int getIdUjiParticipant() {
        return idUjiParticipant;
    }

    public void setIdUjiParticipant(int idUjiParticipant) {
        this.idUjiParticipant = idUjiParticipant;
    }

}
