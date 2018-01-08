package vn.manmo.search.object;

/**
 * Created by NguyenQuocCuong on 11/14/2017.
 */

public class FurnitureObject {
    private String id;
    private String name;
    private String classname;
    private String image;

    public FurnitureObject() {
    }

    public FurnitureObject(String id, String name, String classname, String image) {
        this.id = id;
        this.name = name;
        this.classname = classname;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
