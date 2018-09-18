package ai.loko.hk.ui.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProfileEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public ProfileEntity(int uid, String name, float x1, float y1, float x2, float y2) {
        this.uid = uid;
        this.name = name;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }



    public int getUid() {
        return uid;
    }

    public ProfileEntity setUid(int uid) {
        this.uid = uid;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProfileEntity setName(String name) {
        this.name = name;
        return this;
    }

    public float getX1() {
        return x1;
    }

    public ProfileEntity setX1(float x1) {
        this.x1 = x1;
        return this;
    }

    public float getY1() {
        return y1;
    }

    public ProfileEntity setY1(float y1) {
        this.y1 = y1;
        return this;
    }

    public float getX2() {
        return x2;
    }

    public ProfileEntity setX2(float x2) {
        this.x2 = x2;
        return this;
    }

    public float getY2() {
        return y2;
    }

    public ProfileEntity setY2(float y2) {
        this.y2 = y2;
        return this;
    }
}
