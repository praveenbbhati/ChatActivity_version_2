package com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference;

import android.app.Application;
import android.content.Context;


public class SharedApplication extends Application {

    public static Context context;

    /**
     * The instance.
     */
    private static SharedApplication mInstance = null;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        mInstance = this;
        context = getApplicationContext();
       /* try {
            FirebaseApp.initializeApp(this);
        }
        catch (Exception e) {
        }*/
        super.onCreate();
//        Crashlytics crashlyticsKit = new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                .build();
//        Fabric.with(this, crashlyticsKit);
//        FirebaseAnalyticsPaypro.initFirebase(this);
    }

    public static Context getApplicationInstance() {
        return mInstance;
    }

    /**
     * Gets the app instance.
     *
     * @return the app instance
     */
    public static SharedApplication getAppInstance() {
        return mInstance;
    }

}
