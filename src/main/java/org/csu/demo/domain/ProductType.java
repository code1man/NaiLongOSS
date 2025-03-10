package org.csu.demo.domain;

import lombok.Getter;

/*商品类型*/
@Getter
public enum ProductType {
    NAILONG_TANGTANG_EMOJI("奶龙唐唐表情包", CategoryType.EMOJI, 1),
    NAILONG_OTHER_EMOJI("奶龙其他表情包", CategoryType.EMOJI, 2),
    NAILONG_EMOTION_EMOJI("奶龙情绪表情包", CategoryType.EMOJI, 3),
    NAILONG_WALLPAPER("奶龙壁纸", CategoryType.EMOJI, 4),
    NAILONG_DOLL("奶龙玩偶", CategoryType.TOY, 5),
    NAILONG_EDUCATIONAL_TOY("奶龙儿童早教玩具", CategoryType.TOY, 6),
    NAILONG_BLIND_BOX("奶龙盲盒", CategoryType.TOY, 7),
    NAILONG_CUP("奶龙水杯/保温杯/咖啡杯", CategoryType.DAILYITEM, 8),
    NAILONG_KEYCHAIN("奶龙钥匙扣", CategoryType.DAILYITEM, 9),
    NAILONG_FIGURE("奶龙手办/摆件", CategoryType.TOY, 10);

    private final String description;
    private final CategoryType categoryType;
    private final int index;

    ProductType(String description, CategoryType categoryType, int index) {
        this.description = description;
        this.categoryType = categoryType;
        this.index = index;
    }
}
