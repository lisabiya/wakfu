package study.lisabiya.com.materialtest;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Hero implements Serializable{

    private int id;

    private String professional;

    private int imageId;

    public Hero(String professional, int imageId){
        this.professional=professional;
        this.imageId=imageId;
    }

    public String getProfessional() {
        return professional;
    }

    public int getImageId() {
        return imageId;
    }

}
