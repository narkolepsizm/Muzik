package yasinaydin.muzik;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import static yasinaydin.muzik.MainActivity.e;
import static yasinaydin.muzik.MainActivity.eski;
import static yasinaydin.muzik.MainActivity.konumlar;
import static yasinaydin.muzik.MainActivity.liste;
import static yasinaydin.muzik.MainActivity.r;
import static yasinaydin.muzik.MainActivity.sanatciii;
import static yasinaydin.muzik.MainActivity.say;
import static yasinaydin.muzik.MainActivity.simdi;
import static yasinaydin.muzik.MainActivity.w;
import static yasinaydin.muzik.MainActivity.yesay;
import static yasinaydin.muzik.MainActivity.z;
import static yasinaydin.muzik.MainActivity.t;

/**
 * Created by Bluestack on 10.13.2017.
 */

public class Muzikk extends Service implements AudioManager.OnAudioFocusChangeListener{
    static long y;
    String muzik;

    int dc=0;
    static MediaPlayer oynat;
    static int o=0,d,pos;
    boolean arama=false,fokus=false;
    IntentFilter inf=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    IntentFilter inf2=new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    AudioManager am;
    Intent durdur,ileri,geri,kapat;
    PendingIntent durpi,ilerpi,geripi,kappi;
    MediaSessionCompat msc;
    NotificationManager nm;
    NotificationCompat.Builder nb;
    PlaybackState pbs;
    NotificationChannel nc;
    Notification n;
    MediaMetadataRetriever mmt=new MediaMetadataRetriever();
    RemoteViews view=new RemoteViews("yasinaydin.muzik",R.layout.bildirimduzeni);
    MediaSession ms;
    long zam;
    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
            if(fokus){
                oynatt();
                fokus=false;
            }
        }
        if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
            if(System.currentTimeMillis()-zam<2500){
                onDestroy();
            }
            else {
                durdur();
            }
        }
        if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
            durdur();
        }
        if(focusChange==AudioManager.AUDIOFOCUS_GAIN_TRANSIENT){
            if(fokus){
                oynatt();
                fokus=false;
            }
        }
    }
    PhoneStateListener psl =new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(state==TelephonyManager.CALL_STATE_IDLE){
                if(arama) {
                    oynatt();
                    arama=false;
                }
            }
            else if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                arama();
            }
            else if(state==TelephonyManager.CALL_STATE_RINGING){
                arama();
            }
            super.onCallStateChanged(state,incomingNumber);
        }
    };
    TelephonyManager tm;
    private BroadcastReceiver bc= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                durdurr();
            }
            else if(intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)){
                Log.e("ANANI","ÇALIŞTI");
            }
        }
    };
    private BroadcastReceiver bc2=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            oynatt();
        }
    };
    private BroadcastReceiver bc3=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG","Ananı...");
            Toast.makeText(Muzikk.this,"Ananı...",Toast.LENGTH_LONG).show();
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Nullable
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        if(intent.getBooleanExtra("kapat", false)){
            System.exit(0);
        }
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        vernotu();
        dc=am.requestAudioFocus(Muzikk.this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        if(tm!=null){
            tm.listen(psl,PhoneStateListener.LISTEN_CALL_STATE);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ms=new MediaSession(Muzikk.this,"MSTAG");
            simdi=System.currentTimeMillis();
            eski=0;
            say=0;
            yesay=0;
            ms.setCallback(new MediaSession.Callback() {
                @Override
                public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
                    KeyEvent ev=(KeyEvent)mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    if(ev!=null){
                        int kc=ev.getKeyCode();
                        int act=ev.getAction();
                        if(act==KeyEvent.ACTION_DOWN) {
                            Log.d("TAG",""+kc);
                            if (kc==KeyEvent.KEYCODE_HEADSETHOOK || kc == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || kc == KeyEvent.KEYCODE_MEDIA_PLAY || kc == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                                eski=simdi;
                                simdi=System.currentTimeMillis();
                                say=yesay+simdi-eski;
                                if((say)<500){
                                    z = 1;
                                    w = w - 2;
                                    if(w<0){
                                        w=0;
                                    }
                                    Intent in = new Intent(Muzikk.this, Muzikk.class);
                                    startService(in);
                                    Log.i("TAG","geri");
                                }
                                else if((say-yesay)<250){
                                    z = 1;
                                    w = w + 1;
                                    Intent in = new Intent(Muzikk.this, Muzikk.class);
                                    startService(in);
                                    Log.i("TAG","ileri");
                                }
                                else {
                                    if(w>=0&&w<konumlar.size()) {
                                        oyna();
                                    }
                                }
                                say=say-yesay;
                                yesay=say;
                            }
                            else if (kc == KeyEvent.KEYCODE_MEDIA_NEXT) {
                                z = 1;
                                w = w + 1;
                                Intent in = new Intent(Muzikk.this, Muzikk.class);
                                startService(in);
                            }
                            else if (kc == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                                z = 1;
                                w = w - 1;
                                Intent in = new Intent(Muzikk.this, Muzikk.class);
                                startService(in);
                            }
                        }
                    }
                    return super.onMediaButtonEvent(mediaButtonIntent);
                }
            });
            pbs=new PlaybackState.Builder().setActions(PlaybackState.ACTION_PLAY_PAUSE).setActions(PlaybackState.ACTION_SKIP_TO_PREVIOUS).setActions(PlaybackState.ACTION_SKIP_TO_NEXT).setState(PlaybackState.STATE_PLAYING,0,0,0).build();
            ms.setPlaybackState(pbs);
            ms.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
            ms.setActive(true);
        }
        else{
            Log.i("VERSION_TAG","Versionnnnnn");
        }
        registerReceiver(bc,inf);
        if(z==1) {
            if(w>=0) {
                oynat(w);
                z = 0;
            }
        }
        if(z==2){
            oyna();
            z = 0;
        }
        if(z==3){
            durdur();
            z=0;
        }
        oynat.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(r==0){
                    w=w+1;
                    z=1;
                    Intent in2=new Intent(Muzikk.this,Muzikk.class);
                    zam=System.currentTimeMillis();
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        startForegroundService(in2);
                    }
                    else{
                        startService(in2);
                    }
                }
            }
        });
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        try {
            unregisterReceiver(bc);
            unregisterReceiver(bc3);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("TAG","aNasKm");
        }

        //unregisterReceiver(bc2);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            if(ms!=null){
                ms.setActive(false);
            }
        }
        if(tm!=null){
            tm.listen(psl,PhoneStateListener.LISTEN_NONE);
        }
    }
    public void oynat(int position){
        if(y!=0) {
            oynat.stop();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ms.setPlaybackState(new PlaybackState.Builder()
                            .setState(PlaybackState.STATE_STOPPED, oynat.getCurrentPosition(), 1f)
                            .build());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        if(position<konumlar.size()) {
            muzik = konumlar.get(position);
            oynat = new MediaPlayer();
            try {
                oynat.setDataSource(muzik);
            } catch (IOException e) {
                Toast.makeText(this, "Müzik Oynatmada Hata!! 2", Toast.LENGTH_LONG).show();
            }
            try {
                oynat.setAudioStreamType(AudioManager.STREAM_MUSIC);
                oynat.prepare();
                oynat.start();
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                    ms.setPlaybackState(new PlaybackState.Builder()
                            .setState(PlaybackState.STATE_PLAYING, oynat.getCurrentPosition(), 1f)
                            .build());
                }
                o = 1;
                e = oynat.getDuration();
                //Toast.makeText(Muzikk.this,"E = "+e,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Müzik Oynatmada Hata!! 3", Toast.LENGTH_LONG).show();
            }
            y = oynat.getDuration();
            d=0;
            MediaMetadataRetriever mmt=new MediaMetadataRetriever();
            mmt.setDataSource(muzik);
            String album=mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist=mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String track=mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            Bitmap bitmap;
            byte[] resim = mmt.getEmbeddedPicture();
            if(resim != null){
                bitmap = BitmapFactory.decodeByteArray(resim, 0, resim.length);
            }
            else{
                bitmap = BitmapFactory.decodeResource(Muzikk.this.getResources(), R.drawable.music);
            }
            Intent inte=new Intent("com.android.music.metachanged");
            inte.putExtra("track",track);
            inte.putExtra("album",album);
            inte.putExtra("artist",artist);
            sendBroadcast(inte);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {

                MediaMetadata mmd = new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, album)
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                        .putString(MediaMetadata.METADATA_KEY_TITLE, track)
                        .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, bitmap)
                        .putBitmap(MediaMetadata.METADATA_KEY_ART, bitmap)
                        .putLong(MediaMetadata.METADATA_KEY_DURATION, oynat.getDuration())
                        .build();
                ms.setMetadata(mmd);
            }
            //Toast.makeText(Muzikk.this,"Track : "+track+"\n"+"Album : "+album+"\n"+"Artist : "+artist,Toast.LENGTH_LONG).show();
            if(t==1){
                deyis();
            }
            notdegis();
        }
    }
    public static int pozisyonal(){
        pos=oynat.getCurrentPosition();
        return pos;
    }
    public void oyna(){
        if(oynat.isPlaying()){
            oynat.pause();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, oynat.getCurrentPosition(), 1f)
                        .build());
            }
        }
        else{
            oynat.start();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PLAYING, oynat.getCurrentPosition(), 1f)
                        .build());
            }
        }
        if(t==1){
            deyis();
        }
        notdegis();
    }
    public void durdurr(){
        if(oynat.isPlaying()){
            oynat.pause();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, oynat.getCurrentPosition(), 1f)
                        .build());
            }
            if(t==1){
                deyis();
            }
        }
        notdegis();
    }
    public void durdur(){
        if(oynat.isPlaying()){
            oynat.pause();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, oynat.getCurrentPosition(), 1f)
                        .build());
            }
            fokus=true;
            if(t==1){
                deyis();
            }
        }
        notdegis();
    }
    public void arama(){
        if(oynat.isPlaying()){
            oynat.pause();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, oynat.getCurrentPosition(), 1f)
                        .build());
            }
            arama=true;
            if(t==1){
                deyis();
            }
        }
        notdegis();
    }
    public void oynatt(){
        if(!oynat.isPlaying()){
            oynat.start();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ms.setPlaybackState(new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PLAYING, oynat.getCurrentPosition(), 1f)
                        .build());
            }
            if(t==1){
                deyis();
            }
        }
        notdegis();
    }
    public void deyis(){
        Intent yolla=new Intent();
        yolla.setAction("deyis");
        sendBroadcast(yolla);
    }
    public void vernotu(){
        Log.i("TAG","vernotu");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            nc=new NotificationChannel("aNasKm","Bildirim",NotificationManager.IMPORTANCE_LOW);
            nc.canBypassDnd();
            nm.createNotificationChannel(nc);
            durdur = new Intent(Muzikk.this, VerBildiriyi.class);
            ileri = new Intent(Muzikk.this, VerBildiriyi.class);
            geri = new Intent(Muzikk.this, VerBildiriyi.class);
            kapat = new Intent(Muzikk.this, VerBildiriyi.class);
            durdur.putExtra("malzeme", 10);
            ileri.putExtra("malzeme", 15);
            geri.putExtra("malzeme", 5);
            kapat.putExtra("malzeme", 20);
            durpi = PendingIntent.getBroadcast(this, 666, durdur, 0);
            ilerpi = PendingIntent.getBroadcast(this, 999, ileri, 0);
            geripi = PendingIntent.getBroadcast(this, 333, geri, 0);
            kappi = PendingIntent.getBroadcast(this, 369, kapat, 0);
            view.setOnClickPendingIntent(R.id.imageButtonumsu, durpi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu2, geripi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu3, ilerpi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu4, kappi);
            view.setImageViewResource(R.id.imageButtonumsu, android.R.drawable.ic_media_play);
            view.setImageViewResource(R.id.imageButtonumsu2, android.R.drawable.ic_media_previous);
            view.setImageViewResource(R.id.imageButtonumsu3, android.R.drawable.ic_media_next);
            view.setImageViewResource(R.id.imageButtonumsu4, android.R.drawable.ic_menu_close_clear_cancel);
            n=new Notification.Builder(this,"aNasKm")
            .setSmallIcon(R.drawable.music)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setCustomContentView(view)
            .build();
            startForeground(1235, n);
        }
        else {
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nb = new NotificationCompat.Builder(Muzikk.this);
            nb.setSmallIcon(R.drawable.music);
            nb.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            nb.setOngoing(true);
            durdur = new Intent(Muzikk.this, VerBildiriyi.class);
            ileri = new Intent(Muzikk.this, VerBildiriyi.class);
            geri = new Intent(Muzikk.this, VerBildiriyi.class);
            kapat = new Intent(Muzikk.this, VerBildiriyi.class);
            durdur.putExtra("malzeme", 10);
            ileri.putExtra("malzeme", 15);
            geri.putExtra("malzeme", 5);
            kapat.putExtra("malzeme", 20);
            durpi = PendingIntent.getBroadcast(this, 666, durdur, 0);
            ilerpi = PendingIntent.getBroadcast(this, 999, ileri, 0);
            geripi = PendingIntent.getBroadcast(this, 333, geri, 0);
            kappi = PendingIntent.getBroadcast(this, 369, kapat, 0);
            view.setOnClickPendingIntent(R.id.imageButtonumsu, durpi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu2, geripi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu3, ilerpi);
            view.setOnClickPendingIntent(R.id.imageButtonumsu4, kappi);
            view.setImageViewResource(R.id.imageButtonumsu, android.R.drawable.ic_media_play);
            view.setImageViewResource(R.id.imageButtonumsu2, android.R.drawable.ic_media_previous);
            view.setImageViewResource(R.id.imageButtonumsu3, android.R.drawable.ic_media_next);
            view.setImageViewResource(R.id.imageButtonumsu4, android.R.drawable.ic_menu_close_clear_cancel);
            nb.setContent(view);
            startForeground(1235, nb.build());
        }
    }
    public void notdegis(){
        Log.i("TAG","notdegis");
        mmt.setDataSource(konumlar.get(w));
        byte[] data = mmt.getEmbeddedPicture();
        if (data != null) {
            view.setImageViewBitmap(R.id.kapakfotosu, BitmapFactory.decodeByteArray(data, 0, data.length));
        } else {
            view.setImageViewResource(R.id.kapakfotosu,R.drawable.music);
        }
        view.setTextViewText(R.id.muzikadisi,liste.get(w));
        view.setTextViewText(R.id.sanatciadisi,sanatciii.get(w));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            if(oynat.isPlaying()){
                view.setImageViewResource(R.id.imageButtonumsu,android.R.drawable.ic_media_pause);
                n=new Notification.Builder(this,"aNasKm")
                .setSmallIcon(R.drawable.music)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setCustomContentView(view)
                .build();
                startForeground(1235,n);
            }
            else {
                view.setImageViewResource(R.id.imageButtonumsu, android.R.drawable.ic_media_play);
                n=new Notification.Builder(this,"aNasKm")
                .setSmallIcon(R.drawable.music)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(false)
                .setCustomContentView(view)
                .build();
                startForeground(1235,n);
            }
        }
        else{
            if(oynat.isPlaying()){
                view.setImageViewResource(R.id.imageButtonumsu,android.R.drawable.ic_media_pause);
                nb.setContent(view);
                nb.setOngoing(true);
                startForeground(1235,nb.build());
            }
            else {
                view.setImageViewResource(R.id.imageButtonumsu, android.R.drawable.ic_media_play);
                nb.setContent(view);
                nb.setOngoing(false);
                startForeground(1235,nb.build());
            }
        }

    }
}
