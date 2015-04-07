package mmm.m2gla.istic.fr.droneapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

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


        if (textLogin.getText().length() != 0) {
            Toast.makeText(getApplicationContext(), textLogin.getText(), Toast.LENGTH_SHORT).show();
        }

        if (textPassword.getText().length() != 0) {
            Toast.makeText(getApplicationContext(), textPassword.getText(), Toast.LENGTH_SHORT).show();
        }

        radioBSelect = roleRadioG.getCheckedRadioButtonId();

        if (radioBSelect == R.id.codisRadioButton) {
            Toast.makeText(getApplicationContext(), "CODIS", Toast.LENGTH_SHORT).show();
        }
        else if (radioBSelect == R.id.userRadioButton) {
            Toast.makeText(getApplicationContext(), "Superviseur", Toast.LENGTH_SHORT).show();
        }

        // lancement de l'activité, suivante
//        startActivity(intent);
    }

}
