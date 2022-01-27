package jiesu.service.util

import jiesu.service.TokenAuthenticationFilter.Companion.TOKEN_NAME
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class HttpEntityBuilder {
    private val headers = HttpHeaders()
    private var body: Any? = null

    fun contentType(mediaType: MediaType): HttpEntityBuilder {
        headers.contentType = mediaType
        return this
    }

    fun header(key: String, value: String): HttpEntityBuilder {
        headers[key] = value
        return this
    }

    fun body(key: String, value: Any): HttpEntityBuilder {
        val tmp: MultiValueMap<String, Any> =
            if (body is MultiValueMap<*, *>) body as MultiValueMap<String, Any> else LinkedMultiValueMap()
        tmp.add(key, value)
        body = tmp
        return this
    }

    fun body(value: Any): HttpEntityBuilder {
        body = value
        return this
    }

    fun token(token: String): HttpEntityBuilder {
        headers[TOKEN_NAME] = token
        return this
    }

    fun build(): HttpEntity<Any> = HttpEntity(body, headers)
}