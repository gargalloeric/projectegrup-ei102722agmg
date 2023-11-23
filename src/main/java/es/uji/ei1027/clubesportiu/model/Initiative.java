package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.ClassificationType;
import es.uji.ei1027.clubesportiu.enums.StatusType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Initiative {
    private  int idInitiative;
    private String name;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private StatusType status;
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime lastModified;
    private  int idSdg;
    private int idUjiParticipant;

    private ClassificationType classification;

    @Override
    public String toString() {
        return "Initiative{" +
                "id_initiative='" + idInitiative + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", lastModified=" + lastModified +
                ", id_sgd='" + idSdg + '\'' +
                ", id_ujiparticipant='" + idUjiParticipant + '\'' +
                '}';
    }

    public int getIdInitiative() {
        return idInitiative;
    }

    public void setIdInitiative(int idInitiative) {
        this.idInitiative = idInitiative;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public int getIdSdg() {
        return idSdg;
    }

    public void setIdSdg(int idSdg) {
        this.idSdg = idSdg;
    }

    public int getIdUjiParticipant() {
        return idUjiParticipant;
    }

    public void setIdUjiParticipant(int idUjiParticipant) {
        this.idUjiParticipant = idUjiParticipant;
    }


    public ClassificationType getClassification() {
        return classification;
    }

    public void setClassification(ClassificationType classification) {
        this.classification = classification;
    }

}
