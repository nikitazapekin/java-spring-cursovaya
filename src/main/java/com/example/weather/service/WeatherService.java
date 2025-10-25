package com.example.weather.service;

import com.example.weather.entity.Weather;
import com.example.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public List<Weather> findAll() {
        return (List<Weather>) weatherRepository.findAll();
    }

    public Optional<Weather> findById(Long id) {
        return weatherRepository.findById(id);
    }

    public Weather save(Weather weather) {
        return weatherRepository.save(weather);
    }

    public void deleteById(Long id) {
        weatherRepository.deleteById(id);
    }

    public List<Weather> findByRegionName(String regionName) {
        return weatherRepository.findByRegionName(regionName);
    }

    public List<Weather> findByRegionId(Long regionId) {
        return weatherRepository.findByRegionId(regionId);
    }

    public List<Weather> findSnowDaysWithTemperatureBelow(String regionName, Double temperature) {
        return weatherRepository.findSnowDaysWithTemperatureBelow(regionName, temperature);
    }

    public List<Weather> findByResidentLanguageAndLastWeek(String language) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return weatherRepository.findByResidentLanguageAndDateRange(language, startDate, endDate);
    }

    public Double findAverageTemperatureForLargeRegions(Double minArea) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        Double result = weatherRepository.findAverageTemperatureForLargeRegions(minArea, startDate, endDate);
        return result != null ? Math.round(result * 100.0) / 100.0 : 0.0;
    }

    public List<Weather> findLastWeekWeather() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return weatherRepository.findByDateRange(startDate, endDate);
    }

    public List<Weather> findByPrecipitation(String precipitation) {
        return weatherRepository.findByPrecipitation(precipitation);
    }

    public List<Weather> findByTemperatureLessThan(Double temperature) {
        return weatherRepository.findByTemperatureLessThan(temperature);
    }

    public List<Weather> findByTemperatureGreaterThan(Double temperature) {
        return weatherRepository.findByTemperatureGreaterThan(temperature);
    }

    public List<Weather> findByDate(LocalDate date) {
        return weatherRepository.findByDate(date);
    }

    public List<Weather> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return weatherRepository.findByDateBetween(startDate, endDate);
    }

    public List<Weather> findByResidentTypeName(String residentTypeName) {
        return weatherRepository.findByResidentTypeName(residentTypeName);
    }

    public List<Weather> findByRegionAreaGreaterThan(Double area) {
        return weatherRepository.findByRegionAreaGreaterThan(area);
    }

    public List<Weather> findAllOrderByDateDesc() {
        return weatherRepository.findAllOrderByDateDesc();
    }

    public List<String> findDistinctPrecipitationTypes() {
        return weatherRepository.findDistinctPrecipitationTypes();
    }

    public List<Object[]> findAverageTemperatureByRegion(LocalDate startDate, LocalDate endDate) {
        return weatherRepository.findAverageTemperatureByRegion(startDate, endDate);
    }
}