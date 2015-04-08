package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 09/02/15.
 */

public class UserActivity  extends Activity {
/*
    private ListView idList;
    private ArrayList<HashMap<String, String>> listItem;
    private SimpleAdapter mSchedule;
    private PersoInfoDataDAO                    clientDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mmm.m2gla.istic.fr.droneapplication.R.layout.activity_clients);

        //Récupération de la listview créée dans le fichier clients.xml
        this.idList = (ListView) findViewById(mmm.m2gla.istic.fr.droneapplication.R.id.idListView);

        //Création d'une instance de la classe DAO (PersoInfoDataDAO)
        PersoInfoDataDAO clientDAO = new PersoInfoDataDAO(this);

        //Création de la ArrayList qui nous permettra de remplire la listView
        this.listItem = new ArrayList<HashMap<String, String>>();

        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue disp_item
        this.mSchedule = new SimpleAdapter(this.getBaseContext(), this.listItem, mmm.m2gla.istic.fr.droneapplication.R.layout.disp_item,
//                new String[] {"nom", "prenom, dateNais"},
//                new int[] {R.id.nomElem, R.id.prenomElem, R.id.dateNaisElem});
                new String[] {"nom", "prenom", "dateNais", "ville"},
                new int[] {mmm.m2gla.istic.fr.droneapplication.R.id.nomElem, mmm.m2gla.istic.fr.droneapplication.R.id.prenomElem, mmm.m2gla.istic.fr.droneapplication.R.id.dateNaisElem, mmm.m2gla.istic.fr.droneapplication.R.id.villeElem});

        //On attribut à notre listView l'adapter que l'on vient de créer
        this.idList.setAdapter(mSchedule);

//        System.out.println("- TOTO -");


    }

    public void finClt(View view) {
        // arret de l'activity ici
        finish();
    }

    public void actiNewClt(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        //lancement de la seconde activité, en demandant un code retour
        startActivityForResult(intent, 0);

    }

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