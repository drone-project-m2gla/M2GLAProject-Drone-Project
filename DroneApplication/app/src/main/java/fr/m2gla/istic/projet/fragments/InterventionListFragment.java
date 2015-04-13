package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.UserQualification;


public class InterventionListFragment extends Fragment {

    private UserQualification                   userQualification = UserQualification.SIMPLEUSER;
    private ListView                            idList;
    private ArrayList<HashMap<String, String>>  listItem;
    private SimpleAdapter                       mSchedule;
    private View                                view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String      roleStr;


        this.view = inflater.inflate(R.layout.fragment_intervention_list, container, false);

        if (getArguments() != null) {
            roleStr = getArguments().getString(GeneralConstants.REF_ACT_ROLE);
            if (roleStr != null) {
                Toast.makeText(this.view.getContext(), roleStr, Toast.LENGTH_SHORT).show();

                if (roleStr.compareTo(UserQualification.CODIS.toString()) == 0) {
                    Toast.makeText(this.view.getContext(), " * CODIS * ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.CODIS;
                }
                else if (roleStr.compareTo(UserQualification.SIMPLEUSER.toString()) == 0) {
                    Toast.makeText(this.view.getContext(), " * Sapeur * ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                }
                else {
                    Toast.makeText(this.view.getContext(), " * Aucun * ", Toast.LENGTH_SHORT).show();
                    this.userQualification = UserQualification.SIMPLEUSER;
                }
            }
            else {
                Toast.makeText(this.view.getContext(), " * Null ! * ", Toast.LENGTH_SHORT).show();
                this.userQualification = UserQualification.SIMPLEUSER;
            }
        }
        else {
            Toast.makeText(this.view.getContext(), "* Pas D'Argument ! *", Toast.LENGTH_SHORT).show();
            this.userQualification = UserQualification.SIMPLEUSER;
        }

        refreshList();

        return this.view;

    }


    public void refreshList() {

        //Récupération de la listview créée dans le fichier clients.xml
        this.idList = (ListView) this.view.findViewById(R.id.interventionListView);

        //Création de la ArrayList qui nous permettra de remplire la listView
        this.listItem = new ArrayList<HashMap<String, String>>();

        addInterventionInList("00", "Inter", true);


        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue disp_item
//        this.mSchedule = new SimpleAdapter(getActivity().getApplicationContext(), this.listItem, R.layout.disp_intervention,
        this.mSchedule = new SimpleAdapter(getActivity(), this.listItem, R.layout.disp_intervention,
                new String[] {GeneralConstants.INTER_LIST_ELEM1, GeneralConstants.INTER_LIST_ELEM2},
                new int[] {R.id.interventionCode, R.id.interventionData});




        //On attribut à notre listView l'adapter que l'on vient de créer
        this.idList.setAdapter(mSchedule);

        Log.i("ADAPTER", this.mSchedule.toString() + " --- " + this.idList.getAdapter().getCount());
        Log.i("ADAPTER", this.mSchedule.toString() + " --- " + this.idList.getAdapter().getItem(0).toString());

        // Ajouter un écouteur sur la selection d'un element de la liste
        this.idList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map;

                map = (HashMap<String, String>)idList.getItemAtPosition(position);

                Toast.makeText(view.getContext(), map.get(GeneralConstants.INTER_LIST_ELEM1) + " " + map.get(GeneralConstants.INTER_LIST_ELEM2), Toast.LENGTH_SHORT).show();

            }
        });

        TextView    textView = (TextView) this.view.findViewById(R.id.interventionCode);
        if (textView == null) Log.i("ADAPTER", "NULL");
        else textView.setText("0001");
        Log.i("ADAPTER", "--> " + R.id.interventionCode + " --- " + R.id.interventionData + " <--");


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


    public void addInterventionInList(String code, String data) {
        addInterventionInList(code, data, false);
    }


    public void addInterventionInList(String code, String data, boolean initial) {
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

        if (initial != true) {
            // Mettre a jour la liste d'affichage
            this.mSchedule.notifyDataSetChanged();
        }
    }


}