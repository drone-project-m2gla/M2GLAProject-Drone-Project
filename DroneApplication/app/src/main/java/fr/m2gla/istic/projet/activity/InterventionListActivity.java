
package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.m2gla.istic.projet.context.GeneralConstants;

import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.fragments.InterventionDetailFragment;
import fr.m2gla.istic.projet.fragments.InterventionListFragment;

/**
 * Created by david on 19/04/15.
 */

public class InterventionListActivity extends Activity {
    private static final String TAG = "InterListActivity";

    private UserQualification userQualification = UserQualification.SIMPLEUSER;
    private FragmentManager fragmentManager;
    private Fragment listFragment;


    /**
     * Methode Principale de l'activité liste d'intervention
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_list);

        Intent intent;
        String roleStr;
        Button addButton = (Button) findViewById(R.id.addInterButton);
        Bundle bundle = new Bundle();


        this.fragmentManager = getFragmentManager();
        intent = getIntent();

        if (intent != null) {
            roleStr = intent.getStringExtra(GeneralConstants.REF_ACT_ROLE);
            if (roleStr != null) {
                // Toast.makeText(getApplicationContext(), roleStr, Toast.LENGTH_SHORT).show();

                if (roleStr.compareTo(UserQualification.CODIS.toString()) == 0) {
                    Toast.makeText(getApplicationContext(), " - CODIS - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.CODIS;
                } else if (roleStr.compareTo(UserQualification.SIMPLEUSER.toString()) == 0) {
                    Toast.makeText(getApplicationContext(), " - Sapeur - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                } else {
                    Toast.makeText(getApplicationContext(), " - Aucun - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                    roleStr = UserQualification.SIMPLEUSER.toString();
                }
            } else {
                Toast.makeText(getApplicationContext(), " - Null - ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.SIMPLEUSER;
                roleStr = UserQualification.SIMPLEUSER.toString();
            }

        } else {
            Toast.makeText(getApplicationContext(), "PAS D'INTENT !", Toast.LENGTH_SHORT).show();
            roleStr = UserQualification.SIMPLEUSER.toString();
        }

        if (this.userQualification == UserQualification.SIMPLEUSER) {
            addButton.setEnabled(false);
            addButton.setVisibility(View.INVISIBLE);
        }


        // Transferer le role vers les fragments
        bundle.putString(GeneralConstants.REF_ACT_ROLE, roleStr);

        this.listFragment = this.fragmentManager.findFragmentById(R.id.fragment_intervention_list);

        if (this.listFragment == null) Log.i(TAG, "Fragment null");
        else {
            ((InterventionListFragment) this.listFragment).setUserQualification(this.userQualification);
        }

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
     * Methode de rafraichissement de l'activity
     *
     * @param -
     */
    public void interventionRefresh() {
        InterventionListFragment interventionListFragment;


        // Demander le rafraichissement de la liste
        interventionListFragment = (InterventionListFragment) this.listFragment;
        interventionListFragment.refreshList();
    }


    /**
     * Methode de rafraichissement de l'activity appelee par button
     *
     * @param view : vue courante
     */
    public void interventionRefresh(View view) {
        interventionRefresh();
    }


    /**
     * Methode de fin de l'activity appelee par button
     *
     * @param view : vue courante
     */
    public void endInterventionSelection(View view) {
        // arret de l'activity ici
        finish();
    }


    /**
     * Methode de lancement de l'activity d'ajout de d'intervention appelee par button
     *
     * @param view : vue courante
     */
    public void interventionSelection(View view) {
        Intent intent = new Intent(getApplicationContext(), NewInterventionActivity.class);

        // lancement de la seconde activité, en demandant un code retour
        // startActivity(intent);
        startActivityForResult(intent, 0);
    }


    /**
     * Methode de recuperation du retours de l'activite lance
     *
     * @param a
     * @param b
     * @param retIntent
     */
    @Override
    public void onActivityResult(int a, int b, Intent retIntent) {
        interventionRefresh();
    }

    /**
     * Methode de lancement de l'activity d'affichage des moyens appelee par button
     *
     * @param view : vue courante
     */
    public void MeanTableViewCall(View view) {
        Intent  intent;
        String  idIntervention;

        // Creation d'un intent pour appeler une autre activité (SecondaryActivity)
        intent = new Intent(getApplicationContext(), MeanTableActivity.class);

        // Demander un rafraichissement de la liste des interventions et des details de
        // l'intervention en cours
        InterventionDetailFragment fragmentDetailIntervention = (InterventionDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);
        idIntervention = fragmentDetailIntervention.getIdIntervention();

        // Ajout de données supplémentaires dans l'intent
        intent.putExtra(GeneralConstants.REF_ACT_IDINTER, "" + idIntervention);

        // Lancement de l'activité, suivante
        startActivity(intent);

    }

}