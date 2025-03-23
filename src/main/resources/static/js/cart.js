$(function () {
    let cartList = document.getElementById("J_miniCartList");

    if (cartList) {
        let cartItems = cartList.children;
        console.log(cartItems);
        let currentCartListTotalCount = parseInt($('#CartListTotalCount').text());
        let currentCartListTotalPrice = parseInt($('#CartListTotalPrice').text());

        if (cartItems && cartItems.length > 0) {
            for (let cartItem of cartItems) {
                console.log(cartItem);
                let i_btn = cartItem.querySelector(".count_i");
                let d_btn = cartItem.querySelector(".count_d");
                let itemCount = cartItem.querySelector(".Item-count");
                let itemPrice = cartItem.querySelector(".Item-price");
                let currentCount = parseInt(itemCount.textContent);
                let ItemPrice = parseInt(itemPrice.textContent);
                let itemId = cartItem.getAttribute('data-item-id');

                let delBtn = cartItem.querySelector('.btn-del');

                // **删除商品**
                delBtn.addEventListener("click", async (event) => {
                    let result = confirm("确定删除吗?");
                    if (!result) return;

                    try {
                        let response = await fetch('/removeItem', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ itemId }),
                            credentials: 'include'
                        });

                        if (!response.ok) throw new Error('删除失败');

                        console.log("删除成功");
                        alert("删除成功");
                        currentCartListTotalCount -= currentCount;
                        currentCartListTotalPrice -= ItemPrice * currentCount;

                        $('#topBarItemCount').text(`(${currentCartListTotalCount})`);

                        if (currentCartListTotalCount > 0) {
                            cartList.removeChild(event.target.closest('li'));
                            $('#CartListTotalCount').text(currentCartListTotalCount);
                            $('#CartListTotalPrice').text(currentCartListTotalPrice);
                        } else {
                            $('#J_miniCartList').html('');
                            $('#J_miniCartListTotal').html('<div class="msg msg-empty">购物车中还没有商品，赶紧选购吧！</div>');
                        }
                    } catch (error) {
                        console.error("删除失败", error);
                        alert("删除失败");
                    }
                });

                // **增加商品数量**
                i_btn.addEventListener("click", () => {
                    console.log("+1")
                    currentCount++;
                    currentCartListTotalCount++;
                    currentCartListTotalPrice += ItemPrice;

                    itemCount.textContent = String(currentCount);
                    $('#CartListTotalCount').text(currentCartListTotalCount);
                    $('#CartListTotalPrice').text(currentCartListTotalPrice);
                    $('#topBarItemCount').text(`(${currentCartListTotalCount})`);

                    updateDatabase(itemId, currentCount);
                });

                // **减少商品数量**
                d_btn.addEventListener("click", async (event) => {
                    console.log("-1")

                    currentCount--;
                    currentCartListTotalCount--;
                    currentCartListTotalPrice -= ItemPrice;

                    if (currentCount === 0) {
                        let result = confirm("本次操作将从购物车移除此商品，确定删除吗?");
                        if (!result) {
                            currentCount++;
                            currentCartListTotalCount++;
                            currentCartListTotalPrice += ItemPrice;
                            return;
                        }

                        $('#topBarItemCount').text(`(${currentCartListTotalCount})`);

                        try {
                            let response = await fetch('/removeItem', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ itemId }),
                                credentials: 'include'
                            });

                            if (!response.ok) throw new Error('删除失败');

                            console.log("删除成功");
                            cartList.removeChild(event.target.closest('li'));
                            $('#CartListTotalCount').text(currentCartListTotalCount);
                            $('#CartListTotalPrice').text(currentCartListTotalPrice);

                            if (currentCartListTotalCount === 0) {
                                $('#J_miniCartList').html('');
                                $('#J_miniCartListTotal').html('<div class="msg msg-empty">购物车中还没有商品，赶紧选购吧！</div>');
                            }

                        } catch (error) {
                            console.error("删除失败", error);
                            alert("删除失败");
                        }
                    } else {
                        itemCount.textContent = String(currentCount);
                        $('#CartListTotalCount').text(currentCartListTotalCount);
                        $('#CartListTotalPrice').text(currentCartListTotalPrice);
                        $('#topBarItemCount').text(`(${currentCartListTotalCount})`);

                        updateDatabase(itemId, currentCount);
                    }
                });
            }
        }
    }

    const updateDatabase = (itemID, count) => {
        fetch('/updateCart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ itemID, count })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
            })
            .then(data => {
                console.log("改变数目成功", data);
            })
            .catch(error => {
                console.error("改变数目失败", error);
            });
    };
})

function addToCart(button) {
    let itemId = button.getAttribute("data-item");

    fetch(`/AddItemToCart?itemId=${itemId}`, {
        method: "GET",
        credentials: "include"
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            return response.text();
        })
        .then(data => {
            alert("商品已成功加入购物车！");  // 弹出成功提示
            window.location.href = "/mainForm";
        })
        .catch(error => {
            alert(error.message || "添加购物车失败，请稍后再试！");
        });
}
