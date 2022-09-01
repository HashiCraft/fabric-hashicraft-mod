package com.hashicorp.hashicraft.watcher;

import java.io.IOException;

import fi.iki.elonen.NanoWSD;
import fi.iki.elonen.NanoWSD.WebSocketFrame.CloseCode;

public class Listener extends NanoWSD {
  public Listener(String hostname, int port) {
    super(hostname, port);
  }

  @Override
  protected WebSocket openWebSocket(IHTTPSession ihttpSession) {
    return new WsdSocket(ihttpSession);
  }

  private static class WsdSocket extends WebSocket {
    public WsdSocket(IHTTPSession handshakeRequest) {
      super(handshakeRequest);
    }

    // override onOpen, onClose, onPong and onException methods

    @Override
    protected void onMessage(WebSocketFrame webSocketFrame) {
      // Event event = Event.fromBytes(webSocketFrame.getBinaryPayload());
      // System.out.println("event: " + event.event);
      // System.out.println("topic: " + event.topic);
      // System.out.println("source: " + event.source);
      // System.out.println("timestamp: " + event.timestamp);
      // System.out.println("payloadJSON: " + event.payload);
      // System.out.println("-----");
    }

    @Override
    protected void onOpen() {
    }

    @Override
    protected void onClose(CloseCode code, String reason, boolean initiatedByRemote) {
    }

    @Override
    protected void onPong(WebSocketFrame pong) {
    }

    @Override
    protected void onException(IOException exception) {
    }
  }
}
