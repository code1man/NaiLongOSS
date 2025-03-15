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
@Data
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
    @TableField(exist = false)
    private boolean is_online=false;
    @TableField(exist = false)
    private boolean is_frozen=false;
    @TableField(exist = false)
    private String frozen_reason="";
//信誉
    @TableField(exist = false)
    private int credit;

    @TableField(exist = false)
    private List<Item> cart=new ArrayList<>();

}
