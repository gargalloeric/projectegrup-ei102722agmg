package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.StatusType;

import java.time.LocalDate;

public class ParticipationAction {
    private int idAction;
    private  int idParticipant;
    private LocalDate startDate;
    private LocalDate endDate;
    private StatusType status;

    @Override
    public String toString() {
        return "ParticipationAction{" +
                "id_action='" + idAction + '\'' +
                ", id_participant='" + idParticipant + '\'' +
                ", startDate=" + startDate +
                ", enDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
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

    public void setEndDate(LocalDate enDate) {
        this.endDate = enDate;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

}
