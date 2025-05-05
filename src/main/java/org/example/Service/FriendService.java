//package org.example.Service;
//
//import org.apache.ibatis.session.SqlSession;
//import org.example.Client;
//import org.example.Dao.FriendMapper;
//import org.example.Model.Domain.FriendInfo;
//import org.example.Model.message.requestMessage.FriendRequestMessage;
//import org.example.Util.MyBatisUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
//public class FriendService {
//    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);
//
//    public boolean sendFriendRequest(String friendId) {
//        try {
//            // 检查当前用户是否已登录
//            if (Client.getInstance().getCurrentUser() == null) {
//                logger.error("当前未登录，无法添加好友");
//                return false;
//            }
//
//            // 检查连接状态
//            if (!Client.getInstance().isConnected()) {
//                logger.error("与服务器连接已断开，无法发送好友请求");
//                return false;
//            }
//
//            FriendRequestMessage request = new FriendRequestMessage();
//            request.setUserId(Client.getInstance().getCurrentUser().getId().toString());
//            request.setFriendId(friendId);
//            request.setSequenceId(org.example.Util.SequenceIdUtil.getSequenceId());
//
//            logger.info("发送好友请求: userId={}, friendId={}, sequenceId={}",
//                    request.getUserId(), request.getFriendId(), request.getSequenceId());
//
//            Client.getInstance().sendMessage(request);
//            return true;
//        } catch (Exception e) {
//            logger.error("发送好友请求失败", e);
//            return false;
//        }
//    }
//
//
//    public boolean addFriend(FriendInfo friendInfo) {
//        try(SqlSession session = MyBatisUtil.getSqlSession()) {
//            // 设置当前用户ID - 使用正确的方法名 setUserid
//            friendInfo.setUserid(Client.getInstance().getCurrentUser().getId().toString());
//
//            // 添加日志，方便调试 - 使用正确的方法名 getUserid
//            logger.info("添加好友到本地数据库: userid={}, friendId={}, nickName={}",
//                    friendInfo.getUserid(), friendInfo.getFriendId(), friendInfo.getFriendNickName());
//
//            FriendMapper mapper = session.getMapper(FriendMapper.class);
//
//            // 将字符串转换为Integer类型
//            try {
//                // 检查是否已存在此好友 - 使用正确的方法名和参数类型
//                FriendInfo existingFriend = mapper.findFriendById(
//                        Integer.parseInt(friendInfo.getUserid()),
//                        Integer.parseInt(friendInfo.getFriendId())
//                );
//
//                int result;
//                if (existingFriend != null) {
//                    logger.info("好友已存在，更新信息");
//                    result = mapper.updateFriend(friendInfo);
//                } else {
//                    result = mapper.addFriend(friendInfo);
//                }
//
//                session.commit();
//
//                // 添加日志记录结果
//                logger.info("添加好友结果: {}", result > 0 ? "成功" : "失败");
//                return result > 0;
//            } catch (NumberFormatException e) {
//                logger.error("用户ID或好友ID格式不正确，无法转换为整数", e);
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("添加好友失败", e);
//            return false;
//        }
//    }
//
//    public List<FriendInfo> getAllFriends() {
//        try(SqlSession session = MyBatisUtil.getSqlSession()) {
//            FriendMapper mapper = session.getMapper(FriendMapper.class);
//            return mapper.getAllFriends(Client.getInstance().getCurrentUser().getId().toString());
//        }catch (Exception e){
//            logger.error("获取好友列表失败",e);
//            return null;
//        }
//    }
//}

package org.example.Service;

import org.apache.ibatis.session.SqlSession;
import org.example.Client;
import org.example.Dao.FriendMapper;
import org.example.Model.Domain.FriendInfo;
import org.example.Model.message.requestMessage.FriendRequestMessage;
import org.example.Util.MyBatisUtil;
import org.example.Util.SequenceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    public boolean sendFriendRequest(String friendId) {
        try {
            // 检查当前用户是否已登录
            if (Client.getInstance().getCurrentUser() == null) {
                logger.error("当前未登录，无法添加好友");
                return false;
            }

            // 检查连接状态
            if (!Client.getInstance().isConnected()) {
                logger.error("与服务器连接已断开，无法发送好友请求");
                return false;
            }

            FriendRequestMessage request = new FriendRequestMessage();
            request.setUserId(Client.getInstance().getCurrentUser().getId().toString());
            request.setFriendId(friendId);
            request.setSequenceId(SequenceIdUtil.getSequenceId());

            logger.info("发送好友请求: userId={}, friendId={}, sequenceId={}",
                    request.getUserId(), request.getFriendId(), request.getSequenceId());

            Client.getInstance().sendMessage(request);
            return true;
        } catch (Exception e) {
            logger.error("发送好友请求失败", e);
            return false;
        }
    }

    public boolean addFriend(FriendInfo friendInfo) {
        try(SqlSession session = MyBatisUtil.getSqlSession()) {
            // 设置当前用户ID - 使用正确的方法名 setUserid
            friendInfo.setUserid(Client.getInstance().getCurrentUser().getId().toString());

            // 添加日志，方便调试 - 使用正确的方法名 getUserid
            logger.info("添加好友到本地数据库: userid={}, friendId={}, nickName={}",
                    friendInfo.getUserid(), friendInfo.getFriendId(), friendInfo.getFriendNickName());

            FriendMapper mapper = session.getMapper(FriendMapper.class);

            // 将字符串转换为Integer类型
            try {
                // 检查是否已存在此好友 - 使用正确的方法名和参数类型
                FriendInfo existingFriend = mapper.findFriendById(
                        Integer.parseInt(friendInfo.getUserid()),
                        Integer.parseInt(friendInfo.getFriendId())
                );

                int result;
                if (existingFriend != null) {
                    logger.info("好友已存在，更新信息");
                    result = mapper.updateFriend(friendInfo);
                } else {
                    result = mapper.addFriend(friendInfo);
                }

                session.commit();

                // 添加日志记录结果
                logger.info("添加好友结果: {}", result > 0 ? "成功" : "失败");
                return result > 0;
            } catch (NumberFormatException e) {
                logger.error("用户ID或好友ID格式不正确，无法转换为整数", e);
                return false;
            }
        } catch (Exception e) {
            logger.error("添加好友失败", e);
            return false;
        }
    }

    public List<FriendInfo> getAllFriends() {
        try(SqlSession session = MyBatisUtil.getSqlSession()) {
            FriendMapper mapper = session.getMapper(FriendMapper.class);
            return mapper.getAllFriends(Client.getInstance().getCurrentUser().getId().toString());
        }catch (Exception e){
            logger.error("获取好友列表失败",e);
            return null;
        }
    }
}