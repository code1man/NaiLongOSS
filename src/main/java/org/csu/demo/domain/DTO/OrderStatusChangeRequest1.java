package org.csu.demo.domain.DTO;

import lombok.Data;
import org.csu.demo.domain.Order;

import java.util.List;

@Data
public class OrderStatusChangeRequest1 {
    private String behavior;
    private String nextStatus;
    private List<String> currentOrderList;
}
