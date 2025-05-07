package org.example.Model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.example.Model.message.requestMessage.*;
import org.example.Model.message.responseMessage.AddFriendResponseMessage;
import org.example.Model.message.requestMessage.*;
import org.example.Model.message.responseMessage.*;
import org.example.Model.message.responseMessage.LoginRequestResponseMessage;
import org.example.Model.message.responseMessage.LoginStatusResponseMessage;
import org.example.Model.message.responseMessage.RegisterRequestResponseMessage;
import org.example.Model.message.responseMessage.SearchFriendResponseMessage;
import org.example.Model.message.responseMessage.UserInfoResponseMessage;
import org.example.Util.SequenceIdUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;
    private int messageType;

    public Message() {
        this.sequenceId= SequenceIdUtil.getSequenceId();
    }

    public abstract int getMessageType();

    public static final int LoginRequestEvent = 0;
    public static final int LoginResponseEvent = 1;
    public static final int RegisterRequestEvent = 2;
    public static final int RegisterResponseEvent = 3;
    public static final int LoginStatusRequestMessage=4;
    public static final int LoginStatusResponseMessage=5;
    public static final int RegisterCodeRequestMessage=6;



    public static final int SingleTextRequestMessage=10;
    public static final int SingleImageRequestMessage = 11;
    public static final int SingleVideoRequestMessage = 12;
    public static final int SingleFileRequestMessage = 13;

    // 添加新的消息类型常量
    public static final int SearchFriendRequestMessage = 20;
    public static final int SearchFriendResponseMessage = 21;
    public static final int AddFriendRequestMessage = 22;
    public static final int AddFriendResponseMessage = 23;
    public static final int UserInfoRequestMessage = 24;
    public static final int UserInfoResponseMessage = 25;

    public static final int GroupTextMessage = 40;
    public static final int GroupImageMessage = 41;
    public static final int GroupVideoMessage = 42;
    public static final int GroupFileMessage = 43;
    public static final int GroupCreateMessage = 44;
    public static final int GroupJoinMessage = 45;
    public static final int GroupQuitMessage = 46;
    public static final int GroupMembersMessage = 47;
    public static final int GroupTextResponseMessage = 48;
    public static final int GroupImageResponseMessage = 49;
    public static final int GroupVideoResponseMessage = 50;
    public static final int GroupFileResponseMessage = 51;
    public static final int GroupCreateResponseMessage = 52;
    public static final int GroupJoinResponseMessage = 53;
    public static final int GroupQuitResponseMessage = 54;
    public static final int GroupMembersResponseMessage = 55;
    public static final int IdentityVerifyMessage=110;
    //   public static final int IdentityVerifyResponseMessage=111;

    public  static final int InitOKMessage=111;
    public static final int PingMessage=126;
    public static final int PongMessage=127;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestEvent, LoginRequestMessage.class);
        messageClasses.put(LoginResponseEvent, LoginRequestResponseMessage.class);
        messageClasses.put(RegisterRequestEvent, RegisterRequestMessage.class);
        messageClasses.put(RegisterResponseEvent, RegisterRequestResponseMessage.class);
        messageClasses.put(LoginStatusRequestMessage, LoginStatusRequestMessage.class);
        messageClasses.put(LoginStatusResponseMessage, LoginStatusResponseMessage.class);
        messageClasses.put(RegisterCodeRequestMessage, RegisterCodeRequestMessage.class);
        messageClasses.put(SingleTextRequestMessage, SingleChatTextRequestMessage.class);
        messageClasses.put(SingleImageRequestMessage, SingleChatImageRequestMessage.class);
        messageClasses.put(SingleVideoRequestMessage, SingleChatVideoRequestMessage.class);
        messageClasses.put(SingleFileRequestMessage, SingleChatFileRequestMessage.class);
        messageClasses.put(GroupTextMessage, GroupChatTextRequestMessage.class);
        messageClasses.put(GroupImageMessage, GroupChatImageRequestMessage.class);
        messageClasses.put(GroupVideoMessage, GroupChatVideoRequestMessage.class);
        messageClasses.put(GroupFileMessage, GroupChatFileRequestMessage.class);
        messageClasses.put(GroupCreateMessage, GroupCreateRequestMessage.class);
        messageClasses.put(GroupJoinMessage, GroupJoinRequestMessage.class);
        messageClasses.put(GroupQuitMessage, GroupQuitRequestMessage.class);
        messageClasses.put(GroupMembersMessage, GroupMembersRequestMessage.class);
        messageClasses.put(GroupTextResponseMessage, GroupChatTextResponseMessage.class);
        messageClasses.put(GroupImageResponseMessage, GroupChatImageResponseMessage.class);
        messageClasses.put(GroupFileResponseMessage,GroupChatFileResponseMessage.class);
        messageClasses.put(GroupVideoResponseMessage, GroupChatVideoResponseMessage.class);
        messageClasses.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinResponseMessage, GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitResponseMessage, GroupQuitResponseMessage.class);
        messageClasses.put(GroupMembersResponseMessage, GroupMembersResponseMessage.class);
        messageClasses.put(IdentityVerifyMessage, IdentityVerifyMessage.class);
        messageClasses.put(InitOKMessage, InitOKMessage.class);
        messageClasses.put(PingMessage, PingMessage.class);
        messageClasses.put(PongMessage, PongMessage.class);

        messageClasses.put(SearchFriendRequestMessage, SearchFriendRequestMessage.class);
        messageClasses.put(SearchFriendResponseMessage, SearchFriendResponseMessage.class);
        messageClasses.put(AddFriendRequestMessage, AddFriendRequestMessage.class);
        messageClasses.put(AddFriendResponseMessage, AddFriendResponseMessage.class);
        messageClasses.put(UserInfoRequestMessage, UserInfoRequestMessage.class);
        messageClasses.put(UserInfoResponseMessage, UserInfoResponseMessage.class);

    }



}
