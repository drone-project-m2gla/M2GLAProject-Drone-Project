package fr.m2gla.istic.projet.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import fr.m2gla.istic.projet.service.impl.PushServiceImpl;

public class MainActivity extends Activity {

    private     String      loginName;
    private     String      loginPassword;


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
        EditText        textLogin = (EditText) findViewById(R.id.loginGet);
        EditText        textPassword = (EditText) findViewById(R.id.passwordGet);

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
        EditText        textLogin = (EditText) findViewById(R.id.loginGet);
        EditText        textPassword = (EditText) findViewById(R.id.passwordGet);
        RadioGroup      roleRadioG = (RadioGroup) findViewById(R.id.roleRadioGroup);
        RadioButton     codisRadioB = (RadioButton) findViewById(R.id.codisRadioButton);


        // Recuperer le nom de login
        if (textLogin.getText().length() != 0) {
            this.loginName = "" + textLogin.getText();
            Toast.makeText(getApplicationContext(), textLogin.getText(), Toast.LENGTH_SHORT).show();
        }
        else {
            this.loginName = "Aucun";
        }

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

        PushServiceImpl.getInstance().setContext(getApplicationContext());
        PushServiceImpl.getInstance().register();
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

            Toast.makeText(getApplicationContext(), loginName + " et " + loginPassword, Toast.LENGTH_SHORT).show();

        }
    }


}
