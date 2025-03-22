// 分类交互逻辑
let currentMainCategory = null;
let currentSubcategory = null;
let selectedFile = null;

function toggleMainCategory(category, element) {
    // 切换主分类激活状态
    document.querySelectorAll('.main-category').forEach(el => el.classList.remove('active'));
    element.classList.add('active');
    currentMainCategory = category;

    // 显示对应子分类
    document.querySelectorAll('.subcategory-panel').forEach(panel => {
        panel.classList.toggle('active', panel.dataset.parent === category);
    });
}

// 商品加载逻辑
async function loadProducts(categoryId, element) {
    const tbody = document.getElementById('productList');
    const table = document.getElementById('productTable');
    const messageContainer = document.getElementById('messageContainer');

    // 清除旧状态
    table.classList.remove('active');
    tbody.innerHTML = '<tr class="loading-row"><td colspan="6">加载中...</td></tr>';
    messageContainer.innerHTML = '';

    // 更新分类状态
    document.querySelectorAll('.subcategory').forEach(el => el.classList.remove('active'));
    if(element) element.classList.add('active');
    currentSubcategory = categoryId; // ✅ 使用正确参数名

    try {

        const response = await fetch(`/api/products?categoryId=${categoryId}`);
        const products = await response.json();
        // // 模拟数据
        // await new Promise(resolve => setTimeout(resolve, 500));
        // const products = subcategory ? [{
        //     id: 1,
        //     name: "示例商品",
        //     url: "https://via.placeholder.com/60",
        //     stock: 10,
        //     price: 99.99
        // }] : [];

        if (products.length > 0) {
            tbody.innerHTML = products.map(product => `
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td><img src="${product.url}" class="product-thumbnail"></td>
                    <td>${product.remainingNumb}</td>
                    <td>¥${product.price.toFixed(2)}</td>
                    <td>
                        <button onclick="editProduct('${product.id}')">编辑</button>
                        <button onclick="deleteProduct('${product.id}')">删除</button>
                    </td>
                </tr>
            `).join('');
            table.classList.add('active');
        } else {
            tbody.innerHTML = `
                <tr class="empty-row">
                    <td colspan="6" class="empty-message">
                        🛒 当前分类下没有商品
                    </td>
                </tr>`;
        }
    } catch (error) {
        table.classList.remove('active');
        tbody.innerHTML = '';
        messageContainer.innerHTML = `
            <div class="error-message">
                ❌ 数据加载失败：${error.message}
               <button onclick="loadProducts(${categoryId}, this)">重试</button> 
            </div>`;
    }
}



async function deleteProduct(productId) {
    if (confirm('确认删除该商品？')) {
        try {
            // 实际删除操作
            // await fetch(`/api/products/${productId}`, { method: 'DELETE' });
            await new Promise(resolve => setTimeout(resolve, 500));
            showMessage('✅ 商品删除成功', 'success');
            loadProducts(currentSubcategory, document.querySelector('.subcategory.active'));
        } catch (error) {
            showMessage(`❌ 删除失败: ${error.message}`, 'error');
        }
    }
}

// 新增商品相关逻辑
function openAddProductModal() {
    const modal = document.getElementById('addProductModal');
    modal.style.display = 'block';
    document.getElementById('addProductForm').reset();
    document.getElementById('mainCategorySelect').selectedIndex = 0;
    document.getElementById('subcategorySelect').disabled = true;
    document.getElementById('subcategorySelect').innerHTML = '<option value="">请先选择主分类</option>';
    document.getElementById('imagePreview').innerHTML = '';
    selectedFile = null;
}

function handleMainCategoryChange(select) {
    const mainCategory = select.value;
    const subcategorySelect = document.getElementById('subcategorySelect');

    subcategorySelect.innerHTML = mainCategory ? '' : '<option value="">请先选择主分类</option>';
    subcategorySelect.disabled = !mainCategory;

    if (mainCategory) {
        document.querySelectorAll(`.subcategory-panel[data-parent="${mainCategory}"] .subcategory`)
            .forEach(sub => {
                const option = document.createElement('option');
                option.value = sub.textContent.trim();
                option.textContent = sub.textContent.trim();
                subcategorySelect.appendChild(option);
            });
    }
}

