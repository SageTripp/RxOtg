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
class MultipleExecuteObservable(val msg: String, val receiveCount: Int = 1, val delay: Long = 1, val config: OTGConfig = OTGConfig) : Observable.OnSubscribe<String>, ObservableHelper<String> {
    override fun call(subscriber: Subscriber<in String>) {
        checkConnect(subscriber)
        val c = execute(subscriber)
        if (c < receiveCount)
            subscriber.onError(Throwable("超时"))
        subscriber.onCompleted()
    }

    /**
     * 执行数据的发送以及接收
     */
    private fun execute(subscriber: Subscriber<in String>): Int {
        var count = 0 //标志接收数据次数
        var needTry = false //标志是够需要重试
        var i = 0
        do {
            needTry = !sendAndQuery(subscriber)
        } while (needTry && config.tryTimes > i++)
        if (needTry)
            return count

        count++
        if (receiveCount - 1 > 0) {
            for (c in 1..receiveCount) {
                if (query(subscriber)) {
                    count++
                } else {
                    return count
                }
            }
        }

        return count
    }

    /**
     * 查询
     */
    private fun query(subscriber: Subscriber<in String>): Boolean {
        var isComplete = false//标志是否完成
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
        while (!isComplete && c++ < delay / 100) {
            Thread.sleep(100)
        }
        //如果没有取消订阅则取消
        if (!s.isUnsubscribed)
            s.unsubscribe()
        return isComplete
    }

    /**
     * 发送并查询
     */
    private fun sendAndQuery(subscriber: Subscriber<in String>): Boolean {
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