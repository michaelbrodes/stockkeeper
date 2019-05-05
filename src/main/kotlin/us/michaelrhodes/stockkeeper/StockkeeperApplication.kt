package us.michaelrhodes.stockkeeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockkeeperApplication

fun main(args: Array<String>) {
    runApplication<StockkeeperApplication>(*args)
}
