package com.prod.main.baskettime.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
 
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Integer id;  // 플레이어 ID
    private String firstName;
    private String lastName;
    private Integer teamId;  // 팀 ID
    private String weight;  
    private String height;
    private String position;
    private String jerseyNumber;
    private Integer draftYear;
    private Integer draftRound;
    private Integer draftNumber;
    private String country;
    private String college;
    private String type;

    @Transient // DB에 저장되지 않음
    private String teamName;

    // getter, setter 추가
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCollege() {
        return college;
    }
    public void setCollege(String college) {
        this.college = college;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }
    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
    public Integer getDraftYear() {
        return draftYear;
    }
    public void setDraftYear(Integer draftYear) {
        this.draftYear = draftYear;
    }
    public Integer getDraftRound() {
        return draftRound;
    }
    public void setDraftRound(Integer draftRound) {
        this.draftRound = draftRound;
    }
    public Integer getDraftNumber() {
        return draftNumber;
    }
    public void setDraftNumber(Integer draftNumber) {
        this.draftNumber = draftNumber;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
