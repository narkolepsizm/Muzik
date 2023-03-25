package yasinaydin.muzik;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static yasinaydin.muzik.Muzikk.oynat;

public class MainActivity extends AppCompatActivity {
    static List<String> liste,liste2,sureee,sanatciii,sanatciii2,sureee2;
    List<OzelListe> listee;
    static ArrayList<String> konumlar,konumlar2,albummm,adlarrr;
    ListView lw;
    File dosya;
    String konum,sarki,model;
    int a=0;
    static int z,w,e,r,t;
    static long eski;
    static long simdi;
    static long say;
    static long yesay;
    String name,sure,sanatci,isim;
    static Typeface tf;
    TextView tw,tw11,tw12,tw13;
    ProgressBar pb;
    ImageView iw;
    ImageButton ib;
    LinearLayout ll3,ll2,ll;
    LinearLayout ll4,ll5;
    char[]isimm={};
    char[]isimmm={};
    ContentResolver cr;
    Cursor cur;
    IntentFilter inf=new IntentFilter();
    private BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deyis();
        }
    };
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tf= Typeface.createFromAsset(getAssets(),"glober_book.otf");
        iw=(ImageView)findViewById(R.id.imageView3);
        ib=(ImageButton)findViewById(R.id.imageButton2);
        ll=(LinearLayout)findViewById(R.id.ll);
        ll4=(LinearLayout)findViewById(R.id.ll4);
        ll5=(LinearLayout)findViewById(R.id.ll5);
        lw = (ListView) findViewById(R.id.lw);
        tw=(TextView)findViewById(R.id.textView9);
        tw11=(TextView)findViewById(R.id.textView11);
        tw12=(TextView)findViewById(R.id.textView12);
        tw13=(TextView)findViewById(R.id.textView13);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        ll3=(LinearLayout)findViewById(R.id.ll3);
        t=1;
        inf.addAction("deyis");
        registerReceiver(br,inf);
        tw11.setTypeface(tf);
        tw12.setTypeface(tf);
        tw11.setSelected(true);
        tw12.setSelected(true);
        tw13.setSelected(true);
        tw13.setTypeface(tf);
        tw.setTypeface(tf);
        tw.setText("Cihazınızdaki Müzikler Listeleniyor\nLütfen Bekleyin...");
        if(oynat!=null){
            deyis();
        }
        sarki=null;
        model=Build.MANUFACTURER;
        if(model.equals("Reeder P11S")){
            konum=Environment.getExternalStorageDirectory().toString();
        }
        else {
            konum = "/mnt";
        }
        dosya = new File(konum);
        liste = new ArrayList<String>();
        listee=new ArrayList<OzelListe>();
        konumlar = new ArrayList<String>();
        konumlar2=new ArrayList<String>();
        liste2=new ArrayList<String>();
        sanatciii=new ArrayList<String>();
        sureee=new ArrayList<String>();
        sanatciii2=new ArrayList<String>();
        sureee2=new ArrayList<String>();
        albummm=new ArrayList<String>();
        adlarrr=new ArrayList<String>();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            liste=new ArrayList<String>();
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else{
                bul();
            }
        }
        else{
            bul();
        }
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MainActivity.this,Muzikk.class);
                z=2;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(in);
                }
                else{
                    startService(in);
                }
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(in);
            }
        });
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(in);
            }
        });
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tew=(TextView)view.findViewById(R.id.muzikadi);
                sarki=tew.getText().toString();
                Intent in = new Intent(MainActivity.this, Muzikk.class);
                z = 1;
                w = position;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    startForegroundService(in);
                }
                else{
                    startService(in);
                }
                //startService(in);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        if (requestCode == 1) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                bul();
            } else {
                if (grantResult.length > 0) {
                    System.exit(0);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        t=0;
        unregisterReceiver(br);
        super.onDestroy();
    }
    private void deyis(){
        MediaMetadataRetriever mmt=new MediaMetadataRetriever();
        mmt.setDataSource(konumlar.get(w));
        byte[] data = mmt.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            iw.setImageBitmap(bitmap);
        } else {
            iw.setImageResource(R.drawable.music);
        }
        if(Muzikk.oynat.isPlaying()){
            ib.setImageResource(android.R.drawable.ic_media_pause);
        }
        else{
            ib.setImageResource(android.R.drawable.ic_media_play);
        }
        tw11.setText(liste.get(w));
        tw12.setText(sanatciii.get(w));
        ll.setVisibility(View.VISIBLE);
    }
    @SuppressLint("Range")
    public void bul(){
        cr=getContentResolver();
        cur=cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DATA},(MediaStore.Audio.Media.IS_MUSIC+"!=0"),null,null);
        if(cur!=null){
            if(cur.getCount()>0){
                while(cur.moveToNext()){
                    liste2.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                    konumlar2.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    sanatciii2.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    sureee2.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    albummm.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    adlarrr.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                }
            }
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for(int as=0;as<cur.getCount();as++){
                    Log.i("TAG","as : "+as);
                    try {
                        sure="";
                        sure = sureee2.get(as);
                        if (!sure.equals("")) {
                            a = Integer.parseInt(sure);
                            a = a / 1000;
                            if(a<3600) {
                                if (a % 60 < 10) {
                                    sure = ("" + (a / 60) + ":0" + (a % 60) + "");
                                } else {
                                    sure = ("" + (a / 60) + ":" + (a % 60) + "");
                                }
                            }
                            else{
                                int b=a/60;
                                if(b%60<10){
                                    if (a % 60 < 10) {
                                        sure = (""+ (a/3600) + ":0" + (b % 60) + ":0" + (a % 60) + "");
                                    } else {
                                        sure = (""+ (a/3600) + ":0" + (b % 60) + ":" + (a % 60) + "");
                                    }
                                }
                                else{
                                    if (a % 60 < 10) {
                                        sure = ("" + (a/3600) + ":" + (b % 60) + ":0" + (a % 60) + "");
                                    } else {
                                        sure = ("" + (a/3600) + ":" + (b % 60) + ":" + (a % 60) + "");
                                    }
                                }
                            }
                        }
                        else {
                            sure = "0:00";
                        }
                        if(sanatciii2.get(as)!=null){
                            if (!sanatciii2.get(as).equals("<unknown>")){
                                sanatci = (sanatciii2.get(as));
                            }
                            else{
                                sanatci = ("Bilinmeyen Sanatçı");
                            }
                        }
                        else{
                            sanatci = ("Bilinmeyen Sanatçı");
                        }
                        if(adlarrr.get(as)!=null){
                            if (!adlarrr.get(as).equals("<unknown>")){
                                isim = adlarrr.get(as);
                            }
                            else{
                                isimm = liste2.get(as).toCharArray();
                                for (int j = liste2.get(as).length(); j > 0; j--) {
                                    if (isimm[j - 1] == 46) {
                                        isimmm = new char[j];
                                        j = 0;
                                    }
                                }
                                for (int k = 0; k < isimmm.length - 1; k++) {
                                    isimmm[k] = isimm[k];
                                }
                                isim = (new String(isimmm));
                            }
                        }
                        else{
                            isimm = liste2.get(as).toCharArray();
                            for (int j = liste2.get(as).length(); j > 0; j--) {
                                if (isimm[j - 1] == 46) {
                                    isimmm = new char[j];
                                    j = 0;
                                }
                            }
                            for (int k = 0; k < isimmm.length - 1; k++) {
                                isimmm[k] = isimm[k];
                            }
                            isim = (new String(isimmm));
                        }
                        /*if (!sanatciii2.get(as).equals("<unknown>")&&sanatciii2.get(as)!=null) {
                            sanatci = (sanatciii2.get(as));
                        }
                        else {
                            sanatci = ("Bilinmeyen Sanatçı");
                        }
                        if (!adlarrr.get(as).equals("<unknown>")&&adlarrr.get(as)!=null) {
                            isim = adlarrr.get(as);
                        }
                        else {
                            isimm = liste2.get(as).toCharArray();
                            for (int j = liste2.get(as).length(); j > 0; j--) {
                                if (isimm[j - 1] == 46) {
                                    isimmm = new char[j];
                                    j = 0;
                                }
                            }
                            for (int k = 0; k < isimmm.length - 1; k++) {
                                isimmm[k] = isimm[k];
                            }
                            isim = (new String(isimmm));
                        }*/
                                    /*byte[] data = mmt.getEmbeddedPicture();
                                    if (data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        bmp=(bitmap);
                                    }
                                    else {
                                        bmp=BitmapFactory.decodeResource(MainActivity.this.getResources(),R.drawable.music);
                                    }
                                    try {
                                        Thread.sleep(10);
                                    }
                                    catch (Exception e){
                                        Log.i("TAG","Olmadı");
                                    }*/
                        liste.add(isim);
                        konumlar.add(konumlar2.get(as));
                        sureee.add(sure);
                        sanatciii.add(sanatci);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.i("HATA", "HATA");
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int uzuu=liste.size();
                        HashMap<String, String> listeeee = new HashMap<String, String>();
                        HashMap<String, String> konumlarrrr = new HashMap<String, String>();
                        HashMap<String, String> sanatciiii= new HashMap<String, String>();
                        HashMap<String, String> sureeee = new HashMap<String, String>();
                        for(int ik = 0; ik < uzuu ; ik++){
                            listeeee.put(liste.get(ik), liste.get(ik));
                            konumlarrrr.put(liste.get(ik), konumlar.get(ik));
                            sanatciiii.put(liste.get(ik), sanatciii.get(ik));
                            sureeee.put(liste.get(ik), sureee.get(ik));
                        }
                        TreeMap<String, String> sorted = new TreeMap<>();
                        sorted.putAll(listeeee);
                        liste = new ArrayList<String>();
                        konumlar = new ArrayList<String>();
                        sanatciii = new ArrayList<String>();
                        sureee = new ArrayList<String>();
                        for (Map.Entry<String, String> entry : sorted.entrySet()) {
                            liste.add(entry.getValue());
                            konumlar.add(konumlarrrr.get(entry.getKey()));
                            sanatciii.add(sanatciiii.get(entry.getKey()));
                            sureee.add(sureeee.get(entry.getKey()));
                        }
                        /*for(int ik=0;ik<uzuu-1;ik++){
                            for(int il=0;il<uzuu-1-ik;il++){
                                Log.d("TAG", "ik: "+ik+" il: "+il);
                                if(liste.get(il).compareToIgnoreCase(liste.get(il+1))>=0){
                                    String konu=null;
                                    String konui=null;
                                    konu=konumlar.get(il);
                                    konui=konumlar.get(il+1);
                                    konumlar.remove(il);
                                    konumlar.add(il,konui);
                                    konumlar.remove(il+1);
                                    konumlar.add(il+1,konu);
                                    String aka =null;
                                    String akai=null;
                                    aka=liste.get(il);
                                    akai=liste.get(il+1);
                                    liste.remove(il);
                                    liste.add(il,akai);
                                    liste.remove(il+1);
                                    liste.add(il+1,aka);
                                    String boka =null;
                                    String bokai=null;
                                    boka=sanatciii.get(il);
                                    bokai=sanatciii.get(il+1);
                                    sanatciii.remove(il);
                                    sanatciii.add(il,bokai);
                                    sanatciii.remove(il+1);
                                    sanatciii.add(il+1,boka);
                                    String kaka =null;
                                    String kakai=null;
                                    kaka=sureee.get(il);
                                    kakai=sureee.get(il+1);
                                    sureee.remove(il);
                                    sureee.add(il,kakai);
                                    sureee.remove(il+1);
                                    sureee.add(il+1,kaka);
                                }
                            }
                        }*/
                        uzuu = liste.size();
                        for(int as=0;as<uzuu;as++){
                            listee.add(new OzelListe(liste.get(as),sureee.get(as),sanatciii.get(as),konumlar.get(as)));
                        }
                        ListeAdap la = new ListeAdap(MainActivity.this,listee);
                        if(sarki!=null) {
                            for (int ad = 0; ad < liste.size(); ad++) {
                                if (sarki.equals(liste.get(ad))) {
                                    w = ad;
                                    ad = liste.size();
                                }
                            }
                        }
                        lw.setAdapter(la);
                        ll3.setVisibility(View.INVISIBLE);
                        ll2.setVisibility(View.VISIBLE);
                        liste2=null;
                        konumlar2=null;
                        sureee=null;
                        albummm=null;
                        adlarrr=null;
                        sureee2=null;
                        sanatciii2=null;
                    }
                });
            }
        });
    }
}
