package com.sayhitoiot.coronanews.commom.realm.model

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import io.realm.RealmObject

open class UserRealm : RealmObject() {

    var id: Int = RealmDB.DEFAULT_INTEGER
    var name: String = ""
    var email: String = ""
    var birthdate: String = ""
    var token: String = ""

}