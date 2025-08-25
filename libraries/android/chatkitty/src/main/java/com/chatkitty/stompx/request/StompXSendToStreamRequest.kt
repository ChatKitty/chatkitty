package com.chatkitty.stompx.request

import com.chatkitty.stompx.StompXUploadProgressListener
import com.chatkitty.stompx.model.StompXError
import java.io.InputStream

data class StompXSendToStreamRequest<R>(
  val stream: String,
  val grant: String,
  val blob: InputStream,
  val properties: Map<String, Any>,
  val onSuccess: ((R) -> Unit)? = null,
  val onError: ((StompXError) -> Unit)? = null,
  val progressListener: StompXUploadProgressListener? = null
)
