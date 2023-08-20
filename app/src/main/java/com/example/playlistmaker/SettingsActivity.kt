package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.SettingsActivityBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSwitcherTheme()

        binding.themeSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        })

        binding.back.setOnClickListener {
            this.finish()
        }

        binding.share.setOnClickListener {
            shareAction()
        }

        binding.shareView.setOnClickListener {
            shareAction()
        }

        binding.sendToSupport.setOnClickListener {
            sendToSupportAction()
        }

        binding.supportView.setOnClickListener {
            sendToSupportAction()
        }

        binding.viewUserAgreement.setOnClickListener {
            userAgreementAction()
        }

        binding.viewUserAgreement.setOnClickListener {
            userAgreementAction()
        }
    }

    private fun setSwitcherTheme() {
        binding.themeSwitch.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
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
