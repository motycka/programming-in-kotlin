package com.motycka.edu.game.user

import com.motycka.edu.game.user.model.Account
import org.springframework.stereotype.Repository

@Repository
interface IAccountRepository {
    fun selectByUsername(username: String): Account?
    fun insert(account: Account): Account?
}
