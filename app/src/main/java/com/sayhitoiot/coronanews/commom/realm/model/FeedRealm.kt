package com.sayhitoiot.coronanews.commom.realm.model

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import io.realm.RealmObject

open class FeedRealm : RealmObject() {
    var country: String = RealmDB.DEFAULT_STRING
    var day: String = RealmDB.DEFAULT_STRING
    var cases: Int = RealmDB.DEFAULT_INTEGER
    var recoveries: Int = RealmDB.DEFAULT_INTEGER
    var deaths: Int = RealmDB.DEFAULT_INTEGER
    var newCases: String = RealmDB.DEFAULT_STRING
    var favorite: Boolean = RealmDB.DEFAULT_BOOLEAN
}