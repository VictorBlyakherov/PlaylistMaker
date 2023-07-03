package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val buttonBack = findViewById<ImageButton>(R.id.back)

        buttonBack.setOnClickListener {
            this.finish()
        }

        val buttonShare = findViewById<Button>(R.id.share)

        buttonShare.setOnClickListener {
            val link = getString(R.string.shareLink)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "plain/text";
            shareIntent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(shareIntent)
        }


        val buttonSendToSupport = findViewById<Button>(R.id.sendToSupport)

        buttonSendToSupport.setOnClickListener {
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

        val buttonViewUserAgreement = findViewById<Button>(R.id.viewUserAgreement)

        buttonViewUserAgreement.setOnClickListener {
            val link = getString(R.string.userAgreementLink)
            val viewAgreementIntent = Intent(Intent.ACTION_VIEW)
            viewAgreementIntent.setData(Uri.parse(link))
            startActivity(viewAgreementIntent)
        }


    }

}
