package com.okq.rxdevice.execute

import android.content.Context
import com.okq.rxdevice.serialPort.SerialPort
import rx.Observable
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by zst on 2016-11-17  0017.
 * 描述:
 */
class RsExecute(val baud: Int = 9600) : Execute(baud) {
    lateinit override var ctx: Context

    override var isStart: Boolean = false

    lateinit var serialPort: SerialPort

    override fun open(ctx: Context) {
        serialPort = SerialPort(File("/dev/ttyS4"), baud, 0)
        isStart = serialPort.isOpen
    }

    override fun send(msg: String) {
        serialPort.sendData(msg)
    }

    override @Synchronized fun receiveData(timeout: Long, interval: Long): String {
        var ret = ""
        var wait = true
        Observable.interval(interval, TimeUnit.MILLISECONDS)//每 interval ms检测一次是否收到数据
                .map {
                    serialPort.receiveData()
                }
                .filter(String::isNotBlank)
                .timeout(timeout, TimeUnit.MILLISECONDS)
                .first()
                .subscribe(
                        { data ->
                            ret = data
                        },
                        { e ->
                            wait = false
                        },
                        {
                            wait = false
                        }
                )
        while (wait){
        }

        return ret
    }

    override fun close() {
        serialPort.close()
    }

}