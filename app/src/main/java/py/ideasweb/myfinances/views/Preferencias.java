package py.ideasweb.myfinances.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import py.ideasweb.myfinances.R;
import py.ideasweb.myfinances.utils.UtilLogger;

public class Preferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
// then you use
        UtilLogger.info("PIN " + prefs.getBoolean("pin", true));
        UtilLogger.info("Usuario: " + prefs.getString("usuario", "nada"));

    }

}
