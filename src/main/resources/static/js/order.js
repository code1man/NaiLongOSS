'use strict'

$(function () {
    console.log("hello");
    //这里的selectedAddress是id
    let selectedAddress = $(".address.active").attr("data"); // 获取当前选中的地址
    let $button1 = $('#submitCartItems');
    let $button2 = $('#submitItem');
    let $changeable = $('.changed');

    console.log(selectedAddress);

    $('.address-grid').click(function (){
        selectedAddress = $(".address.active").attr("data");
        console.log(selectedAddress);
    });

    $button1.click(function () {
        console.log("here1.....")
        //新增订单
        $.ajax({
            url: "/CartHandler",
            type: "POST",
            data: {address: selectedAddress}, // 直接传递字符串
            success: function (response) {
                if (response === "success") {
                    alert("订单提交成功！");
                    $changeable.html('<button class="submit-order" id = "payCartOrder">支付</button>'+
                                           '<button class="submit-order" id = "notPay">暂时不支付</button>');
                } else {
                    alert("提交订单失败，请稍后重试！");
                }
            },
            error: function () {
                alert("提交订单请求失败，请稍后重试！");
            }
        });

    });

    $button2.click(function () {
        console.log("here.....")
        //新增订单
        $.ajax({
            url: "/ItemHandler",
            type: "POST",
            data: {address: selectedAddress}, // 直接传递字符串
            success: function (response) {
                if (response === "success") {
                    alert("订单提交成功！");
                    $changeable.html('<button class="submit-order" id = "payOrder">支付</button>'+
                                            '<button class="submit-order" id = "notPay">暂时不支付</button>');
                } else {
                    alert("提交订单失败，请稍后重试！");
                }
            },
            error: function () {
                alert("提交订单请求失败，请稍后重试！");
            }
        });
    });

    // 状态转换（给动态创建的按钮绑定事件）
    $(document).on('click', '#payCartOrder', function () {
        console.log('here2....');
        $.ajax({
            url: "/statusChange",
            type: "POST",
            data: {nextStatus: "1", behavior: "payCartOrder"}, // 直接传递字符串
            success: function (response) {
                if (response === "success") {
                    alert("支付成功！即将跳回主页");
                    window.location.href = "/mainForm"; // 回到主页
                } else {
                    alert("1:支付失败，请稍后重试！");
                }
            },
            error: function (xhr, status, error) {
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    });

    // 状态转换（给动态创建的按钮绑定事件）
    $(document).on('click', '#payOrder', function () {
        console.log('here2....');
        $.ajax({
            url: "/statusChange",
            type: "POST",
            data: {nextStatus: "1", behavior: "payOrder"}, // 直接传递字符串
            success: function (response) {
                if (response === "success") {
                    alert("支付成功！即将跳回主页");
                    window.location.href = "/mainForm"; // 回到主页
                } else {
                    alert("1:支付失败，请稍后重试！");
                }
            },
            error: function (xhr, status, error) {
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("2:支付失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    });

    //不想支付
    $(document).on('click', '#notPay', function () {
        window.location.href = "/mainForm"; // 回到主页
    });



});