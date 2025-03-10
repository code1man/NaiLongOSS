package org.csu.demo.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CategoryType {
    EMOJI("表情包"),
    TOY("玩具"),
    DAILYITEM("日常用品");

    private final String description;

    CategoryType(String description) {
        this.description = description;
    }

    public static List<ProductType> getContainingProductTypes(CategoryType categoryType) {
        List<ProductType> productTypes = new ArrayList<>();

        switch (categoryType) {
            case EMOJI:
                productTypes.add(ProductType.NAILONG_TANGTANG_EMOJI);
                productTypes.add(ProductType.NAILONG_OTHER_EMOJI);
                productTypes.add(ProductType.NAILONG_EMOTION_EMOJI);
                productTypes.add(ProductType.NAILONG_WALLPAPER);
                break;

            // 处理其他类别
            case TOY:
                productTypes.add(ProductType.NAILONG_DOLL);
                productTypes.add(ProductType.NAILONG_BLIND_BOX);
                productTypes.add(ProductType.NAILONG_FIGURE);
                productTypes.add(ProductType.NAILONG_EDUCATIONAL_TOY);
                break;

            case DAILYITEM:
                productTypes.add(ProductType.NAILONG_CUP);
                productTypes.add(ProductType.NAILONG_KEYCHAIN);
                break;
        }

        return productTypes;
    }
}
