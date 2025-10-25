package com.example.weather.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_type_id")
    private ResidentType residentType;

    @JsonIgnore
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Weather> weatherRecords = new ArrayList<>();

    // Добавляем Many-to-Many с JoinTable
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "region_weather_types",
            joinColumns = @JoinColumn(name = "region_id"),
            inverseJoinColumns = @JoinColumn(name = "weather_type_id")
    )
    private List<Weather> weatherTypes = new ArrayList<>();

    // Constructors
    public Region() {}

    public Region(String name, Double area, ResidentType residentType) {
        this.name = name;
        this.area = area;
        this.residentType = residentType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public ResidentType getResidentType() { return residentType; }
    public void setResidentType(ResidentType residentType) { this.residentType = residentType; }

    public List<Weather> getWeatherRecords() { return weatherRecords; }
    public void setWeatherRecords(List<Weather> weatherRecords) { this.weatherRecords = weatherRecords; }

    public List<Weather> getWeatherTypes() { return weatherTypes; }
    public void setWeatherTypes(List<Weather> weatherTypes) { this.weatherTypes = weatherTypes; }




    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area=" + area +
                ", residentType=" + (residentType != null ? residentType.getName() : "null") +
                '}';
    }
}