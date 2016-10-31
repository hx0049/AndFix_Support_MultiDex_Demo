package test.andfix;

import android.support.multidex.MultiDexApplication;

import com.alipay.euler.andfix.YuanAndFix;

/**
 * Created by hx on 2016/10/31.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        YuanAndFix.inject(this,true);
    }
}
