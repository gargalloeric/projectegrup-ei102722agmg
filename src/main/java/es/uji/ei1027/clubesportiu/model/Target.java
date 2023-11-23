package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.RelevanceType;

public class Target {
    private int idTarget;
    private String number;
    private String description;
    private RelevanceType relevance;
    private int idSdg;

    @Override
    public String toString() {
        return "Target{" +
                "id_target='" + idTarget + '\'' +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", relevance='" + relevance + '\'' +
                ", id_sdg='" + idSdg + '\'' +
                '}';
    }

    public int getIdSdg() {
        return idSdg;
    }

    public void setIdSdg(int idSdg) {
        this.idSdg = idSdg;
    }

    public RelevanceType getRelevance() {
        return this.relevance;
    }

    public void setRelevance(RelevanceType relevance) {
        this.relevance = relevance;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

}
