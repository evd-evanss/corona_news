package com.sayhitoiot.coronanews.commom.realm.entity

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.model.FeedRealm
import io.realm.Realm

class FeedEntity (
    var country: String,
    var day: String,
    var cases: Int,
    var recovereds: Int,
    var deaths: Int,
    var newCases: String,
    var favorite: Boolean = false
) {

    constructor(userRealm: FeedRealm) : this(
        country = userRealm.country,
        day = userRealm.day,
        cases = userRealm.cases,
        recovereds = userRealm.recoveries,
        deaths = userRealm.deaths,
        newCases = userRealm.newCases,
        favorite = userRealm.favorite
    )

    companion object {

        const val TAG = "feed-entity"

        fun create(
            country: String,
            day: String,
            cases: Int?,
            recoveries: Int?,
            deaths: Int?,
            newCases: String?,
            favorite: Boolean?
        ) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val feedModel = realm.createObject(FeedRealm::class.java)

            feedModel.country = country
            feedModel.day = day
            feedModel.cases = cases ?: RealmDB.DEFAULT_INTEGER
            feedModel.recoveries = recoveries ?: RealmDB.DEFAULT_INTEGER
            feedModel.deaths = deaths ?: RealmDB.DEFAULT_INTEGER
            feedModel.newCases = newCases ?: RealmDB.DEFAULT_STRING
            feedModel.favorite = favorite ?: RealmDB.DEFAULT_BOOLEAN

            realm.commitTransaction()
            realm.close()
        }

        fun update(
            country: String,
            day: String,
            cases: Int?,
            recoveries: Int?,
            deaths: Int?,
            newCases: String?
        ) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val feedModel = realm.createObject(FeedRealm::class.java)

            feedModel.country = country
            feedModel.day = day
            feedModel.cases = cases ?: RealmDB.DEFAULT_INTEGER
            feedModel.recoveries = recoveries ?: RealmDB.DEFAULT_INTEGER
            feedModel.deaths = deaths ?: RealmDB.DEFAULT_INTEGER
            feedModel.newCases = newCases ?: RealmDB.DEFAULT_STRING

            realm.commitTransaction()
            realm.close()
        }

        fun delete() {
            val realm = Realm.getDefaultInstance()

            realm.beginTransaction()

            realm.delete(FeedRealm::class.java)

            realm.commitTransaction()
            realm.close()
        }

        fun getAll() : MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            val feedList = realm.where(FeedRealm::class.java)
                .findAllAsync()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return feedList.toMutableList()
        }

        fun getAllFavorites() : MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            val feedList = realm.where(FeedRealm::class.java)
                .equalTo("favorite", true)
                .findAll()
                .mapNotNull { FeedEntity(it) }

            realm.close()

            return feedList.toMutableList()

        }

        fun favoriteFeed(
            country: String,
            favorite: Boolean
        ) {
            val realm = Realm.getDefaultInstance()

            realm.beginTransaction()

            val feedModel = realm.where(FeedRealm::class.java)
                .equalTo("country", country)
                .findFirstAsync()

            feedModel?.favorite = favorite

            realm.commitTransaction()
            realm.close()




        }

    }



}