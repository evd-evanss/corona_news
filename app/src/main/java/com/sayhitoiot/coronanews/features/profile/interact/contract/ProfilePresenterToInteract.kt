package com.sayhitoiot.coronanews.features.profile.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity

interface ProfilePresenterToInteract {
    fun didFetchUserOnDB(user: UserEntity)
    fun didFinishSignOut()
}