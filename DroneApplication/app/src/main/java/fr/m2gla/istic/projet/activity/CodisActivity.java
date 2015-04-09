package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.R;
import fr.m2gla.istic.projet.context.UserQualification;

/**
 * Created by david on 09/02/15.
 */

public class CodisActivity extends Activity {

    private     String                              voie;
    private     String                              codePostal;
    private     String                              ville;
    private     String                              sinistre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codis);

        initializeElement();
    }


    /**
     * Methode d'initialisation des elements
     * @param -
     */
    private void initializeElement() {
        EditText    textAddress = (EditText) findViewById(R.id.addressGet);
        EditText    textCP = (EditText) findViewById(R.id.getCodePostal);
        EditText    textVille = (EditText) findViewById(R.id.getVille);
        RadioGroup  sinistreRadioG = (RadioGroup) findViewById(R.id.sinistreRadioGroup);


        // Reinitialisation des éléments de saisie
        textAddress.setText("");
        textCP.setText("");
        textVille.setText("");
        sinistreRadioG.check(R.id.SAPRadioButton);

        // Initialisation des attributs
        this.voie = "";
        this.codePostal = "";
        this.ville = "";
        this.sinistre = "";

    }


    /**
     * Methode de gestion de la fin d'activity
     * @param view : vue courante
     */
    public void finAjoutIntervention(View view) {

        // Arret de l'activity ici
        finish();
    }

    /**
     * Methode de gestion de l'ajout d'une intervention
     * @param view : vue courante
     */
    public void ajoutIntervention (View view) {

        int         radioBSelect;
        EditText    textAddress = (EditText) findViewById(R.id.addressGet);
        EditText    textCP = (EditText) findViewById(R.id.getCodePostal);
        EditText    textVille = (EditText) findViewById(R.id.getVille);
        RadioGroup  sinistreRadioG = (RadioGroup) findViewById(R.id.sinistreRadioGroup);
        RadioButton selectedRadioB;


        // Recuperer le nom de la rue
        if (textAddress.getText().length() != 0) {
            this.voie = "" + textAddress.getText();
            Toast.makeText(getApplicationContext(), this.voie, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Manque la rue", Toast.LENGTH_SHORT).show();
            Log.i("ajoutIntervention", "Pas de rue");
            this.initializeElement();
            return;
        }

        // Recuperer le code postal
        if (textCP.getText().length() != 0) {
            this.codePostal = "" + textCP.getText();
            Toast.makeText(getApplicationContext(), "" + this.codePostal, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Manque le code postal", Toast.LENGTH_SHORT).show();
            Log.i("ajoutIntervention", "Pas de code postal");
            this.initializeElement();
            return;
        }

        // Recuperer le nom de la ville
        if (textVille.getText().length() != 0) {
            this.ville = "" + textVille.getText();
            Toast.makeText(getApplicationContext(), this.ville, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Manque la ville", Toast.LENGTH_SHORT).show();
            Log.i("ajoutIntervention", "Pas de ville");
            this.initializeElement();
            return;
        }

        // Recuperer le sinistre selectionne
        radioBSelect = sinistreRadioG.getCheckedRadioButtonId();
        selectedRadioB = (RadioButton) findViewById(radioBSelect);
        if (selectedRadioB.getText().length() != 0) {
            this.sinistre = "" + selectedRadioB.getText();
            Toast.makeText(getApplicationContext(), this.sinistre, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Manque le code sinistre", Toast.LENGTH_SHORT).show();
            Log.i("ajoutIntervention", "Pas de code sinistre");
            this.initializeElement();
            return;
        }

    }


}