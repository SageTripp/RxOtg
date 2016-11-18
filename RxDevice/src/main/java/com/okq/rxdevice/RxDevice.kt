package com.okq.rxdevice

import android.content.Context
import com.okq.rxdevice.utils.RxParam
import com.okq.rxdevice.execute.Execute

/**
 * Created by zst on 2016-11-17  0017.
 * 描述:
 */
object RxDevice {
    @JvmStatic fun bindExecute(exe: Execute){
        RxParam.execute = exe
    }

    @JvmStatic fun config(timeout:Long,tryTimes:Int){
        RxParam.timeout = timeout
        RxParam.tryTimes = tryTimes
    }

    @JvmStatic fun start(ctx:Context){
        RxParam.execute.open(ctx)
    }

    @JvmStatic fun close(){
        RxParam.execute.close()
    }

}