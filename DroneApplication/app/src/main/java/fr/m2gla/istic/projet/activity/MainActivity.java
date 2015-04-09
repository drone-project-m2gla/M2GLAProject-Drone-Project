package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.*;

public class MainActivity extends Activity {

    private String loginName;
    private String loginPassword;
    private UserQualification userQualification = UserQualification.SIMPLEUSER;


    /**
     * Methode Principale
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Methode de creation du menu de l'application
     *
     * @param menu : Objet de definition du menu principal
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Methode de gestion de l'usage du menu principal
     *
     * @param item : Objet de sélection dans le menu principal
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Methode d'initialisation des elements
     *
     * @param -
     */
    private void initializeElement() {
        EditText textLogin = (EditText) findViewById(R.id.loginGet);
        EditText textPassword = (EditText) findViewById(R.id.passwordGet);
        RadioGroup roleRadioG = (RadioGroup) findViewById(R.id.roleRadioGroup);

        // Reinitialisation des éléments de saisie
        textLogin.setText("");
        textPassword.setText("");
        roleRadioG.check(R.id.userRadioButton);

        // Initialisation des attributs
        this.loginName = "";
        this.loginPassword = "";
        this.userQualification = UserQualification.SIMPLEUSER;

    }

    /**
     * Methode de gestion de la fin de l'application
     *
     * @param view : vue courante
     */
    public void finMain(View view) {

        // Arret de l'activity ici
        finish();
    }

    /**
     * Methode de gestion de la validation du login
     *
     * @param view : vue courante
     */
    public void actiValider(View view) {

        int radioBSelect;
        EditText textLogin = (EditText) findViewById(R.id.loginGet);
        EditText textPassword = (EditText) findViewById(R.id.passwordGet);
        RadioGroup roleRadioG = (RadioGroup) findViewById(R.id.roleRadioGroup);
        RadioButton codisRadioB = (RadioButton) findViewById(R.id.codisRadioButton);


        // Recuperer le nom de login
        if (textLogin.getText().length() != 0) {
            this.loginName = "" + textLogin.getText();
        } else {
            Toast.makeText(getApplicationContext(), "Manque le login", Toast.LENGTH_SHORT).show();
            Log.i("actiValider", "Pas de Login");
            this.initializeElement();
            return;
        }


        // Recuperer le mot de passe
        if (textPassword.getText().length() != 0) {
            this.loginPassword = "" + textPassword.getText();
        } else {
            this.loginPassword = "";
        }

        // Recuperer le role
        radioBSelect = roleRadioG.getCheckedRadioButtonId();

        if (radioBSelect == R.id.codisRadioButton) {
            this.userQualification = UserQualification.CODIS;
        } else if (radioBSelect == R.id.userRadioButton) {
            this.userQualification = UserQualification.SIMPLEUSER;
        }

        // Demander l'envoi des éléments de connexion au serveur
        sendLoginAsync();

        // Lancement d'une tache asynchrone pour envoyer les donnees de connexion au serveur
        // new SendLoginAsync().execute();

        PushServiceImpl.getInstance().setContext(getApplicationContext());
        PushServiceImpl.getInstance().register();
    }


    /**
     * Methode de gestion de l'envoi des elements de login au serveur
     *
     * @param -
     */
    private boolean sendLoginAsync() {

        RestService loginSnd = RestServiceImpl.getInstance();
        List<NameValuePair> loginList = new ArrayList<>();
        NameValuePair loginPair = new BasicNameValuePair("username", this.loginName);
        NameValuePair passwordPair = new BasicNameValuePair("password", this.loginPassword);


        loginList.add(loginPair);
        loginList.add(passwordPair);

        loginSnd.post(RestAPI.POST_PUSH_LOGIN, loginList, new LoginResult(), null);

        return true;
    }

    /**
     * Methode d'action post login reussi
     *
     * @param -
     */
    private void postOkLoginAction() {
        Intent intent;


        if (this.userQualification == UserQualification.CODIS) {

            // Creation d'un intent pour appeler une autre activité (SecondaryActivity)
            intent = new Intent(getApplicationContext(), CodisActivity.class);

            Toast.makeText(getApplicationContext(), "postLoginAction() : " + "CODIS", Toast.LENGTH_SHORT).show();

            // Lancement de l'activité, suivante
            startActivity(intent);

        } else {

            // Creation d'un intent pour appeler une autre activité (SecondaryActivity)
            intent = new Intent(getApplicationContext(), InterventionListActivity.class);

            Toast.makeText(getApplicationContext(), "postLoginAction() : " + "Sapeur", Toast.LENGTH_SHORT).show();

        }

        // Lancement de l'activité, suivante
        startActivity(intent);


        postLoginAction();

    }

    /**
     * Methode d'action post login
     *
     * @param -
     */
    private void postLoginAction() {
        this.initializeElement();
    }


    /**
     * Classe en patron command pour la prise en compte du resultat de la demande de connexion
     */
    private class LoginResult implements Command {
        /**
         * Methode d'action command sur resultat de connexion
         *
         * @param response : réponse de connexion
         */
        @Override
        public void execute(HttpResponse response) {
            Toast.makeText(getApplicationContext(), "Status de ligne : " + response.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT).show();
            Log.i("HttpResponse", "Status = " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() != GeneralConstants.HTTP_RESP_OK) {
                // Echec d'identification. Retours à l'activity principale
                Toast.makeText(getApplicationContext(), "Echec de connexion", Toast.LENGTH_SHORT).show();
                postLoginAction();
                return;
            }

            // Demander la prise en compte de la validation de l'identification
//            Toast.makeText(getApplicationContext(), "Connexion", Toast.LENGTH_SHORT).show();
            postOkLoginAction();

        }
    }

}
