package org.example.Model.Domain;

import lombok.Data;

@Data
public class GroupInfo {
    private String groupId;
    private String groupName;
    private String ownerId;
    private String createTime;
    private String updateTime;
}

