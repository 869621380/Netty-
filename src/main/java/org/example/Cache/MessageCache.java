package org.example.Cache;

import lombok.Getter;
import lombok.Setter;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.responseMessage.AddFriendResponseMessage;
import org.example.Model.message.responseMessage.SearchFriendResponseMessage;
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

    // 添加搜索好友回调接口和实例
    public interface SearchFriendCallback {
        void handleResponse(SearchFriendResponseMessage response);
    }
    
    private static SearchFriendCallback searchFriendCallback;
    
    public static void setSearchFriendCallback(SearchFriendCallback callback) {
        searchFriendCallback = callback;
    }
    
    public static SearchFriendCallback getSearchFriendCallback() {
        return searchFriendCallback;
    }
    
    // 添加好友回调接口和实例
    public interface AddFriendCallback {
        void handleResponse(AddFriendResponseMessage response);
    }
    
    private static AddFriendCallback addFriendCallback;
    
    public static void setAddFriendCallback(AddFriendCallback callback) {
        addFriendCallback = callback;
    }
    
    public static AddFriendCallback getAddFriendCallback() {
        return addFriendCallback;
    }
}
