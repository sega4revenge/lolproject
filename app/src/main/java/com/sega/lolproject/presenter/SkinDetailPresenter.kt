package com.sega.lolproject.presenter

import android.os.Looper
import android.util.Log
import com.androidnetworking.error.ANError
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.sega.lolproject.model.Champion
import com.sega.lolproject.model.Response
import com.sega.lolproject.model.ResponseList
import com.sega.lolproject.util.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * Created by sega4 on 27/07/2017.
 */

class SkinDetailPresenter(view: skinsDetail) {
    internal var mSkinListView = view
    var userdetail = "USERDETAIL"
    private val disposables = CompositeDisposable()

    fun getDatachampion(name : String) {

        disposables.add(getObservable(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableObserver()))


    }
   
  
    private fun getObservable(name : String): Observable<Response> {
        return  Rx2AndroidNetworking.get(Constants.BASE_URL +"data/{id}")
                .addPathParameter("id", name)
                .setTag(this)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    Log.d(userdetail, " timeTakenInMillis : " + timeTakenInMillis)
                    Log.d(userdetail, " bytesSent : " + bytesSent)
                    Log.d(userdetail, " bytesReceived : " + bytesReceived)
                    Log.d(userdetail, " isFromCache : " + isFromCache)
                }
                .getObjectObservable(Response::class.java)
    }

    private fun getDisposableObserver(): DisposableObserver<Response> {

        return object : DisposableObserver<Response>() {

            override fun onNext(response: Response) {
                Log.d(userdetail, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()).toString())
            //*/    if(response?.listproduct?.size!! > 0)
           //     {
                Log.d("ssssss","!1")

                    mSkinListView.getDetail(response.champion!!)
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
                        mSkinListView.setErrorNotFound()
                    }else{
                        mSkinListView.setErrorMessage(e.errorDetail)
                    }


                } else {
                    Log.d(userdetail, "onError errorMessage : " + e.message)
                    mSkinListView.setErrorMessage(e.message!!)
                }
            }

            override fun onComplete() {

            }
        }
    }
  //================
  fun getListChampions() {

      disposables.add(getObservable()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(getDisposableObserverList()))


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

    private fun getDisposableObserverList(): DisposableObserver<ResponseList> {

        return object : DisposableObserver<ResponseList>() {

            override fun onNext(response: ResponseList) {
                Log.d(userdetail, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()).toString())
                //*/    if(response?.listproduct?.size!! > 0)
                //     {
                Log.d("ssssss","!1")

                mSkinListView.getListChampion(response.listchampion!!)
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
                        mSkinListView.setErrorNotFound()
                    }else{
                        mSkinListView.setErrorMessage(e.errorDetail)
                    }


                } else {
                    Log.d(userdetail, "onError errorMessage : " + e.message)
                    mSkinListView.setErrorMessage(e.message!!)
                }
            }

            override fun onComplete() {

            }
        }
    }
    interface skinsDetail {
        fun setErrorNotFound()
        fun setErrorMessage(errorMessage: String)
        fun getDetail(productlist: Champion)
        fun getListChampion(productlist: ArrayList<String>)


    }
    fun stopRequest() {
        disposables.clear()
        println("cancel")
    }
}
