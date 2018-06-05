package py.ideasweb.myfinances.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 05/12/16.
 */

public class Utilities {
    public static Integer version_code = 0;
    public static String blank ="";

    public Double getRedondeo(Double valor) {
        BigDecimal bd = new BigDecimal(valor);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public String toDouble(Double value ) {
        return new DecimalFormat("#").format(value);
    }


    public static String toStringFromDate(Date date ){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(date);
    }

    public static String toStringFromDoubleWithFormat(Double value ){
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        return  df.format(value);
    }

    public static String toStringFromFloatWithFormat(Float value ){
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        return  df.format(value);
    }

    public static String toStringFromIntWithFormat(Integer value ){
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        return  df.format(value);
    }

    //Comprueba que este conectado a la red
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            return false;
        }
        return true;
    }



    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }





    // redimensionar imagen
    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    public static TextWatcher numberFormat(final EditText editText){
        TextWatcher tw = new TextWatcher() {
            boolean isManualChange = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(".", "");
                    String reverseValue = new StringBuilder(value).reverse()
                            .toString();
                    StringBuilder finalValue = new StringBuilder();
                    for (int i = 1; i <= reverseValue.length(); i++) {
                        char val = reverseValue.charAt(i - 1);
                        finalValue.append(val);
                        if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                            finalValue.append(".");
                        }
                    }
                    isManualChange = true;
                    editText.setText(finalValue.reverse());
                    editText.setSelection(finalValue.length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        return tw;

    }


    public static void maskDate(final EditText editText){

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(tw);
    }


    public static String getUltSync(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Semaforo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("fecha", "");
    }

    public static  void setUltSync(Context context , String date){
        SharedPreferences SPUbicacion =context.getSharedPreferences("Semaforo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("fecha", date);
        editor.commit();
    }

    public static boolean purgreDB(Context context)  {

        Boolean ban = false;
        try{

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = getUltSync(context);
            UtilLogger.info("FECHA: " + date);
            if(!date.equals("")){
                //fecha database
                Date convertedDate = new Date();
                convertedDate = dateFormat.parse(date);

                //fecha actual
                Date d = new Date();
                CharSequence now  = DateFormat.format("dd/MM/yyyy ", d.getTime());

                int dias=(int) ((d.getTime()-convertedDate.getTime())/86400000);


                UtilLogger.info(now.toString() + " - " + date + " = " + dias );
                if(dias > 29){
                    ban = true;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ban;


    }



    private static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }


    /*Actualizacion del sistema disponible*/
    public static boolean updateAvailable(Context context){
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionNumber = pinfo.versionCode;
        System.out.println("Codigo de version" + versionNumber);
        System.out.println("Utilities code" + Utilities.version_code);
        if( Utilities.version_code > versionNumber &&  Utilities.version_code > 0){
            return true;
        }else{
            return false;
        }


    }


    public static String getCurrentDate(){

        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()).getTime());

        return s;
    }

    public static void  recordarFecha (Context context){
        SharedPreferences SPUbicacion = context.getSharedPreferences("Semaforo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("fecha_sinc", Utilities.getCurrentDate());
        editor.commit();
    }


    public static String fechaGuardada(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Semaforo", Context.MODE_PRIVATE);
        String fecha= "no";
        fecha = sharedPreferences.getString("fecha_sinc", "");
        return fecha;
    }




}
