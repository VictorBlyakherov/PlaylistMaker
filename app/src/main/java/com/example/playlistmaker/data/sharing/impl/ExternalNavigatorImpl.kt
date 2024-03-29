package com.example.playlistmaker.data.sharing.impl

import android.content.Context

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.model.EmailData
import com.example.playlistmaker.data.sharing.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(sharedLink: String) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.type = "plain/text";
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedLink)

        context.startActivity(shareIntent)
    }

    override fun openLink(termAddress: String) {
        val viewAgreementIntent = Intent(Intent.ACTION_VIEW)
        viewAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        viewAgreementIntent.setData(Uri.parse(termAddress))

        context.startActivity(viewAgreementIntent)
    }

    override fun openEmail(dataToSend: EmailData) {
        val sendToSupportIntent = Intent(Intent.ACTION_SENDTO)
        sendToSupportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        sendToSupportIntent.data = Uri.parse("mailto:")
        sendToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(dataToSend.fromAddress))
        sendToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, dataToSend.subject)
        sendToSupportIntent.putExtra(Intent.EXTRA_TEXT, dataToSend.message)
        context.startActivity(sendToSupportIntent)
    }
}