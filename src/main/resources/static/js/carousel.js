$(document).ready(function () {
    const $carousel = $('#carousel'); // 获取轮播图的 <ul>
    const $slides = $('#carousel li'); // 获取所有的 <li>
    const $indicators = $('.carousel-indicators span'); // 分页指示器
    const totalSlides = $slides.length; // 图片总数
    let currentIndex = 0; // 当前图片索引
    let interval; // 定时器

    // 切换到指定索引
    function goToSlide(index) {
        currentIndex = index;
        $carousel.css('transform', `translateX(-${currentIndex * 100}%)`);
        $indicators.removeClass('active').eq(currentIndex).addClass('active');
    }

    // 切换到下一张
    function showNextSlide() {
        const nextIndex = (currentIndex + 1) % totalSlides;
        goToSlide(nextIndex);
    }

    // 自动轮播
    function startCarousel() {
        interval = setInterval(showNextSlide, 3000); // 每隔 3 秒切换
    }

    // 停止轮播
    function stopCarousel() {
        clearInterval(interval);
    }

    // 分页导航点击事件
    $indicators.click(function () {
        const index = $(this).data('index');
        goToSlide(index);
    });

    // 鼠标悬停时暂停，离开后继续
    $('.home-hero').hover(stopCarousel, startCarousel);

    // 初始化轮播
    startCarousel();
});
