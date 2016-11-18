package com.okq.rxdevice.execute

import android.content.Context
import com.okq.rxdevice.utils.RxParam
import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Created by zst on 2016-11-14  0014.
 * 描述:
 */
abstract class Execute(baud: Int = 9600) {
    abstract var ctx: Context
    abstract var isStart: Boolean

    abstract fun open(ctx: Context)
    abstract protected fun send(msg: String)
    abstract protected fun receiveData(timeout: Long, interval: Long = 100): String
    abstract fun close()

    val thread: Scheduler = Schedulers.newThread()


    private var trys: Int = 0

    @Synchronized fun query(msg: String, timeout: Long, interval: Long = 100): String {
        if (!isStart) {
            throw IllegalStateException("设备没有连接或打开")
        }
        send(msg)
        println("发送:[$msg]")
        val receiveData = receiveData(timeout, interval)
        if (receiveData.isEmpty() && RxParam.tryTimes >= trys++) {
            return query(msg, timeout, interval)
        } else if (receiveData.isNotEmpty()) {
            trys = 0
            return receiveData
        } else {
            trys = 0
            return ""
        }
    }

    /**
     * 执行OTG命令
     * @param msg 要执行的命令
     */
    @Synchronized fun execute(msg: String): Observable<String> {
        return Observable.just(RxParam.execute.query(msg, RxParam.timeout)).subscribeOn(thread)
    }

    /**
     * 执行OTG命令,此方法适用于需要多条回复的场景
     * @param msg 要执行的命令
     * @param count 等待回复的命令数量
     * @param delay 等待回复的时间 单位ms
     */
    @Synchronized fun execute(msg: String, count: Int, delay: Long): Observable<String> {
        return Observable.just("")
    }

}