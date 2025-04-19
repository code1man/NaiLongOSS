package org.csu.demo.domain.DTO;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class OrderStatusChangeRequest2 {

    String orderId;
    String nextStatus;

}
