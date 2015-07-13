package io.grpc
package scala

import io.grpc.transport.netty.{NegotiationType, NettyChannelBuilder}

object Channel {
  def apply(host: String, port: Int): ChannelImpl =
    NettyChannelBuilder
      .forAddress(host, port)
      .negotiationType(NegotiationType.PLAINTEXT)
      .build()
}
