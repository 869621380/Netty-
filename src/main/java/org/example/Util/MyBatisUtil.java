package org.example.Util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.Dao.ChatListMapper;
import org.example.Dao.ChatMessageMapper;
import org.example.Dao.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class MyBatisUtil {
    private static final Logger log = LoggerFactory.getLogger(MyBatisUtil.class);
    private static SqlSessionFactory sqlSessionFactory;
    public static UserInfoMapper userInfoMapper;
    public static ChatMessageMapper chatMessageMapper;
    public static ChatListMapper chatListMapper;
    static {
        try {
            String resource = Constants.MYBATIS_XML_Path;
            InputStream inputStream = MyBatisUtil.class.getClassLoader().getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            userInfoMapper=sqlSessionFactory.openSession().getMapper(UserInfoMapper.class);
            chatMessageMapper=sqlSessionFactory.openSession().getMapper(ChatMessageMapper.class);
            chatListMapper=sqlSessionFactory.openSession().getMapper(ChatListMapper.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
