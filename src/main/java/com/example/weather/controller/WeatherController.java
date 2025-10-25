package com.example.weather.controller;


import com.example.weather.entity.Weather;
import com.example.weather.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<Weather>> getAllWeatherRecords() {
        try {
            List<Weather> weatherRecords = weatherService.findAll();
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weather> getWeatherById(@PathVariable Long id) {
        try {
            Optional<Weather> weather = weatherService.findById(id);
            return weather.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createWeatherRecord(@RequestBody Weather weather) {
        try {
            if (weather.getRegion() == null || weather.getRegion().getId() == null) {
                return ResponseEntity.badRequest().body("Region is required");
            }
            if (weather.getDate() == null) {
                return ResponseEntity.badRequest().body("Date is required");
            }
            if (weather.getTemperature() == null) {
                return ResponseEntity.badRequest().body("Temperature is required");
            }
            if (weather.getPrecipitation() == null || weather.getPrecipitation().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Precipitation is required");
            }

            Weather savedWeather = weatherService.save(weather);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWeather);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating weather record: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWeatherRecord(@PathVariable Long id,
                                                 @RequestBody Weather weatherDetails) {
        try {
            Optional<Weather> existingWeather = weatherService.findById(id);
            if (existingWeather.isPresent()) {
                Weather weather = existingWeather.get();
                weather.setRegion(weatherDetails.getRegion());
                weather.setDate(weatherDetails.getDate());
                weather.setTemperature(weatherDetails.getTemperature());
                weather.setPrecipitation(weatherDetails.getPrecipitation());

                Weather updatedWeather = weatherService.save(weather);
                return ResponseEntity.ok(updatedWeather);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating weather record: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeatherRecord(@PathVariable Long id) {
        try {
            if (weatherService.findById(id).isPresent()) {
                weatherService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/region/{regionName}")
    public ResponseEntity<List<Weather>> getWeatherByRegion(@PathVariable String regionName) {
        try {
            List<Weather> weatherRecords = weatherService.findByRegionName(regionName);
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/region-id/{regionId}")
    public ResponseEntity<List<Weather>> getWeatherByRegionId(@PathVariable Long regionId) {
        try {
            List<Weather> weatherRecords = weatherService.findByRegionId(regionId);
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/snow-cold/{regionName}/{temperature}")
    public ResponseEntity<List<Weather>> getSnowDaysWithColdTemperature(
            @PathVariable String regionName,
            @PathVariable Double temperature) {
        try {
            List<Weather> weatherRecords = weatherService.findSnowDaysWithTemperatureBelow(regionName, temperature);
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/last-week/language/{language}")
    public ResponseEntity<List<Weather>> getLastWeekWeatherByLanguage(@PathVariable String language) {
        try {
            List<Weather> weatherRecords = weatherService.findByResidentLanguageAndLastWeek(language);
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/average-temperature/large-regions/{minArea}")
    public ResponseEntity<Double> getAverageTemperatureForLargeRegions(@PathVariable Double minArea) {
        try {
            Double averageTemperature = weatherService.findAverageTemperatureForLargeRegions(minArea);
            return ResponseEntity.ok(averageTemperature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/last-week")
    public ResponseEntity<List<Weather>> getLastWeekWeather() {
        try {
            List<Weather> weatherRecords = weatherService.findLastWeekWeather();
            return ResponseEntity.ok(weatherRecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}