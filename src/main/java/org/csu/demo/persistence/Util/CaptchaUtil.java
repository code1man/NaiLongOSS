package org.csu.demo.persistence.Util;


import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {

    //定义验证码图像属性
    private static final int WIDTH = 80;//图像宽度
    private static final int HEIGHT = 40;//图像高度
    private static final int LENGTH = 4;//验证码字符数量
    private static final Random random = new Random();// 用于生成随机数，便于设置rgb参数


    public static String createCaptchaImage(HttpServletResponse response) {
        // 创建BufferedImage对象，这是一个包含图像数据的缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D对象，可以在图像上进行绘制
        Graphics2D g = image.createGraphics();

        // 设置背景色为白色并填充整个图像区域
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体，使用Arial字体，加粗，大小20
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // 随机生成验证码文本字符
        String captchaText = generateRandomText(LENGTH);
        int x = 10; // 横坐标开始位置
        // 逐个绘制验证码中的字符
        for (char c : captchaText.toCharArray()) {
            // 随机生成字符颜色(rgb)
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            // 绘制字符，并随机调整字符的垂直位置
            g.drawString(String.valueOf(c), x, 20 + random.nextInt(20));
            x += 20;// 将横坐标移动到下一个字符的绘制位置
        }

        // 添加噪点，使图像中的验证码不易被自动识别
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(12);
            int y2 = random.nextInt(12);
            g.drawLine(x1, y1, x1 + x2, y1 + y2);// 绘制一条小线段，代表噪点
        }

        // 完成图像的绘制，释放图形上下文使用的系统资源
        g.dispose();

        // 设置响应内容类型为JPEG图像
        response.setContentType("image/jpeg");
        try {
            // 将创建的图像写入响应的输出流中
            ImageIO.write(image, "JPEG", response.getOutputStream());
            // 刷新输出流，确保图像数据完整发送
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回生成的验证码文本
        return captchaText;
    }


    private static String generateRandomText(int length) {
        // 定义可能出现在验证码中的字符
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // 随机选择字符并添加到StringBuilder中
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 将StringBuilder转换为字符串并返回
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}