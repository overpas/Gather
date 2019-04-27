package com.github.overpass.gather.model.usecase.geo;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.repo.geocode.GeocodeRepo;

public class GeoUseCase {

    private final GeocodeRepo geocodeRepo;

    public GeoUseCase(GeocodeRepo geocodeRepo) {
        this.geocodeRepo = geocodeRepo;
    }

    public LiveData<String> geoDecode(double latitude, double longitude) {
        return geocodeRepo.reverseGeocode(latitude, longitude);
    }
}
