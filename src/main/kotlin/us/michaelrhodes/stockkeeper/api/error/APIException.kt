package us.michaelrhodes.stockkeeper.api.error

import org.springframework.http.HttpStatus

/**
 * Exception thrown at the API layer to give more user facing context to errors.
 *
 * Although every [APIException] should have a user facing message, it should only be used as a 
 * fallback in the UI layer.
 * 
 * * [userMessage] is the user friendly version of error. 
 * * [errorCode] is a code that a user can reference when talking with a developer. 500 is for errors that we don't 
 * expect to happen (i.e. [SeverityLevel.HIGH] or [SeverityLevel.MEDIUM]).
 * * [severityLevel] indicates how _bad_ an error was. Each severity level has its own log file.
 * * [httpStatus] is the status code that should returned in the exception mapper. 
 * * [message] is the actual message associated with the exception.
 */
class APIException(val userMessage: String,
                   val errorCode: Int,
                   val httpStatus: HttpStatus,
                   val severityLevel: SeverityLevel,
                   override val message: String)
    : RuntimeException(message) {
    companion object {
        const val UNEXPECTED_ERROR_CODE = 500
        
        fun commonError(commonErrorMessage: CommonErrorMessage,
                        httpStatus: HttpStatus,
                        severityLevel: SeverityLevel,
                        logMessage: String) : APIException {
            return APIException(commonErrorMessage.message,
                    commonErrorMessage.errorCode,
                    httpStatus,
                    severityLevel,
                    message = logMessage)
        }

        fun unexpectedError(userMessage: String, 
                            httpStatus: HttpStatus, 
                            severityLevel: SeverityLevel, 
                            logMessage: String) : APIException {
            return APIException(userMessage, 
                    UNEXPECTED_ERROR_CODE, 
                    httpStatus, 
                    severityLevel, 
                    message = logMessage)
        }
    }
}