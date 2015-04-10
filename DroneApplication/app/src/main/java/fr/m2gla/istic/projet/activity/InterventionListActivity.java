
package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.fragments.InterventionListFragment;

/**
 * Created by david on 09/02/15.
 */

public class InterventionListActivity extends Activity {
    private static final String TAG = "InterventionListActivity";

    private UserQualification                   userQualification = UserQualification.SIMPLEUSER;
    private FragmentManager                     fragmentManager;
    private Fragment                            listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_list);

        RestServiceImpl.getInstance().get(RestAPI.GET_ALL_INTERVENTION, null, Intervention[].class,
                new Command() {
                    /**
                     * Success connection
                     * @param response Response object type Intervention[]
                     */
                    @Override
                    public void execute(Object response) {
                        //TODO list intervention
                    }
                }, new Command() {
                    /**
                     * Error connection
                     * @param response Response error type HttpClientErrorException
                     */
                    @Override
                    public void execute(Object response) {
                        Log.e(TAG, "connection error");
                    }
                });

        Intent              intent;
        String              roleStr;
        Button              addButton = (Button) findViewById(R.id.addInterButton);
        Bundle              bundle = new Bundle();


        this.fragmentManager = getFragmentManager();
        intent = getIntent();
Log.i("InterListActivity", "Debut");

        if (intent != null) {
            roleStr = intent.getStringExtra(GeneralConstants.REF_ACT_ROLE);
            if (roleStr != null) {
                Toast.makeText(getApplicationContext(), roleStr, Toast.LENGTH_SHORT).show();

                if (roleStr.compareTo(UserQualification.CODIS.toString()) == 0) {
                    Toast.makeText(getApplicationContext(), " - CODIS - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.CODIS;
                }
                else if (roleStr.compareTo(UserQualification.SIMPLEUSER.toString()) == 0) {
                    Toast.makeText(getApplicationContext(), " - Sapeur - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                }
                else {
                    Toast.makeText(getApplicationContext(), " - Aucun - ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                    roleStr = UserQualification.SIMPLEUSER.toString();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), " - Null - ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.SIMPLEUSER;
                roleStr = UserQualification.SIMPLEUSER.toString();
            }

        }
        else {
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

        if (this.listFragment == null) Log.i("InterListActivity", "Fragment null");

        this.listFragment.setArguments(bundle);

    }


    public void interventionRefresh(View view) {
        InterventionListFragment    interventionListFragment;


        // Demander le rafraichissement de la liste
        interventionListFragment = (InterventionListFragment) this.listFragment;
        interventionListFragment.refreshList();
    }


    public void endInterventionSelection(View view) {
        // arret de l'activity ici
        finish();
    }


    public void interventionSelection(View view) {
        Intent intent = new Intent(getApplicationContext(), CodisActivity.class);

        // lancement de la seconde activité, en demandant un code retour
        // startActivityForResult(intent, 0);
        startActivity(intent);
    }


/*
    @Override
    public void onActivityResult(int a, int b, Intent retIntent) {
        PersoInfoData persoInfoData;

        persoInfoData = retIntent.getParcelableExtra(MainActivity.refReturn);

        // Verifier si une insertion est a faire
        if ((persoInfoData == null) || (persoInfoData.getNom().length() <= 0)) {
            return;
        }

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();

        //on insère un élément nom que l'on récupérera dans le textView titre créé dans le fichier disp_item.xml
        map.put("nom", persoInfoData.getNom());
        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier disp_item.xml
        map.put("prenom", persoInfoData.getPrenom());
        //enfin on ajoute cette hashMap dans la arrayList
        map.put("dateNais", persoInfoData.getDateNais());
        //enfin on ajoute cette hashMap dans la arrayList
        map.put("ville", persoInfoData.getVille());
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        // Mettre a jour la liste d'affichage
        this.mSchedule.notifyDataSetChanged();


        Toast.makeText(getApplicationContext(), persoInfoData.getNom().toString(), Toast.LENGTH_SHORT).show();

    }
*/
}