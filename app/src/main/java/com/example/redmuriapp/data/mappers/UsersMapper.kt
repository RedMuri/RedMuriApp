package com.example.redmuriapp.data.mappers

import com.example.redmuriapp.db.UserDbModel
import com.example.redmuriapp.domain.entities.User

class UsersMapper {

    fun userDbModelToEntity(userDbModel: UserDbModel) = User(
        id = userDbModel.id,
        firstName = userDbModel.firstName,
        lastName = userDbModel.lastName,
        email = userDbModel.email,
        password = userDbModel.password,
        location = userDbModel.location,
        profileImage = userDbModel.profileImage,
    )

    fun userEntityToDbModel(user: User) = UserDbModel(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        password = user.password,
        location = user.location,
        profileImage = user.profileImage,
    )
}