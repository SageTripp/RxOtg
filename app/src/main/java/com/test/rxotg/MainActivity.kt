package com.test.rxotg

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.okq.rxdevice.rx.RxEnvironment
import com.okq.rxdevice.serialPort.SerialPort
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        RxView.clicks(start)
//                //throttleFirst操作符作用:在一段时间内只发射第一次的值来进行处理,在这里使用意味着,300ms内只有第一次的点击操作才会被处理
//                .throttleFirst(300, TimeUnit.MILLISECONDS)//300ms防抖处理
//                .subscribe {
//                    RxMCU.query("aa", 2, 1000 * 16)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe({ data ->
//                                receive.append("收到:$data\n")
//                                println("收到:$data")
//                            }, { e ->
//                                receive.append("异常了\n")
//                                e.printStackTrace()
//                            }, {
//                                receive.append("--------------完成了-----------------\n")
//                                println("--------------完成了-----------------")
//                            })
//                }
//        ttt()
        thread {
            ttt()
            RxEnvironment.query("AA006400A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006401A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006402A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006403A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006404A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006401A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006400A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006401A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006402A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006403A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006404A5")
                    .subscribe(::println)
            RxEnvironment.query("AA006401A5")
                    .subscribe(::println)
        }

//        t()
    }

    private fun ttt() {
        RxEnvironment.query(arrayOf("AA006401A5", "AA006402A5", "AA006403A5", "AA006401A5", "AA006402A5", "AA006404A5", "AA006401A5", "AA006402A5", "AA006403A5", "AA006401A5", "AA006402A5", "AA006403A5", "AA006404A5", "AA006402A5", "AA006403A5", "AA006401A5", "AA006402A5", "AA006403A5", "AA006401A5", "AA006404A5", "AA006403A5", "AA006404A5", "AA006402A5", "AA006403A5", "AA006401A5", "AA006402A5", "AA006403A5"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    receive.append("$data\n")
                    println("收到 ===> [$data]")
                }, Throwable::printStackTrace, {
                    receive.append("完成了\n")
                    println("完成 ===>")
                })
    }

    fun t() {
        val serialPort = SerialPort(File("/dev/ttyS4"), 9600, 0)
        serialPort.sendData("AA006400A5")
        Observable.interval(100, TimeUnit.MILLISECONDS)//每 interval ms检测一次是否收到数据
                .map { i ->
                    println("i = [$i]")
                }
                .subscribeOn(Schedulers.io())
                .map {
                    println("map")
                    serialPort.receiveData()
                }
                .subscribeOn(Schedulers.newThread())
                .doOnNext { s ->
                    println("s = [$s]")
                }
                .filter(String::isNotBlank)
                .timeout(1000, TimeUnit.MILLISECONDS)
                .first()
                .subscribe(::println,::print,{ println("完了")})
    }
}
