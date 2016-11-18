package com.okq.rxdevice.rx

import com.okq.rxdevice.utils.RxParam
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
    @JvmStatic @Synchronized fun query(msg: String): Observable<String> {
        return RxParam.execute.execute(msg)
    }

    /**
     * 多条环境信息查询
     */
    @JvmStatic @Synchronized fun query(msgs: Array<String>): Observable<String> {
        return Observable.from(msgs)
//                .distinct()//去除重复
                .flatMap { msg ->
                    query(msg)
                }
    }
}