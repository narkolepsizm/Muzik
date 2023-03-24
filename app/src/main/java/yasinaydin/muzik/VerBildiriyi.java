package yasinaydin.muzik;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static yasinaydin.muzik.MainActivity.w;
import static yasinaydin.muzik.MainActivity.z;
import static yasinaydin.muzik.Muzikk.oynat;

public class VerBildiriyi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int a=intent.getIntExtra("malzeme",0);
        if(oynat!=null) {
            if (a == 10) {
                Intent in = new Intent(context, Muzikk.class);
                z = 2;
                context.startService(in);
            }
            if (a == 5) {
                Intent in = new Intent(context, Muzikk.class);
                z = 1;
                w = w - 1;
                context.startService(in);
            }
            if (a == 15) {
                Intent in = new Intent(context, Muzikk.class);
                z = 1;
                w = w + 1;
                context.startService(in);
            }
            if(a == 20){
                Intent in = new Intent(context, Muzikk.class);
                in.putExtra("kapat", true);
                context.startService(in);
            }
        }
        else{
            NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(1235);
        }
    }
}
