package by.overpass.gather.model.usecase.geo;

import androidx.lifecycle.LiveData;

import by.overpass.gather.model.repo.geocode.GeocodeRepo;

import javax.inject.Inject;

public class GeoUseCase {

    private final GeocodeRepo geocodeRepo;

    @Inject
    public GeoUseCase(GeocodeRepo geocodeRepo) {
        this.geocodeRepo = geocodeRepo;
    }

    public LiveData<String> geoDecode(double latitude, double longitude) {
        return geocodeRepo.reverseGeocode(latitude, longitude);
    }
}
