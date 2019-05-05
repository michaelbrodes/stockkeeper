package us.michaelrhodes.stockkeeper.api.health

data class StockkeeperHealth(val heapUsedInMB: Long, 
                             val freeHeapInMB: Long, 
                             val totalHeapInMB: Long, 
                             val maxHeapInMB: Long, 
                             val serviceStatus: ServiceStatus)