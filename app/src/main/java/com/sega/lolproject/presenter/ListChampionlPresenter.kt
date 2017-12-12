package com.sega.lolproject.presenter

import android.os.Looper
import android.util.Log
import com.androidnetworking.error.ANError
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.sega.lolproject.model.Champion
import com.sega.lolproject.model.ResponseList
import com.sega.lolproject.util.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList


/**
 * Created by sega4 on 27/07/2017.
 */

class ListChampionlPresenter(view: listchampions) {
    internal var mListchampions = view
    var userdetail = "ListChampion"
    private val disposables = CompositeDisposable()

    fun getListChampions() {

        disposables.add(getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableObserver()))


    }
   
  
    private fun getObservable(): Observable<ResponseList> {
        return  Rx2AndroidNetworking.get(Constants.BASE_URL +"/getfullchampion")
                .setTag(this)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    Log.d(userdetail, " timeTakenInMillis : " + timeTakenInMillis)
                    Log.d(userdetail, " bytesSent : " + bytesSent)
                    Log.d(userdetail, " bytesReceived : " + bytesReceived)
                    Log.d(userdetail, " isFromCache : " + isFromCache)
                }
                .getObjectObservable(ResponseList::class.java)
    }

    private fun getDisposableObserver(): DisposableObserver<ResponseList> {

        return object : DisposableObserver<ResponseList>() {

            override fun onNext(response: ResponseList) {
                Log.d(userdetail, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()).toString())
            //*/    if(response?.listproduct?.size!! > 0)
           //     {
                Log.d("ssssss","!1")

                mListchampions.getDetail(response.listchampion!!)
              //  }else{
           //         mSkinListView.setErrorNotFound()*/
             //   }
            }
            override fun onError(e: Throwable) {
                if (e is ANError) {


                    Log.d(userdetail, "onError errorCode : " + e.errorCode)
                    Log.d(userdetail, "onError errorBody : " + e.errorBody)
                    Log.d(userdetail, e.errorDetail + " : " + e.message)
                    if(e.errorCode == 404){
                        mListchampions.setErrorNotFound()
                    }else{
                        mListchampions.setErrorMessage(e.errorDetail)
                    }


                } else {
                    Log.d(userdetail, "onError errorMessage : " + e.message)
                    mListchampions.setErrorMessage(e.message!!)
                }
            }

            override fun onComplete() {

            }
        }
    }
  
    interface listchampions {
        fun setErrorNotFound()
        fun setErrorMessage(errorMessage: String)
        fun getDetail(productlist: RealmList<Champion>)



    }
    fun stopRequest() {
        disposables.clear()
        println("cancel")
    }
}
