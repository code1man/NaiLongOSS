"use strict"

$(function (){

    let isBusiness;
    if($('.business').length > 0){
        console.log("商家……")
        isBusiness = 1;
    }

    // 每30秒刷新一次订单状态
    setInterval(function() {
        updateOrderStatus();  // 调用刷新状态的函数
    }, 10000); // 10秒


    function updateOrderStatus() {
        // 发送 AJAX 请求到服务器，获取最新的订单项数据
        $.ajax({
            url: '/updateMyOrder',
            type: 'GET',
            success: function(response) {
                // 解析 JSON 响应
                const orderItems = JSON.parse(response);

                // 遍历返回的 orderItems
                orderItems.forEach(function(orderItem) {
                    // 找到对应的订单项并更新 HTML
                    const orderElement = document.querySelector(`[data-id='${orderItem.order_id}']`).closest('tr');
                    const statusDiv = orderElement.querySelector('.order-status');
                    console.log("hh:"+orderItem.order_id);
                    console.log('orderElement'+orderElement);
                    console.log('statusDiv'+statusDiv);
                    // 根据状态更新按钮
                    if(isBusiness)
                        updateButtons2(orderItem, statusDiv);
                    updateButtons1(orderItem, statusDiv);
                });
            },
            error: function() {
                console.error("更新订单状态失败！");
            }
        });
    }

    function updateButtons1(orderItem, statusDiv) {
        console.log("刷新1……");
        let status = orderItem.status;

        // 清空当前状态显示的按钮
        statusDiv.innerHTML = '';

        // 根据状态进行相应的显示
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
                <button>申请售后</button>
                <button>催发货</button>
                <button>查看订单信息</button>
            `;
                break;
            case 2:  // 已发货
                statusDiv.innerHTML = `
                <button>申请售后</button>
                <button>确认收货</button>
                <button>查看订单信息</button>
            `;
                break;
            case 3:  // 待取件
                statusDiv.innerHTML = `
                <button>申请售后</button>
                <button>确认收货</button>
                <button>查看订单信息</button>
            `;
                break;
            case 4:  // 已签收
                statusDiv.innerHTML = `
                <button>申请售后</button>
                <button>确认收货</button>
                <button>查看订单信息</button>
            `;
                break;
            case 5:  // 商家已处理售后
                statusDiv.innerHTML = `
                <button>申请售后</button>
                <button>确认收货</button>
                <button>查看订单信息</button>
            `;
                break;
            case 10: // 已取消订单
                statusDiv.innerHTML = '';  // 不显示按钮
                break;
            case 11: // 已申请售后
                statusDiv.innerHTML = `
                <button>申请售后</button>
                <button>确认收货</button>
                <button>查看订单信息</button>
            `;
                break;
            default:
                break;
        }
    }

    function updateButtons2(orderItem, statusDiv) {
        console.log("刷新2……");
        let status = orderItem.status;

        // 清空当前状态显示的按钮
        statusDiv.innerHTML = '';

        // 根据状态进行相应的显示
        switch (status) {
            case 0:  // 待支付
                statusDiv.innerHTML = `
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
<!--                <button>确认收货</button>-->
<!--                <button>查看订单信息</button>-->
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
                statusDiv.innerHTML = '<button>查看订单信息</button>';

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