'use strict'

$(function (){
    //发货
    $(document).on('click', '.send', function(){

        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let statusTd = $(this).closest('tr').find('.status');
        $.ajax({
            url: "/business/statusChange",
            type: "POST",
            data: {nextStatus: "2", orderId: orderId}, // 直接传递字符串
            success: function (response){
                console.log("已发货");
                //修改dom
                buttonDiv.html('<button class="check_info">查看订单信息</button>');
                statusTd.html('<span>已发货</span>'); // 更新订单状态
            },
            error: function (xhr, status, error){
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    });

    //处理售后
    $(document).on('click', '.solve', function() {
        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let statusTd = $(this).closest('tr').find('.status');
        $.ajax({
            url: "/business/getAfterSaleInfo",
            type: "GET",
            data: {orderId: orderId}, // 直接传递字符串
            success: function (response){
                switch (response){
                    case '1': alert("要求换货");break;
                    case '2': alert("要求退货退款");break;
                    case '3': alert("要求仅退款");break;
                }
                $.ajax({
                    url: "/business/statusChange",
                    type: "POST",
                    data: {nextStatus: "5",orderId: orderId}, // 直接传递字符串
                    success: function (response){
                        console.log("已处理");
                        //修改dom
                        buttonDiv.html('<button class="check_info">查看订单信息</button>');
                        statusTd.html('<span>已处理售后</span>'); // 更新订单状态
                    },
                    error: function (xhr, status, error){
                        console.error("状态码:", xhr.status);  // HTTP 状态码
                        console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                        console.error("错误:", error);  // jQuery 解析错误信息
                        alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
                    }
                });

            },
            error: function (xhr, status, error){
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    });

    //查看订单按钮
    $(document).on('click', '.check_info', function() {
        // 获取当前点击的按钮的父 div 元素
        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let $statusTd = $('#status');
        let address;
        let order;

        // 先获取 address，再获取 order，最后执行逻辑
        getAddress(orderId, function (address) {
            getOrder(orderId, function (order) {
                console.log('最终获取的 address:', address);
                console.log('最终获取的 order:', order);

                let selectedAddress = `${address.province}省 ${address.city}市 ${address.district}区<br>
                               详细地址：${address.detailAddress}<br>
                               收件人：${address.receiverName}<br>
                               电话：${address.phoneNumber}`;

                console.log("最终生成的 selectedAddress:", selectedAddress);
                // 这里可以更新模态框等
                // 避免重复生成模态框
                $("#orderModal").remove();

                // 生成模态框的 HTML 结构
                let modalHtml = `
            <div id="orderModal" class="modal">
                <div class="modal-content">
                     <span class="close">&times;</span>
                    <div class="address-info">
                        <h2>收货地址</h2>
                        <p>${selectedAddress}</p>
                    </div>
                   <div class="order-summary">
                        ${order.order_id ? `<span><strong>订单号：</strong> ${order.order_id}</span><br>` : ""}
                        ${order.client ? `<span><strong>下单用户：</strong> ${order.client}</span><br>` : ""}
                        ${order.item_id ? `<span><strong>商品ID：</strong> ${order.item_id}</span><br>` : ""}
                        ${order.amount ? `<span><strong>商品数量：</strong> ${order.amount}</span><br>` : ""}
                        ${order.total_price ? `<span><strong>订单总金额：</strong> ${order.total_price}</span><br>` : ""}
                        ${order.supplier ? `<span><strong>供应商ID：</strong> ${order.supplier}</span><br>` : ""}
                        ${order.create_time ? `<span><strong>订单创建时间：</strong> ${order.create_time}</span><br>` : ""}
                        ${order.pay_time ? `<span><strong>支付时间：</strong> ${order.pay_time}</span><br>` : ""}
                        ${order.pay_method ? `<span><strong>支付方式：</strong> ${order.pay_method}</span><br>` : ""}
                        ${order.ship_time ? `<span><strong>发货时间：</strong> ${order.ship_time}</span><br>` : ""}
                        ${order.confirm_time ? `<span><strong>确认收货时间：</strong> ${order.confirm_time}</span><br>` : ""}
                        ${order.after_sale_time ? `<span><strong>申请售后时间：</strong> ${order.after_sale_time}</span><br>` : ""}
                        ${order.remark ? `<span><strong>备注：</strong> ${order.remark}</span><br>` : ""}
                
                        <div class="total-container">
                            <span class="total-text">订单总价：</span>
                            <span class="price">¥${order.total_price}</span>
                        </div>
                    </div>
            </div>
        `;

                // 将模态框添加到 body
                $("body").append(modalHtml);
                $("#orderModal").fadeIn();


                // 关闭模态框
                $(".close").click(function () {
                    $("#orderModal").fadeOut(function () {
                        $(this).remove();
                    });
                });
            });
        });


    })

    function getAddress(orderId, callback) {
        $.ajax({
            url: "/getAddress",
            type: "GET",
            data: { orderId: orderId },
            success: function (response) {
                console.log('服务器返回的地址数据:', response);
                let address = JSON.parse(response);
                console.log('解析后的 address:', address);
                callback(address); // 通过回调函数返回 address
            },
            error: function (xhr, status, error) {
                console.error("状态码:", xhr.status);
                console.error("响应文本:", xhr.responseText);
                console.error("错误:", error);
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    }

    function getOrder(orderId, callback) {
        $.ajax({
            url: "/getOrder",
            type: "GET",
            data: { orderId: orderId },
            success: function (response) {
                let order = JSON.parse(response);
                console.log('解析后的 order:', order);
                callback(order); // 通过回调函数返回 order
            },
            error: function (xhr, status, error) {
                console.error("状态码:", xhr.status);
                console.error("响应文本:", xhr.responseText);
                console.error("错误:", error);
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    }
});