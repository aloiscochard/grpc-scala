package io.grpc.scala
package examples

import scala.concurrent.ExecutionContext.Implicits.global
import io.grpc.stub.StreamObserver

import io.grpc.examples.helloworld.{GreeterGrpc, HelloRequest, HelloResponse}

object HelloWorldClient {
  def main(args: Array[String]) {
    val channel = Channel("localhost", 50051)
    val stub = GreeterGrpc.newFutureStub(channel)
    val request = HelloRequest.newBuilder().setName(args.headOption.getOrElse("Giskard")).build()

    stub.sayHello(request).asFuture.map { response =>
      println(response.getMessage)
    }.onComplete { _ =>
      channel.shutdown()//.awaitTerminated(5, TimeUnit.SECONDS)
    }
  }
}

object HelloWorldServer {
  object Greeter extends GreeterGrpc.Greeter {
    override def sayHello(request: HelloRequest, observer: StreamObserver[HelloResponse]) {
      val response = HelloResponse.newBuilder().setMessage(s"Hello ${request.getName}").build()
      observer.onValue(response)
      observer.onCompleted
    }
  }

  def main(args: Array[String]) {
    val server = Server(50051, List(GreeterGrpc.bindService(Greeter)))
    server.start
  }
}
