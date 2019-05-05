package us.michaelrhodes.stockkeeper.entity

import java.lang.IllegalStateException

enum class FamilyPermission(val uid: Long) {
    OWNER(1), 
    EDITOR(2), 
    VIEWER(3);
     
    companion object {
        private val BY_UID = values().associateBy { it.uid }
        
        fun fromUid(uid: Long) : FamilyPermission {
            return BY_UID[uid] ?: throw IllegalStateException("The database has a family_permission_uid=$uid that doesn't exist in code!")
        }
    }
}