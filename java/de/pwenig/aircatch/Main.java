package de.pwenig.aircatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnHoverListener;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Activity{

    private List<List<Integer>> Hidden;
    private int width;
    private int height;
    private int count = 0;
    private int space = 20;
    private boolean breakpoint = false;
    private TextView tv;
    private TextView tv2;
    private RelativeLayout it;
    private long tstamp;
    int hhe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);//

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.adView);

        AdView adView;
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7889997852695519/5530979788");

        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        // Optionally populate the ad request builder.
        //adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        // Add the AdView to the view hierarchy.
        layout.addView(adView);

        // Start loading the ad.
        adView.loadAd(adRequestBuilder.build());

        hhe = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        tstamp = System.currentTimeMillis()/1000;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Hidden = getPoints();

        tv = (TextView) findViewById(R.id.text);
        tv2 = (TextView) findViewById(R.id.count);
        it = (RelativeLayout) findViewById(R.id.it);
        //RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);

        it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breakpoint) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

        it.setOnHoverListener(new OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {
                if(!breakpoint) {
                    tv.setText((int)event.getX() + " x " + (int)event.getY());
                    for (int i = 0; i < 5; i++) {
                        if ((event.getX() < getSaved(i, 0)+space && event.getX() > getSaved(i, 0)-space) && (event.getY() < getSaved(i, 1)+space && event.getY() > getSaved(i, 1)-space) && getSaved(i, 2) == 0) {
                            switch (count) {
                                case 0:
                                    tv2.setText("1 / 5");
                                    it.setBackgroundColor(Color.parseColor("#AA66CC"));
                                    break;
                                case 1:
                                    tv2.setText("2 / 5");
                                    it.setBackgroundColor(Color.parseColor("#99CC00"));
                                    break;
                                case 2:
                                    tv2.setText("3 / 5");
                                    it.setBackgroundColor(Color.parseColor("#FFBB33"));
                                    break;
                                case 3:
                                    tv2.setText("4 / 5");
                                    it.setBackgroundColor(Color.parseColor("#FF4444"));
                                    break;
                                case 4:
                                    tv2.setText("5 / 5");
                                    it.setBackgroundColor(Color.parseColor("#33B5E5"));
                                    tv.setText("Your time:\n" + ((System.currentTimeMillis() / 1000) - tstamp) + " sec");
                                    breakpoint = true;
                                    break;
                            }
                            setSaved(i, 2, 1);
                            count++;
                        }
                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    protected int randMetrics(int max, int min){
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    protected List<List<Integer>> getPoints(){
        List<List<Integer>> points = new ArrayList<List<Integer>>();
        for(int i = 0; i < 5; i++) {
            points.add(i, getObj());
        }
        return points;
    }

    protected List<Integer> getObj(){
        List<Integer> obj = new ArrayList<Integer>();
        obj.add(0, randMetrics(width-160, 80));
        obj.add(1, randMetrics(height-(80+hhe), 80));
        obj.add(2, 0);
        return obj;
    }

    protected int getSaved(int big, int lil){
        List<Integer> obj = Hidden.get(big);
        return obj.get(lil);
    }

    protected void setSaved(int big, int lil, int val){
        List<Integer> obj = Hidden.get(big);
        obj.set(lil, val);
        Hidden.set(big, obj);
    }
}
