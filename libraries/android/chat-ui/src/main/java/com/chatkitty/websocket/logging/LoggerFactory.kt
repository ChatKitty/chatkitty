/*
 * Copyright (C) Howdi, Inc - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This source is proprietary and confidential.
 */
package com.chatkitty.websocket.logging

import kotlin.reflect.KClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerImpl<T : Any>(
  clazz: KClass<T>,
  private val logger: Logger = LoggerFactory.getLogger(clazz.java)
) : Logger by logger

fun <T : Any> T.loggerFactory(): Lazy<Logger> =
  lazy { LoggerImpl(this::class) }
