package com.sayhitoiot.coronanews.commom.model

import com.sayhitoiot.coronanews.commom.RealmDB

class UserFirebaseModel {
    var id: Int = RealmDB.DEFAULT_INTEGER
    var name: String = ""
    var email: String = ""
    var birth: String = ""
    var token: String = ""
}