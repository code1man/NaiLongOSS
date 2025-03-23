// 扩展switchTab函数
async function switchTab(tab) {
    // 移除所有active状态
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });

    // 隐藏所有静态模块
    ['users-section', 'merchants-section'].forEach(id => {
        document.getElementById(id).style.display = 'none';
    });

    // 处理动态模块
    const container = document.getElementById('dynamic-module-container');
    if (tab === 'product') {
        await loadProductModule(container);
        event.target.classList.add('active');
    } else {
        // 原有静态模块逻辑
        document.querySelector(`.nav-item[onclick*="${tab}"]`).classList.add('active');
        document.getElementById(`${tab}-section`).style.display = 'block';
    }
}

// 动态加载商品管理模块
async function loadProductModule(container) {
    if (!container.dataset.loaded) {
        try {
            // const response = await fetch('/product_manage.html');
            const response = await fetch('/ProductManage');
            const html = await response.text();
            container.innerHTML = html;
            container.dataset.loaded = true;

            // 动态加载配套脚本
            loadModuleScript('/js/ProductManage.js');
        } catch (error) {
            console.error('模块加载失败:', error);
            container.innerHTML = '<div class="error">🐉 商品数据走丢了...</div>';
        }
    }
    container.style.display = 'block';
}

function loadModuleScript(src) {
    const script = document.createElement('script');
    script.src = src;
    document.body.appendChild(script);
}