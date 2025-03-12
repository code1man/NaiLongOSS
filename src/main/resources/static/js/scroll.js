$(document).ready(function () {
    // 为所有带有 .title 类的链接添加点击事件
    $('.itemTitle').on('click', function (event) {
        // 阻止默认的跳转行为
        event.preventDefault();

        // 获取目标元素的 ID
        let targetId = $(this).attr('href');

        // 找到目标元素
        let targetElement = $(targetId);

        console.log('targetElement');

        // 如果目标元素存在，则执行平滑滚动
        if (targetElement.length) {
            $('html, body').animate({
                scrollTop: targetElement.offset().top // 滚动到目标元素的顶部
            }, 500); // 500 毫秒的滚动时间
        }
    });
});
