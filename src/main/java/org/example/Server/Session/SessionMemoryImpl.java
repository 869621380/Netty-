package org.example.Server.Session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private final Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, Integer> channelUserIdMap = new ConcurrentHashMap<>();
    private final Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();


    public void bind(Channel channel, Integer userId) {
        userIdChannelMap.put(userId, channel);
        channelUserIdMap.put(channel, userId);
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        Integer userId = channelUserIdMap.remove(channel);
        userIdChannelMap.remove(userId);
        channelAttributesMap.remove(channel);
    }

    @Override
    public Object getAttribute(Channel channel, String key) {
        return channelAttributesMap.get(channel).get(key);
    }

    @Override
    public void setAttribute(Channel channel, String key, Object value) {
        channelAttributesMap.get(channel).put(key, value);
    }

    @Override
    public Channel getChannel(Integer username) {
        return userIdChannelMap.get(username);
    }

}
