package us.michaelrhodes.stockkeeper.api.error

/**
 * A collection of error messages for [SeverityLevel.LOW] errors.
 */
enum class CommonErrorMessage(val message: String, val errorCode: Int) {
    NO_FAMILY_MEMBER("We cannot verify that you are member of this family. You may have been disowned!", 100),
    NO_ACCOUNT("We cannot find your account in our database. Did you delete it?",101),
    BAD_FAMILY_PERMISSION("You don't have the required permission to do this action.", 200)
}
