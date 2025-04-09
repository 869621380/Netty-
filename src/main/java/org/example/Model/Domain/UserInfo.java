package org.example.Model.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    //用户账号id
    private Integer id;
    //用户名
    private String nickname;
    //头像路径
    private String avatarPath;
    //邮箱
    private String email;
    //账号创建时间
    @Format(formats = "yyyy-MM-dd hh:mm::ss")
    private LocalDate createTime;
    //上次更新时间
    @Format(formats = "yyyy-MM-dd hh:mm::ss")
    private LocalDate updateTime;
}
