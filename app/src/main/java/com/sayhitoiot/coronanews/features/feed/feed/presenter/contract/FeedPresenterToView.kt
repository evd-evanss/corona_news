package com.sayhitoiot.coronanews.features.feed.feed.presenter.contract

interface FeedPresenterToView{
    fun onViewCreated()
    fun buttonSearchTapped()
    fun buttonBackTapped()
    fun onResume()
    fun didFinishInitializeViews()
    fun filterData(text: String)
}