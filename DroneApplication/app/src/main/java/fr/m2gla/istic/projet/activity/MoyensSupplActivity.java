package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class MoyensSupplActivity extends Activity {

    private static Spinner moyenSuppSpinner;
    private static ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moyens_suppl);

        adapter = ArrayAdapter.createFromResource(this, R.array.departements,
                android.R.layout.simple_spinner_item);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_moyens_suppl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_moyens_suppl, container, false);

            moyenSuppSpinner = (Spinner) rootView
                    .findViewById(R.id.moyensSpinner);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            moyenSuppSpinner.setAdapter(adapter);

            return rootView;
        }
    }

    public void envoyerListMoyens(View view) {
        Toast.makeText(getApplicationContext(), "Bonjour", Toast.LENGTH_LONG).show();
    }

    public void ajouterMoyen(View view) {
        Toast.makeText(getApplicationContext(), "Ajout moyens", Toast.LENGTH_SHORT).show();
    }
}
