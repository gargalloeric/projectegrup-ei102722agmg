package es.uji.ei1027.clubesportiu.model;

import es.uji.ei1027.clubesportiu.enums.RelevanceType;

public class Sdg {
    private int idSdg;
    private String name;
    private int number;
    private RelevanceType relevance;

    @Override
    public String toString() {
        return "Ods{" +
                "id='" + idSdg + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", relevance=" + relevance +
                '}';
    }

    public int getIdSdg() {
        return idSdg;
    }

    public void setIdSdg(int idSdg) {
        this.idSdg = idSdg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public RelevanceType getRelevance() {
        return relevance;
    }

    public void setRelevance(RelevanceType relevance) {
        this.relevance = relevance;
    }
}
