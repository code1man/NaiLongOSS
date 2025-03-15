$(function () {
    $(".thumbImg").mouseenter(function (event) {
        console.log("鼠标移入图片");

        // 获取商品的相关数据
        let productName = $(this).data('name');
        let productPrice = $(this).data('price');
        let productDescription = $(this).data('description');

        // 设置悬浮窗的内容
        $("#hover-product-name").text(productName);
        $("#hover-product-price").text(productPrice);
        $("#hover-product-description").text(productDescription);

        // 获取图片的位置（相对于页面的坐标）
        let imgOffset = $(this).offset();  // 获取图片的偏移位置
        let imgWidth = $(this).width();     // 获取图片的宽度
        let imgHeight = $(this).height();   // 获取图片的高度

        // 计算悬浮窗的位置，放在图片的正上方
        let mouseX = imgOffset.left + imgWidth / 2 - $("#product-hover-info").width() / 2;  // 居中显示
        let mouseY = imgOffset.top - $("#product-hover-info").height() - 10;  // 放在图片的上方，距离图片10px

        // 显示悬浮窗并调整位置
        $("#product-hover-info").css({
            "left": mouseX + "px",
            "top": mouseY + "px",
            "display": "block"
        });

    });

    $(".thumbImg").mouseleave(function () {
        console.log("鼠标移出图片");
        $("#product-hover-info").hide();
    });
});
