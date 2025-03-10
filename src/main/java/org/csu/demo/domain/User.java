package org.csu.demo.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*用户*/
@Data
@AllArgsConstructor
@Builder
public class User {

    private int id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private int age;
    private String email;
    private String responsibility;

    private final List<Item> hasBeenPutInShoppingCartProducts = new ArrayList<Item>();

    public User(){};
}
