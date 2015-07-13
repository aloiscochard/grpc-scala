package io.grpc

import _root_.scala.concurrent.{Future, Promise}
import com.google.common.util.concurrent.{Futures, FutureCallback, ListenableFuture}

package object scala {
  implicit class RichListenableFuture[A](self: ListenableFuture[A]) {
    def asFuture: Future[A] = {
      val p = Promise[A]()
      Futures.addCallback(self, new FutureCallback[A] {
        def onFailure(t: Throwable): Unit = p.failure(t)
        def onSuccess(result: A): Unit    = p.success(result)
      })
      p.future
    }
  }
}
