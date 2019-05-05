package us.michaelrhodes.stockkeeper.api.family

/**
 * Model holding data inputted through the "Family Creation Modal".
 * TODO eventually allow users to specify who belongs to a family.
 */
data class FamilyCreationRequest(val name: String)