function previewImage(event) {
    const preview = document.getElementById('imagePreview');
    preview.innerHTML = '';
    selectedFile = event.target.files[0];

    if (selectedFile) {
        if (!validateFile(selectedFile)) {
            event.target.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = e => {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.className = 'preview-image';
            preview.appendChild(img);
        };
        reader.readAsDataURL(selectedFile);
    }
}

async function submitProductForm(event) {
    event.preventDefault();
    const form = event.target;

    // 表单验证
    if (!validateForm()) return;

    // 显示上传状态
    const progressContainer = document.createElement('div');
    progressContainer.innerHTML = `
        <div class="upload-progress">
            <div class="progress-bar" style="width: 0%"></div>
        </div>`;
    document.getElementById('imagePreview').appendChild(progressContainer);

    try {
        // 模拟上传过程
        await simulateUploadWithProgress(progressContainer);

        // 实际提交应替换此部分
        // const formData = new FormData(form);
        // const response = await fetch('/api/products', {
        //     method: 'POST',
        //     body: formData
        // });

        showMessage('✅ 商品添加成功', 'success');
        closeModal();
        if(currentSubcategory === form.subcategory.value) {
            loadProducts(currentSubcategory, document.querySelector('.subcategory.active'));
        }
    } catch (error) {
        showMessage(`❌ 保存失败: ${error.message}`, 'error');
    }
}

// 辅助函数
function validateFile(file) {
    const maxSize = 5 * 1024 * 1024;
    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (!file) {
        showMessage('请选择要上传的图片', 'error');
        return false;
    }
    if (!allowedTypes.includes(file.type)) {
        showMessage('仅支持 JPG/PNG/WEBP 格式', 'error');
        return false;
    }
    if (file.size > maxSize) {
        showMessage('文件大小不能超过5MB', 'error');
        return false;
    }
    return true;
}

function validateForm() {
    const requiredFields = [
        'mainCategorySelect',
        'subcategorySelect',
        'productName',
        'productStock',
        'productPrice'
    ];

    for (const fieldId of requiredFields) {
        const field = document.getElementById(fieldId);
        if (!field.value.trim()) {
            showMessage('请填写所有必填字段', 'error');
            field.focus();
            return false;
        }
    }
    return true;
}

function simulateUploadWithProgress(container) {
    return new Promise(resolve => {
        let percent = 0;
        const interval = setInterval(() => {
            percent += 10;
            container.querySelector('.progress-bar').style.width = `${percent}%`;
            if (percent >= 100) {
                clearInterval(interval);
                resolve();
            }
        }, 200);
    });
}

function showMessage(message, type) {
    const container = document.getElementById('messageContainer');
    container.innerHTML = `<div class="message ${type}">${message}</div>`;
    setTimeout(() => container.innerHTML = '', 3000);
}

function closeModal() {
    document.getElementById('addProductModal').style.display = 'none';
}

// 初始化默认状态
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('productTable').classList.remove('active');
});// 编辑功能相关函数
let editingProduct = null;

async function editProduct(productId) {
    try {
        // 获取商品详细信息
        const response = await fetch(`/api/products/${productId}`);
        const product = await response.json();

        console.log("📦 响应数据:", product);

        // if (!product.success) {
        //     throw new Error(product.message || "请求失败");
        // }

        // 填充表单数据
        editingProduct = product;
        document.getElementById('editProductId').value = product.id;
        document.getElementById('editProductName').value = product.name;
        document.getElementById('editProductStock').value = product.remainingNumb;
        document.getElementById('editProductPrice').value = product.price.toFixed(2);

        // 处理分类选择
        const mainCategorySelect = document.getElementById('editMainCategory');
        mainCategorySelect.value = product.mainCategory;
        handleEditMainCategoryChange(mainCategorySelect, product.subcategory);

        // 显示当前图片
        const currentPreview = document.getElementById('currentImagePreview');
        currentPreview.innerHTML = product.url ?
            `<img src="${product.url}" alt="当前图片">` :
            '<div>暂无图片</div>';

        // 打开模态框
        document.getElementById('editProductModal').style.display = 'block';
    } catch (error) {
        showMessage(`❌ 获取商品信息失败: ${error.message}`, 'error');
    }
}

