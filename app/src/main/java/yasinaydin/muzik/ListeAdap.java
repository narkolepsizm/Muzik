package yasinaydin.muzik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static yasinaydin.muzik.MainActivity.tf;

/**
 * Created by Bluestack on 1.26.2018.
 */

public class ListeAdap extends BaseAdapter {
    private List<OzelListe> lis=null;
    private LayoutInflater li=null;
    private Typeface tff=null;
    Activity actt;
    private MediaMetadataRetriever mmt=new MediaMetadataRetriever();
    public ListeAdap(Activity act, List<OzelListe>ls){
        lis=ls;
        actt=act;
        li=(LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tff= Typeface.createFromAsset(act.getAssets(),"glober_book.otf");
    }
    @Override
    public int getCount() {
        return lis.size();
    }
    @Override
    public Object getItem(int position) {
        return lis.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gorunum=li.inflate(R.layout.elemanduzeni,null);
        TextView tw=(TextView)gorunum.findViewById(R.id.muzikadi);
        TextView tw2=(TextView)gorunum.findViewById(R.id.sanatciadi);
        TextView tw3=(TextView)gorunum.findViewById(R.id.sure);
        ImageView iw=(ImageView)gorunum.findViewById(R.id.kapakfoto);
        OzelListe ele=lis.get(position);
        tw.setText(ele.getIsim());
        tw.setTypeface(tff);
        tw2.setTypeface(tff);
        tw3.setTypeface(tff);
        tw3.setText(ele.getSure());
        tw2.setText(ele.getSanatci());
        /*mmt.setDataSource(ele.getKonum());
        byte[] data = mmt.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            iw.setImageBitmap(bitmap);
        } else {
            iw.setImageResource(R.drawable.music);
        }*/
        //deneme
        iw.setImageResource(R.drawable.music);
        return gorunum;
    }
}
