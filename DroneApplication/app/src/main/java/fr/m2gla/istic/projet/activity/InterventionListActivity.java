
package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.UserQualification;

/**
 * Created by david on 09/02/15.
 */

public class InterventionListActivity extends Activity {

    private ListView                            idList;
    private ArrayList<HashMap<String, String>>  listItem;
    private SimpleAdapter                       mSchedule;
    private UserQualification                   userQualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_list);

        Intent              intent;
        String              roleStr;
        Button              addButton = (Button) findViewById(R.id.addInterButton);


        intent = getIntent();

        if (intent != null) {
            roleStr = intent.getStringExtra(GeneralConstants.REF_ACT_ROLE);
            Toast.makeText(getApplicationContext(), roleStr, Toast.LENGTH_SHORT).show();

            if (roleStr.compareTo(UserQualification.CODIS.toString()) == 0) {
                Toast.makeText(getApplicationContext(), " - CODIS - ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.CODIS;
            }
            else if (roleStr.compareTo(UserQualification.SIMPLEUSER.toString()) == 0) {
                Toast.makeText(getApplicationContext(), " - Sapeur - ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.SIMPLEUSER;
                addButton.setEnabled(false);
                addButton.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(getApplicationContext(), " - Aucun - ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.SIMPLEUSER;
            }

        } else {
            Toast.makeText(getApplicationContext(), "PAS D'INTENT !", Toast.LENGTH_SHORT).show();
        }

        refreshList();

    }


    private void refreshList() {

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

        addInterventionInList("01", "Inter1");
        addInterventionInList("02", "Intervention 2");
        addInterventionInList("03", "La mienne");
        addInterventionInList("04", "Inter4");
        addInterventionInList("05", "Intervention 5");
        addInterventionInList("06", "La leur");
        addInterventionInList("07", "Inter7");
        addInterventionInList("08", "Intervention 8");
        addInterventionInList("09", "La votre");
        addInterventionInList("10", "Inter10");
        addInterventionInList("11", "Intervention 11");
        addInterventionInList("12", "La notre");

    }


    public void interventionRefresh(View view) {
        // Demander le rafraichissement de la liste
        refreshList();
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