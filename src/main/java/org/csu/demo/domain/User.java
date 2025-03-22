package org.csu.demo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*用户*/
@Data//通过lombok自动生成getter和setter还有toString方法还有equals和hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("userInfo")
public class User {
    private int id;

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

    private int age;
    private String email;
    private String responsibility;

    //user_status
    private boolean is_online=false;
    private boolean is_frozen=false;

    //信誉
    @NotBlank(message = "冻结原因不能为空")//校验不能为空，会送到@validated进行校验
    private String frozen_reason="不喜欢奶龙的小朋友，你好呀，你的账号已经被奶龙风风光光了";
    //信誉
    private int credit;

    private List<Item> cart=new ArrayList<>();
}
