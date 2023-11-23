package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.enums.UjiType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ParticipantInitiativeDetails {
    private String name;
    private int idParticipant;
    private UjiType type;
    private StatusType status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public UjiType getType() {
        return type;
    }

    public void setType(UjiType type) {
        this.type = type;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ParticipantInitiativeDetails{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
