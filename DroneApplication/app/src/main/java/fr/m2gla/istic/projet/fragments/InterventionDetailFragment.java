package fr.m2gla.istic.projet.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.ItemsAdapter;
import fr.m2gla.istic.projet.command.ListAdapterCommand;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyCodisValidateMean;


/**
 * Fragment dde gestion de l'affichage des détails d'une intervention
 */
public class InterventionDetailFragment extends Fragment implements ListAdapterCommand {
    private static final String TAG = "Inter";
    private Intervention intervention;

    private String idIntervention = "";
    private String[] titles;
    private String[] images;
    private ArrayList<String> titlesList;
    private View view = null;


    // Declaring the Integer Array with resourse Id's of Images for the Spinners

    /**
     * Methode principale
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);
        StrategyCodisValidateMean.getINSTANCE().setFragment(this);

        return view;
    }


    /**
     * Methode permettant de specifier l'intervention en cours
     *
     * @param idIntervention
     */
    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;

        if (!idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("id", idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, getCallbackSuccess(), getCallbackError());
        }
    }


    /**
     * Methode de gestion des retours en erreur d'un appel à un service REST
     *
     * @return la classe "command" de gestion d'erreur attentue par le service REST
     */
    private Command getCallbackError() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Toast.makeText(getActivity(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG);
            }
        };
    }

    /**
     * Methode de gestion des retours en succes d'un appel à un service REST
     *
     * @return la classe "command" de gestion de succes attentue par le service REST
     */
    private Command getCallbackSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                intervention = (Intervention) response;
                // Toast.makeText(getActivity(), "  test intervention return " + intervention.getId(), Toast.LENGTH_LONG).show();
                List<Mean> meanList = intervention.meansRequested();
                // Initialisation des titres et images.
                initImagesTitles(intervention, meanList);
                List<Drawable> drawables = new ArrayList<Drawable>();

                if (images.length > 0) {
                    for (String imageId : images) {
                        Log.wtf(TAG, "iamge id " + (imageId != null));
                        if (!imageId.equals("")) {
                            SVG svg = null;
                            try {
                                svg = SVG.getFromResource(getActivity(), getResources().getIdentifier(imageId, "raw", getActivity().getPackageName()));
                            } catch (SVGParseException e) {
                                e.printStackTrace();
                            }
                            drawables.add(new PictureDrawable(svg.renderToPicture()));

                        } else {
                            drawables.add(getResources().getDrawable(R.drawable.bubble_shadow));
                        }
                    }

                }

                ListView moyensListView = (ListView) view.findViewById(R.id.intervention_detail_list);
                Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);
                Context activity = InterventionDetailFragment.this.getActivity();
