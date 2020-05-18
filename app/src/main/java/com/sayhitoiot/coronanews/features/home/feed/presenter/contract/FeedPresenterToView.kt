package com.sayhitoiot.coronanews.features.home.feed.presenter.contract

interface FeedPresenterToView{
    fun onViewCreated()
    fun buttonSearchTapped()
    fun buttonBackTapped()
    fun onResume()
    fun didFinishInitializeViews()
}