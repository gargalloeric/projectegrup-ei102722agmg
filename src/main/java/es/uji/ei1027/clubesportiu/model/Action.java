package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.ProgressType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Action {
    private int idAction;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDate lastModified;
    private String description;
    private ProgressType progress;
    private int idTarget;
    private int idInitiative;
    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProgressType getProgress() {
        return progress;
    }

    public void setProgress(ProgressType progress) {
        this.progress = progress;
    }

    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

    public int getIdInitiative() {
        return idInitiative;
    }

    public void setIdInitiative(int idInitiative) {
        this.idInitiative = idInitiative;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "Action{" +
                "idAction=" + idAction +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", lastModified=" + lastModified +
                ", description='" + description + '\'' +
                ", progress=" + progress +
                ", idTarget=" + idTarget +
                ", idInitiative=" + idInitiative +
                '}';
    }
}
