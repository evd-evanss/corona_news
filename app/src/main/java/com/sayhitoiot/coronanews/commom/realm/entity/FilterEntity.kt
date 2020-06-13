package com.sayhitoiot.coronanews.commom.realm.entity

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.model.FilterRealm
import io.realm.Realm

class FilterEntity (
    var filter: Int
) {

    constructor(filterRealm: FilterRealm) : this(
        filter = filterRealm.filter
    )

    companion object {

        const val TAG = "feed-entity"

        fun create(
            filter: Int?
        ) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val filterModel = realm.createObject(FilterRealm::class.java)

            filterModel.filter = filter ?: RealmDB.DEFAULT_INTEGER

            realm.commitTransaction()
            realm.close()
        }

        fun update(
            filter: Int?
        ) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val feedModel = realm.where(FilterRealm::class.java)
                .findFirst()

            feedModel?.filter = filter ?: RealmDB.DEFAULT_INTEGER

            realm.commitTransaction()
            realm.close()
        }


        fun getFilter() : FilterEntity?{
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val filterModel = realm.where(FilterRealm::class.java)
                .findFirst()

            var filterEntity: FilterEntity? = null

            if (filterModel != null) {
                filterEntity = FilterEntity(filterModel)
            }

            realm.close()

            return filterEntity
        }


    }



}