package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;


public class SVGActivity extends Activity {
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svg_layout);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        imageView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try {
            SVG svg = SVG.getFromResource(this, R.raw.vehicule_incendie_seul_prevu);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            imageView1.setImageDrawable(drawable);
        }
        catch(SVGParseException e) {}
    }
}
