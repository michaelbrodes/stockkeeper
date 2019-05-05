package us.michaelrhodes.stockkeeper.api.health

data class DBHealth(val serviceStatus: ServiceStatus, 
                    val totalDiskUsage: String? = null, 
                    val tableToDiskUsage: Map<String, String>? = null)