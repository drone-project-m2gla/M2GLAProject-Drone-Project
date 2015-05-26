package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Target;
import fr.m2gla.istic.projet.observer.Observable;
import fr.m2gla.istic.projet.observer.ObserverTarget;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

/**
 * Created by baptiste on 16/04/15.
 */
public class DroneTargetActionFragment extends Fragment implements Observable {
    private static final String TAG = "DroneAction";

    private Target target;
    private List<ObserverTarget> observers;

    /**
     * Constructeur
     */
    public DroneTargetActionFragment() {
        target = new Target();
        observers = new ArrayList<>();
    }

    /**
     * Ajout d'une position au drone
     * @param position : Nouvelle position pour le drone
     */
    public void addPosition(Position position) {
        target.addPosition(position);
    }

    /**
     * Recuperation des cibles du drone
     * @return : cibles du drone
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Ajoute un observateur
     * @param observer : observateur à ajouter
     */
    @Override
    public void addObserver(ObserverTarget observer) {
        observers.add(observer);
    }

    /**
     * Suppression d'un observateur
     * @param observer : : observateur à supprimer
     */
    @Override
    public void removeObserver(ObserverTarget observer) {
        observers.remove(observer);
    }

    /**
     * Methode principale
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_target_drone, container, false);

        view.findViewById(R.id.validTargetDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestServiceImpl.getInstance()
                        .post(RestAPI.POST_PARCOURS_DRONE, null, target, Void.class,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.i(TAG, "Target set success");
                                        for (ObserverTarget observer : observers) {
                                            observer.notifySend();
                                        }
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.i(TAG, "Target set error");
                                    }
                                });
            }
        });

        view.findViewById(R.id.closeTargetDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ObserverTarget observer : observers) {
                    observer.notifyClose();
                }
                target.setClose(true);
            }
        });

        view.findViewById(R.id.cleanTargetDrone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ObserverTarget observer : observers) {
                    observer.notifyClear();
                }
                target.getPositions().clear();
                target.setClose(false);
            }
        });

        return view;
    }
}
