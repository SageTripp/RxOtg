package com.okq.rxdevice

import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by zst on 2016-11-18  0018.
 * 描述:
 */
object Test {
    var s = ""
    val tt = object : Thread() {
        override fun run() {
            Thread.sleep(800)
            s = "sdfsd"
        }
    }
    @JvmStatic fun main(args: Array<String>) {
        rrr()
    }

    private fun rrr() {
        var getData = false
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .map {
                    println("map")
                    exe()
                }
                .filter(String::isNotBlank)
                .timeout(2000,TimeUnit.MILLISECONDS)
                .first()
                .subscribe(
                        { data ->
                            println("data = [$data]")
                            getData = true
                        }, Throwable::printStackTrace,
                        {
                            s = ""
                            println("结束")
                            getData = true
                        }
                )
        tt.start()
        while (!getData) {

        }
    }

    fun exe(): String {
        return s
    }
}