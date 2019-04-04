package com.rxiu.wechat.compent.button;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class Button {

    public enum ButtonType {
        click(0),
        view(1),
        scancode_push(2),
        scancode_waitmsg(3),
        pic_sysphoto(4),
        pic_photo_or_album(5),
        pic_weixin(6),
        location_select(7),
        media_id(8),
        view_limited(9),
        //小程序
        miniprogram(10);

        Integer index;
        ButtonType(Integer index) {
            this.index = index;
        }
    }
    private String name;

    private String type;

    private Button[] sub_button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Button[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }
}
