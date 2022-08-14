package com.quangpv.punk.exception

class InternalServerException : RuntimeException("Error internal server")
class ServerResponseNullException : RuntimeException("Server response no Content")
class ParameterInvalidException(message: String) : RuntimeException(message)

open class ApiRequestException(message: String?) : RuntimeException(message)

class UnAuthorException(message: String) : RuntimeException(message)
