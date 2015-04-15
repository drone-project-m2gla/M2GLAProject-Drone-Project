package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;


public class InterventionListFragment extends Fragment {
    private static final String TAG = "InterListFragment";

    private UserQualification userQualification = UserQualification.SIMPLEUSER;
    private ListView idList;
    private ArrayList<HashMap<String, String>> listItem;
    private SimpleAdapter mSchedule;
    private View view;


    /**
     * Methode d'entree
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String roleStr;


        this.view = inflater.inflate(R.layout.fragment_intervention_list, container, false);

        if (getArguments() != null) {
            roleStr = getArguments().getString(GeneralConstants.REF_ACT_ROLE);
            if (roleStr != null) {
                Toast.makeText(this.view.getContext(), "I'm here!!! " + roleStr, Toast.LENGTH_SHORT).show();

                if (roleStr.compareTo(UserQualification.CODIS.toString()) == 0) {
                    Log.i(TAG, "is Codis\t" + roleStr);
                    this.userQualification = UserQualification.CODIS;
                } else if (roleStr.compareTo(UserQualification.SIMPLEUSER.toString()) == 0) {
                    this.userQualification = UserQualification.SIMPLEUSER;
                    Log.i(TAG, "is Simple user\t" + roleStr);
                } else {
                    this.userQualification = UserQualification.SIMPLEUSER;
                    Log.i(TAG, "is nothing\t" + roleStr);
                }
            } else {
                this.userQualification = UserQualification.SIMPLEUSER;
            }
        } else {
            this.userQualification = UserQualification.SIMPLEUSER;
        }


        //Récupération de la listview créée dans le fichier clients.xml
        this.idList = (ListView) this.view.findViewById(R.id.interventionListView);

        //Création de la ArrayList qui nous permettra de remplire la listView
        this.listItem = new ArrayList<HashMap<String, String>>();

        // Pour TEST
        // addInterventionInList("00", "Inter", true);


        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue disp_item
        this.mSchedule = new SimpleAdapter(getActivity(), this.listItem, R.layout.disp_intervention,
                new String[]{GeneralConstants.INTER_LIST_ID, GeneralConstants.INTER_LIST_CODE, GeneralConstants.INTER_LIST_DATA},
                new int[]{R.id.interventionId, R.id.interventionCode, R.id.interventionData});

        //On attribut à notre listView l'adapter que l'on vient de créer
        this.idList.setAdapter(mSchedule);


        // Ajouter un écouteur sur la selection d'un element de la liste
        this.idList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map;

                map = (HashMap<String, String>) idList.getItemAtPosition(position);
                Log.i(TAG, "OnClicklistener\t" + userQualification);

                String idIntervention = map.get(GeneralConstants.INTER_LIST_ID).toString();
                if (userQualification == UserQualification.CODIS) {
                Toast.makeText(view.getContext(), "CODIS : " + map.get(GeneralConstants.INTER_LIST_ID) + " " + map.get(GeneralConstants.INTER_LIST_CODE) + " " + map.get(GeneralConstants.INTER_LIST_DATA), Toast.LENGTH_SHORT).show();
                    InterventionDetailFragment fragmentDetailIntervention = (InterventionDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);
                    fragmentDetailIntervention.setIdIntervention(idIntervention);
                } else {
                    Toast.makeText(view.getContext(), "Sapeur : " + map.get(GeneralConstants.INTER_LIST_ID) + " " + map.get(GeneralConstants.INTER_LIST_CODE) + " " + map.get(GeneralConstants.INTER_LIST_DATA), Toast.LENGTH_SHORT).show();
                    callMap(idIntervention);
                }

            }
        });


        refreshList();

        return this.view;

    }


    /**
     * Methode setter permettant à l'activity appelante de passer le role de l'utilisateur
     *
     * @param userQualification : role de l'utilisateur
     */
    public void setUserQualification(UserQualification userQualification) {
        this.userQualification = userQualification;
    }


    /**
     * Methode permettant de rafraichir la liste des intervention
     *
     * @param -
     */
    public void refreshList() {

        // Changement de la ArrayList qui nous permettra de remplire la listView
        this.listItem.clear();

        RestServiceImpl.getInstance().get(RestAPI.GET_ALL_INTERVENTION, null, Intervention[].class,
                new Command() {
                    /**
                     * Success connection
                     * @param response Response object type Intervention[]
                     */
                    @Override
                    public void execute(Object response) {
                        Intervention intervention[];

                        if (response == null) {
                            return;
                        }

                        intervention = (Intervention[]) response;

                        Toast.makeText(view.getContext(), " Taille liste - " + intervention.length + " - ", Toast.LENGTH_SHORT).show();
                        if (intervention.length == 0) {
                            return;
                        }


                        for (Intervention inter : intervention) {
                            if (inter == null) {
                                Log.w(TAG, "Element NULL");
                                continue;
                            }
                            addInterventionInList("" + inter.getId(), "" + inter.getDisasterCode(), "" + inter.getAddress() + " " + inter.getPostcode() + " " + inter.getCity());
                        }


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

/* Pour TEST
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
*/
    }


    /**
     * Methode demandant l'ajout d'une intervention dans la liste
     *
     * @param id    : Reference (id) de l'intervention
     * @param code    : Code d'intervention
     * @param data : données de l'intervention
     */
    public void addInterventionInList(String id, String code, String data) {
        addInterventionInList(id, code, data, false);
    }


    /**
     * Methode demandant l'ajout d'une intervention dans la liste
     *
     * @param id    : Reference (id) de l'intervention
     * @param code    : Code d'intervention
     * @param data    : données de l'intervention
     * @param initial : true pour specifier qu'il s'agit d'une intervention ajoutée pendant l'initialisation
     */
    public void addInterventionInList(String id, String code, String data, boolean initial) {
        // Verifier si une insertion est a faire
        if ((code == null) || (code.length() <= 0)) {
            return;
        }

        //On déclare la HashMap qui contiendra les informations pour un element de la liste
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier element de notre listView
        map = new HashMap<String, String>();

        //on insère un élément code que l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_ID, id);
        //on insère un élément data que l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_CODE, code);
        //on insère un élément data que l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_DATA, data);
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        if (initial != true) {
            // Mettre a jour la liste d'affichage
            this.mSchedule.notifyDataSetChanged();
        }
    }


    /**
     * Methode de lancement de l'activity d'affichage de la carte correspondant a l'intervention
     *
     * @param idIntervention : Reference (id) de l'intervention
     */
    public void callMap(String idIntervention) {
        Intent intent = new Intent(getActivity(), MapActivity.class);


        // Ajouter l'id dans l'intent
        intent.putExtra(GeneralConstants.ID_INTERVENTION, idIntervention);

        // lancement de l'activité d'affichage de la carte
        startActivity(intent);
    }


}