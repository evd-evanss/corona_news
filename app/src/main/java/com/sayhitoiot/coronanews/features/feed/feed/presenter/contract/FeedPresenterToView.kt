package com.sayhitoiot.coronanews.features.feed.feed.presenter.contract

interface FeedPresenterToView{
    fun onViewCreated()
    fun buttonSearchTapped()
    fun buttonBackTapped()
    fun onResume()
    fun filterData(text: String)
    fun moreCasesTapped()
    fun fewerCasesTapped()
    fun continentCasesTapped()
    fun allCasesTapped()
}