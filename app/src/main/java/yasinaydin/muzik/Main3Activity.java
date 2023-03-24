package yasinaydin.muzik;

import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Main3Activity extends AppCompatActivity implements AudioManager.OnAudioFocusChangeListener{
    TextView tw8,tw7,tw6,tw5;
    int a,dc=0,g;
    TelephonyManager tm;
    AudioManager am;
    MediaPlayer mp;
    File muzik;
    Typeface tf;
    ImageButton ib;
    ImageView iw;
    AsyncTask at;
    MediaMetadataRetriever mmt;
    SeekBar sb;
    Runnable r3,r4,r5;
    char[] isimm={};
    char[] isimmm={};
    boolean arama=false;
    PowerManager pm;
    PowerManager.WakeLock wl;
    PhoneStateListener psl =new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(state== TelephonyManager.CALL_STATE_IDLE){
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main3);
        mp=new MediaPlayer();
        muzik=new File(getIntent().getData().getPath());
        iw=(ImageView)findViewById(R.id.imageView2);
        sb=(SeekBar)findViewById(R.id.seekBar);
        mmt=new MediaMetadataRetriever();
        ib=(ImageButton)findViewById(R.id.imageButton);
        tw8=(TextView)findViewById(R.id.textView8);
        tw8.setSelected(true);
        tw7=(TextView)findViewById(R.id.textView7);
        tw7.setSelected(true);
        tw6=(TextView)findViewById(R.id.textView6);
        tw5=(TextView)findViewById(R.id.textView5);
        tf=Typeface.createFromAsset(getAssets(),"glober_book.otf");
        tw5.setTypeface(tf);
        tw6.setTypeface(tf);
        tw7.setTypeface(tf);
        tw8.setTypeface(tf);
        mmt.setDataSource(muzik.getPath());
        byte[] data = mmt.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            iw.setImageBitmap(bitmap);
        } else {
            iw.setImageResource(R.drawable.music);
        }
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        dc=am.requestAudioFocus(Main3Activity.this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        wl=pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"Muzik:EKRAN");
        wl.acquire();
        if(tm!=null){
            tm.listen(psl,PhoneStateListener.LISTEN_CALL_STATE);
        }
        try {
            mp.setDataSource(muzik.getPath());
            mp.prepare();
            mp.start();
        }
        catch(Exception e){
            Toast.makeText(Main3Activity.this,"Müzik Oynatmada Hata!! 5",Toast.LENGTH_LONG).show();
        }
        sb.setMax(mp.getDuration());
        a=mp.getDuration()/1000;
        if(a<3600) {
            if (a % 60 < 10) {
                tw6.setText("" + (a / 60) + ":0" + (a % 60) + "");
            } else {
                tw6.setText("" + (a / 60) + ":" + (a % 60) + "");
            }
        }
        else{
            int b=a/60;
            if(b%60<10){
                if (a % 60 < 10) {
                    tw6.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                } else {
                    tw6.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                }
            }
            else{
                if (a % 60 < 10) {
                    tw6.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                } else {
                    tw6.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                }
            }
        }
        if(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null){
            tw7.setText(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        }
        else{
            isimm=muzik.getName().toCharArray();
            for(int j=muzik.getName().length();j>0;j--){
                if(isimm[j-1]==46){
                    isimmm=new char[j];
                    j=0;
                }
            }
            for(int k=0;k<isimmm.length-1;k++){
                isimmm[k]=isimm[k];
            }
            tw7.setText(new String(isimmm));
        }
        if(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null){
            tw8.setText(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }
        else{
            if(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null){
                tw8.setText(mmt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            }
            else{
                tw8.setText("Bilinmeyen Sanatçı");
            }
        }
        at=new arka().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        r3=new Runnable() {
            @Override
            public void run() {
                if(mp!=null) {
                    if (mp.isPlaying()) {
                        ib.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        ib.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
                sb.setProgress(g * 1000);
                int a=g;
                if(a<3600) {
                    tw5.setText("" + (a / 60) + ":0" + (a % 60) + "");
                }
                else{
                    int b=a/60;
                    if(b%60<10){
                        tw5.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                    }
                    else{
                        tw5.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                    }
                }
            }
        };
        r4=new Runnable() {
            @Override
            public void run() {
                if(mp!=null) {
                    if (mp.isPlaying()) {
                        ib.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        ib.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
                sb.setProgress(g * 1000);
                int a=g;
                if(a<3600) {
                    tw5.setText("" + (a / 60) + ":" + (a % 60) + "");
                }
                else{
                    int b=a/60;
                    if(b%60<10){
                        tw5.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                    }
                    else{
                        tw5.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                    }
                }
            }
        };
        r5=new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Main3Activity.this,"Müzik Oynatmada Hata!! 6",Toast.LENGTH_LONG).show();
            }
        };
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int a=progress/1000;
                if(mp.getCurrentPosition()-progress>1250){
                    mp.seekTo(progress);
                }
                if(progress-mp.getCurrentPosition()>1250){
                    mp.seekTo(progress);
                }
                if(a<3600) {
                    if (a % 60 < 10) {
                        tw5.setText("" + (a / 60) + ":0" + (a % 60) + "");
                    } else {
                        tw5.setText("" + (a / 60) + ":" + (a % 60) + "");
                    }
                }
                else{
                    int b=a/60;
                    if(b%60<10){
                        if (a % 60 < 10) {
                            tw5.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                        } else {
                            tw5.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                        }
                    }
                    else{
                        if (a % 60 < 10) {
                            tw5.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                        } else {
                            tw5.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                }
                else{
                    mp.start();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        mp.stop();
        if(at!=null){
            at.cancel(true);
        }
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(tm!=null){
            tm.listen(psl,PhoneStateListener.LISTEN_NONE);
        }
        am.abandonAudioFocus(this);
        finish();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        if(at!=null){
            at.cancel(true);
        }
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(tm!=null){
            tm.listen(psl,PhoneStateListener.LISTEN_NONE);
        }
        am.abandonAudioFocus(this);
        wl.release();
        mp.release();
        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
            oynatt();
        }
        if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
            durdur();
        }
        if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
            durdur();
        }
        if(focusChange==AudioManager.AUDIOFOCUS_GAIN_TRANSIENT){
            oynatt();
        }
    }
    public void durdur(){
        if(mp.isPlaying()){
            mp.pause();
        }
    }
    public void arama(){
        if(mp.isPlaying()){
            mp.pause();
            arama=true;
        }
    }
    public void oynatt(){
        if(!mp.isPlaying()){
            mp.start();
        }
    }
    private class arka extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                while(true){
                    g=mp.getCurrentPosition()/1000;
                    if(g%60<10){
                        runOnUiThread(r3);
                    }
                    else {
                        runOnUiThread(r4);
                    }
                    Thread.sleep(1);
                }
            }
            catch (Exception e){
                runOnUiThread(r5);
            }
            return null;
        }

    }
}
