package by.overpass.gather.model.repo.geocode;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import by.overpass.gather.model.data.HttpClient;
import by.overpass.gather.model.data.entity.geo.Feature;
import by.overpass.gather.model.data.entity.geo.GeoDecode;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.overpass.gather.model.data.entity.geo.Feature;
import by.overpass.gather.model.data.entity.geo.GeoDecode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static by.overpass.gather.di.NamedKt.MAPBOX_TOKEN;

public class GeocodeRepo {

    private static final String TAG = "GeocodeRepo";
    private static final String GEO_BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places";
    private static final String DEFAULT_DECODED = "Fail";

    private final Gson gson;
    private final HttpClient httpClient;
    private final String mapboxToken;

    @Inject
    public GeocodeRepo(HttpClient httpClient, Gson gson, @Named(MAPBOX_TOKEN) String mapboxToken) {
        this.httpClient = httpClient;
        this.gson = gson;
        this.mapboxToken = mapboxToken;
    }

    public LiveData<String> reverseGeocode(double longitude, double latitude) {
        MutableLiveData<String> resultData = new MutableLiveData<>();
        String url = GEO_BASE_URL + "/" + longitude + "," + latitude + ".json";
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                resultData.postValue(DEFAULT_DECODED);
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String body = responseBody.string();
                    GeoDecode geoDecode = gson.fromJson(body, GeoDecode.class);
                    resultData.postValue(parseAddress(geoDecode));
                } else {
                    resultData.postValue(DEFAULT_DECODED);
                }
            }
        };
        httpClient.get(url, callback, null, "access_token",
                mapboxToken);
        return resultData;
    }

    private String parseAddress(GeoDecode geoDecode) {
        List<Feature> features = geoDecode.getFeatures();
        if (features != null && !features.isEmpty()) {
            return features.get(0).getPlaceName();
        }
        return DEFAULT_DECODED;
    }
}
