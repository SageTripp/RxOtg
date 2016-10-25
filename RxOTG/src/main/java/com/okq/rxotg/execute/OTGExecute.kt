package com.okq.rxotg.execute

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.ftdi.j2xx.FT_Device
import com.okq.rxotg.utils.HexUtils
import com.okq.rxotg.utils.OtgException
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by zst on 2016-10-12  0012.
 * 描述:
 */
object OTGExecute {
    lateinit private var ctx: Context
    private var ftDev: FT_Device? = null
    lateinit private var d2xxManager: D2xxManager

    private var devCount: Int = -1
    private var openIndex: Int = -1
    private var currentIndex: Int = -1


    @JvmStatic var isStart = false

    /**
     * 打开OTG
     */
    @JvmStatic fun open(ctx: Context) {
        this.ctx = ctx
        if (!isStart) {
            isStart = true
            d2xxManager = D2xxManager.getInstance(OTGExecute.ctx)
            createDeviceList()
            if (devCount > 0) {
                connectFunction()
                SetConfig()
                ftDev?.restartInTask()
            }
        }
    }

    /**
     * 将数据发送到传感器
     */
    @JvmStatic fun send(msg: String) {
        if (!ftDev?.isOpen!!) {
            throw OtgException("OTG设备没有打开")
        }
        ftDev?.latencyTimer = 16.toByte()
        ftDev?.write(HexUtils.hexString2Bytes(msg))
    }

    @JvmStatic fun close() {
        disconnectFunction()
        isStart = false
    }

    /**
     * 接收数据,阻塞式方法
     * @return 传感器返回的数据
     */
    @JvmStatic fun receiveData(): Observable<String> {
        return Observable
                .interval(200, TimeUnit.MILLISECONDS)//每200ms检测一次是否收到数据
                .map {
                    ftDev?.queueStatus!! //将每一次的计数值映射为ftDev的收到的数据位数
                }
                .filter { isAvailable ->
                    isAvailable > 0 //对收到的数据个数进行过滤,如果数据个数小于等于0则被剔除掉
                }
                .map { isAvailable -> //最终映射为接收到的数据,以字符串的形式发射出去
                    val readData = ByteArray(isAvailable)
                    ftDev?.read(readData, isAvailable, 200)
                    //将接收到的数据转换为16进制字符串
                    val sb = StringBuilder()
                    for (b in readData) {
                        sb.append(String.format("%02X", b))
                    }
                    //返回转换完成后的16进制字符串
                    sb.toString()
                }
    }

    /**
     * 生成设备列表
     */
    private fun createDeviceList() {
        val tempDevCount = d2xxManager.createDeviceInfoList(ctx)

        if (tempDevCount > 0) {
            if (devCount != tempDevCount) {
                devCount = tempDevCount
                openIndex = tempDevCount - 1
            }
        } else {
            devCount = -1
            currentIndex = -1
        }
    }


    /**
     * 断开设备连接
     */
    private fun disconnectFunction() {
        try {
            Thread.sleep(50)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        if (ftDev != null) {
            synchronized(ftDev!!) {
                if (ftDev?.isOpen!!) {
                    ftDev?.close()
                }
            }
        }
    }


    /**
     * 连接设备
     */
    private fun connectFunction() {
        if (currentIndex != openIndex) {
            if (null == ftDev) {
                ftDev = d2xxManager.openByIndex(ctx, openIndex)
            } else {
                synchronized(ftDev!!) {
                    ftDev = d2xxManager.openByIndex(ctx, openIndex)
                }
            }
        } else {
            return
        }

        if (ftDev == null) {
            return
        }

        if (ftDev?.isOpen!!) {
            currentIndex = openIndex
        }
    }


    /**
     * 设置设备串口参数
     */
    private fun SetConfig() {
        if (!ftDev?.isOpen!!) {
            return
        }
        // configure our port
        // reset to UART mode for 232 devices
        ftDev?.setBitMode(0.toByte(), D2xxManager.FT_BITMODE_RESET)
        //设置波特率
        ftDev?.setBaudRate(OtgParam.baud)
        //设置停止位,数据位...
        ftDev?.setDataCharacteristics(OtgParam.data_bit, OtgParam.stop_bit, OtgParam.parity)
        ftDev?.setFlowControl(D2xxManager.FT_FLOW_RTS_CTS, 0x0b.toByte(), 0x0d.toByte())
        ftDev?.clrDtr()
        ftDev?.clrRts()
        ftDev?.setRts()

    }

    object OtgParam {
        /**
         * 波特率
         */
        @JvmStatic var baud = 9600
        /**
         * 数据位
         */
        @JvmStatic var data_bit = D2xxManager.FT_DATA_BITS_8
        /**
         * 停止位
         */
        @JvmStatic var stop_bit = D2xxManager.FT_STOP_BITS_1
        /**
         * 奇偶校验位
         */
        @JvmStatic var parity = D2xxManager.FT_PARITY_NONE

        /**
         * 使参数生效
         */
        @JvmStatic fun vaildate() {

        }
    }

}