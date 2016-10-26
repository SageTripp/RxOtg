package com.okq.rxotg

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.okq.rxotg.execute.OTGExecute

/**
 * Created by zst on 2016-10-11  0011.
 * 描述:
 */
object OTGConfig {

    var tryTimes: Int = 1
    var timeout: Long = 1000L

    /**
     * 设置重试次数
     * @param times 重试次数
     */
    @JvmStatic fun tryTimes(times: Int): OTGConfig {
        this.tryTimes = times
        return this
    }

    /**
     * 设置超时时间
     * @param timeout 超时时间
     */
    @JvmStatic fun timeout(timeout: Long): OTGConfig {
        this.timeout = timeout
        return this
    }

    /**
     * 设置OTG传输参数 参考[D2xxManager]
     *
     * [baud] 波特率 默认:9600
     *
     * [data_bit] 数据位 默认:8
     *
     * [stop_bit] 停止位 默认:0
     *
     * [parity] 奇偶校验位 默认:0
     */
    fun setParam(baud: Int = 9600,
                 data_bit: Byte = D2xxManager.FT_DATA_BITS_8,
                 stop_bit: Byte = D2xxManager.FT_STOP_BITS_1,
                 parity: Byte = D2xxManager.FT_PARITY_NONE): OTGConfig {
        OTGExecute.OTGParam.apply {
            this.baud = baud
            this.data_bit = data_bit
            this.stop_bit = stop_bit
            this.parity = parity
        }
        return this
    }

    /**
     * 构建参数,同时打开OTG
     */
    fun build(ctx: Context) {
        OTGExecute.open(ctx)
    }

}