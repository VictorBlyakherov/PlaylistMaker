package com.example.playlistmaker.ui.common

class Utils {
    fun getTrackCountString(trackCount: Int): String {
        val number = kotlin.math.abs(trackCount);
        var strRes = "$trackCount "
        if (number % 10 == 1 && number % 100 != 11) {
            strRes = strRes + "трек"
        } else if ((number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20))) {
            strRes = strRes + "трека"
        } else {
            strRes = strRes + "треков"
        }
        return strRes
    }
}