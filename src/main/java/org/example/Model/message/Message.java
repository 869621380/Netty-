package org.example.Model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Model.message.requestMessage.*;
import org.example.Model.message.responseMessage.LoginRequestResponseMessage;
import org.example.Model.message.responseMessage.LoginStatusResponseMessage;
import org.example.Model.message.responseMessage.RegisterRequestResponseMessage;
import org.example.Model.message.responseMessage.RequestResponseMessage;
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


    public static final int SingleTextRequestMessage=10;
    public static final int SingleImageRequestMessage = 11;
    public static final int SingleVideoRequestMessage = 12;
    public static final int SingleFileRequestMessage = 13;

    public static final int GroupTextMessage = 40;
    public static final int GroupImageMessage = 41;
    public static final int GroupVideoMessage = 42;
    public static final int GroupFileMessage = 43;

    public static final int IdentityVerifyMessage=110;
 //   public static final int IdentityVerifyResponseMessage=111;

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
        messageClasses.put(SingleTextRequestMessage, SingleChatTextRequestMessage.class);
        messageClasses.put(SingleImageRequestMessage, SingleChatImageRequestMessage.class);
        messageClasses.put(SingleVideoRequestMessage, SingleChatVideoRequestMessage.class);
        messageClasses.put(SingleFileRequestMessage, SingleChatFileRequestMessage.class);
        messageClasses.put(IdentityVerifyMessage, IdentityVerifyMessage.class);
        messageClasses.put(PingMessage, PingMessage.class);
        messageClasses.put(PongMessage, PongMessage.class);

    }



}