//                ListAdapter adapter = new ItemsAdapter(activity, R.layout.custom_detail_moyen, titles, imagesArray);
                ListAdapter adapter = new ItemsAdapter(activity, R.layout.custom_detail_moyen, titlesList, imagesArray, getIdIntervention(), meanList, InterventionDetailFragment.this);
                Log.i(TAG, "adapterMeans  " + (adapter == null) + " \nImage array  " + imagesArray.length + " \ntitles " + (titles == null) + "\nactivity  " + (activity == null));

                Log.i(TAG, "List\t" + (moyensListView == null));

                moyensListView.setAdapter(adapter);
            }
        };
    }


    /**
     * Formattage des moyens extra pour l'adapter de la liste des moyens en attente de validation
     *
     * @param intervention
     * @param listXtra
     */
    private void initImagesTitles(Intervention intervention, List<Mean> listXtra) {
        int listXtraNotDeclinedSize = 0;

        for (Mean m : listXtra) {
            if (!m.refusedMeans()) {
                listXtraNotDeclinedSize++;
            }
        }
        titles = new String[listXtraNotDeclinedSize];
        images = new String[listXtraNotDeclinedSize];
        titlesList = new ArrayList<String>();

        LinearLayout nomLayout = (LinearLayout) this.view.findViewById(R.id.nomDetailsLayout);
        LinearLayout codeLayout = (LinearLayout) this.view.findViewById(R.id.codeDetailsLayout);
        LinearLayout adresseLayout = (LinearLayout) this.view.findViewById(R.id.adresseDetailsLayout);
        LinearLayout villeLayout = (LinearLayout) this.view.findViewById(R.id.villeDetailsLayout);
        LinearLayout buttonLayout = (LinearLayout) this.view.findViewById(R.id.meanTableButtonLayout);
        ListView newMeanList = (ListView) this.view.findViewById(R.id.intervention_detail_list);
        TextView nomIntervention = (TextView) this.view.findViewById(R.id.details_nom);
        TextView codeIntervention = (TextView) this.view.findViewById(R.id.details_code);
        TextView addresseIntervention = (TextView) this.view.findViewById(R.id.details_adresse);
        TextView villeIntervention = (TextView) this.view.findViewById(R.id.details_ville);
        TextView titleFragement = (TextView) this.view.findViewById(R.id.details_titre_moyen);
        TextView titleNoMoyen = (TextView) this.view.findViewById(R.id.details_moyens);

        // Renseignement des champs
        nomIntervention.setText(intervention.getLabel());
        codeIntervention.setText(intervention.getDisasterCode().toString());
        addresseIntervention.setText(intervention.getAddress());
        villeIntervention.setText(intervention.getPostcode() + " " + intervention.getCity());

        // Ajoute les couleurs aux textes
        nomIntervention.setTextColor(Color.YELLOW);
        codeIntervention.setTextColor(Color.YELLOW);
        addresseIntervention.setTextColor(Color.YELLOW);
        villeIntervention.setTextColor(Color.YELLOW);
        titleFragement.setTextColor(Color.GREEN);
        titleNoMoyen.setTextColor(Color.GREEN);


        // Valider l'affichage des donnees
        nomLayout.setVisibility(View.VISIBLE);
        codeLayout.setVisibility(View.VISIBLE);
        adresseLayout.setVisibility(View.VISIBLE);
        villeLayout.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);
        newMeanList.setVisibility(View.VISIBLE);


        // Declencher les affichages en fonction de la presence de moyens en attente
        if (listXtraNotDeclinedSize > 0) {
            titleFragement.setVisibility(View.VISIBLE);
            titleNoMoyen.setVisibility(View.GONE);
            int position = 0;
            for (Mean m : listXtra) {

                if (!m.refusedMeans()) {

                    titles[position] = m.getVehicle().toString();
                    titlesList.add(m.getVehicle().toString());

                    images[position] = Symbol.getImage(m.getVehicle().toString());

                    position++;
                }

            }

        } else {
            titleFragement.setVisibility(View.GONE);
            titleNoMoyen.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "intervention " + intervention.getId() + "\n n'a pas de demandes de moyens extra ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Effacement de l'affichage
     */
    public void dispNoDetails() {
        // Verifier si une vue est a effacer
        if (this.view == null) {
            return;
        }

        LinearLayout nomLayout = (LinearLayout) this.view.findViewById(R.id.nomDetailsLayout);
        LinearLayout codeLayout = (LinearLayout) this.view.findViewById(R.id.codeDetailsLayout);
        LinearLayout adresseLayout = (LinearLayout) this.view.findViewById(R.id.adresseDetailsLayout);
        LinearLayout villeLayout = (LinearLayout) this.view.findViewById(R.id.villeDetailsLayout);
        LinearLayout buttonLayout = (LinearLayout) this.view.findViewById(R.id.meanTableButtonLayout);
        ListView newMeanList = (ListView) this.view.findViewById(R.id.intervention_detail_list);
        TextView titleFragement = (TextView) this.view.findViewById(R.id.details_titre_moyen);
        TextView titleNoMoyen = (TextView) this.view.findViewById(R.id.details_moyens);

        // Valider le non affichage des donnees
        nomLayout.setVisibility(View.GONE);
        codeLayout.setVisibility(View.GONE);
        adresseLayout.setVisibility(View.GONE);
        villeLayout.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
        newMeanList.setVisibility(View.GONE);
        titleFragement.setVisibility(View.GONE);
        titleNoMoyen.setVisibility(View.GONE);
    }


    /**
     * Methode de récupération pas saisie écran du nom attribué au moyen validé
     * @param xtraMean
     * @param position
     */
    private void meanNameGet(final Mean xtraMean, final int position) {
        final Map<String, String> map = new HashMap<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(GeneralConstants.MEAN_DIALOG_TITLE);

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(GeneralConstants.MEAN_DIALOG_OK_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String meanName = input.getText().toString();

                xtraMean.setName(meanName);
                map.put("idintervention", idIntervention);
                RestServiceImpl.getInstance()
                        .post(RestAPI.POST_VALIDER_MOYEN, map, xtraMean, String.class,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        //Toast.makeText(getContext(), "Moyen validé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                        Log.i("itemsAdapter", "Moyen validé : " + position);
                                        refreshList();

                                        // Mise en place de la stratégie.
                                        updateFragmentOnMap(null);
                                        MoyensInitFragment fragment = (MoyensInitFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_init);
//                                        Activity initFragment = fragment.getActivity();
                                        if (fragment != null) {
                                            Toast.makeText(getActivity(), "Hello\nUn moyen a été mis à jour", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Toast.makeText(getActivity(), "Moyen n'a pas été validé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                    }
                                });


            }
        });
        builder.setNegativeButton(GeneralConstants.MEAN_DIALOG_CANCEL_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /**
     * Demande de rafraichissement de la list des intervention et des détails de l'intervention
     * sélectionnée
     * @return true
     */
    @Override
    public boolean refreshList() {

        // Demander un rafraichissement de la liste des interventions et des details de
        // l'intervention en cours
        InterventionListFragment fragmentListIntervention = (InterventionListFragment) getFragmentManager().findFragmentById(R.id.fragment_intervention_list);
        fragmentListIntervention.refreshList(idIntervention);

        return true;
    }

    /**
     * Methode d'action sur validation d'un moyen en attente
     * @param xtraMean : Moyen validé
     * @param position : Position dans la liste
     * @return true si validation effectuée, false sinon
     */
    @Override
    public boolean onValidateClick(final Mean xtraMean, final int position) {
        final Map<String, String> map = new HashMap<>();

        if (xtraMean == null) {
            Log.d(TAG, "Validation, Moyen NULL !");
            return (false);
        }

        meanNameGet(xtraMean, position);
        return (true);
    }

    /**
     * Methode d'action sur invalidation d'un moyen en attente
     * @param xtraMean : Moyen non validé
     * @param position : Position dans la liste
     * @return true si invalidation effectuée, false sinon
     */
    @Override
    public boolean onCancelClick(final Mean xtraMean, final int position) {
        final Map<String, String> map = new HashMap<>();

        if (xtraMean == null) {
            Log.d(TAG, "Refus, Moyen NULL !");
            return (false);
        }

        map.put("idintervention", idIntervention);
        //Toast.makeText(getContext(), "button annuler " + position, Toast.LENGTH_LONG).show();

        RestServiceImpl.getInstance()
                .post(RestAPI.POST_ANNULLER_MOYEN, map, xtraMean, String.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                //Toast.makeText(getContext(), "Moyen annulé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                Log.i("itemsAdapter", "Moyen annulé : " + position);
                                refreshList();
                            }
                        }, new Command() {
                            @Override
                            public void execute(Object response) {
                                Toast.makeText(getActivity(), "Moyen n'a pas été annulé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                            }
                        });


        return (true);
    }

    /**
     * Recupération d'un moyen en fonction de sa position
     * @param position : position du moyen dans la liste
     * @return : Moyen trouvé
     */
    public Mean getMeanXtra(int position) {
        return intervention.meansRequested().get(position);
    }

    /**
     * Récupération de l'identifiant de l'intervention en cours
     * @return : identifiant de l'intervention
     */
    public String getIdIntervention() {
        return idIntervention;
    }

    public void updateFragmentOnMap(Mean mean) {
        MoyensInitFragment fragment = (MoyensInitFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_init);
        if (fragment != null) {
            fragment.updateMeanValidation(mean);
        }
    }
}
