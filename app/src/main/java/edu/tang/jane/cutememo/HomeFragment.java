package edu.tang.jane.cutememo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;



public class HomeFragment extends Fragment implements DownloadListener {
    private TextView mProgress;
    private TextView mTitle;
    private Button mDownloadBtn;
    private SurfaceView mSurface;
    private MainActivity mParent;
    private MediaPlayer mPlayer;
    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (mPlayer != null) {
                mPlayer.setDisplay(surfaceHolder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = new MediaPlayer();
                mDownloadBtn.setText("Download & Play");
                mDownloadBtn.setClickable(true);
                mTitle.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParent = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.activity_home_tab, container, false);
        mProgress = v.findViewById(R.id.video_progress);
        mTitle = v.findViewById(R.id.video_title);
        mSurface = v.findViewById(R.id.video_view);
        mPlayer = new MediaPlayer();
        mSurface.getHolder().addCallback(mCallback);

        mDownloadBtn = v.findViewById(R.id.video_btn);

        final EditText urlEt = v.findViewById(R.id.editText);
        urlEt.setText(MainActivity._MP4_PATH);
        mDownloadBtn.setText("Download & Play");
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Log.e("ttt", urlEt.getText().toString());
                                                mParent.startDownload(urlEt.getText().toString(), HomeFragment.this);
                                                mProgress.setVisibility(View.VISIBLE);
                                                mProgress.setText("0%");
                                                mTitle.setVisibility(View.GONE);
                                                mDownloadBtn.setText("Downloading...");

                                                /*if (ContextCompat.checkSelfPermission(mParent,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                                        != PackageManager.PERMISSION_GRANTED)
                                                {

                                                    ActivityCompat.requestPermissions(mParent,
                                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                            100);
                                                } else
                                                {
                                                    HomeFragment.this.onFinish("/sdcard/demo.mp4", null);
                                                }*/
                                            }
                                        }
        );

        return v;
    }

    @Override
    public void onProgress(int progress) {
        if (progress > 100) progress = 100;
        mProgress.setText(progress + "%");
    }

    @Override
    public void onFinish(String path, String err) {
        mProgress.setVisibility(View.GONE);
        if (err != null) {
            Toast.makeText(mParent, err, Toast.LENGTH_SHORT).show();
            mDownloadBtn.setText("Download & Play");
        } else {
            mDownloadBtn.setText("Playing");
            mDownloadBtn.setClickable(false);

            try {
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mPlayer.start();
                    }
                });

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mDownloadBtn.setText("Download & Play");
                        mDownloadBtn.setClickable(true);
                        mTitle.setVisibility(View.VISIBLE);
                        mPlayer.stop();
                        mPlayer.release();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                mDownloadBtn.setText("Download & Play");
                mDownloadBtn.setClickable(true);
            }
        }
    }
}
