let currentFreezeAccount = null;

// 冻结账户逻辑
function freezeAccount(userId) {
    currentFreezeAccount = userId;
    const modal = document.getElementById('freezeModal');
    modal.style.display = 'block';
}

// 确认冻结
function confirmFreeze() {
    const reasonSelect = document.getElementById('freezeReason');
    const customReason = document.getElementById('customReason');
    const fullReason = `${reasonSelect.value}${customReason.value ? '：' + customReason.value : ''}`;

    if (!fullReason) {
        alert('请选择冻结原因！');
        return;
    }

    $.ajax({
        url: `/api/freezeAccount/${currentFreezeAccount}`,
        method: 'POST',
        data: { reason: fullReason },
        success: function(response) {
            if (response.success) {
                alert('账户已成功冻结！');
                location.reload();
            } else {
                alert('冻结失败：' + response.message);
            }
        },
        error: function(xhr) {
            alert('操作失败：' + (xhr.responseJSON?.message || '服务器错误'));
        }
    });
}

// 解冻账户
function unfreezeAccount(userId) {
    if (!confirm('确定要解冻该账户吗？')) {
        return;
    }

    $.ajax({
        url: `/api/unfreezeAccount/${userId}`,
        method: 'POST',
        success: function(response) {
            if (response.success) {
                alert('账户已成功解冻！');
                location.reload();
            } else {
                alert('解冻失败：' + response.message);
            }
        },
        error: function(xhr) {
            alert('操作失败：' + (xhr.responseJSON?.message || '服务器错误'));
        }
    });
}

// 关闭模态框
document.querySelector('.close').onclick = function() {
    document.getElementById('freezeModal').style.display = 'none';
    document.getElementById('customReason').value = ''; // 清空自定义原因
}

window.onclick = function(event) {
    const modal = document.getElementById('freezeModal');
    if (event.target == modal) {
        modal.style.display = 'none';
        document.getElementById('customReason').value = ''; // 清空自定义原因
    }
}