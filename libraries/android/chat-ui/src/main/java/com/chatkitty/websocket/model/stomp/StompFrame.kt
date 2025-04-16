/*
 * Copyright 2020 ChatKitty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chatkitty.websocket.model.stomp

import java.io.StringReader
import java.util.Scanner
import java.util.regex.Pattern

private val HEADER_PATTERN = Pattern.compile("([^\\r\\n]+):([^\\r\\n]+)")

data class StompClientFrame(
  val command: StompClientCommand,
  val headers: List<StompHeader>,
  val body: String = ""
) {
  val text: String =
    buildString {
      append(command.toString() + '\n')

      headers.forEach {
        append(it.name + ":" + it.value + '\n')
      }

      append('\n' + body + '\u0000')
    }
}

data class StompServerFrame(
  val command: StompServerCommand,
  val headers: Map<String, StompHeader>,
  val body: String
) {
  val text: String =
    buildString {
      append(command.toString() + '\n')

      headers.values.forEach {
        append(it.name + ":" + it.value + '\n')
      }

      append('\n' + body + '\u0000')
    }

  operator fun get(name: String): String? =
    headers[name]?.value
}

fun String.asStompServerFrame(): StompServerFrame {
  val reader = Scanner(StringReader(this))

  reader.useDelimiter("\\n")

  val command = StompServerCommand.valueOf(reader.next())
  val headers = mutableMapOf<String, StompHeader>()

  while (reader.hasNext(HEADER_PATTERN)) {
    val matcher = HEADER_PATTERN.matcher(reader.next())

    matcher.find()

    val name = matcher.group(1)!!
    val value = matcher.group(2)!!

    headers[name] = stompHeader(name, value)
  }

  reader.skip("\n\n")

  reader.useDelimiter("\\u0000")

  val body = if (reader.hasNext()) reader.next() else ""

  return StompServerFrame(command, headers, body)
}
