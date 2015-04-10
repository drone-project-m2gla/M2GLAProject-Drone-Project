package fr.m2gla.istic.projet.intent;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.TooManyListenersException;

import fr.m2gla.istic.projet.receiver.GcmBroadcastReceiver;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

public class GcmIntentService extends IntentService {
    private static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Log.i(TAG, "Message receive is " + extras.toString());

            for(Strategy strategy : StrategyRegistery.getInstance().getStrategies()) {
                String message = (String)extras.get(strategy.getScopeName());
                if (message != null) {
                    strategy.call(message);
                }
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
