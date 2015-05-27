
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
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import fr.m2gla.istic.projet.model.MeanState;
import fr.m2gla.istic.projet.model.Vehicle;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.Strategy;

/**
 * Created by david on 21/05/15.
 */

public class MeanTableActivity extends Activity {
    private static final    String TAG = "MeanTableActivity";
    private String          idIntervention = null;
    private Intervention    intervention = null;
    private boolean         withRefusedMeans = false;
    private boolean         withTimeColor = false;

    private String          titleMeanTab[] = {
                                GeneralConstants.MEAN_TABLE_1,
                                GeneralConstants.MEAN_TABLE_2,
                                GeneralConstants.MEAN_TABLE_3,
                                GeneralConstants.MEAN_TABLE_4,
                                GeneralConstants.MEAN_TABLE_5,
                                GeneralConstants.MEAN_TABLE_6
                            };


    /**
     * Methode Principale de l'activité gerant l'affichage de la table des moyens d'une intervention
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mean_table);

        Intent      intent;
        Button      addButton = (Button) findViewById(R.id.addInterButton);
        CheckBox    withRefusedMeansCheck = (CheckBox) findViewById(R.id.refusedMeanCheckDisp);
        CheckBox    timeColorMeanCheck = (CheckBox) findViewById(R.id.timeColorCheckDisp);


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

        withRefusedMeansCheck.setTextColor(Color.CYAN);
        timeColorMeanCheck.setTextColor(Color.MAGENTA);

        MeanTableViewRefresh();

    }


    /**
     * Methode permettant de specifier l'intervention en cours
     *
     * @param idIntervention
     */
    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;

