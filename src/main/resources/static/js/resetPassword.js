// 重置密码
function resetPassword(userId) {
    if (!confirm('确定要重置该用户的密码吗？重置后的密码将发送到用户邮箱。')) {
        return;
    }

    $.ajax({
        url: `/admin/users/${userId}/reset-password`,
        method: 'POST',
        success: function(response) {
            alert('密码重置成功，新密码已发送到用户邮箱');
        },
        error: function(xhr) {
            alert('密码重置失败：' + xhr.responseText);
        }
    });
}