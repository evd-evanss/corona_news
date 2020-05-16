package com.sayhitoiot.coronanews.features.splash.interactor

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.RealmDB
import com.sayhitoiot.coronanews.commom.entity.UserEntity
import com.sayhitoiot.coronanews.commom.model.UserFirebaseModel
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashInteractToPresenter
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashPresenterToInteract

class SplashInteract(private val presenter: SplashPresenterToInteract)
    : SplashInteractToPresenter {

    companion object {
        const val TAG = "splash-interactor"
    }

    override fun fetchUser() {
        fetchUserOnDB()
    }

    private fun fetchUserOnDB()  {
        val user = UserEntity.getUser()
        if(user != null) {
            presenter.didFinishFetchUserOnDB()
            Log.d(TAG,"Usuário ${user.name} online")
        } else {
            presenter.requestLogin()
        }

    }

}