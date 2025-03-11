// 对于个人资料编辑
function openModal(event) {
    const modal = document.getElementById("modal");
    modal.style.display = "block";

    // 监听点击事件
    document.addEventListener('click', closeModalOutside)
}

// 关闭模态框
function closeModal() {
    const modal = document.getElementById("modal");
    modal.style.display = "none";

    // 移除点击事件监听器
    document.removeEventListener('click', closeModalOutside);
}

// 关闭模态框的外部点击处理
function closeModalOutside(event) {
    const modal = document.getElementById("modal");
    const modalContent = document.querySelector('.topBar-info');

    // 只有在模态框显示时，并且点击目标不在模态框内部时关闭模态框
    if (modal.style.display === "block" && !modalContent.contains(event.target)) {
        closeModal();
    }
}

/*------------------------------------------------------------------------------------*/
// 对于购物车
const cartMenu = document.getElementById('J_miniCartMenu');
const cartButton = document.querySelector('.topBar-cart');

// 显示购物车菜单
cartButton.addEventListener('mouseenter', () => {
    if (cartMenu)
        cartMenu.style.height = `auto`; // 设置为内容高度
});

// 隐藏购物车菜单
document.addEventListener('mousemove', (event) => {
    if (cartMenu && cartMenu.style.height !== '0px') {
        const cartMenuRect = cartMenu.getBoundingClientRect();
        const cartButtonRect = cartButton.getBoundingClientRect();

        // 检查鼠标是否在购物车菜单和按钮外部
        if (
            event.clientY < cartButtonRect.top || // 在按钮上方
            event.clientY > cartMenuRect.bottom || // 在菜单下方
            event.clientX < cartMenuRect.left || // 在菜单左侧
            event.clientX > cartMenuRect.right // 在菜单右侧
        ) {
            // 隐藏菜单
            cartMenu.style.height = '0'; // 重置为 0
        }
    }
});