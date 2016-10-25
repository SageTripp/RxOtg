package com.test.rxotg

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding.view.RxView
import com.okq.rxotg.rx.RxEnvironment
import com.okq.rxotg.rx.RxMCU
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxView.clicks(start)
                //throttleFirst操作符作用:在一段时间内只发射第一次的值来进行处理,在这里使用意味着,300ms内只有第一次的点击操作才会被处理
                .throttleFirst(300, TimeUnit.MILLISECONDS)//300ms防抖处理
                .subscribe {
                    RxMCU.query("aa", 2, 1000 * 16)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ data ->
                                receive.append("收到:$data\n")
                                println("收到:$data")
                            }, { e ->
                                receive.append("异常了\n")
                                e.printStackTrace()
                            }, {
                                receive.append("--------------完成了-----------------\n")
                                println("--------------完成了-----------------")
                            })
                }
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
}
