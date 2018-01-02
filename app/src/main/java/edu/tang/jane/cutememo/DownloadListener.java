package edu.tang.jane.cutememo;



public interface DownloadListener {
    void onProgress(int progress);
    void onFinish(String path, String err);
}
