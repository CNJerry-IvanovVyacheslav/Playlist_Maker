package com.melongame.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.sharing.domain.ExternalNavigator
import com.melongame.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(link: String, share: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, link)
            type = TYPE_FOR_SHARE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooserIntent = Intent.createChooser(shareIntent, share).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooserIntent)
    }

    override fun openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(URI_PARSE)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(emailIntent)
    }

    private fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getSupportEmail(): String {
        return getString(R.string.support_mail)
    }

    override fun getSupportSubject(): String {
        return getString(R.string.subject_line)
    }

    override fun getSupportMessage(): String {
        return getString(R.string.message)
    }

    override fun getShareAppLink(): String {
        return getString(R.string.link_for_share)
    }

    override fun getShareAppText(): String {
        return getString(R.string.share)
    }

    override fun getTermsLink(): String {
        return getString(R.string.agreement_link)
    }

    companion object {
        private const val TYPE_FOR_SHARE = "text/plain"
        private const val URI_PARSE = "mailto:"
    }
}