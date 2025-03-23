package org.csu.demo.Controller;

import org.csu.demo.domain.Item;
import org.csu.demo.domain.User;
import org.csu.demo.service.BusinessService;
import org.csu.demo.service.ItemService;
import org.csu.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Controller
public class ProductManageController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private BusinessService businessService;

    @Autowired
    private ProductService productService;

    @GetMapping("/ProductManage")
    public String productsPage() {
        return "ProductManage :: content";
    }

    // 数据接口（处理前端表格请求）
    @GetMapping("/admin/products")
    @ResponseBody
    public List<Item> getAllProducts(@RequestParam(name = "categoryId", required = false) Integer categoryId) {
        return businessService.getBusinessItemByIdIgnoreList(categoryId);
    }

    @GetMapping("/merchant/products")
    @ResponseBody
    public List<Item> getMerchantProducts(@SessionAttribute("loginUser") User user,  // 从 session 获取 user
                                          @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        int merchantId = user.getId();
        return businessService.getBusinessItemByIdAndMerchantId(categoryId, merchantId);
    }

    @DeleteMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable("productId") int productId) {
        businessService.deleteItem(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/products")
    @ResponseBody
    public ResponseEntity<?> newItemCreate(
            @SessionAttribute("loginUser") User user,
            @RequestParam("name") String name,
            @RequestParam("subcategoryName") int subcategoryId,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("price") int price,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            // 1️⃣ 处理图片上传（如果有图片）
            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = saveUploadedFile(imageFile);  // ⬅️ 你需要实现这个方法存储图片
            }

            // 2️⃣ 创建 `Item` 对象
            Item newItem = Item.builder()
                    .id(subcategoryId + (int)(Math.random() * 10086))
                    .name(name)
                    .businessId(user.getId())
                    .description(description)
                    .price(price)
                    .product_id(subcategoryId)
                    .remainingNumb(stock)  // `stock` 对应 `remainingNumb`
                    .url(imageUrl)  // 存储图片路径
                    .build();

            // 3️⃣ 存入数据库
            int isOK = businessService.insertItem(newItem);
            if (isOK == 1) {
                System.out.println("成功创建");
            }

            // 4️⃣ 返回成功响应
            return ResponseEntity.ok(Map.of(
                    "message", "商品创建成功",
                    "item", newItem
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "创建商品失败: " + e.getMessage()));
        }
    }


    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public Item getProductById(@PathVariable("productId") int productId) { // 明确指定路径变量名

        System.out.println("✅ 收到商品详情请求，ID: " + productId);
        System.out.println(itemService.getItemByItemId(productId));

        return itemService.getItemByItemId(productId);
    }

    @PutMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> updateProductById(
            @PathVariable("productId") int itemId,
            @RequestParam("name") String name,
            @RequestParam("subcategoryName") int subcategoryId,
            @RequestParam("stock") int stock,
            @RequestParam("price") int price,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            // 1. 创建 Item 对象并设置字段
            Item item = itemService.getItemByItemId(itemId);
            item.setName(name);
            item.setProduct_id(subcategoryId);
            item.setPrice(price);
            item.setRemainingNumb(stock);
            System.out.println("✅ 收到编辑商品详情请求，商品为，: " + item);

            // 2. 处理文件上传
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveUploadedFile(imageFile);
                item.setUrl(imagePath);
                System.out.println("✅ 收到编辑商品详情请求，:2 ");
            }
            System.out.println("✅ 收到编辑商品详情请求，:2.1 ");
            // 3. 调用 Service 层更新数据
            int isOK = businessService.updateItem(item);
            System.out.println("✅ 是否成功编辑商品 : " + isOK);

            return ResponseEntity.ok(item);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "文件上传失败: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "更新失败: " + e.getMessage()));
        }
    }

    @PutMapping("/admin/products/{productId}/toggle")
    @ResponseBody
    public ResponseEntity<?> toggleProductAvailability(@PathVariable("productId") int productId) {
        try {
            // 根据 productId 查找商品
            Item product = itemService.getItemByItemId(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("商品不存在");
            }

            // 切换商品的上下架状态
            boolean newStatus = !product.isListing();
            businessService.updateProductAvailability(productId, newStatus);

            return ResponseEntity.ok(Map.of(
                    "message", "操作成功",
                    "newStatus", newStatus
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("操作失败: " + e.getMessage());
        }
    }


    private String saveUploadedFile(MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/Products";
        Path uploadPath = Paths.get(uploadDir);

        // 确保目录存在
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // 保存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "../images/Products/" + filename;
    }
}



