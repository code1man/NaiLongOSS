'use strict'

$(function (){

    //查看处理结果事件
    $(document).on('click', '.check_solve', function() {
        let buttonDiv = $(this).closest('.order-status');
        let orderId = buttonDiv.data('id'); // 获取订单ID

        $.ajax({
            url: '/afterSale/getAfterSaleInfo',
            type: 'GET',
            data: {orderId: orderId},
            success: function(response) {
                let data = JSON.parse(response); // 解析 JSON 字符串

                // 状态文字映射函数
                function getStatusText(code) {
                    switch(code) {
                        case -1: return '已拒绝';
                        case 0: return '未处理';
                        case 1: return '已同意';
                        default: return '未知状态';
                    }
                }

                // 赋值状态文本
                $('#businessSolveResult').text(getStatusText(data.business_solve));
                $('#adminSolveResult').text(getStatusText(data.admin_solve));

                // 赋值时间（注意格式化时间，可自定义格式）
                $('#businessSolveTime').text(data.business_solve_time || '暂无时间');
                $('#adminSolveTime').text(data.admin_solve_time || '暂无时间');
                // 打开模态框
                $('#solveModal').show();
            },
            error: function() {
                alert('请求失败，请检查网络或服务器！');
            }
        });
    });

// 模态框关闭按钮
    $('#closeSolveModal').on('click', function() {
        $('#solveModal').hide();
    });


    // 申请售后按钮点击事件
    $(document).on('click', '.after_sale', function() {
        // 获取当前点击的按钮的父 div 元素
        let buttonDiv = $(this).closest('.order-status');
        let orderId = buttonDiv.data('id'); // 获取订单ID
        let statusTd = $(this).closest('tr').find('.status'); // 获取状态列

        // 显示模态框
        $('#myModal').fadeIn();

        // 在模态框的按钮点击事件中发送异步请求
        $('.action-btn').click(function () {
            const buttonId = $(this).data('id'); // 获取按钮ID
            console.log('按钮ID：', buttonId);
            console.log('订单ID：', orderId); // 打印当前订单ID

            // 发送 AJAX 请求到 Spring MVC Controller
            $.ajax({
                url: '/afterSale/buttonClick/' + orderId, // 请求路径，将orderId拼接到路径中
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ id: buttonId }), // 发送的请求数据，包含按钮ID
                success: function (response) {
                    $('#myModal').fadeOut(); // 请求成功后关闭模态框
                },
                error: function () {
                    alert('请求失败，请检查网络或服务器！'); // 请求失败后提示
                }
            });

            $.ajax({
                url: "/statusChange",
                type: "Post",
                data: {nextStatus: "11", orderId: orderId}, // 直接传递字符串
                success: function (response){
                    if (response === "success"){
                        console.log("来了……")
                        buttonDiv.html('<button class="after_sale">申请售后</button>\n                ' +
                            '<button class="confirm">确认收货</button>\n                ' +
                            '<button class="check_info">查看订单信息</button>'); // 清空按钮
                        statusTd.html('<span>已申请售后</span>');
                    }
                },
                error: function (xhr, status, error){
                    console.error("状态码:", xhr.status);  // HTTP 状态码
                    console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                    console.error("错误:", error);  // jQuery 解析错误信息
                    alert("申请售后失败，请稍后重试！错误码：" + xhr.status);
                }
            })
        });

        // 关闭模态框
        $('#closeModal').click(function () {
            $('#myModal').fadeOut();
        });
    });


    //取消订单按钮
    $(document).on('click', '.cancel', function() {
        // 获取当前点击的按钮的父 div 元素
        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let statusTd = $(this).closest('tr').find('.status');

        // 输出 order_id
        console.log(orderId);

        $.ajax({
            url: "/statusChange",
            type: "Post",
            data: {nextStatus: "10", orderId: orderId}, // 直接传递字符串
            success: function (response){
                if (response === "success"){
                    alert("订单取消……");
                    buttonDiv.html(''); // 清空按钮
                    statusTd.html('<span>已取消订单</span>'); // 更新订单状态
                }
            },
            error: function (xhr, status, error){
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        })
    })

    //支付按钮
    $(document).on('click', '.pay', function() {
        // 获取当前点击的按钮的父 div 元素
        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let statusTd = $(this).closest('tr').find('.status');
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
                    <div class="address-info">
                        <h2>收货地址</h2>
                        <p>${selectedAddress}</p>
                    </div>
                    <!--<h2>商品详情</h2>
                    <div class="order-summary">
                        <span>订单号</span>
                        <span>用户ID</span>
                        <span>收货地址ID</span>
                        <span>商品ID</span>
                        <span>商品数量</span>
                        <span>订单总金额</span>
                        <span>供应商ID</span>
                        <span>订单状态</span>
                        <span>订单创建时间</span>
                        <span>支付时间</span>
                        <span>支付方式</span>
                        <span>发货时间</span>
                        <span>确认收货时间</span>
                        <span>申请售后时间</span>
                        <span>备注</span>
                    </div>-->
                    <div class="total-container">
                        <span class="total-text">订单总价：</span>
                        <span class="price">¥<span>${order.total_price}</span></span>
                        <span class="changed">
                            <button class="submit-order" id="yes">确认支付</button>
                            <button class="submit-order" id="no">暂不支付</button>
                        </span>
                    </div>
                </div>
            </div>
        `;

                // 将模态框添加到 body
                $("body").append(modalHtml);
                $("#orderModal").fadeIn();

            });
        });

        $(document).on('click', '#yes', function() {
            $("#orderModal").remove();
            $.ajax({
                url: "/statusChange",
                type: "POST",
                data: {nextStatus: "1", orderId: orderId}, // 直接传递字符串
                success: function (response){
                    console.log("已购买");
                    //修改dom
                    buttonDiv.html('<button class="after_sale">申请售后</button>\n' +

                        '                            <button class="check_info">查看订单信息</button>');
                    statusTd.html('<span>已支付</span>'); // 更新订单状态
                },
                error: function (xhr, status, error){
                    console.error("状态码:", xhr.status);  // HTTP 状态码
                    console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                    console.error("错误:", error);  // jQuery 解析错误信息
                    alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
                }
            });
        });

        $(document).on('click', '#no', function() {
            $("#orderModal").remove();
        });

    })

    //确认签收
    $(document).on('click', '.confirm', function() {
        // 获取当前点击的按钮的父 div 元素
        let buttonDiv = $(this).closest('.order-status')
        let orderId = buttonDiv.data('id');
        let statusTd = $(this).closest('tr').find('.status');
        $.ajax({
            url: "/statusChange",
            type: "POST",
            data: {nextStatus: "4", orderId: orderId}, // 直接传递字符串
            success: function (response){
                console.log("已签收");
                alert("已签收");
                //修改dom
                buttonDiv.html('<button class="after_sale">申请售后</button>\n' +

                    '                            <button class="check_info">查看订单信息</button>');
                statusTd.html('<span>已签收</span>'); // 更新订单状态
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
        let statusTd = $(this).closest('tr').find('.status');  // 选择包含状态的 <td> 元素
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