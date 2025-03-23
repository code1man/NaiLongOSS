// 重置密码
function resetPassword(userId) {
    if (!confirm('确定要重置该用户的密码为 123456 吗？')) {
        return;
    }

    $.ajax({
        url: '/api/resetPassword',
        method: 'POST',
        data: { userId: userId },
        success: function(response) {
            if (response.success) {
                alert('密码已重置为：123456');
                location.reload();
            } else {
                alert('密码重置失败：' + response.message);
            }
        },
        error: function(xhr) {
            alert('操作失败：' + (xhr.responseJSON?.message || '服务器错误'));
        }
    });
}