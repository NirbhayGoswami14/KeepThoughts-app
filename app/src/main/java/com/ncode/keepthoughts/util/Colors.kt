package com.ncode.keepthoughts.util

import android.util.Log

object Colors {
    private val color= arrayOf("#FE9A37","#CBDB57","#9585BA","#5C4F45","#F96A4B","#DEA44D")
    var colorIndex=0
    val TAG="COLORS"

    fun getColor():String
    {
        colorIndex= (colorIndex+1)% color.size
        return color[colorIndex]
    }
}