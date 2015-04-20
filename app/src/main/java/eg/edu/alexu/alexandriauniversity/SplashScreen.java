package eg.edu.alexu.alexandriauniversity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

import eg.edu.alexu.alexandriauniversity.activity.ChannelsActivity;

public class SplashScreen extends Activity {
    private static boolean splashLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!splashLoaded) {
            setContentView(R.layout.activity_splash_screen);
            splashLoaded = true;

            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreen.this, AlexUListActivity.class));
                    finish();
                }
            }, secondsDelayed * 500);
        } else {
            Intent goToMainActivity = new Intent(this, AlexUListActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}