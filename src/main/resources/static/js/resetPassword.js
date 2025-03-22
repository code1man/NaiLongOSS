
// 重置密码
function resetPassword(account) {
    if(confirm(`确定要重置 ${account} 的密码吗？`)) {
        // AJAX请求示例
        $.ajax({
            url: '/api/resetPassword',
            method: 'POST',
            data: { account: account },
            success: function(response) {
                alert('密码已重置为：123456');
            }
        });
    }
}