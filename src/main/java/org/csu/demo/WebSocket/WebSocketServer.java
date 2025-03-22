/*
package org.csu.demo.WebSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
    @ServerEndpoint("/ws/{userId}")
    public class WebSocketServer {

        private static UserService userService;
        private static final ConcurrentHashMap<Integer, Session> onlineUsers = new ConcurrentHashMap<>();

        @Autowired
        public void setUserService(UserService userService) {
            WebSocketServer.userService = userService;
        }
        @OnOpen
        public void onOpen(@PathParam("userId") int userId, Session session) {
            onlineUsers.put(userId, session);
            userService.setIsOnlineTrue(userId);
            System.out.println("用户" + userId + "上线啦");
        }

        @OnClose
        public void onClose(@PathParam("userId") int userId) {
            onlineUsers.remove(userId);  // 移除用户
            userService.setIsOnlineFalse(userId);
            System.out.println("用户" + userId + "下线啦");
        }


    }
*/
