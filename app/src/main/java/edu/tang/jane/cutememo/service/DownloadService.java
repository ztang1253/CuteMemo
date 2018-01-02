package edu.tang.jane.cutememo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    private boolean mDownloading;
    private int mTotalLen;
    private int mDownloadLen;
    private File mDownloadFile;
    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloading = false;
    }

    public int getProgress() {
        if (mTotalLen > 0) {
            return (int)((long)(mDownloadLen * 100) / mTotalLen);
        }

        return 0;
    }

    public boolean getStatus() {
        return mDownloading;
    }

    public String getDownloadPath() {
        if (mDownloadFile != null) {
            return mDownloadFile.getAbsolutePath();
        }

        return null;
    }

    public void startDownload(final String path) {
        if (!mDownloading) {
            mDownloading = true;

            new Thread() {
                @Override
                public void run() {
                    URL url;
                    HttpURLConnection connection;
                    try {
                        url = new URL(path);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(15000);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Charset", "utf-8");
                        connection.setRequestProperty("Accept-Encoding", "identity");
                        connection.connect();
                        String fileName = "demo.mp4";
                        mDownloadFile = new File(getFilesDir(), fileName);
                        if (mDownloadFile.exists()) {
                            mDownloadFile.delete();
                        }

                        FileOutputStream outputStream = new FileOutputStream(mDownloadFile);
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = connection.getInputStream();
                            mTotalLen = connection.getContentLength();
                            if (mTotalLen <= 0) mTotalLen = 126230; // the given video has not Content-Length value in server
                            BufferedInputStream bfi = new BufferedInputStream(inputStream);
                            int len;
                            mDownloadLen = 0;
                            byte[] bytes = new byte[1024];
                            while ((len = bfi.read(bytes)) != -1) {
                                mDownloadLen += len;
                                outputStream.write(bytes, 0, len);
                            }

                            outputStream.close();
                            inputStream.close();
                            bfi.close();
                            mDownloading = false;
                        } else {
                            mTotalLen = 1;
                            mDownloadLen = 0;
                            mDownloading = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mDownloading = false;
                    }
                }
            }.start();
        }
    };

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }
}
