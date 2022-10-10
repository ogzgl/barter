package com.valar.barter.utils

import net.logstash.logback.argument.StructuredArguments.keyValue
import org.slf4j.LoggerFactory
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class InboundHttpInterceptor : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (request.method != "OPTIONS") {
            val startTime = System.currentTimeMillis()
            val queryString = getRawQueryStringifPresent(request)
            request.setAttribute(START_TIME_ATTRIBUTE_NAME, startTime)
            LOGGER.trace(
                "Request ${request.method} ${request.requestURI}$queryString",
                keyValue("requestUri", request.requestURI),
                keyValue("requestMethod", request.method),
                keyValue("httpDirection", "Request")
            )
        }
        return true
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable exception: java.lang.Exception?
    ) {
        if (request.method != "OPTIONS") {
            val startTime: Long? = request.getAttribute(START_TIME_ATTRIBUTE_NAME) as Long?
            val currentTime = System.currentTimeMillis()
            val executionTime = if (startTime != null) currentTime - startTime else null
            val queryString = getRawQueryStringifPresent(request)

            LOGGER.trace(
                "Response to ${request.method} ${request.requestURI}$queryString => ${response.status} - $executionTime ms",
                keyValue("requestUri", request.requestURI),
                keyValue("requestMethod", request.method),
                keyValue("responseStatusCode", response.status),
                if (executionTime != null) keyValue("responseExecutionTime", executionTime) else null,
                keyValue("httpDirection", "Response")

            )
        }
    }

    private fun getRawQueryStringifPresent(request: HttpServletRequest): String =
        if (!request.queryString.isNullOrBlank()) {
            "?${request.queryString}"
        } else {
            ""
        }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(InboundHttpInterceptor::class.java)
        private const val START_TIME_ATTRIBUTE_NAME = "TIMING_START"
    }
}
