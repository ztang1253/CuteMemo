package edu.tang.jane.cutememo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import edu.tang.jane.cutememo.DB.JTDBManager;
import edu.tang.jane.cutememo.DB.JTMemoEntity;
import edu.tang.jane.cutememo.service.DownloadService;

public class MainActivity extends FragmentActivity {
    public static final String TAG_HOME = "home";
    public static final String TAG_TODAY = "today";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_DETAILS = "details";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_ABOUT = "about";

    public static final String _MP4_PATH = "http://www.sample-videos.com/video/mp4/240/big_buck_bunny_240p_2mb.mp4";//"https://drive.google.com/open?id=1KwyssmK9DxjYi22yaoDXXUh7s2IWXcIx";

    public int mScreenWidth;
    public int mScreenHeight;
    private TabHost mTabHost;
    private TabManager mTabManager;
    private Handler mHandler;
    private JTDBManager mDBM;
    private ArrayList<JTMemoEntity> mAllData;
    private JTMemoEntity mCurrentData;

    private View mFooter;
    private Animation mFooterShowAnim;
    private Animation mFooterHiddenAnim;
    private Runnable mFooterHideRunnable = new Runnable() {
        @Override
        public void run() {
            mFooter.setVisibility(View.VISIBLE);
            mFooter.startAnimation(mFooterHiddenAnim);
            mFooter.setVisibility(View.GONE);
        }
    };

    private DownloadService mService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) iBinder;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    public void addEntity(JTMemoEntity entity) {
        mDBM.add(entity);
        if (mAllData != null) {
            mAllData.add(entity);
        }
    }

    public void updateEntity(JTMemoEntity entity) {
        mDBM.update(entity, entity.date);
        if (mAllData != null) {
            int i = 0;
            for (; i < mAllData.size(); ++i) {
                if (entity.date == mAllData.get(i).date) {
                    mAllData.remove(i);
                    break;
                }
            }

            mAllData.add(i, entity);
        }
    }

    public void removeCurrentData() {
        if (mCurrentData != null) {
            mAllData.remove(mCurrentData);
            mDBM.delete(mCurrentData.date);
            mCurrentData = null;
        }
    }

    public void clearData() {
        mAllData.clear();
        mDBM.clearData();
    }

    public ArrayList<JTMemoEntity> getAllData() {
        if (mAllData == null) {
            mAllData = mDBM.searchAllData();
        }

        return mAllData;
    }

    public void setCurrentData(int i) {
        if (mAllData == null) {
            mAllData = mDBM.searchAllData();
        }

        if (i < mAllData.size()) {
            mCurrentData = mAllData.get(i);
        } else {
            mCurrentData = null;
        }
    }

    public JTMemoEntity getCurrentData() {
        return mCurrentData;
    }

    public void go2FragmentByTag(String tag) {
        if (tag == TAG_TODAY) {
            TodayFragment today = (TodayFragment) mTabManager.getFragmentByTag(TAG_TODAY);
            if (today != null) today.setEntity(mCurrentData);
        }

        if ((tag == TAG_DETAILS && mTabManager.getCurrentFragmentTag() == TAG_OVERVIEW) || (tag == TAG_OVERVIEW  && mTabManager.getCurrentFragmentTag() == TAG_DETAILS)) {
            mTabManager.replaceFragment(tag);
        } else if (tag == TAG_DETAILS && mTabManager.getCurrentFragmentTag() != TAG_OVERVIEW && mTabManager.getCurrentFragmentTag() != TAG_DETAILS) {
            mTabManager.highlightedTab(TAG_OVERVIEW);
            mTabManager.replaceFragment(tag);
        } else {
            mTabManager.highlightedTab(tag);
        }
    }

    public void startDownload(String url, final DownloadListener listener) {
        if (mService != null) {
            mService.startDownload(url);
        }

        if (listener == null || mHandler == null)
            return;

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (mService == null)
                    return;

                int progress = mService.getProgress();
                listener.onProgress(progress);
                if (!mService.getStatus()) {
                    if (progress < 100) {
                        listener.onFinish(mService.getDownloadPath(), "failed");
                    } else {
                        listener.onFinish(mService.getDownloadPath(), null);
                    }
                } else {
                    mHandler.postDelayed(this, 500);
                }
            }
        };

        mHandler.postDelayed(progressRunnable, 500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mFooter = findViewById(R.id.footer_part);

        View homeTab = (View) LayoutInflater.from(this).inflate(R.layout.tabbtn, null);
        TextView tv = (TextView) homeTab.findViewById(R.id.tabbtn_text);
        ImageView iv = (ImageView) homeTab.findViewById(R.id.tabbtn_image);
        tv.setText("Home");
        iv.setImageResource(R.drawable.home_white);

        View todayTab = (View) LayoutInflater.from(this).inflate(R.layout.tabbtn, null);
        tv = (TextView) todayTab.findViewById(R.id.tabbtn_text);
        iv = (ImageView) todayTab.findViewById(R.id.tabbtn_image);
        tv.setText("Today");
        iv.setImageResource(R.drawable.plus_white);

        View overviewTab = (View) LayoutInflater.from(this).inflate(R.layout.tabbtn, null);
        tv = (TextView) overviewTab.findViewById(R.id.tabbtn_text);
        iv = (ImageView) overviewTab.findViewById(R.id.tabbtn_image);
        tv.setText("Overview");
        iv.setImageResource(R.drawable.bullets_white);

        View locationTab = (View) LayoutInflater.from(this).inflate(R.layout.tabbtn, null);
        tv = (TextView) locationTab.findViewById(R.id.tabbtn_text);
        iv = (ImageView) locationTab.findViewById(R.id.tabbtn_image);
        tv.setText("Locations");
        iv.setImageResource(R.drawable.grid_white);

        View aboutTab = (View) LayoutInflater.from(this).inflate(R.layout.tabbtn, null);
        tv = (TextView) aboutTab.findViewById(R.id.tabbtn_text);
        iv = (ImageView) aboutTab.findViewById(R.id.tabbtn_image);
        tv.setText("About");
        iv.setImageResource(R.drawable.info_white);

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
        mTabManager.addTab(mTabHost.newTabSpec(TAG_HOME).setIndicator(homeTab), HomeFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAG_TODAY).setIndicator(todayTab), TodayFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAG_OVERVIEW).setIndicator(overviewTab), OverviewFragment.class, null);
        // Share indicator with overview fragment
        mTabManager.addTabWithoutSpec(mTabHost.newTabSpec(TAG_DETAILS).setIndicator(overviewTab), DetailsFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAG_LOCATION).setIndicator(locationTab), LocationFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAG_ABOUT).setIndicator(aboutTab), AboutFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics= new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mHandler = new Handler();
        mFooterHiddenAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttom_out);
        mFooterShowAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttom_in);

        mDBM = new JTDBManager(this);
        mCurrentData = null;
        hideFooterDelay(3000);

        bindService(new Intent(this, DownloadService.class), mConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mFooter.setVisibility(View.GONE);
        mFooter.startAnimation(mFooterShowAnim);
        mFooter.setVisibility(View.VISIBLE);
        hideFooterDelay(5000);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDBM != null) {
            mDBM.closeDB();
        }
    }

    private void hideFooterDelay(int ms) {
        mHandler.postDelayed(mFooterHideRunnable, ms);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //callPhone();
                HomeFragment today = (HomeFragment) mTabManager.getFragmentByTag(TAG_HOME);
                today.onFinish("/sdcard/demo.mp4", null);
            }
            else
            {

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
