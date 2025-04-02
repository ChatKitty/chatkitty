package com.chatkitty.ui.stompx

interface StompXUploadProgressListener {
  fun onStarted() = Unit
  fun onProgress(progress: Int) = Unit
  fun onCompleted() = Unit
  fun onFailed() = Unit
  fun onCancelled() = Unit
}
