package hightecit.andalus.kuwait.model;

/**
 * Created by HTISPL on 24-Dec-16.
 */

public class DrawerItem {

    private String name;
    private int imgRes;
    private DrawerEnum drawerEnum;

    public DrawerItem() {

    }

    public DrawerItem(String name, DrawerEnum drawerEnum, int image) {
        this.name = name;
        this.imgRes = image;
        this.drawerEnum = drawerEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public DrawerEnum getDrawerEnum() {
        return drawerEnum;
    }

    public void setDrawerEnum(DrawerEnum drawerEnum) {
        this.drawerEnum = drawerEnum;
    }
}