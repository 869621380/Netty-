package org.example.Server.Session;

import io.netty.channel.Channel;

public interface Session {


    void bind(Channel channel, Integer userId);

    void unbind(Channel channel);

    /**
     * 获取属性
     */
    Object getAttribute(Channel channel, String key);

    /**
     * 设置属性
     */
    void setAttribute(Channel channel, String key, Object value);

    /**
     * 根据用户ID获取 channel
     */
    Channel getChannel(Integer userId);


}
