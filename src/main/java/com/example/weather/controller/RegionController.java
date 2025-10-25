package com.example.weather.controller;



import com.example.weather.entity.Region;
import com.example.weather.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin(origins = "*")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<Region>> getAllRegions() {
        try {
            List<Region> regions = regionService.findAll();
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        try {
            Optional<Region> region = regionService.findById(id);
            return region.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRegion(@RequestBody Region region) {
        try {
            if (region.getName() == null || region.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Region name is required");
            }
            if (regionService.existsByName(region.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Region with name '" + region.getName() + "' already exists");
            }

            Region savedRegion = regionService.save(region);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating region: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id,
                                          @RequestBody Region regionDetails) {
        try {
            Optional<Region> existingRegion = regionService.findById(id);
            if (existingRegion.isPresent()) {
                Region region = existingRegion.get();
                region.setName(regionDetails.getName());
                region.setArea(regionDetails.getArea());
                region.setResidentType(regionDetails.getResidentType());

                Region updatedRegion = regionService.save(region);
                return ResponseEntity.ok(updatedRegion);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating region: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        try {
            if (regionService.findById(id).isPresent()) {
                regionService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/area-greater-than/{area}")
    public ResponseEntity<List<Region>> getRegionsWithAreaGreaterThan(@PathVariable Double area) {
        try {
            List<Region> regions = regionService.findByAreaGreaterThan(area);
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<Region>> getRegionsByResidentLanguage(@PathVariable String language) {
        try {
            List<Region> regions = regionService.findByResidentLanguage(language);
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/resident-type/{residentTypeId}")
    public ResponseEntity<List<Region>> getRegionsByResidentType(@PathVariable Long residentTypeId) {
        try {
            List<Region> regions = regionService.findByResidentTypeId(residentTypeId);
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
