package com.melongame.playlistmaker
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton=findViewById<ImageView>(R.id.back_light)

        backButton.setOnClickListener{
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.shareTheApp)

        shareButton.setOnClickListener {
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_for_share))
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent, getString(R.string.share)))
        }

        val support = findViewById<FrameLayout>(R.id.writeInSupport)

        support.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_line))
            startActivity(supportIntent)
        }

        val agreement = findViewById<FrameLayout>(R.id.agreement)
        agreement.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(agreementIntent)
        }
    }

}