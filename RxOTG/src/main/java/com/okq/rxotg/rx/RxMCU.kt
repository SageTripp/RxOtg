package com.okq.rxotg.rx

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
    @JvmStatic @Synchronized fun query(msg: String, scheduler: Scheduler = Schedulers.newThread()): Observable<String> {
        return RxOTG.execute(msg).subscribeOn(scheduler)
    }

    /**
     * 单条命令信息查询
     */
    @JvmStatic @Synchronized fun query(msg: String, count: Int = 2, delay: Long = 1000, scheduler: Scheduler = Schedulers.newThread()): Observable<String> {
        return RxOTG.execute(msg, count, delay).subscribeOn(scheduler)
    }
}