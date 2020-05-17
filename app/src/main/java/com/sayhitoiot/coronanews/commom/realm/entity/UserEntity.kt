package com.sayhitoiot.coronanews.commom.realm.entity

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
            realm.beginTransaction()

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

            realm.beginTransaction()

            realm.delete(UserRealm::class.java)

            realm.commitTransaction()
            realm.close()
        }

    }

    fun update(
        name: String? = null,
        email: String? = null,
        birth: String? = null,
        token: String? = null
    ) {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        val userModel = realm.where(UserRealm::class.java)
            .equalTo("id", this.id)
            .findFirst()

        userModel?.name = name ?: this.name
        userModel?.email = email ?: this.email
        userModel?.birthdate = birth ?: this.birth
        userModel?.token = token ?: this.token


        realm.commitTransaction()
        realm.close()
    }


}