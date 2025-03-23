package org.csu.demo.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/*用户*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private int id;

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

    private String email;

    private int age;

    private String responsibility;

    // user_status
    private int isOnline;
    private int isFrozen;
    // 数据库中用tinyint表示，这里应该用int的

    // 信誉
//    @NotBlank(message = "冻结原因不能为空") // 校验不能为空，会送到@validated进行校验
    private String frozenReason;
    // 信誉
    private int credit;

    private int merchantCredit;
    @Setter
    private String stars;

    private List<Item> cart = new ArrayList<>();

}
