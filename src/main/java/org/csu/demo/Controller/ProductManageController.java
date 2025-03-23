package org.csu.demo.Controller;

import org.csu.demo.domain.Item;
import org.csu.demo.service.BusinessService;
import org.csu.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
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

    
    @GetMapping("/ProductManage")
    public String productsPage() {
        return "ProductManage :: content";
    }

    // 数据接口（处理前端表格请求）
    @GetMapping("/api/products")
    @ResponseBody
     public List<Item> getAllProducts( @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        // 添加调试日志
        System.out.println("getAllProducts");
        System.out.println("[DEBUG] 请求到达 /api/products，categoryId=" + categoryId);

        return businessService.getBusinessItemById(categoryId);
    }


    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public Item getProductById(@PathVariable("productId") int productId) { // 明确指定路径变量名

        System.out.println("✅ 收到商品详情请求，ID: " + productId);
        System.out.println(itemService.getItemByItemId(productId));

        return itemService.getItemByItemId(productId) ;
    }
    @PutMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> updateProductById(
            @PathVariable("productId") int productId,
            @RequestParam("name") String name,
            @RequestParam("subcategoryId") int subcategoryId,
            @RequestParam("stock") int stock,
            @RequestParam("price") int price,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        System.out.println("✅ 收到编辑商品详情请求，:0 ");


        try {
            // 1. 创建 Item 对象并设置字段
            Item item = new Item();
            item.setName(name);
            item.setProduct_id(subcategoryId);
            item.setRemainingNumb(stock);
            item.setPrice(price);
            System.out.println("✅ 收到编辑商品详情请求，:1 ");


            // 2. 处理文件上传
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveUploadedFile(imageFile);
                item.setUrl(imagePath);
                System.out.println("✅ 收到编辑商品详情请求，:2 ");

            }
            System.out.println("✅ 收到编辑商品详情请求，:2.1 ");
            // 3. 调用 Service 层更新数据
            Item updatedItem = businessService.updateItem(productId, item);
            System.out.println("✅ 收到编辑商品详情请求，:3 ");

            return ResponseEntity.ok(updatedItem);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "文件上传失败: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("✅ 收到编辑商品详情请求，:4 ");

            return ResponseEntity.badRequest()
                    .body(Map.of("error", "更新失败: " + e.getMessage()));
        }
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        System.out.println("✅ 收到编辑商品详情请求，:5 ");


        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }
}



