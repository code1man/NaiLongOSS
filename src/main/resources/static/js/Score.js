function Score(merchants) {
    const tbody = document.querySelector('#merchants-section tbody');
    tbody.innerHTML = merchants.map(merchant => `
        <tr>
            <td><div class="rating-stars">${getRatingStars(merchant.rating)}</div></td>
            <td>${merchant.name}</td>
            <td>${merchant.productCount}</td>
            <td>${merchant.mainProduct}</td>
            <td>
                ${merchant.isFrozen ?
        `<button class="btn btn-unfreeze" onclick="unfreezeAccount('${merchant.id}')">🆓 解封</button>
                     <div class="freeze-reason">原因：${merchant.freezeReason}</div>` :
        `<button class="btn btn-freeze" onclick="freezeAccount('${merchant.id}')">❄️ 冻结</button>`
    }
                <button class="btn btn-reset" onclick="resetPassword('${merchant.id}')">🔑 重置</button>
            </td>
        </tr>
    `).join('');
}

function getRatingStars(rating) {
    const full = '★'.repeat(Math.floor(rating));
    const half = rating % 1 >= 0.5 ? '½' : '';
    const empty = '☆'.repeat(5 - Math.ceil(rating));
    return `${full}${half}${empty}`;
}