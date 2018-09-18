package ai.loko.hk.ui.model;

public class Profile {

    private String name;
    private float x1,x2,y1,y2;

    public Profile() {
    }

    public Profile(String name, float x1, float y1, float x2, float y2) {
        this.name = name;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public String getName() {

        return name;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public float getX1() {
        return x1;
    }

    public Profile setX1(float x1) {
        this.x1 = x1;
        return this;
    }

    public float getX2() {
        return x2;
    }

    public Profile setX2(float x2) {
        this.x2 = x2;
        return this;
    }

    public float getY1() {
        return y1;
    }

    public Profile setY1(float y1) {
        this.y1 = y1;
        return this;
    }

    public float getY2() {
        return y2;
    }

    public Profile setY2(float y2) {
        this.y2 = y2;
        return this;
    }
}
