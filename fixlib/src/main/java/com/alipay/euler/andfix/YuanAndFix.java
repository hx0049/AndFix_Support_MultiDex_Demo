package com.alipay.euler.andfix;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hx on 2016/10/19.
 */

public class YuanAndFix {
    private static final String TAG = "YuanAndFix";
    public static final String apatch_name = "out.apatch";//"out.apatch";

    public static void inject(Context context,Boolean fromLocal){
        final String patchPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + apatch_name;
        File patchFile = new File(patchPath);
        try {
            if(patchFile.exists()){
                PatchManager patchManager = new PatchManager(context);
                patchManager.init(BuildConfig.VERSION_NAME + "");//current version
                patchManager.loadPatch();
                patchManager.addPatch(patchPath);
            }else{
                Toast.makeText(context, "补丁文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void inject(final Context context) {
        if (!readPatchState(context)) {
            return;
        }
        final String patchPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + apatch_name;
        try{
            File file = new File(patchPath);
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            Toast.makeText(context, "创建文件失败", Toast.LENGTH_SHORT).show();
        }
        final String urlStr = "http://ofe46he1s.bkt.clouddn.com/" + BuildConfig.VERSION_CODE + apatch_name;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        String result = download(urlStr, patchPath);
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            PatchManager patchManager = new PatchManager(context);
                            patchManager.init(BuildConfig.VERSION_NAME + "");//current version
                            patchManager.loadPatch();
                            try {
                                File file = new File(patchPath);
                                if (file.exists()) {
                                    try {
                                        patchManager.addPatch(patchPath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (NoClassDefFoundError e){
                                        Log.e(TAG, "onNext: 补丁没有找到类的说");
                                    }
                                    savePatchState(context);
                                    Log.d(TAG, "打补丁完成");
                                    Toast.makeText(context, "文件下载成功", Toast.LENGTH_SHORT).show();
                                    file.delete();
                                } else {
                                    Log.d(TAG, "文件不存在，打补丁失败");
                                    Toast.makeText(context, "文件下载失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    private static synchronized boolean readPatchState(Context context) {
        SharedPreferences sp = context.getSharedPreferences("patch_state", Context.MODE_PRIVATE);
        return sp.getBoolean(BuildConfig.VERSION_CODE + "", true);
    }

    private static void savePatchState(Context context) {
        SharedPreferences sp = context.getSharedPreferences("patch_state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(BuildConfig.VERSION_CODE + "", false);
        editor.commit();
    }


    static String download(String urlStr, String fileName) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.connect();
             /* 下载文件的大小 */
            int fileOfLength = conn.getContentLength();
                /* 每次下载的大小与总下载的大小 */
            int totallength = 0;
            int length = 0;
                /* 输入流 */
            InputStream in = conn.getInputStream();

            FileOutputStream out = new FileOutputStream(new File(fileName));
                /* 缓存模式，下载文件 */
            byte[] buff = new byte[1024 * 1024];
            while ((length = in.read(buff)) > 0) {
                totallength += length;
                String str1 = "" + (int) ((totallength * 100) / fileOfLength);
                out.write(buff, 0, length);
            }
                /* 关闭输入输出流 */
            in.close();
            out.flush();
            out.close();
            return fileName;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
