package us.michaelrhodes.stockkeeper.api.pantry

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.michaelrhodes.stockkeeper.data.dao.PantryDAO
import us.michaelrhodes.stockkeeper.entity.Pantry
import java.util.UUID

@RestController
@RequestMapping("/v1/pantry")
class PantryController(val pantryDAO: PantryDAO) {
    @GetMapping("/{pantryUuid}")
    fun getPantry(@PathVariable pantryUuid: UUID) : Pantry? {
        return pantryDAO.getByUuid(pantryUuid)
    }
}