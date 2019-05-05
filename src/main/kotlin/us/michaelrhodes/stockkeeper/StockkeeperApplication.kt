package us.michaelrhodes.stockkeeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@SpringBootApplication
class StockkeeperApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    runApplication<StockkeeperApplication>(*args)
}
