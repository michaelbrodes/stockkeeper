package us.michaelrhodes.stockkeeper.api.health

data class Health (val stockkeeperHealth: StockkeeperHealth, val dbHealth: DBHealth)