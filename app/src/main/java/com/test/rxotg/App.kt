package com.test.rxotg

import android.app.Application
import com.okq.rxdevice.RxDevice
import com.okq.rxdevice.execute.RsExecute

/**
 * Created by zst on 2016-10-15  0015.
 * 描述:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxDevice.bindExecute(RsExecute())
        RxDevice.config(1000,1)
        RxDevice.start(applicationContext)
    }
}