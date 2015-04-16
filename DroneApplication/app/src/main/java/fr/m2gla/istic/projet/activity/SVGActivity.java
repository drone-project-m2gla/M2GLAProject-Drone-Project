package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.InputStream;

import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;


public class SVGActivity extends Activity {
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svg_layout);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        imageView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try {
            Symbol symbol = new Symbol(Symbol.SymbolType.colonne_incendie_active, "AAA", "BBB", "ffff00");
            symbol.setValidated(true);
            InputStream is = getApplicationContext().getResources().openRawResource(getResources().getIdentifier(symbol.getSymbolType().name(), "raw", getPackageName()));

            SVG svg = SVG.getFromInputStream(SVGAdapter.modifySVG(is, symbol));
            //SVG svg = SVG.getFromInputStream(is);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            imageView1.setImageDrawable(drawable);
        }
        catch(SVGParseException e) {}
    }
}
