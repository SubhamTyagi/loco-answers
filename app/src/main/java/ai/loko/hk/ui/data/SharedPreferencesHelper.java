package ai.loko.hk.ui.data;

import android.content.Context;
import android.content.SharedPreferences;

import ai.loko.hk.ui.constants.Constant;

public class SharedPreferencesHelper {
      private static final String ClipBounds="ClipBounds";
    private static SharedPreferencesHelper instance;
    SharedPreferences sharedPref;
    private Context context;

    public static SharedPreferencesHelper getInstance(){
        if (instance==null) return new SharedPreferencesHelper();
        return instance;
    }


    public SharedPreferencesHelper setContext(Context context) {
        this.context = context;
        sharedPref=context.getSharedPreferences(ClipBounds,Context.MODE_PRIVATE);
        return this;
    }

    public boolean setClipBounds(float[] box){
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putFloat(Constant.X1_FLOAT,box[0]);
        editor.putFloat(Constant.Y1_FLOAT,box[1]);
        editor.putFloat(Constant.X2_FLOAT,box[2]);
        editor.putFloat(Constant.Y2_FLOAT,box[3]);
        return editor.commit();

    }
    public float[] getClipBounds(){
        float []box=new float[4];
        box[0]=sharedPref.getFloat(Constant.X1_FLOAT,0f);
        box[1]=sharedPref.getFloat(Constant.Y1_FLOAT,0f);
        box[2]=sharedPref.getFloat(Constant.X2_FLOAT,0f);
        box[3]=sharedPref.getFloat(Constant.Y2_FLOAT,0f);
        return box;
    }


}
