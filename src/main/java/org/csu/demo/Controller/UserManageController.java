package org.csu.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManageController {

    @GetMapping("/UserManage")
    public String UserManage() {
        return "UserManage";
    }
}
