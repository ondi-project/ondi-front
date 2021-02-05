package com.ondi.android_ondi.Defined;

public class DefinedCategory {

    private static DefinedCategory instance = new DefinedCategory();
    private DefinedCategory() {}
    public static DefinedCategory getInstance() {
        return instance;
    }

    public final String NOTHING = "카테고리를 선택하세요.";
    public final String DIGITAL = "전자제품";
    public final String CLOTH = "의류";
    public final String BEAUTY = "미용";
    public final String ACCESSORY = "가방 및 악세사리";
    public final String FURNITURE = "가구";
    public final String BABY = "유아용품";
    public final String SPORTS = "스포츠용품";
    public final String ETC = "기타";

}
