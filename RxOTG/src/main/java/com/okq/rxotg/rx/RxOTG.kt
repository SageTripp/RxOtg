package com.okq.rxotg.rx

import android.content.Context
import com.okq.rxotg.OtgConfig
import com.okq.rxotg.execute.OTGExecute
import com.okq.rxotg.observable.ExecuteObservable
import com.okq.rxotg.observable.MultipleExecuteObservable
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by zst on 2016-10-11  0011.
 * 描述:
 */
object RxOTG {

    private lateinit var config: OtgConfig

    @JvmStatic fun main(args: Array<String>) {
//        Observable.from(Array(8, { i -> "haha$i" }))
//                .flatMap { msg ->
//                    Observable.create<String> { subscriber ->
//                        Thread.sleep(1000)
//                        subscriber.onNext("${System.currentTimeMillis()}-->$msg 转换")
//                        Thread.sleep(200)
//                        subscriber.onNext("${System.currentTimeMillis()}-->$msg 转换2")
//                        subscriber.onCompleted()
//                    }
//                }.subscribe({ data ->
//            println("输出:$data")
//        }, Throwable::printStackTrace, {
//            println("结束了")
//        })

        tt("aa").subscribe({ data ->
            print("${System.currentTimeMillis() / 1000}==> 输出:$data\n")
        }, Throwable::printStackTrace, {
            print("${System.currentTimeMillis() / 1000}==> 结束 aa\n")
        })

        Thread({
            tt("bb", 2)
                    .observeOn(Schedulers.newThread())
                    .subscribe({ data ->
                        print("${System.currentTimeMillis() / 1000}==> 输出:$data\n")
                    }, Throwable::printStackTrace, {
                        print("${System.currentTimeMillis() / 1000}==> 结束 bb\n")
                    })
        }).run()

        Thread({
            tt("cc").subscribe({ data ->
                print("${System.currentTimeMillis() / 1000}==> 输出:$data\n")
            }, Throwable::printStackTrace, {
                print("${System.currentTimeMillis() / 1000}==> 结束 cc\n")
            })
        }).run()

        Thread({
            Observable.from(Array(8, { i -> "dd$i" }))
                    .flatMap { msg ->
                        tt(msg, 2)
                    }.observeOn(Schedulers.newThread())
                    .subscribe({ data ->
                        print("${System.currentTimeMillis() / 1000}==> 输出:$data\n")
                    }, Throwable::printStackTrace, {
                        print("${System.currentTimeMillis() / 1000}==> 结束 dd\n")
                    })
        }).run()
    }

    /**
     * OTG配置
     */
    @JvmStatic fun config(ctx: Context): OtgConfig {
        config = OtgConfig
        OTGExecute.open(ctx)
        return config
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

    @JvmStatic fun tt(msg: String, count: Int = 1): Observable<String> {
        return Observable.create { subscriber ->
            for (i in 1..count) {
                Thread.sleep(1000)
                subscriber.onNext("${System.currentTimeMillis()}-->$msg 转换$i")
            }
            subscriber.onCompleted()
        }
    }

}