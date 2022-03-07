package demo.service;

import demo.domain.Doc;
import demo.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WsService {

    @Autowired
    private WebSocketServer webSocketServer;
    @Async
    public void sendInfo(String message){

        webSocketServer.sendInfo(message);
    }
}
