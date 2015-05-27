package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import fr.m2gla.istic.projet.adapter.CarouselAdapter;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.GeoImage;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
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


        RestServiceImpl.getInstance().get(RestAPI.GET_IMAGES, null, GeoImage[].class, new Command() {
                @Override
                public void execute(Object response) {
                    FeatureCoverFlow carousel = (FeatureCoverFlow) findViewById(R.id.carousel);

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
