package com.motycka.edu.tracker.error

//public record ErrorResponse(
//String error,
//int status
//)
//{ }

data class ErrorResponse(
    val error: String,
    val status: Int
) {
    companion object {
        fun fromException(e: Exception): ErrorResponse {
            return ErrorResponse(e.message ?: "Unknown error", 500)
        }
    }
}
