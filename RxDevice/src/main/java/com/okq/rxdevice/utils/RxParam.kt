package com.okq.rxdevice.utils

import com.okq.rxdevice.execute.OTGExecute
import com.okq.rxdevice.execute.Execute

/**
 * Created by zst on 2016-11-17  0017.
 * 描述:
 */
object RxParam {
    @JvmStatic var execute: Execute = OTGExecute()
    @JvmStatic var timeout: Long = 1000
    @JvmStatic var tryTimes: Int = 1
}