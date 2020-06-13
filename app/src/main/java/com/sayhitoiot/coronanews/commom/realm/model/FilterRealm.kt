package com.sayhitoiot.coronanews.commom.realm.model

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import io.realm.RealmObject

open class FilterRealm : RealmObject() {
    var filter: Int = RealmDB.DEFAULT_INTEGER
}