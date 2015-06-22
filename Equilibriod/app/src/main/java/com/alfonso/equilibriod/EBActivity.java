package com.alfonso.equilibriod;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.alfonso.equilibriod.modelo.HModelD;


public class EBActivity extends Activity {


    private static final String TAG = EBActivity.class.getSimpleName();

    HModelD hm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new MainGamePanel(this));
        //crea el modelo
        hm = new HModelD(this);

        Log.d(TAG, "Vista añadida");
    }



    @Override
    protected  void onDestroy(){
        Log.d(TAG,"Destruyendo");
        super.onDestroy();
    }


    @Override
    protected void onStop(){
        Log.d(TAG,"Deteniendo");
        super.onStop();
    }
}
