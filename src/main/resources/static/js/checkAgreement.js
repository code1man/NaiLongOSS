$(function() {
// 获取表单、复选框和错误提示元素
    const loginForm = document.getElementById("loginForm");
    const agreementCheckbox = document.getElementById("agreement");
    const submitButton = document.getElementById("submitButton");

    // agreementCheckbox.addEventListener("change", function () {
    //     submitButton.disabled = !this.checked; // 如果未勾选则禁用按钮
    // });

    submitButton.addEventListener("click", function (event) {
        console.log("提交表单");
        if (!agreementCheckbox.checked) { // 如果未勾选协议则阻止提交
            event.preventDefault();
            alert("请阅读并同意协议！");
            agreementCheckbox.focus();
        }
    });
});