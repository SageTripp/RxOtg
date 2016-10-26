package com.okq.rxotg.rx

import com.okq.rxotg.OTGConfig
import com.okq.rxotg.execute.OTGExecute
import com.okq.rxotg.observable.ExecuteObservable
import com.okq.rxotg.observable.MultipleExecuteObservable
import rx.Observable

/**
 * Created by zst on 2016-10-11  0011.
 * 描述:
 */
object RxOTG {

    private lateinit var config: OTGConfig

    /**
     * OTG配置
     */
    @JvmStatic fun config(): OTGConfig {
        config = OTGConfig
        return config
    }

    /**
     * OTG是否已经打开
     */
    @JvmStatic fun isOpen(): Boolean {
        return OTGExecute.isStart
    }

    /**
     * 执行OTG命令
     * @param msg 要执行的命令
     */
    @JvmStatic @Synchronized fun execute(msg: String): Observable<String> {
        return Observable.create(ExecuteObservable(msg, config))
    }

    /**
     * 执行OTG命令,此方法适用于需要多条回复的场景
     * @param msg 要执行的命令
     * @param count 等待回复的命令数量
     * @param delay 等待回复的时间 单位ms
     */
    @JvmStatic @Synchronized fun execute(msg: String, count: Int, delay: Long): Observable<String> {
        return Observable.create(MultipleExecuteObservable(msg, count, delay, config))
    }

}