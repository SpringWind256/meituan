package com.chunfeng.contants;

/**
 * socket 常量
 *
 * @author by 春风能解释
 * <p>
 * 2025/1/22
 */
public interface SocketConstants {

    /**
     * 消息发送者id
     */
    String FROM_ID = "from_id";

    /**
     * 消息接收者id
     */
    String TO_ID = "to_id";

    /**
     * socket 鉴权协议
     */
    String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    /**
     * socket 最大在线人数
     */
    Integer SOCKET_MAX_ONLINE_COUNT = 100;

    /**
     * 用户名
     */
    String SOCKET_USER_NAME = "user_name";
    /**
     * 头像ID
     */
    String SOCKET_AVATAR_ID = "user_avatar";
}
