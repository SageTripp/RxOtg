package com.okq.rxotg.observable

import com.okq.rxotg.execute.OTGExecute
import rx.Subscriber

/**
 * Created by zst on 2016-10-12  0012.
 * 描述:
 */
interface ObservableHelper<T> {
    fun checkConnect(subscriber: Subscriber<in T>) {
        if (!OTGExecute.isStart)
            subscriber.onError(Throwable("OTG未打开,请先打开OTG"))
    }
}