package com.okq.rxotg.utils

/**
 * Created by zst on 2016-10-14  0014.
 * 描述:
 */
object HexUtils {
    @JvmStatic fun hexString2Bytes(src: String): ByteArray {
        val ret = ByteArray(src.length / 2)
        val tmp = src.toByteArray()
        for (i in 0..src.length / 2 - 1) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1])
        }
        return ret
    }

    private fun uniteBytes(src0: Byte, src1: Byte): Byte {
        var _b0 = java.lang.Byte.decode(String.format("0X%s", String(byteArrayOf(src0))))!!
        _b0 = (_b0.toInt() shl 4).toByte()
        val _b1 = java.lang.Byte.decode(String.format("0X%s", String(byteArrayOf(src1))))!!
        return (_b0.toInt() or _b1.toInt()).toByte()
    }
}

