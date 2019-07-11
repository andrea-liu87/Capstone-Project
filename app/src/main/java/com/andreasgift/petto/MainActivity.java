package com.andreasgift.petto;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andreasgift.petto.data.PetContract;
import com.andreasgift.petto.data.PetProviderHelper;
import com.andreasgift.petto.model.Pet;
import com.andreasgift.petto.ui.AnimateView;
import com.andreasgift.petto.ui.FragmentAttributes;
import com.andreasgift.petto.ui.ProgressBarService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.veer.hiddenshot.HiddenShot;

public class MainActivity extends AppCompatActivity {
    private static AnimateView spriteView;
    private FrameLayout sprite_container;

    private Pet pet;

    private MediaPlayer mediaPlayer;

    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23){
        requestPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);}

        MobileAds.initialize(this, "ca-app-pub-9870070910946608~7026798035");
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        sprite_container = findViewById(R.id.sprite_container);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        musicBackgroundTask(this);

        setUI(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (spriteView != null){
        spriteView.pause();}
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (spriteView != null){
        spriteView.resume();}
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create_pet){
            Pet pet = new Pet("Choco");
            PetProviderHelper.createPet(getContentResolver(), pet);
            setPetAttributesScheduler(this,0);
            setUI(0);
        }
        if (item.getItemId() == R.id.screenshoot){
            HiddenShot.getInstance().buildShotAndShare(MainActivity.this);
        }
        if (item.getItemId() == R.id.delete_pet) {
            PetProviderHelper.deleteAllPet(getContentResolver());
            setPetAttributesScheduler(this,1);
            setUI(1);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method function is to display the UI when the pet is created or deleted.
     * It consist display FragmentAttributes for pet attributes and AnimateView for pet character
     * @param mode 0 : when pet is created ; 1: when pet is deleted
     */
    private void setUI (int mode) {
        pet = PetProviderHelper.getPet(getContentResolver());

        if (pet != null && mode == 0) {
            spriteView = new AnimateView(getApplicationContext());
            spriteView.getHolder().setFixedSize((int)spriteView.screenWidth, spriteView.bitmapHeight);
            spriteView.setZOrderOnTop(true);
            spriteView.getHolder().setFormat(PixelFormat.TRANSPARENT);
            spriteView.resume();
            sprite_container.addView(spriteView);

            FragmentAttributes fragmentAttributes = new FragmentAttributes();
            fragmentAttributes.setAttributes(pet.getHunger(), pet.getCleanliness(), pet.getHappiness());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragmentAttributes)
                        .addToBackStack(null)
                        .commit();}

        if (mode == 1) {
                getSupportFragmentManager().popBackStack();
                sprite_container.removeView(spriteView);
            }
        }


    /**
     * This method function is to automatically reduce the pet attributes over time
     * Hunger attributes -1 for every 5 hour
     * Cleanliness and happiness attributes -1 for every 6 hour
     * @param context method context
     * @param mode      0 = pet created; 1 = pet deleted
     */
    private void setPetAttributesScheduler(Context context, int mode){
        Intent hungerIntent = new Intent(context, ProgressBarService.class);
        hungerIntent.putExtra(PetContract.ATTRIBUTES_KEY,PetContract.HUNGER_IND);
        PendingIntent hungerPendingIntent = PendingIntent.getService(context, 0, hungerIntent, 0);

        Intent cleanlinessIntent = new Intent(context, ProgressBarService.class);
        cleanlinessIntent.putExtra(PetContract.ATTRIBUTES_KEY, PetContract.CLEANLINESS_IND);
        PendingIntent clealinessPendgInt = PendingIntent.getService(context,0, cleanlinessIntent,0);

        Intent happinsIntent = new Intent(context, ProgressBarService.class);
        happinsIntent.putExtra(PetContract.ATTRIBUTES_KEY, PetContract.HAPPINESS_IND);
        PendingIntent happnssPndgIntt = PendingIntent.getService(context, 0, happinsIntent, 0);

        //For now testing hunger bar every 1 minute drop -1 but other atributes has been set per designated hour
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (mode == 0){
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                1*60*1000,
                hungerPendingIntent
        );
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                6*60*60*1000,
                clealinessPendgInt
        );
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                6*60*60*1000,
                happnssPndgIntt
        );}
        else {
            alarmManager.cancel(hungerPendingIntent);
            alarmManager.cancel(clealinessPendgInt);
            alarmManager.cancel(happnssPndgIntt);}
    }

    /**
     * Show happy cat jumping on the screen when the pet attributes is added
     */
    public static void showHappyCat(){
        if (spriteView != null) {
            spriteView.drawHappyCat();
        }
    }

    /**
     *  Request app permission for API 23/ Android 6.0
     * @param permission
     */
    private void requestPermission(String permission){
        int MY_PERMISSIONS_REQUEST = 99;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }
    }


    /**
     * Play music background
     * @param context
     */
    private void musicBackgroundTask(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.backmusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
}
