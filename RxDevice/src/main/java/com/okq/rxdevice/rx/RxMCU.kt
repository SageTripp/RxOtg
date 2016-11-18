package com.okq.rxdevice.rx

import com.okq.rxdevice.utils.RxParam
import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Created by zst on 2016-10-19  0019.
 * 描述:
 */
object RxMCU {

    /**
     * 单条命令信息查询
     */
    @JvmStatic @Synchronized fun query(msg: String): Observable<String> {
        return RxParam.execute.execute(msg)
    }

    /**
     * 单条命令信息查询
     */
    @JvmStatic @Synchronized fun query(msg: String, count: Int = 2, delay: Long = 1000): Observable<String> {
        return RxParam.execute.execute(msg, count, delay)
    }
}