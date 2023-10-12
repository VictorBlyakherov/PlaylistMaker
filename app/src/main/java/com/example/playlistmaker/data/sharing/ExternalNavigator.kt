package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.data.model.EmailData

interface ExternalNavigator {
    fun shareLink(sharedLink : String)
    fun openLink(termAddress: String)
    fun openEmail(dataToSend: EmailData)
}