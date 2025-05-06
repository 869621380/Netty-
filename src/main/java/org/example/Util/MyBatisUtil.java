package org.example.Util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.Dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class MyBatisUtil {
    private static final Logger log = LoggerFactory.getLogger(MyBatisUtil.class);
    private static SqlSessionFactory sqlSessionFactory;
    public static UserInfoMapper userInfoMapper;
    public static ChatMessageMapper chatMessageMapper;
    public static ChatListMapper chatListMapper;
    public static UserMapper userMapper;
    public static FriendshipsMapper friendshipsMapper;
    public static Private_chat_recordsMapper private_chat_recordsMapper;
    public static Group_membersMapper group_membersMapper;
    public static Group_chat_recordsMapper groupChatRecordsMapper;
    static {
        try {
            String resource = Constants.MYBATIS_XML_Path;
            InputStream inputStream = MyBatisUtil.class.getClassLoader().getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            userInfoMapper=sqlSessionFactory.openSession().getMapper(UserInfoMapper.class);
            chatMessageMapper=sqlSessionFactory.openSession().getMapper(ChatMessageMapper.class);
            chatListMapper=sqlSessionFactory.openSession().getMapper(ChatListMapper.class);
            userMapper=sqlSessionFactory.openSession().getMapper(UserMapper.class);
            friendshipsMapper=sqlSessionFactory.openSession().getMapper(FriendshipsMapper.class);
            private_chat_recordsMapper=sqlSessionFactory.openSession(true).getMapper(Private_chat_recordsMapper.class);
            group_membersMapper=sqlSessionFactory.openSession(true).getMapper(Group_membersMapper.class);
            groupChatRecordsMapper=sqlSessionFactory.openSession(true).getMapper(Group_chat_recordsMapper.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

}
