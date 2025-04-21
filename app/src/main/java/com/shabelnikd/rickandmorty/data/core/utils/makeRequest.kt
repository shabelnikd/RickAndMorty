package com.shabelnikd.rickandmorty.data.core.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException


suspend inline fun <reified S> HttpClient.makeRequest(
    request: HttpRequestBuilder.() -> Unit,
): Result<S> {
    return try {
        val httpResponse = request {
            request()
        }

        if (httpResponse.status.isSuccess()) {
            // Если ответ успешный (2xx), просто возвращаем Result.success с телом
            httpResponse.body<S>()
                .let { Result.success(it) }
        } else {
            // Если ответ НЕ успешный (не 2xx), определяем ошибку и ВЫБРАСЫВАЕМ ЕЕ
            val errorBody = try {
                // Пытаемся десериализовать тело ответа ошибки
                httpResponse.body<ApiErrorResponse>()
            } catch (e: Exception) {
                // Логируем ошибку десериализации тела ошибки, если она произошла
                println("Failed to deserialize error body (status ${httpResponse.status.value}): ${e.message}")
                null
            }

            // Логика определения ошибки на основе статуса, использующая .value для диапазонов
            val networkError: NetworkError = when (httpResponse.status) {
                HttpStatusCode.BadRequest -> NetworkError.BadRequest(errorBody) // 400
                HttpStatusCode.Unauthorized -> NetworkError.Unauthorized(errorBody) // 401
                HttpStatusCode.Forbidden -> NetworkError.Forbidden(errorBody) // 403
                HttpStatusCode.NotFound -> NetworkError.NotFound(errorBody) // 404
                HttpStatusCode.UnprocessableEntity -> NetworkError.InputValidation(errorBody) // 422

                // Используем .value для проверки диапазонов
                else -> when (httpResponse.status.value) {
                    in 400..499 -> NetworkError.ClientError(
                        httpResponse.status.value,
                        errorBody
                    )

                    in 500..599 -> NetworkError.ServerError(
                        httpResponse.status.value,
                        errorBody
                    )
                    // Все остальные статусы (1xx, 3xx, >=600)
                    else -> NetworkError.Unexpected(
                        "Неизвестный HTTP статус: ${httpResponse.status.value}",
                        null
                    )
                }
            }
            throw networkError // <-- ВЫБРАСЫВАЕМ СОЗДАННЫЙ NetworkError
        }
        // Код здесь ДОСТУПЕН только если try блок успешно завершился БЕЗ ВЫБРАСЫВАНИЯ исключения
        // (только если httpResponse.status.isSuccess() и body() успешно выполнен)
        // Ветка else выше выбрасывает исключение, поэтому здесь не будет проблем с выводом типов
    } catch (e: Exception) {
        // Этот блок catch ловит ЛЮБОЕ исключение, которое произошло:
        // 1. Сетевые исключения Ktor (ClientRequestException, таймауты, IO)
        // 2. NetworkError, который мы сами выбросили в else блоке
        val networkError: NetworkError = when (e) {
            // Если исключение e УЖЕ является нашим типом NetworkError, используем его как есть.
            // Это случится, если мы выбросили NetworkError в блоке else выше.
            is NetworkError -> e
            // Если это исключение Ktor или другое Exception, маппируем его в NetworkError.
            // Это случится для сетевых ошибок (таймауты, нет сети) или ошибок Ktor,
            // которые не были обработаны по HTTP статусу (например, ClientRequestException/ServerResponseException
            // бросаются Ktor автоматически для 4xx/5xx, их тоже ловим здесь)
            is ClientRequestException -> {
                val errorBody = try {
                    e.response.body<ApiErrorResponse>() // Пытаемся прочитать тело ошибки из исключения
                } catch (deserializeException: Exception) {
                    println("Failed to deserialize error body from ClientRequestException (status ${e.response.status.value}): ${deserializeException.message}")
                    null
                }
                // Маппируем ClientRequestException в соответствующий NetworkError, передавая оригинальное исключение 'e' как причину.
                when (e.response.status) {
                    HttpStatusCode.BadRequest -> NetworkError.BadRequest(errorBody, e)
                    HttpStatusCode.Unauthorized -> NetworkError.Unauthorized(errorBody, e)
                    HttpStatusCode.Forbidden -> NetworkError.Forbidden(errorBody, e)
                    HttpStatusCode.NotFound -> NetworkError.NotFound(errorBody, e)
                    HttpStatusCode.UnprocessableEntity -> NetworkError.InputValidation(errorBody, e)
                    // Другие клиентские ошибки (4xx), которые привели к ClientRequestException
                    else -> NetworkError.ClientError(
                        e.response.status.value,
                        errorBody,
                        e // Передаем оригинальное ClientRequestException как причину
                    )
                }
            }

            is ServerResponseException -> {
                val errorBody = try {
                    e.response.body<ApiErrorResponse>() // Пытаемся прочитать тело ошибки из исключения
                } catch (deserializeException: Exception) {
                    println("Failed to deserialize error body from ServerResponseException (status ${e.response.status.value}): ${deserializeException.message}")
                    null
                }
                // ServerResponseException - это всегда 5xx ошибки
                NetworkError.ServerError(
                    e.response.status.value,
                    errorBody,
                    e
                ) // Передаем оригинальное ServerResponseException как причину
            }
            // Таймауты
            is SocketTimeoutException, is ConnectTimeoutException, is TimeoutCancellationException -> NetworkError.Timeout(
                e
            ) // Передаем исключение таймаута как причину
            // Ошибки ввода-вывода (часто нет сети)
            is IOException -> NetworkError.NoConnection(e) // Передаем IOException как причину
            // Любые другие непредвиденные исключения
            else -> NetworkError.Unexpected(
                "Неожиданная ошибка: ${e.message ?: "Неизвестно"}",
                e // Передаем оригинальное исключение как причину
            )
        }
        // Оборачиваем пойманный или созданный networkError (который является Throwable) в Result.failure
        Result.failure(networkError)
    }
}