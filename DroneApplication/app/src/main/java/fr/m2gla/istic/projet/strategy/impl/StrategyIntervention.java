package fr.m2gla.istic.projet.strategy.impl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import fr.m2gla.istic.projet.activity.InterventionListActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.application.DroneApplication;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.strategy.Strategy;

/**
 * Created by baptiste on 09/04/15.
 */
public class StrategyIntervention implements Strategy {
    private static final String TAG = "StratInter";
    private static Strategy INSTANCE;

    public StrategyIntervention() {}

    public static Strategy getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyIntervention();
        }
        return INSTANCE;
    }

    @Override
    public String getScopeName() {
        return "intervention";
    }

    @Override
    public Class<?> getType() {
        return Intervention.class;
    }

    @Override
    public void call(Object object) {
        if (!(object instanceof Intervention)) {
            Log.e(TAG, "Convert error");
            return;
        }

        Context context = DroneApplication.getAppContext();
        Intervention intervention = (Intervention)object;

        Builder builder = new Builder(context)
                    .setContentTitle(context.getString(R.string.title_notification_intervention))
                    .setContentText(intervention.getAddress());

        Intent resultIntent = new Intent(context, InterventionListActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(12, builder.build());
    }
}
