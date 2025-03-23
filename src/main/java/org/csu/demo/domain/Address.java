package org.csu.demo.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    // mybatis plus！！
//    private int id;
//
//    @Value("user_id")
//    private int userId;
//
//    private String province;
//    private String city;
//    private String district;
//
//    @Value("detail_address")
//    private String detailAddress;
//
//    @Value("phone_number")
//    private String phoneNumber;
//
//    @Value("receiver_name")
//    private String receiverName;
//
//    @Value("is_default")
//    private boolean isDefault;

    private int id;
    private int userId;

    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String phoneNumber;
    private String receiverName;
    private Integer isDefault;
}
