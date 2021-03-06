package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.model.User;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.PushServiceImpl;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

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
        getActionBar().setTitle(R.string.presentation_text);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Methode de creation du menu de l'application
     *
     * @param menu : Objet de definition du menu principal
     * @return true si reussite de l'operation, false sinon
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
     * @return true si reussite de l'operation, false sinon
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Recuperer l'option du menu selectionnee
        switch (id) {
            // Reglage prevu pour plus tard
            //case R.id.action_settings :
            //    return true;
            case R.id.action_deconnection:
                this.initializeElement();
                return true;
            case R.id.action_quit:
                Log.i(TAG, "Fin Application");

                // Arret de l'activity ici
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Methode de gestion de l'usage des touches
     *
     * @param keyCode : Code de la touche
     * @param event   : Evènement
     * @return true si reussite de l'operation, false sinon
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Pour TEST
            Log.i(TAG, "Fin Application");
        }
        return super.onKeyDown(keyCode, event);
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

        findViewById(R.id.mainLayout).requestFocus();
    }

    /**
     * Methode de gestion de la fin de l'application appelee par bouton
     *
     * @param view : vue courante
     */
    public void finMain(View view) {
        Log.i(TAG, "Fin Application");

        // Arret de l'activity ici
        finish();
    }

    /**
     * Methode de gestion de la validation du login appelee par bouton
     *
     * @param view : vue courante
     */
    public void activerValider(View view) {

        int radioBSelect;
        EditText textLogin = (EditText) findViewById(R.id.loginGet);
        EditText textPassword = (EditText) findViewById(R.id.passwordGet);
        RadioGroup roleRadioG = (RadioGroup) findViewById(R.id.roleRadioGroup);

        // Recuperer le nom de login
        if (textLogin.getText().length() != 0) {
            this.loginName = "" + textLogin.getText();
        } else {
            Toast.makeText(getApplicationContext(), "Manque le login", Toast.LENGTH_SHORT).show();
            Log.i("activerValider", "Pas de Login");
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
    }


    /**
     * Methode de gestion de l'envoi des elements de login au serveur
     *
     * @param -
     * @return true si reussite de l'operation, false sinon
     */
    private boolean sendLoginAsync() {

        RestService loginSnd = RestServiceImpl.getInstance();

        User user = new User();
        user.setUsername(this.loginName);
        user.setPassword(this.loginPassword);

        loginSnd.post(RestAPI.POST_PUSH_LOGIN, null, user, User.class, new Command() {
            /**
             * Success connection
             * @param response Response object type User
             */
            @Override
            public void execute(Object response) {
                // register to GCM
                PushServiceImpl.getInstance().register(userQualification);

                // Demander la prise en compte de la validation de l'identification
                // Toast.makeText(getApplicationContext(), "Connexion", Toast.LENGTH_SHORT).show();
                postOkLoginAction();
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
                postLoginAction();
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
    private void postOkLoginAction() {
        Intent intent;

        // Creation d'un intent pour appeler une autre activité (SecondaryActivity)
        intent = new Intent(getApplicationContext(), InterventionListActivity.class);

        // Ajout de données supplémentaires dans l'intent
        intent.putExtra(GeneralConstants.REF_ACT_ROLE, "" + this.userQualification);

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

}
