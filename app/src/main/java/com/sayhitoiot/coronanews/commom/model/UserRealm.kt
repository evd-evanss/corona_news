package com.sayhitoiot.coronanews.commom.model

import com.sayhitoiot.coronanews.commom.RealmDB
import io.realm.RealmObject

open class UserRealm : RealmObject() {

    var id: Int = RealmDB.DEFAULT_INTEGER
    var name: String = ""
    var email: String = ""
    var birthdate: String = ""
    var token: String = ""

}