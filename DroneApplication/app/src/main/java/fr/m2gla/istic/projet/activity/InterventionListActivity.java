
package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * Created by david on 09/02/15.
 */

public class InterventionListActivity extends Activity {
    private static final String TAG = "InterventionListActivity";

    private ListView idList;
    private ArrayList<HashMap<String, String>> listItem;
    private SimpleAdapter mSchedule;

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

        //Récupération de la listview créée dans le fichier clients.xml
        this.idList = (ListView) findViewById(R.id.idListView);

        //Création de la ArrayList qui nous permettra de remplire la listView
        this.listItem = new ArrayList<HashMap<String, String>>();

        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue disp_item
        this.mSchedule = new SimpleAdapter(this.getBaseContext(), this.listItem, R.layout.disp_intervention,
                new String[] {GeneralConstants.INTER_LIST_ELEM1, GeneralConstants.INTER_LIST_ELEM2},
                new int[] {R.id.interventionCode, R.id.interventionData});

        //On attribut à notre listView l'adapter que l'on vient de créer
        this.idList.setAdapter(mSchedule);

        // Ajouter un écouteur sur la selection d'un element de la liste
        this.idList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map;

                map = (HashMap<String, String>)idList.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), map.get(GeneralConstants.INTER_LIST_ELEM1) + " " + map.get(GeneralConstants.INTER_LIST_ELEM2), Toast.LENGTH_SHORT).show();

            }
        });

        addInterventionInList("1", "Inter1");
        addInterventionInList("2", "Intervention 2");
        addInterventionInList("3", "La mienne");

    }


    private void addInterventionInList(String code, String data) {
        // Verifier si une insertion est a faire
        if ((code == null) || (code.length() <= 0)) {
            return;
        }

        //On déclare la HashMap qui contiendra les informations pour un element de la liste
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier element de notre listView
        map = new HashMap<String, String>();

        //on insère un élément code que l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_ELEM1, code);
        //on insère un élément data que l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_ELEM2, data);
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        // Mettre a jour la liste d'affichage
        this.mSchedule.notifyDataSetChanged();
    }


    public void endInterventionSelection(View view) {
        // arret de l'activity ici
        finish();
    }


    public void interventionSelection(View view) {
        // Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        //lancement de la seconde activité, en demandant un code retour
        // startActivityForResult(intent, 0);

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