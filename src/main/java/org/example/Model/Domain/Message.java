package org.example.Model.Domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Data
public class Message {
    //发送状态
    @Getter
    protected String sendStatus;
    @Setter
    public Integer senderID;
    @Setter
    public String senderName=""; //单聊默认为空不显示
    @Setter
    public String type;
    @Setter
    public Object content;
    @Setter
    public String sendTime;
    public static final String SENDING="发送中";
    public static final String SENT="已发送";
    public static final String FAILED="发送失败";
    private javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    public void changeSendStatus(String sendStatus){
        this.sendStatus = sendStatus;
        fireContentChanged();
    }

    // 添加监听器
    public void addContentChangedListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }


    // 触发内容改变事件
    protected void fireContentChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "contentChanged"));
            }
        }
    }

}
