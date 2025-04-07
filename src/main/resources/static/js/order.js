'use strict'

$(function () {
    console.log("hello");
    //这里的selectedAddress是id
    let selectedAddress = $(".address.active").attr("data"); // 获取当前选中的地址
    // 获取当前选中地址的详细信息

    let $button1 = $('#submitCartItems');
    let $button2 = $('#submitItem');
    let $changeable = $('.changed');


    let priceText = document.querySelector(".price span").innerText; // 获取内部 <span> 的文本

    let priceValue = parseFloat(priceText); // 转换成浮点数

    let province = document.querySelector(".address-info span:nth-child(1)").innerText;
    let city = document.querySelector(".address-info span:nth-child(2)").innerText;
    let district = document.querySelector(".address-info span:nth-child(3)").innerText;
    let detail = document.querySelector(".address-info span:nth-child(5)").innerText;
    let receiver = document.querySelector(".address-info span:nth-child(7)").innerText;
    let phone = document.querySelector(".address-info span:nth-child(9)").innerText;

    console.log(selectedAddress);

    //获取地址
    function getAddress(){
        province = document.querySelector(".address-info span:nth-child(1)").innerText;
        city = document.querySelector(".address-info span:nth-child(2)").innerText;
        district = document.querySelector(".address-info span:nth-child(3)").innerText;
        detail = document.querySelector(".address-info span:nth-child(5)").innerText;
        receiver = document.querySelector(".address-info span:nth-child(7)").innerText;
        phone = document.querySelector(".address-info span:nth-child(9)").innerText;

        // 组合地址字符串
        selectedAddress = `${province}省 ${city}市 ${district}区<br>
                       详细地址：${detail}<br>
                       收件人：${receiver}<br>
                       电话：${phone}`;
    }

    $('.address-grid').click(function (){
        selectedAddress = $(".address.active").attr("data");
        console.log(selectedAddress);
    });

    $button1.click(function () {
        console.log("here1.....");

        // 新增订单
        $.ajax({
            url: "/CartHandler",
            type: "POST",
            data: { address: selectedAddress }, // 直接传递字符串
            success: function (response) {
                if (response === "success") {
                    getAddress();
                    // 生成模态框的 HTML 结构
                    let modalHtml = `
                    <div id="orderModal" class="modal">
                        <div class="modal-content">
                            <h2 class="center">订单确认</h2> <!-- 修改为居中对齐 -->
                            <div class="countdown">
                                <span id="countdown-timer" style="color: red;">15:00</span> <!-- 设置红色字体 -->
                            </div> 
                            <div class="address-info">
                                <h2>收货地址</h2>
                                <p>${selectedAddress}</p>
                            </div>
                            <h2>商品详情</h2>
                            <div class="order-summary">
                                ${$(".orderTable").prop('outerHTML')} <!-- 复制订单信息 -->
                            </div>
                            <div class="total-container">
                                <span class="total-text">订单总价：</span>
                                <span class="price">¥<span>${priceValue}</span></span>
                                <span class="changed"><button class="submit-order" id="payCartOrder">支付</button>
                                                      <button class="submit-order" id="notPay">暂不支付</button></span>
                            </div>
                        </div>
                    </div>
                `;

                    // 将模态框添加到 body 中
                    $("body").append(modalHtml);

                    // 显示模态框
                    $("#orderModal").fadeIn();

                    // 15分钟倒计时
                    let timeLeft = 15 * 60; // 15分钟转为秒
                    let countdownTimer = setInterval(function () {
                        let minutes = Math.floor(timeLeft / 60);
                        let seconds = timeLeft % 60;
                        $("#countdown-timer").text(
                            `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`
                        );

                        if (timeLeft <= 0) {
                            clearInterval(countdownTimer);
                            alert("临时订单已过期！");
                            $("#orderModal").fadeOut(function () {
                                $(this).remove();
                            });
                        }

                        timeLeft--;
                    }, 1000);
                } else {
                    alert("提交订单失败，请稍后重试！");
                }
            },
            error: function () {
                alert("提交订单请求失败，请稍后重试！");
            },
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
                    getAddress();
                    // 生成模态框的 HTML 结构
                    let modalHtml = `
                    <div id="orderModal" class="modal">
                        <div class="modal-content">
                            <h2 class="center">订单确认</h2> <!-- 修改为居中对齐 -->
                            <div class="countdown">
                                <span id="countdown-timer" style="color: red;">15:00</span> <!-- 设置红色字体 -->
                            </div> 
                            <div class="address-info">
                                <h2>收货地址</h2>
                                <p>${selectedAddress}</p>
                            </div>
                            <h2>商品详情</h2>
                            <div class="order-summary">
                                ${$(".orderTable").prop('outerHTML')} <!-- 复制订单信息 -->
                            </div>
                            <div class="total-container">
                                <span class="total-text">订单总价：</span>
                                <span class="price">¥<span>${priceValue}</span></span>
                                <span class="changed"><button class="submit-order" id="payOrder">支付</button>
                                                      <button class="submit-order" id="notPay">暂不支付</button></span>
                            </div>
                        </div>
                    </div>
                `;

                    // 将模态框添加到 body 中
                    $("body").append(modalHtml);

                    // 显示模态框
                    $("#orderModal").fadeIn();



                    // 15分钟倒计时
                    let timeLeft = 15 * 60; // 15分钟转为秒
                    let countdownTimer = setInterval(function () {
                        let minutes = Math.floor(timeLeft / 60);
                        let seconds = timeLeft % 60;
                        $("#countdown-timer").text(
                            `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`
                        );

                        if (timeLeft <= 0) {
                            clearInterval(countdownTimer);
                            alert("临时订单已过期！");
                            $("#orderModal").fadeOut(function () {
                                $(this).remove();
                            });
                        }

                        timeLeft--;
                    }, 1000);
                } else {
                    alert("该商品已售罄！")
                }
            },
            error: function (xhr, status, error){
                console.error("状态码:", xhr.status);  // HTTP 状态码
                console.error("响应文本:", xhr.responseText);  // 服务器返回的错误信息
                console.error("错误:", error);  // jQuery 解析错误信息
                alert("提交订单失败，请稍后重试！错误码：" + xhr.status);
            }
        });
    });

    // 状态转换（给动态创建的按钮绑定事件）
    $(document).on('click', '#payCartOrder', function () {
        console.log('here2....');
        $.ajax({
            url: "/statusChangeTo1",
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
            url: "/statusChangeTo1",
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