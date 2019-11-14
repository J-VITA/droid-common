package kr.skyware.commons.data;

import java.io.Serializable;

public class SkyUser implements Serializable {

    private String uid;
    private String password;
    private String name;
    private String gender;
    private String age;
    private String birthday;
    private String mdn;
    private String idx;

    public SkyUser() {
        this("", "", "", "", "", "", "", "");
    }
    public SkyUser(String uid, String password, String name, String gender, String age, String birthday, String mdn, String idx) {
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.mdn = mdn;
        this.idx = idx;
    }

    public String getId() {
        return uid;
    }

    public void setId(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMdn() {
        return mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.mdn = idx;
    }
}
