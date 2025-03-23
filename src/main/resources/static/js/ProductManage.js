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
async function loadProducts(categoryId, element, role) {
    const tbody = document.getElementById('productList');
    const table = document.getElementById('productTable');
    const messageContainer = document.getElementById('messageContainer');

    // 清除旧状态
    table.classList.remove('active');
    tbody.innerHTML = '<tr class="loading-row"><td colspan="7">加载中...</td></tr>';
    messageContainer.innerHTML = '';

    // 更新分类状态
    document.querySelectorAll('.subcategory').forEach(el => el.classList.remove('active'));
    if (element) element.classList.add('active');
    currentSubcategory = categoryId; // ✅ 使用正确参数名

    try {
        // 根据 role 选择不同的 API
        const apiUrl = role === 'merchant'
            ? `/merchant/products?categoryId=${categoryId}`
            : `/admin/products?categoryId=${categoryId}`;
        const response = await fetch(apiUrl);
        const products = await response.json();
        console.log(products)

        if (products.length > 0) {
            tbody.innerHTML = products.map(product => `
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td><img src="${product.url}" class="product-thumbnail" alt=""></td>
                    <td>${product.remainingNumb}</td>
                    <td>¥${product.price}</td>
                    ${role === 'merchant' ? `<td>${product.listing ? '✅ 已上架' : '❌ 未上架'}</td>` : ''}
                    <td>
                        ${role === 'merchant' 
                ? `<button onclick="editProduct('${product.id}')">编辑</button>
                               <button onclick="deleteProduct('${product.id}')">删除</button>`
                : `<button onclick="toggleAvailability('${product.id}', ${product.listing})">
                                    ${product.listing ? '下架' : '上架'}
                               </button>`}
                    </td>
                </tr>
         
            `).join('');
            table.classList.add('active');
        } else {
            tbody.innerHTML = `
                <tr class="empty-row">
                    <td colspan="7" class="empty-message">
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
               <button onclick="loadProducts(${categoryId}, this, '${role}')">重试</button> 
            </div>`;
    }
}

function handleEditMainCategoryChange(select, presetSubcategoryId = null) {
    const mainCategory = select.value;
    const subcategorySelect = document.getElementById('editSubcategory');

    if (mainCategory) {
        // 获取对应的子分类元素
        const subcategories = document.querySelectorAll(
            `.subcategory-panel[data-parent="${mainCategory}"] .subcategory`
        );

        subcategorySelect.innerHTML = '';
        subcategories.forEach(sub => {
            const option = document.createElement('option');
            option.value = sub.dataset.id; // ✅ 提交时传 ID
            option.textContent = sub.textContent.trim(); // 显示文本
            subcategorySelect.appendChild(option);
        });

        // 设置默认选中的子分类（根据 ID）
        if (presetSubcategoryId !== null) {
            subcategorySelect.value = presetSubcategoryId.toString();
        }
        subcategorySelect.disabled = false;
    } else {
        subcategorySelect.innerHTML = '<option value="">请先选择主分类</option>';
        subcategorySelect.disabled = true;
    }
}

function handleAddMainCategoryChange(select, presetSubcategoryId = null) {
    const mainCategory = select.value;
    const subcategorySelect = document.getElementById('subcategorySelect');

    if (mainCategory) {
        // 获取对应的子分类元素
        const subcategories = document.querySelectorAll(
            `.subcategory-panel[data-parent="${mainCategory}"] .subcategory`
        );

        subcategorySelect.innerHTML = '';
        subcategories.forEach(sub => {
            const option = document.createElement('option');
            option.value = sub.dataset.id; // ✅ 提交时传 ID
            option.textContent = sub.textContent.trim(); // 显示文本
            subcategorySelect.appendChild(option);
        });

        // 设置默认选中的子分类（根据 ID）
        if (presetSubcategoryId !== null) {
            subcategorySelect.value = presetSubcategoryId.toString();
        }
        subcategorySelect.disabled = false;
    } else {
        subcategorySelect.innerHTML = '<option value="">请先选择主分类</option>';
        subcategorySelect.disabled = true;
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

//编辑按钮
async function editProduct(productId) {
    try {
        // 获取商品详细信息
        const response = await fetch(`/api/products/${productId}`);
        const product = await response.json();

        console.log("📦 响应数据:", product);

        // 填充表单数据
        editingProduct = product;
        document.getElementById('editProductId').value = product.id;
        document.getElementById('editProductName').value = product.name;
        document.getElementById('editProductStock').value = product.remainingNumb;
        document.getElementById('editProductPrice').value = product.price;


        // 处理分类选择
        const mainCategorySelect = document.getElementById('editMainCategory');
        mainCategorySelect.value = product.mainCategory;
        handleEditMainCategoryChange(mainCategorySelect, product.subcategoryid);

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

//删除按钮
async function deleteProduct(productId) {
    if (confirm('确认删除该商品？')) {
        try {
            await fetch(`/api/products/${productId}`, {method: 'DELETE'});
            await new Promise(resolve => setTimeout(resolve, 500));
            showMessage('✅ 商品删除成功', 'success');
            loadProducts(currentSubcategory, document.querySelector('.subcategory.active'), "merchant");
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

function showMessage(message, type) {
    const container = document.getElementById('messageContainer');
    container.innerHTML = `<div class="message ${type}">${message}</div>`;
    setTimeout(() => container.innerHTML = '', 3000);
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


function closeModal() {
    document.getElementById('addProductModal').style.display = 'none';
}

// 初始化默认状态
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('productTable').classList.remove('active');
});// 编辑功能相关函数
let editingProduct = null;

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
    // ✅ 获取子分类ID（关键修复）
    const subcategoryName = document.getElementById('editSubcategory').value;

    // 准备表单数据
    const formData = new FormData();
    const imageFile = document.getElementById('editProductImage').files[0];

    if (imageFile) {
        formData.append('image', imageFile);
    }

    formData.append('name', document.getElementById('editProductName').value);
    formData.append('mainCategory', document.getElementById('editMainCategory').value);
    // formData.append('subcategory', document.getElementById('editSubcategory').value);
    formData.append('subcategoryName', subcategoryName); // ✅ 传递 ID
    formData.append('stock', document.getElementById('editProductStock').value);

    // 获取并转换价格
    const priceInput = document.getElementById('editProductPrice').value;
    const price = parseInt(priceInput, 10);  // 转为整数
    formData.append('price', price);  // 添加整数到表单数据

    console.log(formData);

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
        await loadProducts(currentSubcategory, document.querySelector('.subcategory.active'), "merchant");
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

    // 检查是否上传了图片
    const fileInput = document.getElementById('productImage');
    if (!fileInput.files || fileInput.files.length === 0) {
        showMessage('请上传商品图片', 'error');
        fileInput.focus();
        return false;
    }

    // 检查价格是否合法
    const price = parseFloat(document.getElementById('productPrice').value);
    if (isNaN(price) || price <= 0) {
        showMessage('请输入有效的价格', 'error');
        return false;
    }

    // 检查库存是否合法
    const stock = parseInt(document.getElementById('productStock').value);
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

async function submitProductForm() {
    // 表单验证
    if (!validateForm()) return;
    // ✅ 获取子分类ID（关键修复）
    const subcategoryName = document.getElementById('subcategorySelect').value;

    // 准备表单数据
    const formData = new FormData();
    const imageFile = document.getElementById('productImage').files[0];

    formData.append('image', imageFile);
    formData.append('name', document.getElementById('productName').value);
    formData.append('mainCategory', document.getElementById('mainCategorySelect').value);
    formData.append('subcategoryName', subcategoryName); // 确保名称匹配后端
    formData.append('description', document.getElementById('productDescription').value);
    formData.append('stock', document.getElementById('productStock').value);
    // 获取并转换价格
    const priceInput = document.getElementById('productPrice').value;
    formData.append('price', parseInt(priceInput, 10));  // 添加整数到表单数据

    console.log(formData);

    try {
        // 提交更新
        const response = await fetch(`/api/products`, {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) throw new Error('更新失败');

        showMessage('✅ 商品更新成功', 'success');
        closeEditModal();

        // 刷新当前列表
        await loadProducts(currentSubcategory, document.querySelector('.subcategory.active'), "merchant");
    } catch (error) {
        showMessage(`❌ 添加失败: ${error.message}`, 'error');
    }
}

async function toggleAvailability(productId, currentStatus) {
    try {
        const response = await fetch(`/admin/products/${productId}/toggle`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({isAvailable: !currentStatus})
        });

        if (!response.ok) throw new Error('更新失败');

        showMessage(`✅ 商品 ${!currentStatus ? '已下架' : '已上架'}`, 'success');
        // 重新加载数据
        await loadProducts(currentSubcategory, document.querySelector('.subcategory.active'), 'admin');
    } catch (error) {
        showMessage(`❌ 操作失败: ${error.message}`, 'error');
    }
}
