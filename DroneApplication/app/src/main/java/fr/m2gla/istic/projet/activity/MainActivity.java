package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.os.AsyncTask;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.*;

public class MainActivity extends Activity {

    private     String      loginName;
    private     String      loginPassword;
    private     String      loginServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int             id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void finMain(View view) {

        // arret de l'activity ici
        finish();
    }

    public void actiValider (View view) {

        int             radioBSelect;
//        EditText        textServer = (EditText) findViewById(R.id.urlGet);
        EditText        textLogin = (EditText) findViewById(R.id.loginGet);
        EditText        textPassword = (EditText) findViewById(R.id.passwordGet);
        RadioGroup      roleRadioG = (RadioGroup) findViewById(R.id.roleRadioGroup);
        RadioButton     codisRadioB = (RadioButton) findViewById(R.id.codisRadioButton);


        // Recuperer l'URL du serveur
//        if (textLogin.getText().length() != 0) {
//            this.loginServer = "" + textServer.getText();
//            Toast.makeText(getApplicationContext(), textServer.getText(), Toast.LENGTH_SHORT).show();
//        }
//       else {
            this.loginServer = "Aucun";
//        }

        // Recuperer le nom de login
        if (textLogin.getText().length() != 0) {
            this.loginName = "" + textLogin.getText();
            Toast.makeText(getApplicationContext(), textLogin.getText(), Toast.LENGTH_SHORT).show();
        }
        else {
            this.loginName = "Aucun";
        }

        // Recuperer le mot de passe
        if (textPassword.getText().length() != 0) {
            this.loginPassword = "" + textPassword.getText();
            Toast.makeText(getApplicationContext(), textPassword.getText(), Toast.LENGTH_SHORT).show();
        }
        else {
            this.loginPassword = "Aucun";
        }

        radioBSelect = roleRadioG.getCheckedRadioButtonId();

        if (radioBSelect == R.id.codisRadioButton) {
            Toast.makeText(getApplicationContext(), "CODIS", Toast.LENGTH_SHORT).show();
        }
        else if (radioBSelect == R.id.userRadioButton) {
            Toast.makeText(getApplicationContext(), "Superviseur", Toast.LENGTH_SHORT).show();
        }

        // Lancement d'une tache asynchrone pour envoyer les donnees de connexion au serveur
        new SendLoginAsync().execute();


        // lancement de l'activité, suivante
//        startActivity(intent);
    }


    private boolean sendLoginAsync() {

        RestService         loginSnd = RestServiceImpl.getInstance();
        List<NameValuePair> loginList = new ArrayList<>();
        NameValuePair       loginPair = new BasicNameValuePair("username", this.loginName);
        NameValuePair       passwordPair = new BasicNameValuePair("password", this.loginPassword);


        loginList.add(loginPair);
        loginList.add(passwordPair);

        loginSnd.post(RestAPI.POST_PUSH_LOGIN, loginList, new LoginResult(), null);

        return true;
    }


    private class LoginResult implements Command {
        @Override
        public void execute(HttpResponse response) {

        }
    }


    private class SendLoginAsync extends AsyncTask<Void, Integer, Boolean> {

        protected Boolean doInBackground(Void... params)
        {
            Log.i("doInBackground", "Send Data");
            // Toast.makeText(getApplicationContext(), "doInBackground", Toast.LENGTH_SHORT).show();

            return true;
        }


        protected void onPostExecute(Boolean result)
        {
            if (result) {
                Log.i("onPostExecute", "Send Data OK");
            }
            else {
                Log.i("onPostExecute", "Send Data KO");
            }

            Log.i("onPostExecute", "Send Data");
            Toast.makeText(getApplicationContext(), "onPostExecute", Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(), loginServer + " et " + loginName + " et " + loginPassword, Toast.LENGTH_SHORT).show();

        }
    }


}
