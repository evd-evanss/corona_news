package com.sayhitoiot.coronanews.features.feed.profile.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity

interface ProfilePresenterToInteract {
    fun didFetchUserOnDB(user: UserEntity)
    fun didFinishSignOut()
}