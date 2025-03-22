// 切换选项卡
function switchTab(tab) {
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    document.getElementById('users-section').style.display = 'none';
    document.getElementById('merchants-section').style.display = 'none';

    if(tab === 'users') {
        document.querySelector('.nav-item:nth-child(1)').classList.add('active');
        document.getElementById('users-section').style.display = 'block';
    } else {
        document.querySelector('.nav-item:nth-child(2)').classList.add('active');
        document.getElementById('merchants-section').style.display = 'block';
    }
}
// 动态加载用户数据
function loadUserData() {
    $.get('/api/users', function(users) {
        renderUsers(users);
    });
}

function renderUsers(users) {
    const tbody = document.querySelector('#users-section tbody');
    tbody.innerHTML = users.map(user => `
        <tr>
            <td style="text-align: center;">
                <span class="status ${getStatusClass(user)}">${getStatusText(user)}</span>
            </td>
            <td>${user.username}</td>
            <td>${user.age}</td>
            <td class="mail-number">${user.email}</td>
            <td>${user.registerDate}</td>
            <td>
                <div class="btn-group">
                    ${user.isFrozen ?
        `<button class="btn btn-unfreeze" onclick="unfreezeAccount('${user.id}')">🆓 解封</button>
                         <div class="freeze-reason">原因：${user.freezeReason}</div>` :
        `<button class="btn btn-freeze" onclick="freezeAccount('${user.id}')">❄️ 冻结</button>`
    }
                    <button class="btn btn-reset" onclick="resetPassword('${user.id}')">🔑 重置密码</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getStatusClass(user) {
    if (user.isFrozen) return 'frozen';
    return user.isOnline ? 'online' : 'offline';
}

function getStatusText(user) {
    if (user.isFrozen) return '⛄️ 已冻结';
    return user.isOnline ? '🐾 在线' : '🌙 离线';
}