package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonShare: Button
    private lateinit var viewShare: TextView
    private lateinit var buttonSendToSupport: Button
    private lateinit var viewSendToSupport: TextView
    private lateinit var buttonViewUserAgreement: Button
    private lateinit var userAgreementView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        buttonBack = findViewById<ImageButton>(R.id.back)

        buttonBack.setOnClickListener {
            this.finish()
        }

        buttonShare = findViewById<Button>(R.id.share)
        viewShare = findViewById<TextView>(R.id.shareView)

        buttonShare.setOnClickListener {
            shareAction()
        }

        viewShare.setOnClickListener {
            shareAction()
        }

        buttonSendToSupport = findViewById<Button>(R.id.sendToSupport)
        viewSendToSupport = findViewById<TextView>(R.id.supportView)

        buttonSendToSupport.setOnClickListener {
            sendToSupportAction()
        }

        viewSendToSupport.setOnClickListener {
            sendToSupportAction()
        }


        buttonViewUserAgreement = findViewById<Button>(R.id.viewUserAgreement)
        userAgreementView = findViewById<TextView>(R.id.userAgreementView)

        buttonViewUserAgreement.setOnClickListener {
            userAgreementAction()
        }

        userAgreementView.setOnClickListener {
            userAgreementAction()
        }



    }

    private fun userAgreementAction() {
        val link = getString(R.string.userAgreementLink)
        val viewAgreementIntent = Intent(Intent.ACTION_VIEW)
        viewAgreementIntent.setData(Uri.parse(link))
        startActivity(viewAgreementIntent)
    }

    private fun sendToSupportAction() {
        val message = getString(R.string.messageSupport)
        val subject = getString(R.string.subjectSupport)
        val fromAddress = getString(R.string.fromAddressSupport)
        val sendToSupportIntent = Intent(Intent.ACTION_SENDTO)
        sendToSupportIntent.data = Uri.parse("mailto:")
        sendToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(fromAddress))
        sendToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sendToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(sendToSupportIntent)

    }

    private fun shareAction() {
        val link = getString(R.string.shareLink)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "plain/text";
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(shareIntent)
    }


}
