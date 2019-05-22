package com.allever.daymatter.utils

object TimeUtils {

    private var startTime = 0L
    private var prevTime = 0L
    fun start() {
        startTime = System.currentTimeMillis()
        prevTime = startTime
    }

    fun getInterval(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val interval = currentTimeMillis - prevTime
        prevTime = currentTimeMillis
        return interval
    }


    fun formatTime(time: Long): String {
        var min = (time / (1000 * 60)).toString() + ""
        var sec = (time % (1000 * 60)).toString() + ""
        min = if (min.length < 2) {
            "0" + time / (1000 * 60) + ""
        } else {
            (time / (1000 * 60)).toString() + ""
        }
        when {
            sec.length == 4 -> sec = "0" + time % (1000 * 60) + ""
            sec.length == 3 -> sec = "00" + time % (1000 * 60) + ""
            sec.length == 2 -> sec = "000" + time % (1000 * 60) + ""
            sec.length == 1 -> sec = "0000" + time % (1000 * 60) + ""
        }
        return min + ":" + sec.trim { it <= ' ' }.substring(0, 2)
    }

    fun formatTime(hour: Int, min: Int): String {
        val hourStr =
                if (hour < 10) {
                    "0$hour"
                } else {
                    hour.toString()
                }
        val minStr =
                if (min < 10) {
                    "0$min"
                } else {
                    min.toString()
                }

        return "$hourStr:$minStr"
    }
}