package tn.itskills.android.isgc_firebase.models;

import java.io.Serializable;

/**
 * Created by adnenhamdouni on 25/11/2016.
 */

public class Student implements Serializable{

    private String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
