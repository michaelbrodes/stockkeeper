package us.michaelrhodes.stockkeeper.api.account

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import us.michaelrhodes.stockkeeper.data.dao.AccountDAO
import us.michaelrhodes.stockkeeper.entity.Account
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/v1/account")
class AccountController(private val accountDao: AccountDAO) {
    @PostMapping
    fun createAccount(@RequestBody accountCreationRequest: AccountCreationRequest) : ResponseEntity<Account> {
        val previousAccount = accountDao.getByUsername(accountCreationRequest.username)
        
        return if (previousAccount == null) {
            val uuid = UUID.randomUUID()!!
            val account = Account(uuid, accountCreationRequest.username)
            
            ResponseEntity.created(URI.create("/v1/account/$uuid"))
                    .body(accountDao.insert(account))
        } else {
            ResponseEntity.badRequest().build()
        }
    }
    
    @GetMapping("/{accountUuid}")
    fun getAccount(@PathVariable accountUuid: UUID) : Account? {
        return accountDao.getByUuid(accountUuid)
    }
}