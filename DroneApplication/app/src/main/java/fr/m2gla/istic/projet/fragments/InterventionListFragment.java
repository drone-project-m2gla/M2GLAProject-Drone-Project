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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.InterventionListAdapter;
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
    private int lastSelectedPosition = -1;
    private String selectedInterventionId = null;


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

        this.lastSelectedPosition = -1;
        this.view = inflater.inflate(R.layout.fragment_intervention_list, container, false);

        if (getArguments() != null) {
            roleStr = getArguments().getString(GeneralConstants.REF_ACT_ROLE);
            if (roleStr != null) {
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
        //this.mSchedule = new SimpleAdapter(getActivity(), this.listItem, R.layout.disp_intervention,
        this.mSchedule = new InterventionListAdapter(getActivity(), this.listItem, R.layout.disp_intervention,
                new String[]{GeneralConstants.INTER_LIST_MEAN, GeneralConstants.INTER_LIST_LABEL, GeneralConstants.INTER_LIST_DATE, GeneralConstants.INTER_LIST_CODE, GeneralConstants.INTER_LIST_DATA},
                new int[]{R.id.interventionNewMean, R.id.interventionLabel, R.id.interventionDate, R.id.interventionCode, R.id.interventionData});

        //On attribut à notre listView l'adapterMeans que l'on vient de créer
        this.idList.setAdapter(mSchedule);


        // Ajouter un écouteur sur la selection d'un element de la liste
        this.idList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map, prevmap;

                map = (HashMap<String, String>) idList.getItemAtPosition(position);
                Log.i(TAG, "OnClicklistener\t" + userQualification);

                String idIntervention = map.get(GeneralConstants.INTER_LIST_ID).toString();
                if (userQualification == UserQualification.CODIS) {
//                Toast.makeText(view.getContext(), "CODIS : " + map.get(GeneralConstants.INTER_LIST_ID) + " " + map.get(GeneralConstants.INTER_LIST_CODE) + " " + map.get(GeneralConstants.INTER_LIST_DATA), Toast.LENGTH_SHORT).show();

                    InterventionDetailFragment fragmentDetailIntervention = (InterventionDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);
                    fragmentDetailIntervention.setIdIntervention(idIntervention);


                    // Retablir la couleur de la ligne precedemment selectionnee
                    if (lastSelectedPosition > -1) {
                        prevmap = (HashMap<String, String>) idList.getItemAtPosition(lastSelectedPosition);
                        if (prevmap != null) {
                            prevmap.put(GeneralConstants.INTER_LIST_SELECT, GeneralConstants.UNSELECT_DESC_STR);
                        }

                    }

                    // Changer la couleur de la ligne selectionnee
                    map.put(GeneralConstants.INTER_LIST_SELECT, GeneralConstants.SELECT_DESC_STR);
                    lastSelectedPosition = position;
                    mSchedule.notifyDataSetChanged();
//                    Toast.makeText(view.getContext(), "Position : " + position, Toast.LENGTH_SHORT).show();

                } else {
                    // Toast.makeText(view.getContext(), "Sapeur : " + map.get(GeneralConstants.INTER_LIST_ID) + " " + map.get(GeneralConstants.INTER_LIST_CODE) + " " + map.get(GeneralConstants.INTER_LIST_DATA), Toast.LENGTH_SHORT).show();
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
        lastSelectedPosition = -1;
        this.listItem.clear();

        // Effacement des details affichés
        InterventionDetailFragment fragmentDetailIntervention = (InterventionDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);
        if (fragmentDetailIntervention != null) {
            // Effacement seulement si le fragement existe
            fragmentDetailIntervention.dispNoDetails();
        }


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

                        // Toast.makeText(view.getContext(), " Taille liste - " + intervention.length + " - ", Toast.LENGTH_SHORT).show();
                        if (intervention.length == 0) {
                            return;
                        }


                        for (Intervention inter : intervention) {
                            if (inter == null) {
                                Log.w(TAG, "Element NULL");
                                continue;
                            }
                            addInterventionInList(inter);
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

    }

    public void refreshList(String idIntervention) {

        // Recuperer l'id de l'intervention selectionnee
        this.selectedInterventionId = idIntervention;

        // Refraichir la liste d'intervention
        refreshList();
    }


    /**
     * Methode demandant l'ajout d'une intervention dans la liste
     *
     * @param intervention : Intervention
     */
    public void addInterventionInList(Intervention intervention) {
        String idStr, dcStr, nbExtraStr, addrStr, labelStr, dateStr;
        Long dateLong;
        Date date;
        int nbMeanExtra = 0;
        DateFormat mediumDateFormat;


        mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale("FR", "fr"));

        idStr = intervention.getId();
        dcStr = intervention.getDisasterCode().toString();
//        for (Mean m: intervention.meansRequested()) {
//            if (m.refusedMeans() == false) {
//                nbMeanExtra++;
//            }
//        }


        nbMeanExtra = intervention.meansRequested().size();
        Log.i(TAG, "Size  " + nbMeanExtra);
        nbExtraStr = "[" + nbMeanExtra + "]";
        addrStr = intervention.getAddress() + " \n" + intervention.getPostcode() + " " + intervention.getCity();
        labelStr = intervention.getLabel();
        dateLong = Long.valueOf(intervention.getDateCreate());
        date = new Date(dateLong);
        dateStr = mediumDateFormat.format(date);

        addInterventionInList(idStr, dcStr, nbExtraStr, labelStr, dateStr, addrStr);
    }

    /**
     * Methode demandant l'ajout d'une intervention dans la liste
     *
     * @param id      : Reference (id) de l'intervention
     * @param code    : Code d'intervention
     * @param nbMeans : Nombre de moyens en attente de validation
     * @param label   : Nom de l'intervention
     * @param date    : Date de l'intervention
     * @param data    : Données de l'intervention
     */
    public void addInterventionInList(String id, String code, String nbMeans, String label, String date, String data) {
        addInterventionInList(id, code, nbMeans, label, date, data, false);
    }


    /**
     * Methode demandant l'ajout d'une intervention dans la liste
     *
     * @param id      : Reference (id) de l'intervention
     * @param code    : Code d'intervention
     * @param nbMeans : Nombre de moyens en attente de validation
     * @param label   : Nom de l'intervention
     * @param date    : Date de l'intervention
     * @param data    : Données de l'intervention
     * @param initial : true pour specifier qu'il s'agit d'une intervention ajoutée pendant l'initialisation
     */
    public void addInterventionInList(String id, String code, String nbMeans, String label, String date, String data, boolean initial) {
        // Verifier si une insertion est a faire
        if ((code == null) || (code.length() <= 0)) {
            return;
        }

        //On déclare la HashMap qui contiendra les informations pour un element de la liste
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier element de notre listView
        map = new HashMap<String, String>();

        //on insère les élémentsque l'on récupérera dans le textView titre créé dans le fichier disp_intervention.xml
        map.put(GeneralConstants.INTER_LIST_MEAN, nbMeans);
        map.put(GeneralConstants.INTER_LIST_ID, id);
        map.put(GeneralConstants.INTER_LIST_LABEL, label);
        map.put(GeneralConstants.INTER_LIST_DATE, date);
        map.put(GeneralConstants.INTER_LIST_CODE, code);
        map.put(GeneralConstants.INTER_LIST_DATA, data);
        if ((this.selectedInterventionId != null) && (id.compareTo(this.selectedInterventionId) == 0)) {
            map.put(GeneralConstants.INTER_LIST_SELECT, GeneralConstants.SELECT_DESC_STR);

            // Afficher les details de l'intervention pre-selectionnee
            InterventionDetailFragment fragmentDetailIntervention = (InterventionDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);
            fragmentDetailIntervention.setIdIntervention(this.selectedInterventionId);

            this.lastSelectedPosition = listItem.size();

        } else {
            map.put(GeneralConstants.INTER_LIST_SELECT, GeneralConstants.UNSELECT_DESC_STR);
        }

        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        if (initial != true) {
            // Mettre a jour la liste d'affichage
            this.mSchedule.notifyDataSetChanged();

//            TextView textMean = (TextView) this.idList.getChildAt(listItem.size()).findViewById(R.id.interventionNewMean);

//            textMean.setHighlightColor(Color.RED);
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
        intent.putExtra(GeneralConstants.REF_ACT_IDINTER, idIntervention);

        // lancement de l'activité d'affichage de la carte
        startActivity(intent);
    }


}