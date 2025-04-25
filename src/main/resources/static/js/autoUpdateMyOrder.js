"use strict"

$(function (){

    let isBusiness;
    if($('.business').length > 0){
        console.log("商家……")
        isBusiness = 1;
    }

    // 每5秒刷新一次订单状态
    setInterval(function() {
        updateOrderStatus();  // 调用刷新状态的函数
    }, 5000); // 5秒


    function updateOrderStatus() {
        // 发送 AJAX 请求到服务器，获取最新的订单项数据
        if (!isBusiness){
            $.ajax({
                url: '/updateMyOrder',
                type: 'GET',
                success: function(response) {
                    // 解析 JSON 响应
                    const orderItems = JSON.parse(response);

                    // 获取当前页面上的所有订单 ID
                    let displayedOrders = new Set();
                    document.querySelectorAll(".order-status[data-id]").forEach(element => {
                        displayedOrders.add(element.getAttribute("data-id"));
                    });

                    // 遍历返回的 orderItems
                    orderItems.forEach(function(orderItem) {
                        // 选择对应的 .order-status
                        const statusDiv = document.querySelector(`.order-status[data-id='${orderItem.order_id}']`);
                        if (!statusDiv) {
                            console.warn("订单项未找到，跳过更新:", orderItem.order_id);
                            return;
                        }

                        // 获取所在的 <tr> 行
                        const orderElement = statusDiv.closest('tr');
                        if (!orderElement) {
                            console.warn("未找到订单项的 <tr>, 订单ID:", orderItem.order_id);
                            return;
                        }

                        let $statusTd = orderElement.querySelector('.status');

                        // 根据状态更新按钮
                        updateButtons1(orderItem, statusDiv, $statusTd);

                        // 订单仍然有效，从集合中移除
                        displayedOrders.delete(orderItem.order_id);
                    });

                    // 删除前端仍然显示但不在 orderItems 中的订单（即超时订单）
                    displayedOrders.forEach(orderId => {
                        const element = document.querySelector(`.order-status[data-id='${orderId}']`);
                        if (element) {
                            const row = element.closest('tr');
                            if (row) {
                                row.remove();
                            }
                        }
                    });
                },
                error: function() {
                    console.error("更新订单状态失败！");
                }
            });
        }else{

            $.ajax({
                url: '/business/updateMyOrder',
                type: 'GET',
                success: function(response) {
                    // 解析 JSON 响应
                    const orderItems = JSON.parse(response);

                    // 获取当前页面上的所有订单 ID
                    let displayedOrders = new Set();
                    document.querySelectorAll(".order-status[data-id]").forEach(element => {
                        displayedOrders.add(element.getAttribute("data-id"));
                    });

                    // 遍历返回的 orderItems
                    orderItems.forEach(function(orderItem) {
                        let statusDiv = document.querySelector(`.order-status[data-id='${orderItem.order_id}']`);
                        let orderElement;

                        if (!statusDiv) {
                            //  创建新的订单行（新订单）
                            orderElement = createOrderRow(orderItem);
                            const tableBody = document.querySelector(".order-table-body");
                            tableBody.insertBefore(orderElement, tableBody.firstChild);  // 插入最上方

                            statusDiv = orderElement.querySelector(`.order-status`);
                        } else {
                            orderElement = statusDiv.closest('tr');
                        }

                        if (!orderElement) {
                            console.warn("未找到订单项的 <tr>, 订单ID:", orderItem.order_id);
                            return;
                        }

                        let $statusTd = orderElement.querySelector('.status');

                        // 根据状态更新按钮
                        updateButtons2(orderItem, statusDiv, $statusTd);

                        // 订单仍然有效，从集合中移除
                        displayedOrders.delete(orderItem.order_id);
                    });

                    // 删除前端仍然显示但不在 orderItems 中的订单（即超时订单）
                    displayedOrders.forEach(orderId => {
                        const element = document.querySelector(`.order-status[data-id='${orderId}']`);
                        if (element) {
                            const row = element.closest('tr');
                            if (row) {
                                row.remove();
                            }
                        }
                    });
                },

                error: function() {
                    console.error("更新订单状态失败！");
                }
            });

        }

    }

    function updateButtons1(orderItem, statusDiv,statusTd) {
        console.log("刷新1……");
        let status = orderItem.status;

        // 清空当前状态显示的按钮
        statusDiv.innerHTML = '';

        // 根据状态进行相应的显示
        switch (status) {
            case 0:  // 待支付
            {
                statusDiv.innerHTML = `
                <button class = "cancel">取消订单</button>
                <button class = "pay">支付</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 1:  // 已支付
            {
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 2:  // 已发货
            {
                statusTd.innerHTML = '<span>已发货</span>';
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                <button class="confirm">确认收货</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 3:  // 待取件
            {
                statusTd.innerHTML= '<span>待取件</span>';
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                <button class="confirm">确认收货</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 4:  // 已签收
            {
                statusTd.innerHTML = '<span>已签收</span>';
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 5:  // 商家已处理售后
            {
                statusTd.innerHTML = '<span>已处理售后</span>';
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                <button class="check_solve">查看处理结果</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 10: // 已取消订单
            {
                statusTd.innerHTML = '<span>已取消订单</span>';
                statusDiv.innerHTML = '';  // 不显示按钮
                break;
            }
            case 11: // 已申请售后
            {
                statusTd.innerHTML = '<span>已申请售后</span>';
                statusDiv.innerHTML = `
                <button class="after_sale">申请售后</button>
                <button class="confirm">确认收货</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            default:
                break;
        }
    }

    function updateButtons2(orderItem, statusDiv,statusTd) {
        console.log("刷新2……");
        let status = orderItem.status;

        // 清空当前状态显示的按钮
        statusDiv.innerHTML = '';

        // 根据状态进行相应的显示
        switch (status) {
            case 0:  // 待支付
            {
                statusTd.innerHTML= '<span>待客户支付</span>';
                statusDiv.innerHTML = `
                    <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 1:  // 已支付
            {
                statusTd.innerHTML='<span>客户已支付</span>';
                statusDiv.innerHTML = `
                <button class="send">发货</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 2:  // 已发货
            {
                statusTd.innerHTML='<span>已发货</span>';
                statusDiv.innerHTML = `
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 3:  // 待取件
            {
                statusTd.innerHTML='<span>待取件</span>';
                statusDiv.innerHTML = `
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 4:  // 已签收
            {
                statusTd.innerHTML='<span>客户已签收</span>';
                statusDiv.innerHTML = `
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 5:  // 商家已处理售后
            {
                statusTd.innerHTML='<span>已处理售后</span>';
                statusDiv.innerHTML = `
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            case 10: // 已取消订单
            {
                statusTd.innerHTML='<span>客户已取消订单</span>';
                statusDiv.innerHTML = '<button>查看订单信息</button>';
                break;
            }
            case 11: // 已申请售后
            {
                statusTd.innerHTML='<span>客户已申请售后</span>';
                statusDiv.innerHTML = `
                <button class="solve">处理售后</button>
                <button class="check_info">查看订单信息</button>
            `;
                break;
            }
            default:
                break;
        }
    }


    function createOrderRow(orderItem) {
        const statusMap = {
            0: "待客户支付",
            1: "客户已支付",
            2: "已发货",
            3: "待取件",
            4: "客户已签收",
            5: "已处理售后",
            10: "客户已取消订单",
            11: "客户已申请售后"
        };

        const buttonMap = {
            0: `<button class="check_info">查看订单信息</button>`,
            1: `<button class="send">发货</button><button class="check_info">查看订单信息</button>`,
            2: `<button class="check_info">查看订单信息</button>`,
            3: `<button class="check_info">查看订单信息</button>`,
            4: `<button class="check_info">查看订单信息</button>`,
            5: `<button class="check_info">查看订单信息</button>`,
            10: `<button class="check_info">查看订单信息</button>`,
            11: `<button class="solve">处理售后</button><button class="check_info">查看订单信息</button>`
        };

        const subtotal = (orderItem.price * orderItem.itemNum).toFixed(2);

        const row = document.createElement('tr');
        row.innerHTML = `
        <td>
            <img src="${orderItem.url}" style="width: 100px; height: auto;" alt="商品图片">
            <br>
            <span>${orderItem.name}</span>
        </td>
        <td>¥<span>${orderItem.price}</span></td>
        <td><span>${orderItem.itemNum}</span></td>
        <td>¥<span>${subtotal}</span></td>
        <td>
            <span class="status">${statusMap[orderItem.status]}</span>
        </td>
        <td>
            <div class="order-status" data-id="${orderItem.order_id}">
                ${buttonMap[orderItem.status] || ""}
            </div>
        </td>
    `;
        return row;
    }

});