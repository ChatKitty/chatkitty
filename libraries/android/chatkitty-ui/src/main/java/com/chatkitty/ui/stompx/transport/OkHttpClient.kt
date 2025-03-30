/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.ui.stompx.transport

import com.chatkitty.ui.websocket.logging.loggerFactory
import java.io.File
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.random.Random
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor

fun makeOkHttpClient(
  isDebug: Boolean,
  retryIOExceptions: Boolean,
  wrapIOExceptions: Boolean,
  userAgent: String,
  cacheDirectory: String
): OkHttpClient =
  with(OkHttpClient.Builder()) {
    connectTimeout(30, TimeUnit.SECONDS)
    readTimeout(30, TimeUnit.SECONDS)
    writeTimeout(30, TimeUnit.SECONDS)

    if (wrapIOExceptions) {
      addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response =
          try {
            chain.proceed(chain.request())
          } catch (e: Exception) {
            throw IOException(e)
          }
      })
    }

    if (retryIOExceptions) {
      addInterceptor(IORetryInterceptor())
    }

    addInterceptor(UserAgentInterceptor(userAgent))
    addInterceptor(DefaultContentTypeInterceptor())
    addInterceptor(RateLimitInterceptor())
    addInterceptor(loggingInterceptor(isDebug))

    cache(Cache(File(cacheDirectory, "network"), 100L * 1024 * 1024))

    val authenticator = object : Authenticator {
      @Volatile
      private var headerValue: String? = null

      override fun authenticate(route: Route?, response: Response): Request? =
        try {

          response.request
            .newBuilder()
            .header("Authorization", headerValue!!)
            .build()
        } catch (e: Exception) {
          response.closeBody()

          throw e
        }
    }

    authenticator(authenticator)

    build()
  }

private fun loggingInterceptor(isDebug: Boolean) =
  HttpLoggingInterceptor().apply {
    level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
  }

private class IORetryInterceptor : Interceptor {
  companion object {
    private const val MAX_RETRIES = 10

    private const val MAX_WAIT_INTERVAL = 10 * 1000L
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    var response = chain.proceed()

    if (response is IOException) {
      var retries = 0
      var retry: Boolean

      do {
        val waitTime = retries.waitTime

        Thread.sleep(waitTime)

        response = chain.proceed()

        retry = response is IOException
      } while (retry && retries++ < MAX_RETRIES)
    }

    return if (response is Response) response else throw response as IOException
  }

  private val Int.waitTime
    get() = Random.nextLong((2.0.pow(toDouble()).toLong() * 100L).coerceAtMost(MAX_WAIT_INTERVAL))

  private fun Interceptor.Chain.proceed(): Any =
    try {
      proceed(request())
    } catch (e: IOException) {
      if (e.cause !is UnknownHostException) throw e

      e
    }
}

private class DefaultContentTypeInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()

    if (request.header("Content-Type") == null) {
      request = chain.request()
        .newBuilder()
        .addHeader("Content-Type", "application/json")
        .build()
    }

    return chain.proceed(request)
  }
}

private class UserAgentInterceptor(
  private val userAgent: String
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
      .newBuilder()
      .addHeader("User-Agent", userAgent)
      .build()

    return chain.proceed(request)
  }
}

private class RateLimitInterceptor : Interceptor {
  companion object {
    private const val MAX_RETRIES = 15

    private const val MAX_WAIT_INTERVAL = 60 * 1000L
  }

  private val logger by loggerFactory()

  override fun intercept(chain: Interceptor.Chain): Response {
    var response = chain.proceed(chain.request())

    if (response.code == 429) {
      var retries = 0
      var retry: Boolean

      do {
        val waitTime = retries.waitTime

        response.closeBody()

        with(response.request) {
          logger.info("$method $url rate limited: waiting for ${waitTime}ms. Attempt ${retries + 1}.")
        }

        Thread.sleep(waitTime)

        response = chain.proceed(chain.request())

        retry = response.code == 429
      } while (retry && retries++ < MAX_RETRIES)
    }

    return response
  }

  private val Int.waitTime
    get() = Random.nextLong((2.0.pow(toDouble()).toLong() * 100L).coerceAtMost(MAX_WAIT_INTERVAL))
}

private fun Response.closeBody() {
  if (body != null) close()
}
