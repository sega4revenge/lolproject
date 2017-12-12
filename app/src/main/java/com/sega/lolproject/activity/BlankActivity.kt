package com.sega.lolproject.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sega.lolproject.R
import kotlinx.android.synthetic.main.activity_blank.*

class BlankActivity : AppCompatActivity() {
    val AUTHORITY = "com.example.android.datasync.provider"
    // An account type, in the form of a domain name
    val ACCOUNT_TYPE = "com.sega.lolproject"
    // The account name
    val ACCOUNT = "dummyaccount"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
        createSyncAccount(this)
        button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    fun createSyncAccount(context: Context) {
        // Create the account type and default account
        val newAccount = Account(
                ACCOUNT, ACCOUNT_TYPE)
        // Get an instance of the Android account manager
        val accountManager = context.getSystemService(
                Context.ACCOUNT_SERVICE) as AccountManager
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            println("khoi tao dau tien")
            ContentResolver.setSyncAutomatically(newAccount, "com.sega.lolproject", true);
            ContentResolver.addPeriodicSync(newAccount, "com.sega.lolproject", Bundle.EMPTY, 60);
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
    }
}