        MeanTableViewRefresh();
    }

    /**
     * Methode de gestion des retours en erreur d'un appel à un service REST
     *
     * @return la classe "command" de gestion d'erreur attentue par le service REST
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
     * Methode de gestion des retours en succes d'un appel à un service REST
     *
     * @return la classe "command" de gestion de succes attentue par le service REST
     */
    private Command getCallbackSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                TableLayout     titleTable = (TableLayout) findViewById(R.id.titleMeanTable);
                TableLayout     table = (TableLayout) findViewById(R.id.meanTable);
                TableRow        row;
                TextView        tv;
                Long            dateLong;
                Date            date;
                DateFormat      mediumDateFormat;
                List<Mean>      meanList;
                String          dateStr;
                List<String>    dateList;
                boolean         refusedMean;


                intervention = (Intervention) response;
                meanList = intervention.getMeansList();

                // Initialisations
                mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale("FR", "fr"));


                // Effacement des tables
                titleTable.removeAllViews();
                table.removeAllViews();

                // Ajout du nom de l'intervention
                tv = (TextView) findViewById(R.id.meanTableIdDisp);
                tv.setText(intervention.getLabel());
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(Color.GREEN);

                // Mise en place de la ligne de titres
                row = new TableRow(MeanTableActivity.this);
                for (int i = 0; i < titleMeanTab.length; i++) {
                    tv = new TextView(MeanTableActivity.this);
                    tv.setText(titleMeanTab[i]);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.YELLOW);
                    tv.setTextSize(20);
                    tv.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                    row.addView(tv);
                }
                row.setPadding(1, 3, 1, 8);
                titleTable.addView(row);

                // pour chaque ligne
                for (Mean m:meanList) {
                    String  str;
                    refusedMean = false;
                    int color = Color.WHITE, bgcolor = Color.TRANSPARENT, idx = 6, i;

                    MeanState meanState = m.getMeanState();
                    if (meanState.compareTo(MeanState.REFUSED) == 0) {
                        color = Color.RED;
                        idx = 4;
                    } else if (meanState.compareTo(MeanState.REQUESTED) == 0) {
                        color = Color.BLUE;
                        idx = 0;
                    } else if (meanState.compareTo(MeanState.ACTIVATED) == 0) {
                        color = Color.CYAN;
                        idx = 1;
                    } else if (meanState.compareTo(MeanState.ARRIVED) == 0) {
                        color = Color.GREEN;
                        idx = 2;
                    } else if (meanState.compareTo(MeanState.ENGAGED) == 0) {
                        color = Color.YELLOW;
                        idx = 3;
                    } else if (meanState.compareTo(MeanState.RELEASED) == 0) {
                        color = Color.rgb(0xff, 0x8C, 0x00);
                        idx = 4;
                    }
                    if (withTimeColor != true) {
                        bgcolor = Color.rgb(0xff, 0xff, 0xff);
                        color = getMeanColor(m.getVehicle());
                    }

                    // Verifier si le moyen est a afficher
                    str = m.getDateRefused();
                    if ((str != null) && (str.compareTo("") != 0)) {
                        if (withRefusedMeans == false) {
                            continue;
                        }
                        refusedMean = true;
                    }


                    // création d'une nouvelle ligne
                    row = new TableRow(MeanTableActivity.this);

                    // création cellule
                    tv = new TextView(MeanTableActivity.this);

                    // ajout du texte
                    if ("".compareTo(m.getName()) == 0) {
                        tv.setText(m.getVehicle().toString());
                    }
                    else {
                        tv.setText(m.getVehicle().toString() + " - " + m.getName());
                    }

                    // Specification de la couleur
                    tv.setTextColor(color);

                    // centrage dans la cellule
                    tv.setGravity(Gravity.CENTER);

                    // adaptation de la largeur de colonne à l'écran :
                    tv.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

                    // ajout de la cellule à la ligne
                    row.addView(tv);

                    // Cellules des dates
                    dateList = new ArrayList<String>();
                    if (refusedMean == true) {
                        dateList.add(m.getDateRequested());
                        dateList.add(GeneralConstants.MEAN_REFUSED);
                        dateList.add("");
                        dateList.add("");
                        dateList.add(str);
                    }
                    else {
                        dateList.add(m.getDateRequested());
                        dateList.add(m.getDateActivated());
                        dateList.add(m.getDateArrived());
                        dateList.add(m.getDateEngaged());
                        dateList.add(m.getDateReleased());
                    }

                    i = 0;
                    for (String s:dateList) {
                        tv = new TextView(MeanTableActivity.this);
                        if (withTimeColor == true) {
                            if (i == idx) {
                                tv.setTextColor(color);
                            }
                            else {
                                tv.setTextColor(Color.LTGRAY);
                            }
                        }
                        else {
                            if (i == idx) {
                                tv.setBackgroundColor(bgcolor);
                            }
                            tv.setTextColor(getMeanColor(m.getVehicle()));
                        }
                        i++;

                        if ((s == null) || (s.compareTo("") == 0)) {
                            dateStr = " - ";
                        }
                        else if (s.compareTo(GeneralConstants.MEAN_REFUSED) == 0) {
                            dateStr = s;
                            if (withTimeColor == true) tv.setTextColor(Color.RED);
                        }
                        else {
                            dateLong = Long.valueOf(s);
                            date = new Date(dateLong);
                            dateStr = mediumDateFormat.format(date);
                        }
                        tv.setText(dateStr);

                        tv.setGravity(Gravity.CENTER);
                        tv.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                        row.addView(tv);
                    }

                    // Formatage de ligne
                    row.setPadding(1, 1, 1, 1);

                    // Ajout de la ligne au tableau
                    table.addView(row);
                }
            }
        };
    }


    /**
     * Methode de creation du menu de l'activité
     *
     * @param menu : Objet de definition du menu principal
     * @return true si reussite de l'operation, false sinon
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inter_list_activity, menu);
        return true;
    }


    /**
     * Methode de gestion de l'usage du menu de l'activité
     *
     * @param item : Objet de sélection dans le menu principal
     * @return true si reussite de l'operation, false sinon
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
     * Methode de rafraichissement de l'activité
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
     * Methode de rafraichissement de l'activité appelee par button
     *
     * @param view : vue courante
     */
    public void MeanTableViewRefresh(View view) {
        CheckBox    withRefusedMeansCheck = (CheckBox) findViewById(R.id.refusedMeanCheckDisp);
        CheckBox    timeColorMeanCheck = (CheckBox) findViewById(R.id.timeColorCheckDisp);

        if (withRefusedMeansCheck.isChecked()) {
            this.withRefusedMeans = true;
        }
        else {
            this.withRefusedMeans = false;
        }

        if (timeColorMeanCheck.isChecked()) {
            this.withTimeColor = true;
        }
        else {
            this.withTimeColor = false;
        }

        MeanTableViewRefresh();
    }


    /**
     * Methode de fin de l'activite appelee par button
     *
     * @param view : vue courante
     */
    public void endMeanTableView(View view) {
        // arret de l'activity ici
        finish();
    }


    /**
     * Retourne la couleur en fonction du type de vehicule
     * @param vehicleType : Type de véhicule
     * @return : Couleur sous forme d'entier
     */
    private int getMeanColor(Vehicle vehicleType) {
        switch (vehicleType) {
            case VSAV :
                return Color.GREEN;
            case VSR :
                return Color.RED;
            case VLCG :
                return Color.MAGENTA;
            case EPA :
            case FPT :
                return Color.RED;
            case CCGC :
                return Color.BLUE;
        }
        return (Color.LTGRAY);
    }

}