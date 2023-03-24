package yasinaydin.muzik;

import android.graphics.Bitmap;

/**
 * Created by Bluestack on 6.2.2018.
 */

public class OzelListe{
    String isim;
    String sure;
    String sanatci;
    String konum;
    public OzelListe(String isimm, String suree, String sanatcii, String konumm){
        isim=isimm;
        sure=suree;
        sanatci=sanatcii;
        konum=konumm;
    }
    public void setIsim(String isimm){
        isim=isimm;
    }
    public String getIsim(){
        return isim;
    }
    public void setKonum(String konumm){
        konum=konumm;
    }
    public String getKonum(){
        return konum;
    }
    public void setSure(String suree){
        sure=suree;
    }
    public String getSure(){
        return sure;
    }
    public void setSanatci(String sanatcii){
        sanatci=sanatcii;
    }
    public String getSanatci(){
        return sanatci;
    }
}
