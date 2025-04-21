package com.shabelnikd.rickandmorty.data.core.utils

sealed class NetworkError(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    // Ошибки клиента (4xx)
    data class BadRequest(val errorBody: ApiErrorResponse?, override val cause: Throwable? = null) :
        NetworkError("Неверный запрос", cause) // 400

    data class Unauthorized(
        val errorBody: ApiErrorResponse?,
        override val cause: Throwable? = null
    ) : NetworkError("Ошибка аутентификации", cause) // 401

    data class Forbidden(val errorBody: ApiErrorResponse?, override val cause: Throwable? = null) :
        NetworkError("Нет доступа", cause) // 403

    data class NotFound(val errorBody: ApiErrorResponse?, override val cause: Throwable? = null) :
        NetworkError("Ресурс не найден", cause) // 404

    data class InputValidation(
        val errorBody: ApiErrorResponse?,
        override val cause: Throwable? = null
    ) : NetworkError("Ошибка ввода данных", cause) // 422

    data class ClientError(
        val code: Int,
        val errorBody: ApiErrorResponse?,
        override val cause: Throwable? = null
    ) : NetworkError("Ошибка клиента: $code", cause) // Другие 4xx

    // Ошибки сервера (5xx)
    data class ServerError(
        val code: Int,
        val errorBody: ApiErrorResponse?,
        override val cause: Throwable? = null
    ) : NetworkError("Ошибка сервера: $code", cause) // 5xx

    // Ошибки сети/таймауты
    data class Timeout(override val cause: Throwable? = null) :
        NetworkError("Превышено время ожидания", cause)

    data class NoConnection(override val cause: Throwable? = null) :
        NetworkError("Отсутствует интернет-соединение", cause)

    // Непредвиденные ошибки
    data class Unexpected(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : NetworkError(message, cause)
}