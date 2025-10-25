package com.example.weather.repository;

import com.example.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {

    List<Weather> findByRegionId(Long regionId);
    List<Weather> findByRegionName(String regionName);

    @Query("SELECT w FROM Weather w WHERE w.region.name = :regionName AND w.precipitation = 'Snow' AND w.temperature < :temperature")
    List<Weather> findSnowDaysWithTemperatureBelow(@Param("regionName") String regionName,
                                                   @Param("temperature") Double temperature);

    @Query("SELECT w FROM Weather w WHERE w.region.residentType.communicationLanguage = :language AND w.date BETWEEN :startDate AND :endDate")
    List<Weather> findByResidentLanguageAndDateRange(@Param("language") String language,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(w.temperature) FROM Weather w WHERE w.region.area > :minArea AND w.date BETWEEN :startDate AND :endDate")
    Double findAverageTemperatureForLargeRegions(@Param("minArea") Double minArea,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT w FROM Weather w WHERE w.date BETWEEN :startDate AND :endDate")
    List<Weather> findByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);


    List<Weather> findByPrecipitation(String precipitation);
    List<Weather> findByTemperatureLessThan(Double temperature);
    List<Weather> findByTemperatureGreaterThan(Double temperature);
    List<Weather> findByDate(LocalDate date);
    List<Weather> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT w FROM Weather w WHERE w.region.residentType.name = :residentTypeName")
    List<Weather> findByResidentTypeName(@Param("residentTypeName") String residentTypeName);

    @Query("SELECT w FROM Weather w WHERE w.region.area > :area")
    List<Weather> findByRegionAreaGreaterThan(@Param("area") Double area);

    @Query("SELECT w FROM Weather w ORDER BY w.date DESC")
    List<Weather> findAllOrderByDateDesc();

    @Query("SELECT DISTINCT w.precipitation FROM Weather w")
    List<String> findDistinctPrecipitationTypes();

    @Query("SELECT w.region.name, AVG(w.temperature) FROM Weather w WHERE w.date BETWEEN :startDate AND :endDate GROUP BY w.region.name")
    List<Object[]> findAverageTemperatureByRegion(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
}