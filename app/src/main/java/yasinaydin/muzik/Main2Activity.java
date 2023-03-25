package yasinaydin.muzik;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import static yasinaydin.muzik.MainActivity.e;
import static yasinaydin.muzik.MainActivity.konumlar;
import static yasinaydin.muzik.MainActivity.liste;
import static yasinaydin.muzik.MainActivity.sanatciii;
import static yasinaydin.muzik.MainActivity.z;
import static yasinaydin.muzik.MainActivity.w;
import static yasinaydin.muzik.Muzikk.d;
import static yasinaydin.muzik.Muzikk.o;
import static yasinaydin.muzik.Muzikk.y;

public class Main2Activity extends AppCompatActivity {
    ImageButton geri;
    ImageButton oynat;
    ImageButton ileri;
    SeekBar cubuk;
    TextView tw;
    TextView tw2;
    TextView tw3;
    TextView tw4;
    TextView tw10;
    AsyncTask at;
    ImageView iv;
    Runnable r1,r2,r3,r4,r5;
    int g;
    Context ctx=Main2Activity.this;
    MediaMetadataRetriever mmt;
    Typeface tf;
    Palette p;
    LinearLayout ll6,ll7,ll8;
    ViewGroup.LayoutParams ll7lp,ll8lp;
    GradientDrawable gd;
    PowerManager pm;
    PowerManager.WakeLock wl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ll7=(LinearLayout)findViewById(R.id.ll7);
        ll8=(LinearLayout)findViewById(R.id.ll8);
        ll7lp=ll7.getLayoutParams();
        ll8lp=ll8.getLayoutParams();
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            ll8.setVisibility(View.VISIBLE);
            ll8.setLayoutParams(ll8lp);
            ll7.setVisibility(View.INVISIBLE);
            ll7.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            iv=(ImageView)findViewById(R.id.imageView2);
            o=1;
        }
        else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            ll7.setVisibility(View.VISIBLE);
            ll7.setLayoutParams(ll7lp);
            ll8.setVisibility(View.INVISIBLE);
            ll8.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            iv=(ImageView)findViewById(R.id.imageView);
            o=1;
        }
        MainActivity.r=1;
        tf= Typeface.createFromAsset(getAssets(),"glober_book.otf");
        geri=(ImageButton)findViewById(R.id.button2);
        ileri=(ImageButton)findViewById(R.id.button3);
        oynat=(ImageButton)findViewById(R.id.button4);
        cubuk=(SeekBar)findViewById(R.id.seekBar2);
        tw=(TextView)findViewById(R.id.textView);
        tw2=(TextView)findViewById(R.id.textView2);
        tw3=(TextView)findViewById(R.id.textView3);
        tw4=(TextView)findViewById(R.id.textView4);
        tw10=(TextView)findViewById(R.id.textView10);
        ll6=(LinearLayout)findViewById(R.id.ll6);
        tw.setTypeface(tf);
        tw2.setTypeface(tf);
        tw3.setTypeface(tf);
        tw4.setTypeface(tf);
        tw3.setSelected(true);
        tw10.setTypeface(tf);
        tw10.setSelected(true);
        o=1;
        mmt=new MediaMetadataRetriever();
        pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        wl=pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"Muzik:EKRAN");
        wl.acquire();
        r1=new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Main2Activity.this,"Değişş",Toast.LENGTH_LONG).show();
                cubuk.setMax(e);
                int a=e/1000;
                if(liste.size()>w) {
                    tw3.setText(liste.get(w));
                    tw4.setText((w+1)+"/"+liste.size());
                    mmt.setDataSource(konumlar.get(w));
                    tw10.setText(sanatciii.get(w));
                    byte[] data = mmt.getEmbeddedPicture();
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        iv.setImageBitmap(bitmap);
                        p=Palette.from(bitmap).generate();
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                            gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{ p.getDominantColor(Color.rgb(79, 79, 79)),Color.rgb(79, 79, 79)});
                            ll6.setBackground(gd);
                        }
                        else{
                            ll6.setBackgroundColor(p.getDominantColor(Color.rgb(79,79,79)));
                        }
                    }
                    else {
                        iv.setImageResource(R.drawable.music);
                        ll6.setBackgroundColor(Color.rgb(79,79,79));
                    }
                    if(a<3600) {
                        if (a % 60 < 10) {
                            tw2.setText("" + (a / 60) + ":0" + (a % 60) + "");
                        } else {
                            tw2.setText("" + (a / 60) + ":" + (a % 60) + "");
                        }
                    }
                    else{
                        int b=a/60;
                        if(b%60<10){
                            if (a % 60 < 10) {
                                tw2.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                            } else {
                                tw2.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                            }
                        }
                        else{
                            if (a % 60 < 10) {
                                tw2.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                            } else {
                                tw2.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                            }
                        }
                    }
                }
            }
        };
        r2=new Runnable() {
            @Override
            public void run() {
                if(d==0) {
                    Intent in = new Intent(Main2Activity.this, Muzikk.class);
                    z = 1;
                    w = w + 1;
                    d=1;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        startForegroundService(in);
                    }
                    else{
                        startService(in);
                    }
                }
            }
        };
        r3=new Runnable() {
            @Override
            public void run() {
                if(Muzikk.oynat.isPlaying()){
                    oynat.setImageResource(android.R.drawable.ic_media_pause);
                }
                else{
                    oynat.setImageResource(android.R.drawable.ic_media_play);
                }
                if(Muzikk.oynat.getDuration()-Muzikk.oynat.getCurrentPosition()>250) {
                    cubuk.setProgress(g * 1000);
                    int a=g;
                    if(a<3600) {
                        tw.setText("" + (a / 60) + ":0" + (a % 60) + "");
                    }
                    else{
                        int b=a/60;
                        if(b%60<10){
                            tw.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                        }
                        else{
                            tw.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                        }
                    }
                }
                else{
                    if(d==0) {
                        Intent in = new Intent(ctx, Muzikk.class);
                        z = 1;
                        w = w + 1;
                        d=1;
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            startForegroundService(in);
                        }
                        else{
                            startService(in);
                        }
                    }
                }
            }
        };
        r4=new Runnable() {
            @Override
            public void run() {
                if(Muzikk.oynat.isPlaying()){
                    oynat.setImageResource(android.R.drawable.ic_media_pause);
                }
                else{
                    oynat.setImageResource(android.R.drawable.ic_media_play);
                }
                if(Muzikk.oynat.getDuration()-Muzikk.oynat.getCurrentPosition()>250) {
                    cubuk.setProgress(g * 1000);
                    int a=g;
                    if(a<3600) {
                        tw.setText("" + (a / 60) + ":" + (a % 60) + "");
                    }
                    else{
                        int b=a/60;
                        if(b%60<10){
                            tw.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                        }
                        else{
                            tw.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                        }
                    }
                }
                else{
                    if(d==0) {
                        Intent in = new Intent(ctx, Muzikk.class);
                        z = 1;
                        w = w + 1;
                        d=1;
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            startForegroundService(in);
                        }
                        else{
                            startService(in);
                        }
                    }
                }
            }
        };
        r5=new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Main2Activity.this,"Hata",Toast.LENGTH_LONG).show();
            }
        };
        if(y==0){
            geri.setVisibility(View.INVISIBLE);
            ileri.setVisibility(View.INVISIBLE);
            oynat.setVisibility(View.INVISIBLE);
            cubuk.setVisibility(View.INVISIBLE);
            tw.setVisibility(View.INVISIBLE);
            tw2.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);
            tw3.setVisibility(View.INVISIBLE);
        }
        else{
            geri.setVisibility(View.VISIBLE);
            ileri.setVisibility(View.VISIBLE);
            oynat.setVisibility(View.VISIBLE);
            cubuk.setVisibility(View.VISIBLE);
            tw.setVisibility(View.VISIBLE);
            tw2.setVisibility(View.VISIBLE);
            iv.setVisibility(View.VISIBLE);
            tw3.setVisibility(View.VISIBLE);
            cubuk.setMax(e);
            int a=e/1000;
            if(a<3600) {
                if (a % 60 < 10) {
                    tw2.setText("" + (a / 60) + ":0" + (a % 60) + "");
                } else {
                    tw2.setText("" + (a / 60) + ":" + (a % 60) + "");
                }
            }
            else{
                int b=a/60;
                if(b%60<10){
                    if (a % 60 < 10) {
                        tw2.setText(""+ (a/3600) + ":0" + (a / 60) + ":0" + (a % 60) + "");
                    } else {
                        tw2.setText(""+ (a/3600) + ":0" + (a / 60) + ":" + (a % 60) + "");
                    }
                }
                else{
                    if (a % 60 < 10) {
                        tw2.setText("" + (a/3600) + ":" + (a / 60) + ":0" + (a % 60) + "");
                    } else {
                        tw2.setText("" + (a/3600) + ":" + (a / 60) + ":" + (a % 60) + "");
                    }
                }
            }
            at = new arka().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Main2Activity.this,Muzikk.class);
                z=1;
                w=w-1;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(in);
                }
                else{
                    startService(in);
                }
            }
        });
        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Main2Activity.this,Muzikk.class);
                z=1;
                w=w+1;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(in);
                }
                else{
                    startService(in);
                }
            }
        });

        oynat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Main2Activity.this,Muzikk.class);
                z=2;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(in);
                }
                else{
                    startService(in);
                }
            }
        });
        cubuk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int a=progress/1000;
                if(Muzikk.oynat.getCurrentPosition()-progress>1250){
                    Muzikk.oynat.seekTo(progress);
                }
                if(progress-Muzikk.oynat.getCurrentPosition()>1250){
                    Muzikk.oynat.seekTo(progress);
                }
                if(a<3600) {
                    if (a % 60 < 10) {
                        tw.setText("" + (a / 60) + ":0" + (a % 60) + "");
                    } else {
                        tw.setText("" + (a / 60) + ":" + (a % 60) + "");
                    }
                }
                else{
                    int b=a/60;
                    if(b%60<10){
                        if (a % 60 < 10) {
                            tw.setText(""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                        } else {
                            tw.setText(""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                        }
                    }
                    else{
                        if (a % 60 < 10) {
                            tw.setText("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                        } else {
                            tw.setText("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
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
    }
    private class arka extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                while(true){
                    if(o==1){
                        runOnUiThread(r1);
                        o=0;
                    }
                    g=Muzikk.oynat.getCurrentPosition()/1000;
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
    @Override
    public void onBackPressed(){
        if(at!=null) {
            at.cancel(true);
        }
        MainActivity.r=0;
        finish();
    }

    @Override
    protected void onPause() {
        if(at!=null) {
            at.cancel(true);
        }
        MainActivity.r=0;
        wl.release();
        finish();
        super.onPause();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            ll8.setVisibility(View.VISIBLE);
            ll8.setLayoutParams(ll8lp);
            ll7.setVisibility(View.INVISIBLE);
            ll7.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            iv=(ImageView)findViewById(R.id.imageView2);
            o=1;
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            ll7.setVisibility(View.VISIBLE);
            ll7.setLayoutParams(ll7lp);
            ll8.setVisibility(View.INVISIBLE);
            ll8.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            iv=(ImageView)findViewById(R.id.imageView);
            o=1;
        }
        super.onConfigurationChanged(newConfig);
    }
}
