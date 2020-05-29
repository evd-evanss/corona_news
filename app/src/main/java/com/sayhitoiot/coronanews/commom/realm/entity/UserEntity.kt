package com.sayhitoiot.coronanews.commom.realm.entity

import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.model.UserRealm
import io.realm.Realm

class UserEntity (
    var id: Int,
    var name: String,
    var email: String,
    var birth: String,
    var token: String
) {

    constructor(userRealm: UserRealm) : this(
        id = userRealm.id,
        name = userRealm.name,
        email = userRealm.email,
        birth = userRealm.birthdate,
        token = userRealm.token
    )

    companion object {

        const val TAG = "user-entity"

        fun getUser() : UserEntity? {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val userModel = realm.where(UserRealm::class.java)
                .findFirst()

            var userEntity: UserEntity? = null

            if (userModel != null) {
                userEntity = UserEntity(userModel)
            }

            realm.close()

            return userEntity
        }

        fun create(
            id: Int,
            name: String,
            email: String,
            birth: String,
            token: String
        ) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val userModel = realm.createObject(UserRealm::class.java)

            userModel.id = id
            userModel.name = name
            userModel.email = email
            userModel.birthdate = birth
            userModel.token = token

            realm.commitTransaction()
            realm.close()
        }

        fun delete() {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            realm.delete(UserRealm::class.java)

            realm.commitTransaction()
            realm.close()
        }

        fun update(name: String, birthdate: String) {
            val realm = Realm.getDefaultInstance()

            if(!realm.isInTransaction) {
                realm.beginTransaction()
            }

            val userModel = realm.where(UserRealm::class.java)
                .equalTo("id", RealmDB.DEFAULT_INTEGER)
                .findFirst()

            userModel?.name = name
            userModel?.birthdate = birthdate

            realm.commitTransaction()
            realm.close()
        }

    }

}