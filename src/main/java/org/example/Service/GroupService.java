package org.example.Service;

import org.apache.ibatis.session.SqlSession;
import org.example.Client;
import org.example.Dao.GroupMapper;
import org.example.Model.Domain.GroupInfo;
import org.example.Model.message.requestMessage.GroupCreateRequestMessage;
import org.example.Util.MyBatisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);


    public boolean createGroup(String groupName, String groupAvatar, List<String> initialMembers) {
        try {
            // 检查当前用户是否登录
            if (Client.getInstance().getCurrentUser() == null) {
                logger.error("当前未登录，无法创建群组");
                return false;
            }

            // 检查连接状态
            if (!Client.getInstance().isConnected()) {
                logger.error("与服务器连接已断开，无法发送创建群组请求");
                return false;
            }

            GroupCreateRequestMessage request = new GroupCreateRequestMessage();
            request.setGroupName(groupName);
            request.setGroupAvatar(groupAvatar);
            request.setOwnerId(Client.getInstance().getCurrentUser().getId().toString());
            request.setInitialMembers(initialMembers);
            request.setSequenceId(org.example.Util.SequenceIdUtil.getSequenceId());

            logger.info("发送创建群组请求: groupName={}, ownerId={}, members={}, sequenceId={}",
                    request.getGroupName(), request.getOwnerId(),
                    request.getInitialMembers(), request.getSequenceId());

            Client.getInstance().sendMessage(request);
            return true;
        } catch (Exception e) {
            logger.error("创建群组请求失败", e);
            return false;
        }
    }

    public boolean saveGroup(GroupInfo groupInfo) {
        try(SqlSession session = MyBatisUtil.getSqlSession()){
            logger.info("保存群组信息到本地数据库: groupId={}, groupName={}, ownerId={}",
                    groupInfo.getGroupId(), groupInfo.getGroupName(), groupInfo.getOwnerId());

            GroupMapper mapper = session.getMapper(GroupMapper.class);
            int result = mapper.saveGroup(groupInfo);

            // 如果成功保存群组基本信息，添加群成员关系
            if (result > 0) {
                // 添加自己作为群成员
                mapper.addGroupMember(groupInfo.getGroupId(),
                        Client.getInstance().getCurrentUser().getId().toString());
            }

            session.commit();
            return result > 0;
        } catch (Exception e) {
            logger.error("保存群组信息失败", e);
            return false;
        }
    }
    public List<GroupInfo> getUserGroups() {
        try(SqlSession session = MyBatisUtil.getSqlSession()) {
            GroupMapper mapper = session.getMapper(GroupMapper.class);
            return mapper.getUserGroups(Client.getInstance().getCurrentUser().getId().toString());
        } catch (Exception e) {
            logger.error("获取用户群组失败", e);
            return null;
        }
    }

}
