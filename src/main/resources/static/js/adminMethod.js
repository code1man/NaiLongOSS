"use strict";

$(function () {
    let isAdmin = false; // 初始化判断变量
    if($('.admin').length > 0) {
        console.log("管理员页面……");
        isAdmin = true;  // 如果页面中有 .admin 元素，则是管理员
    }

    // 每10秒刷新一次订单状态
    setInterval(function() {
        updateOrderStatus();  // 调用刷新状态的函数
    }, 10000); // 10秒

    function updateOrderStatus() {
        // 判断管理员身份并执行不同操作
        if (isAdmin) {
            // 如果是管理员，调用管理员相关接口
            $.ajax({
                url: '/admin/Control',
                type: 'GET',
                success: function(response) {
                    const orderItems = JSON.parse(response);

                    orderItems.forEach(function(orderItem) {
                        const orderElement = document.querySelector(`[data-id='${orderItem.order_id}']`).closest('tr');
                        const statusDiv = orderElement.querySelector('.order-status');
                        updateAdminButtons(orderItem, statusDiv);
                    });
                },
                error: function() {
                    console.error("管理员更新订单状态失败！");
                }
            });
        } else {
            // 如果是普通用户，调用普通用户相关接口
            $.ajax({
                url: '/updateMyOrder',
                type: 'GET',
                success: function(response) {
                    const orderItems = JSON.parse(response);

                    orderItems.forEach(function(orderItem) {
                        const orderElement = document.querySelector(`[data-id='${orderItem.order_id}']`).closest('tr');
                        const statusDiv = orderElement.querySelector('.order-status');
                        updateUserButtons(orderItem, statusDiv);
                    });
                },
                error: function() {
                    console.error("更新订单状态失败！");
                }
            });
        }
    }

    // 管理员端的按钮更新（例如修改订单状态、管理订单等）
    function updateAdminButtons(orderItem, statusDiv) {
        console.log("管理员刷新订单状态……");
        let status = orderItem.status;

        // 清空当前状态显示的按钮
        statusDiv.innerHTML = '';

        // 根据状态更新管理员的操作按钮
        switch (status) {
            case 0:  // 待支付
                statusDiv.innerHTML = `
                <button>取消订单</button>
                <button>支付</button>
                <button>查看订单信息</button>
                `;
                break;
            case 1:  // 已支付
                statusDiv.innerHTML = `
                <button>发货</button>
                <button>查看订单信息</button>
                `;
                break;
            case 2:  // 已发货
                statusDiv.innerHTML = `
                <button>查看订单信息</button>
                <button>确认收货</button>
                `;
                break;
            case 3:  // 待取件
                statusDiv.innerHTML = `
                <button>提醒取件</button>
                <button>查看订单信息</button>
                `;
                break;
            case 4:  // 已签收
                statusDiv.innerHTML = `
                <button>查看订单信息</button>
                `;
                break;
            case 5:  // 商家已处理售后
                statusDiv.innerHTML = `
                <button>查看订单信息</button>
                `;
                break;
            case 10: // 已取消订单
                statusDiv.innerHTML = '';  // 不显示按钮
                break;
            case 11: // 已申请售后
                statusDiv.innerHTML = `
                <button>处理售后</button>
                <button>查看订单信息</button>
                `;
                break;
            default:
                break;
        }
    }


});
