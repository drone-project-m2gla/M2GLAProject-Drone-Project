package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.adapter.CarouselAdapter;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.GeoImage;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import it.moondroid.coverflow.components.ui.containers.EndlessLoopAdapterContainer;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by baptiste on 26/05/15.
 */
public class CarouselActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        final Activity _this = this;
        final CarouselAdapter carouselAdapter = new CarouselAdapter(this);

        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra(GeneralConstants.REF_ACT_LAT_IMG, 0.0);
        Double longitude = intent.getDoubleExtra(GeneralConstants.REF_ACT_LON_IMG, 0.0);

        Map<String, String> param = new HashMap<>();
        param.put("latitude", latitude.toString());
        param.put("longitude", longitude.toString());

        RestServiceImpl.getInstance().get(RestAPI.GET_IMAGES, param, GeoImage[].class, new Command() {
                @Override
                public void execute(Object response) {
                    EndlessLoopAdapterContainer carousel =
                            (EndlessLoopAdapterContainer) findViewById(R.id.carousel);

                    carouselAdapter.addItems((GeoImage[]) response);

                    carousel.setAdapter(carouselAdapter);
                }
            }, new Command() {
                @Override
                public void execute(Object response) {
                    Toast.makeText(_this, R .string.no_image, Toast.LENGTH_LONG).show();
                }
            });
    }
}
