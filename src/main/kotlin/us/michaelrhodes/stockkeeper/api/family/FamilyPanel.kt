package us.michaelrhodes.stockkeeper.api.family

import us.michaelrhodes.stockkeeper.entity.Family
import us.michaelrhodes.stockkeeper.entity.Pantry

/**
 * Model holding the data stored within the UI panel of the "Family Management Page".
 */
data class FamilyPanel(val family: Family, val pantry: Pantry, val members: List<FamilyMemberInfo>)