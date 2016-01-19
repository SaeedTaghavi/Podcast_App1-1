package e.aakriti.work.breceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.projectemplate.musicpro.util.NetworkUtil;

import e.aakriti.work.podcast_app.OffLineModeActivity;

/**
 * Created by uadmin on 23/12/15.
 */
public class ConnectionChangeBroadCastReceiver extends BroadcastReceiver {
    static String status = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("e.aakriti.work.podcast_app.SENDBROADCAST")) {
            status = intent.getStringExtra("CONNECTIONCHECKED");
        } else {
            if (!NetworkUtil.checkNetworkAvailable(context)) {
                if (status != null) {
                    Intent offlineModeIntent = new Intent(context, OffLineModeActivity.class);
                    offlineModeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(offlineModeIntent);
                }

            } else if(NetworkUtil.checkNetworkAvailable(context)){
                if(status != null){
                    OffLineModeActivity offLineModeActivity = new OffLineModeActivity();
                    offLineModeActivity.finish();
                }


            }
        }
    }
}