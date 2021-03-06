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

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

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
            country: String?,
            favorite: Boolean
        ) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedModel = realm.where(FeedRealm::class.java)
                .equalTo("country", country)
                .findFirst()

            feedModel?.country = country ?: RealmDB.DEFAULT_STRING
            feedModel?.favorite = favorite

            realm.commitTransaction()
            realm.close()
        }

        fun update(
            country: String?,
            day: String?
        ) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedModel = realm.where(FeedRealm::class.java)
                .equalTo("country", country)
                .findFirst()

            feedModel?.country = country ?: RealmDB.DEFAULT_STRING
            feedModel?.day = day ?: RealmDB.DEFAULT_STRING

            realm.commitTransaction()
            realm.close()
        }

        fun delete() {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            realm.delete(FeedRealm::class.java)

            realm.commitTransaction()
            realm.close()
        }

        fun getAll() : MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedList = realm.where(FeedRealm::class.java)
                .notEqualTo("country", "All").and()
                .notEqualTo("country", "North-America").and()
                .notEqualTo("country", "Europe").and()
                .notEqualTo("country", "Asia").and()
                .notEqualTo("country", "South-America")
                .notEqualTo("country", "Africa")
                .notEqualTo("country", "Oceania")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return feedList.toMutableList()
        }

        fun getNorthAmericaFeed()  : MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val northAmericaFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "North-America").and()
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return northAmericaFeed.toMutableList()
        }

        fun getSouthAmericaFeed(): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val southAmericaFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "South-America")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return southAmericaFeed.toMutableList()
        }

        fun getEuropeFeed(): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val europeFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "Europe")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return europeFeed.toMutableList()
        }

        fun getAsiaFeed(): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val asiaFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "Asia")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return asiaFeed.toMutableList()
        }

        fun getOceaniaFeed(): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val asiaFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "Oceania")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return asiaFeed.toMutableList()
        }

        fun getAfricaFeed(): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val asiaFeed = realm.where(FeedRealm::class.java)
                .equalTo("country", "Africa")
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return asiaFeed.toMutableList()
        }

        fun getAllFavorites() : MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }
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

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedModel = realm.where(FeedRealm::class.java)
                .equalTo("country", country)
                .findFirstAsync()

            feedModel?.favorite = favorite

            realm.commitTransaction()
            realm.close()




        }

        fun findByFilter(text: String) : FeedEntity? {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedModel = realm.where(FeedRealm::class.java)
                .contains("country", text)
                .findFirst()

            realm.close()

            var feedEntity: FeedEntity? = null

            if (feedModel != null) {
                feedEntity = FeedEntity(feedModel)
            }

            return feedEntity
        }

        fun getAllByFilter(filter: String): MutableList<FeedEntity> {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedList = realm.where(FeedRealm::class.java)
                .contains("country", filter)
                .findAll()
                .map {
                    FeedEntity(it)
                }

            realm.close()

            return feedList.toMutableList()
        }


    }



}