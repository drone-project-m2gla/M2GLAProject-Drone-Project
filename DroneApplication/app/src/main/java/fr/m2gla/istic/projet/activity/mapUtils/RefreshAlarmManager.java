package fr.m2gla.istic.projet.activity.mapUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import fr.m2gla.istic.projet.command.Command;

/**
 * Created by fernando on 27/05/15.
 * Cette classe permet de faire des opérations périodiques telles que par exemple rafraîchir les moyens
 * La première exécution a lieu après le temps donnée
 */
public class RefreshAlarmManager {
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private BroadcastReceiver mReceiver;

    private Command command;
    private Long timeInterval;
    private Activity activity;

    /**
     * Constructeur de la classe
     * @param activity activité qui invoque la répétition de la commande
     * @param command commande à exécuter
     * @param timeInterval intervalle de temps entre deux exécutions en millisecondes
     */
    public RefreshAlarmManager(Activity activity, Command command, Long timeInterval) {
        this.activity = activity;
        this.command = command;
        this.timeInterval = timeInterval;
    }

    /**
     * Méthode à appeler pour démarrer le timer d'exécution
     */
    public void RegisterAlarmBroadcast()
    {
        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                command.execute(this);
            }
        };

        activity.registerReceiver(mReceiver, new IntentFilter(""));
        pendingIntent = PendingIntent.getBroadcast(activity, 0, new Intent(""), 0);
        alarmManager = (AlarmManager)(activity.getSystemService(Context.ALARM_SERVICE));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeInterval, pendingIntent);
    }

    /**
     * Méthode à appeler pour arrêter l'exécution du timer
     */
    public void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
        activity.getBaseContext().unregisterReceiver(mReceiver);
    }
}
