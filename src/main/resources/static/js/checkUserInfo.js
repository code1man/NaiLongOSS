$(function () {
    $('#username').on('blur', function () {
        let username = $(this).val().trim();  // 获取输入框的值并去除空格
        checkUsername(username);
    });

    function checkUsername(username) {
        if (username === "" || username.length === 0) {
            $('#feedback').text("用户名不可为空");
            return;
        }
        if (username.length < 2) {
            $('#feedback').text("用户名长度必须大于2");
            return;
        }
        $('#feedback').text("");  // 清空反馈信息

        // 异步检查用户名是否存在
        $.ajax({
            type: 'GET',
            url: '/usernameIsExist',
            data:{username: username},
            success: function (response) {
                if (response.status === 1) {        // 假设 ResponseCode.ERROR.getCode() == 1
                    $("#feedback").text(response.message);
                } else {
                    $("#feedback").text(""); // 成功状态默认返回的是 success，无 message
                }
            },
            error: function () {
                console.log("请求错误");
            }
        });
    }
});
