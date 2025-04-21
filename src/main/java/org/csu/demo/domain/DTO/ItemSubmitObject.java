package org.csu.demo.domain.DTO;

import lombok.Data;
import org.csu.demo.domain.Item;

//用于前端返回数据打包
@Data
public class ItemSubmitObject {
    private int userId;
    private String addressID;
    private Item item;
}
