package com.chatkitty.stompx.model

internal data class StompXPage<I>(
  val _embedded: MutableMap<String, List<I>>?,
  val page: StompXPageMetadata,
  val _relays: StompXPageRelays
)

internal data class StompXPageMetadata(
  val size: Int,
  val totalElement: Int,
  val totalPages: Int,
  val number: Int
)

internal data class StompXPageRelays(
  val first: String?,
  val prev: String?,
  val self: String,
  val next: String?,
  val last: String?
)
