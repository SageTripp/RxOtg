package com.okq.rxotg.observable

import com.okq.rxotg.OTGConfig
import com.okq.rxotg.execute.OTGExecute
import com.okq.rxotg.utils.OtgException
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

/**
 * Created by zst on 2016-10-11  0011.
 * 描述:
 */
class ExecuteObservable(val msg: String, val config: OTGConfig = OTGConfig) : Observable.OnSubscribe<String>, ObservableHelper<String> {
    override fun call(subscriber: Subscriber<in String>) {
        checkConnect(subscriber)
        var i = 0
        do {
            var b = execute(subscriber)
        } while (!b && config.tryTimes > i++)
        subscriber.onCompleted()
    }

    /**
     * 执行数据的发送以及接收
     */
    private fun execute(subscriber: Subscriber<in String>): Boolean {
        var isComplete = false//标志是否完成
        try {
            OTGExecute.send(msg)//将数据给发送出去
            println("发送 ===> $msg")
        } catch (e: OtgException) {
            subscriber.onError(e)
        }
        val s = OTGExecute.receiveData()
                .subscribeOn(Schedulers.newThread())
                .take(1)//设置接收命令次数
                .observeOn(Schedulers.newThread())//在新线程进行接收到数据后的处理
                .subscribe({ data ->//接收到数据并进行处理
                    subscriber.onNext(data)
                }, { e ->//异常处理
                    isComplete = true
                    subscriber.onError(e)
                }, {//完成
                    isComplete = true
                })
        var c = 0
        //当数据接收完成或者超出设置的超时时间后结束阻塞(循环)
        while (!isComplete && c++ < config.timeout / 100) {
            Thread.sleep(100)
        }
        //如果没有取消订阅则取消
        if (!s.isUnsubscribed)
            s.unsubscribe()
        return isComplete
    }
}