function handleEditMainCategoryChange(select, presetSubcategory = null) {
    const mainCategory = select.value;
    const subcategorySelect = document.getElementById('editSubcategory');

    if (mainCategory) {
        // 获取对应的子分类
        const subcategories = document.querySelectorAll(
            `.subcategory-panel[data-parent="${mainCategory}"] .subcategory`
        );

        subcategorySelect.innerHTML = '';
        subcategories.forEach(sub => {
            const option = document.createElement('option');
            option.value = sub.textContent.trim();
            option.textContent = sub.textContent.trim();
            subcategorySelect.appendChild(option);
        });

        // 设置预选子分类
        if (presetSubcategory) {
            subcategorySelect.value = presetSubcategory;
        }
        subcategorySelect.disabled = false;
    } else {
        subcategorySelect.innerHTML = '<option value="">请先选择主分类</option>';
        subcategorySelect.disabled = true;
    }
}

function previewEditImage(event) {
    const preview = document.getElementById('editImagePreview');
    preview.innerHTML = '';
    const file = event.target.files[0];

    if (file) {
        if (!validateFile(file)) {
            event.target.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = e => {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.className = 'preview-image';
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
}

async function submitEditForm(event) {
    event.preventDefault();
    const form = event.target;

    // 表单验证
    if (!validateEditForm()) return;

    // 准备表单数据
    const formData = new FormData();
    const imageFile = document.getElementById('editProductImage').files[0];

    if (imageFile) {
        formData.append('image', imageFile);
    }

    formData.append('name', document.getElementById('editProductName').value);
    formData.append('mainCategory', document.getElementById('editMainCategory').value);
    formData.append('subcategory', document.getElementById('editSubcategory').value);
    formData.append('stock', document.getElementById('editProductStock').value);
    formData.append('price', document.getElementById('editProductPrice').value);

    try {
        // 提交更新
        const response = await fetch(`/api/products/${editingProduct.id}`, {
            method: 'PUT',
            body: formData
        });

        if (!response.ok) throw new Error('更新失败');

        showMessage('✅ 商品更新成功', 'success');
        closeEditModal();

        // 刷新当前列表
        loadProducts(currentSubcategory, document.querySelector('.subcategory.active'));
    } catch (error) {
        showMessage(`❌ 更新失败: ${error.message}`, 'error');
    }
}

function validateEditForm() {
    const requiredFields = [
        'editMainCategory',
        'editSubcategory',
        'editProductName',
        'editProductStock',
        'editProductPrice'
    ];

    for (const fieldId of requiredFields) {
        const field = document.getElementById(fieldId);
        if (!field.value.trim()) {
            showMessage('请填写所有必填字段', 'error');
            field.focus();
            return false;
        }
    }

    const price = parseFloat(document.getElementById('editProductPrice').value);
    if (isNaN(price) || price <= 0) {
        showMessage('请输入有效的价格', 'error');
        return false;
    }

    const stock = parseInt(document.getElementById('editProductStock').value);
    if (isNaN(stock) || stock < 0) {
        showMessage('库存不能为负数', 'error');
        return false;
    }

    return true;
}

function closeEditModal() {
    document.getElementById('editProductModal').style.display = 'none';
    document.getElementById('editProductImage').value = '';
    document.getElementById('editImagePreview').innerHTML = '';
    editingProduct = null;
}