package org.example.Cache;

import lombok.Getter;
import lombok.Setter;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.View.MainFrame;

import java.util.HashMap;
import java.util.Map;

public class MessageCache {
    private static final Map<Integer, Message> messageCache = new HashMap<>();
    @Setter
    private static Map<Integer,ChatWindowMessageController>chatWindowMessageControllerMap;
    @Setter
    @Getter
    private static ChatListController chatListController;
    public static void addMessageCache(Integer id,Message message){
        messageCache.put(id, message);
    }

    public static void handleSuccessMessage(Integer id){
        messageCache.get(id).changeSendStatus(Message.SENT);
        messageCache.remove(id);
    }

    public static Map<Integer, ChatWindowMessageController>GetChatWindowMessageControllerMap(){
        return chatWindowMessageControllerMap;
    }

    @Setter
    @Getter
    private static MainFrame mainFrameCache;

}
