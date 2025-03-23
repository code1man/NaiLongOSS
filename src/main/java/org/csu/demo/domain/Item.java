package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.io.Serializable;

/*商品*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 绑定数据库字段
public class Item{
    private int id;
    private String name;
    private int product_id;
    private String url;
    private int price;
    private String description;
    private int businessId;
    private boolean isListing;
    private boolean isDelete;

    private int remainingNumb;
}
