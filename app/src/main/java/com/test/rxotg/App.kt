package com.test.rxotg

import android.app.Application
import com.okq.rxotg.rx.RxOTG

/**
 * Created by zst on 2016-10-15  0015.
 * 描述:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxOTG.config(applicationContext)
                .tryTimes(1)
                .timeout(1000)
                .setParam()
                .build()
    }
}