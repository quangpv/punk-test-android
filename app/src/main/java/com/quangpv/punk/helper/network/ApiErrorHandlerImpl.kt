package com.quangpv.punk.helper.network

import com.quangpv.punk.exception.ApiRequestException
import com.quangpv.punk.exception.InternalServerException
import com.quangpv.punk.exception.ParameterInvalidException
import com.quangpv.punk.exception.UnAuthorException
import retrofit2.Response

class ApiErrorHandlerImpl : DefaultApiErrorHandler() {
    override fun onResponseError(response: Response<out Any>): Throwable {
        val errorBody = response.errorBody()?.string()
            ?: response.message()
        val errorCode = response.code()

        val message = errorBody.toString()
        return when {
            errorCode == 401 -> UnAuthorException(message)
            errorCode in 400..499 -> ApiRequestException(message)
            errorCode >= 500 -> InternalServerException()
            else -> ParameterInvalidException(message)
        }
    }
}