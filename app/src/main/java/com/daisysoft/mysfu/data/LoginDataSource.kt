package com.daisysoft.mysfu.data

import com.daisysoft.mysfu.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            if (username != "johndoe" || password != "123456") {
                throw Exception("Invalid username or password")
            }

            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "John Doe")
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}