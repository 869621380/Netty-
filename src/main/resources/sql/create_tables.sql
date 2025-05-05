-- 好友列表表
CREATE TABLE IF NOT EXISTS friend_list (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id TEXT NOT NULL,
    friend_id TEXT NOT NULL,
    friend_nickname TEXT NOT NULL,
    friend_avatar TEXT,
    create_time TEXT DEFAULT (datetime('now','localtime')),
    UNIQUE(user_id, friend_id)
    );

-- 群组信息表
CREATE TABLE IF NOT EXISTS group_info (
    group_id TEXT PRIMARY KEY,
    group_name TEXT NOT NULL,
    group_avatar TEXT,
    owner_id TEXT NOT NULL,
    create_time TEXT DEFAULT (datetime('now','localtime')),
    update_time TEXT DEFAULT (datetime('now','localtime'))
    );

-- 群组成员表
CREATE TABLE IF NOT EXISTS group_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_id TEXT NOT NULL,
    member_id TEXT NOT NULL,
    member_nickname TEXT,
    member_role INTEGER NOT NULL, -- 0:成员 1:管理员 2:群主
    join_time TEXT DEFAULT (datetime('now','localtime')),
    UNIQUE(group_id, member_id)
    );