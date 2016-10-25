package com.okq.rxotg.rx

import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Created by zst on 2016-10-14  0014.
 * 描述:
 */
object RxEnvironment {


    /**
     * 单条环境信息查询
     */
    @JvmStatic @Synchronized fun query(msg: String, scheduler: Scheduler = Schedulers.newThread()): Observable<String> {
        return RxOTG.execute(msg).subscribeOn(scheduler)
    }

    /**
     * 多条环境信息查询
     */
    @JvmStatic @Synchronized fun query(msgs: Array<String>, scheduler: Scheduler = Schedulers.newThread()): Observable<String> {
        return Observable.from(msgs)
                .subscribeOn(scheduler)
//                .distinct()//去除重复
                .flatMap { msg ->
                    query(msg, Schedulers.immediate())
                }
    }
}