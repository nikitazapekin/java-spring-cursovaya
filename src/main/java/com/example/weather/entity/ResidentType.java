package com.example.weather.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resident_types")
public class ResidentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "communication_language", nullable = false)
    private String communicationLanguage;

    @JsonIgnore
    @OneToMany(mappedBy = "residentType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Region> regions = new ArrayList<>();

    // Constructors
    public ResidentType() {}

    public ResidentType(String name, String communicationLanguage) {
        this.name = name;
        this.communicationLanguage = communicationLanguage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCommunicationLanguage() { return communicationLanguage; }
    public void setCommunicationLanguage(String communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }

    public List<Region> getRegions() { return regions; }
    public void setRegions(List<Region> regions) { this.regions = regions; }

    @Override
    public String toString() {
        return "ResidentType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", communicationLanguage='" + communicationLanguage + '\'' +
                '}';
    }
}