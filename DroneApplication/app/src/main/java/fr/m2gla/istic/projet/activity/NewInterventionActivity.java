package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.DisasterCode;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.User;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.PushServiceImpl;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

/**
 * Created by david on 09/02/15.
 */

public class NewInterventionActivity extends Activity {

    private     String                              voie;
    private     String                              codePostalStr;
    private     String                              ville;
    private     String                              sinistreStr;
    private     DisasterCode                        disasterCode;
    private     Integer                             postCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intervention);

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
        this.codePostalStr = "";
        this.ville = "";
        this.sinistreStr = "";
        this.disasterCode = DisasterCode.AVP;
        this.postCode = 0;
    }


    /**
     * Methode de gestion de la fin d'activity
     * @param view : vue courante
     */
    public void endAddIntervention(View view) {

        /* Pour retourner un message a l'activite principale */
        Intent intent = new Intent();

        // envoi du resultat
        setResult(RESULT_OK, intent);

        // Arret de l'activity ici
        finish();
    }

    /**
     * Methode de gestion de l'ajout d'une intervention
     * @param view : vue courante
     */
    public void addIntervention (View view) {

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
            this.codePostalStr = "" + textCP.getText();
            Toast.makeText(getApplicationContext(), "" + this.codePostalStr, Toast.LENGTH_SHORT).show();
            this.postCode = Integer.parseInt(this.codePostalStr);
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
            this.sinistreStr = "" + selectedRadioB.getText();
            if (this.sinistreStr.compareTo(DisasterCode.AVP.toString()) == 0) {
                this.disasterCode = DisasterCode.AVP;
            }
            else if (this.sinistreStr.compareTo(DisasterCode.FHA.toString()) == 0) {
                this.disasterCode = DisasterCode.FHA;
            }
            else if (this.sinistreStr.compareTo(DisasterCode.SAP.toString()) == 0) {
                this.disasterCode = DisasterCode.SAP;
            }
            else {
                this.disasterCode = DisasterCode.AVP;
            }

            Log.i("ajoutIntervention", "Sinistre : " + this.sinistreStr + "/" + this.disasterCode);
        }
        else {
            Toast.makeText(getApplicationContext(), "Manque le code sinistre", Toast.LENGTH_SHORT).show();
            Log.i("ajoutIntervention", "Pas de code sinistre");
            this.initializeElement();
            return;
        }

        // Envoyer les donnees au serveur
        sendNewInterventionAsync();



    }


    /**
     * Methode de gestion de l'envoi des elements de login au serveur
     *
     * @param -
     */
    private boolean sendNewInterventionAsync() {

        RestService     newInter = RestServiceImpl.getInstance();
        Intervention    intervention;


        intervention = new Intervention();
        intervention.setDisasterCode(this.disasterCode);
        intervention.setAddress(this.voie);
        intervention.setCity(this.ville);
        intervention.setPostcode(this.codePostalStr);
// Gson toto = new Gson();
// System.out.println(toto.toJson(intervention));

        newInter.post(RestAPI.POST_INTERVENTION, null, intervention, Intervention.class, new Command() {
            /**
             * Success connection
             * @param response Response object type User
             */
            @Override
            public void execute(Object response) {
                // register to GCM
                // PushServiceImpl.getInstance().register(disasterCode);

                // Demander la prise en compte de la validation de l'identification
                Toast.makeText(getApplicationContext(), "Connexion", Toast.LENGTH_SHORT).show();
                postOkNewInterventionAction();
            }
        }, new Command() {
            /**
             * Error connection
             * @param response Response error type HttpClientErrorException
             */
            @Override
            public void execute(Object response) {
                // Echec d'identification. Retours à l'activity principale
                Toast.makeText(getApplicationContext(), "Echec de connexion", Toast.LENGTH_SHORT).show();
                postNewInterventionAction();
                return;
            }
        });

        return true;
    }


    /**
     * Methode d'action post login reussi
     *
     * @param -
     */
    private void postOkNewInterventionAction() {
        postNewInterventionAction();
    }

    /**
     * Methode d'action post login
     *
     * @param -
     */
    private void postNewInterventionAction() {
        this.initializeElement();
    }

}