package com.sega.lolproject.services

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.sega.lolproject.model.Champion
import com.sega.lolproject.presenter.ListChampionlPresenter
import com.sega.lolproject.presenter.SkinDetailPresenter
import io.realm.Realm
import io.realm.RealmList
import java.util.*

/**
 * Created by sega4 on 11/12/2017.
 */

class SyncAdapter internal constructor(private val mContext: Context, autoInitialize: Boolean) : AbstractThreadedSyncAdapter(mContext, autoInitialize), SkinDetailPresenter.skinsDetail, ListChampionlPresenter.listchampions {
    override fun getDetail(productlist: RealmList<Champion>) {
        realm!!.beginTransaction()
        realm!!.copyToRealm(productlist)
        realm!!.commitTransaction()
    }

    override fun setErrorNotFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setErrorMessage(errorMessage: String) {
       println(errorMessage)
    }

    override fun getDetail(productlist: Champion) {
        realm!!.beginTransaction()
        realm!!.copyToRealm(productlist)
        realm!!.commitTransaction()
    }

    private var realm: Realm? = null
    private val mAccountManager: AccountManager
    private var mVersion = ""
    private var mCountPerformSync = 0
    private var mCountSyncCanceled = 0
    private val mCountProviderFailed = 0
    internal var contentResolver: ContentResolver
    private val mCountProviderFailedMax = 3
    internal var calendarid: String? = null
    internal var notifyevent = ArrayList<Uri>()
    var mListChampions: ListChampionlPresenter?= null

    init {
        println("da khoi tao")
        this.realm = RealmController.with(mContext).realm
        mListChampions = ListChampionlPresenter(this)
        mAccountManager = AccountManager.get(mContext)
        try {
            mVersion = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        contentResolver = mContext.contentResolver

    }


    override fun onPerformSync(account: Account, extras: Bundle, authority: String,
                               provider: ContentProviderClient, syncResult: SyncResult) {


        this.mCountPerformSync += 1
//        mSkinPresenter!!.getSkinList("Sona")
        mListChampions?.getListChampions()
        Log.v(TAG, "onPerformSync() count:" + 1.toString() + " on " + account.name + " with URL ")

        Log.i(TAG, "Entries:                       " + syncResult.stats.numEntries.toString())
        Log.i(TAG, "Rows inserted:                 " + syncResult.stats.numInserts.toString())
        Log.i(TAG, "Rows updated:                  " + syncResult.stats.numUpdates.toString())
        Log.i(TAG, "Rows deleted:                  " + syncResult.stats.numDeletes.toString())
        Log.i(TAG, "Rows skipped:                  " + syncResult.stats.numSkippedEntries.toString())
        Log.i(TAG, "Io Exceptions:                 " + syncResult.stats.numIoExceptions.toString())
        Log.i(TAG, "Parse Exceptions:              " + syncResult.stats.numParseExceptions.toString())
        Log.i(TAG, "Auth Exceptions:               " + syncResult.stats.numAuthExceptions.toString())
        Log.i(TAG, "Conflict Detected Exceptions:  " + syncResult.stats.numConflictDetectedExceptions.toString())
        for (uri in notifyevent) {
            context.contentResolver.notifyChange(uri, null, true)
        }


    }


    override fun onSyncCanceled() {
        //TODO: implement SyncCanceled
        this.mCountSyncCanceled += 1
        Log.v(TAG, "onSyncCanceled() count:" + this.mCountSyncCanceled.toString())
    }

    companion object {

        private val TAG = "SyncAdapter"



    }

}

