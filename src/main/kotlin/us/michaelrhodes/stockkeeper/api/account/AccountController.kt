package us.michaelrhodes.stockkeeper.api.account

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.michaelrhodes.stockkeeper.data.dao.AccountDAO
import us.michaelrhodes.stockkeeper.entity.Account
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/v1/account")
class AccountController(private val accountDao: AccountDAO) {
    @PostMapping
    fun createAccount(accountCreationRequest: AccountCreationRequest) : ResponseEntity<Account> {
        val uuid = UUID.randomUUID()
        val account = Account(uuid, accountCreationRequest.username)
        
        return ResponseEntity.created(URI.create("/v1/account/$uuid"))
                .body(accountDao.insert(account))
    }
}