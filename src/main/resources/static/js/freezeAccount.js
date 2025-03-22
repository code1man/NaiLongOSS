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

    $.ajax({
        url: `/api/freezeAccount/${currentFreezeAccount}`,
        method: 'POST',
        data: { reason: fullReason },
        success: function(response) {
            location.reload(); // 刷新数据
            $('#freezeModal').hide();
        }
    });
}

// 解冻账户
function unfreezeAccount(userId) {
    $.ajax({
        url: `/api/unfreezeAccount/${userId}`,
        method: 'POST',
        success: function(response) {
            location.reload(); // 刷新数据
        }
    });
}

// 关闭模态框
document.querySelector('.close').onclick = function() {
    document.getElementById('freezeModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('freezeModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}