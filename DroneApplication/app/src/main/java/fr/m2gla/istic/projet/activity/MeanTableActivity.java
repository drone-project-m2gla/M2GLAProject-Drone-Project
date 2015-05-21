
package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.ItemsAdapter;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.fragments.InterventionDetailFragment;
import fr.m2gla.istic.projet.fragments.InterventionListFragment;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

/**
 * Created by david on 21/05/15.
 */

public class MeanTableActivity extends Activity {
    private static final    String TAG = "MeanTableActivity";
    private String          idIntervention = null;
    private Intervention    intervention = null;


    /**
     * Methode Principale
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mean_table);

        Intent  intent;
        Button  addButton = (Button) findViewById(R.id.addInterButton);


        intent = getIntent();

        if (intent != null) {
            this.idIntervention = intent.getStringExtra(GeneralConstants.REF_ACT_IDINTER);
            if (this.idIntervention != null) {
                // Toast.makeText(getApplicationContext(), idIntervention, Toast.LENGTH_SHORT).show();

                if (this.idIntervention.compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), " - Aucun - ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), " - Null - ", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "PAS D'INTENT !", Toast.LENGTH_SHORT).show();
        }

        // Pas de bouton d'ajout ou d'action. Present juste pour coherence entre écran
        addButton.setEnabled(false);
        addButton.setVisibility(View.INVISIBLE);

        MeanTableViewRefresh();

    }


    /**
     * Set intervention id from another fragment or activity
     *
     * @param idIntervention
     */
    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;

        MeanTableViewRefresh();
    }

    /**
     * Command error
     *
     * @return
     */
    private Command getCallbackError() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Toast.makeText(getApplicationContext(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG);
            }
        };
    }

    /**
     * Command success
     *
     * @return
     */
    private Command getCallbackSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                TableLayout titleTable = (TableLayout) findViewById(R.id.titleMeanTable);
                TableLayout table = (TableLayout) findViewById(R.id.meanTable); // on prend le tableau défini dans le layout
                TableRow    row; // création d'un élément : ligne
                TextView    tv1,tv2; // création des cellules

                intervention = (Intervention) response;
                // Toast.makeText(getApplicationContext(), "  test intervention return " + intervention.getId(), Toast.LENGTH_LONG).show();
                List<Mean> meanList = intervention.getMeansList();
                List<Mean> xtraMeanList = intervention.getMeansXtra();

                // Effacement des tables
                titleTable.removeAllViews();
                table.removeAllViews();

                // Contruction de la ligne de titre
                row = new TableRow(MeanTableActivity.this); // création d'une nouvelle ligne
                tv1 = new TextView(MeanTableActivity.this); // création cellule
                tv1.setText("VEHICULE"); // ajout du texte
                tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
                tv1.setTextColor(Color.YELLOW);
                tv1.setTextSize(20);
                // adaptation de la largeur de colonne à l'écran :
                tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                row.addView(tv1);
                tv1 = new TextView(MeanTableActivity.this); // création cellule
                tv1.setText("DONNEES"); // ajout du texte
                tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
                tv1.setTextColor(Color.YELLOW);
                tv1.setTextSize(20);
                // adaptation de la largeur de colonne à l'écran :
                tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                row.addView(tv1);
                row.setPadding(1, 3, 1, 8);
                titleTable.addView(row);

                // pour chaque ligne
                for (Mean m:meanList) {

                    row = new TableRow(MeanTableActivity.this); // création d'une nouvelle ligne

                    tv1 = new TextView(MeanTableActivity.this); // création cellule
                    tv1.setText(m.getVehicle().toString()); // ajout du texte
                    tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
                    // adaptation de la largeur de colonne à l'écran :
                    tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

                    // idem 2ème cellule
                    tv2 = new TextView(MeanTableActivity.this);
                    tv2.setText(m.getCoordinates().toString());
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

                    // ajout des cellules à la ligne
                    row.addView(tv1);
                    row.addView(tv2);
                    row.setPadding(1, 1, 1, 1);

                    // ajout de la ligne au tableau
                    table.addView(row);
                }

                // pour chaque ligne
                for (Mean m:xtraMeanList) {
                    row = new TableRow(MeanTableActivity.this); // création d'une nouvelle ligne

                    tv1 = new TextView(MeanTableActivity.this); // création cellule
                    tv1.setText(m.getVehicle().toString()); // ajout du texte
                    tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
                    // adaptation de la largeur de colonne à l'écran :
                    tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

                    // idem 2ème cellule
                    tv2 = new TextView(MeanTableActivity.this);
                    tv2.setText(m.getCoordinates().toString());
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

                    // ajout des cellules à la ligne
                    row.addView(tv1);
                    row.addView(tv2);

                    // ajout de la ligne au tableau
                    table.addView(row);
                }

            }
        };
    }



    /**
     * Methode de creation du menu de l'entity
     *
     * @param menu : Objet de definition du menu principal
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inter_list_activity, menu);
        return true;
    }

    /**
     * Methode de gestion de l'usage du menu de l'entity
     *
     * @param item : Objet de sélection dans le menu principal
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent  intent;
        int     id = item.getItemId();

        // Recuperer l'option du menu selectionnee
        switch (id) {
            // Reglage prevu pour plus tard
            //case R.id.action_settings :
            //    return true;
            case R.id.action_deconnection :
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * Methode de rafraichissement de l'activity
     *
     * @param -
     */
    public void MeanTableViewRefresh() {

        if (!this.idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("id", this.idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, getCallbackSuccess(), getCallbackError());
        }

    }


    /**
     * Methode de rafraichissement de l'activity appelee par button
     *
     * @param view : vue courante
     */
    public void MeanTableViewRefresh(View view) {
        MeanTableViewRefresh();
    }


    /**
     * Methode de fin de l'activity appelee par button
     *
     * @param view : vue courante
     */
    public void endMeanTableView(View view) {
        // arret de l'activity ici
        finish();
    }


}