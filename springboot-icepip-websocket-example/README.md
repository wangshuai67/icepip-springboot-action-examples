## 第一种方式 使用JSR 356 Java WebSocket API
* [WebSocketConfig.java](src%2Fmain%2Fjava%2Fcom%2Ficepip%2Fproject%2Fwebsocket%2Fconfig%2FWebSocketConfig.java)
* [BusiEventMsgHandler.java](src%2Fmain%2Fjava%2Fcom%2Ficepip%2Fproject%2Fwebsocket%2Fhandler%2FBusiEventMsgHandler.java)
* [websocketDemo.html](src%2Fmain%2Fresources%2Fstatic%2FwebsocketDemo.html)
## 第二种方式  使用Spring WebSocket API 利用stomp方式+SockJS实现Websocket

* [WebSocketMessageBrokerConfig.java](src%2Fmain%2Fjava%2Fcom%2Ficepip%2Fproject%2Fwebsocket%2Fconfig%2FWebSocketMessageBrokerConfig.java)
* [WebSocketController.java](src%2Fmain%2Fjava%2Fcom%2Ficepip%2Fproject%2Fwebsocket%2Fcontroller%2FWebSocketController.java)
* [stompWebsocketDemo.html](src%2Fmain%2Fresources%2Fstatic%2FstompWebsocketDemo.html)

## 两种方式的实现区别
WebSocket API（JSR 356）和 Spring 的 WebSocket 支持是两种实现 WebSocket 的方式，各自有其特点和区别。以下是主要的几点区别：

**1. 开发方式**

- **WebSocket API（JSR 356）**：这个是 Java 自己的标准，是比较底层的 API，需要手动实现连接、发送消息和接收消息等操作。

- **Spring 的 WebSocket 支持**：Spring 对 WebSocket 进行了高级封装，提供了更为简单、易用的 API，如使用 `@MessageMapping` 注解处理消息，利用 `SimpMessagingTemplate` 发送消息等。

**2. 功能扩展**

- **WebSocket API（JSR 356）**：由于 JSR 356 是 WebSocket 的基础实现，因此一些高级功能，比如消息的广播、用户身份绑定等需要开发者自己实现。

- **Spring 的 WebSocket 支持**：Spring 为 WebSocket 提供了一套完整的解决方案，包括 STOMP 协议支持、消息的广播、用户身份绑定、安全控制等功能。

**3. 集成度**

- **WebSocket API（JSR 356）**：JSR 356 可以独立使用，与其它框架没有依赖关系。

- **Spring 的 WebSocket 支持**：Spring 的 WebSocket 支持需要在 Spring 的环境下使用，可以与 Spring 的其他模块（如 Spring Security）进行集成，实现更为强大的功能。

 选择使用哪种方式实现 WebSocket，主要取决于项目的具体需求和所使用的技术栈。如果项目已经使用了 Spring，并且需要 WebSocket 的一些高级特性，那么选择 Spring 的 WebSocket 支持可能更为合适。如果项目需要更为底层的控制，或者不使用 Spring，那么可以选择使用 WebSocket API（JSR 356）。