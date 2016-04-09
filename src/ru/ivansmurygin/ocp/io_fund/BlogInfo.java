package ru.ivansmurygin.ocp.io_fund;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by SmuryginIM on 25.03.2016.
 */
public class BlogInfo extends BlogInfoParent implements Serializable {
    private static final long serialVersionUID = -1675576111L;
    //this ID is used to distinguish object version,
    // if some fields will be changed it is better to change this ID also

    Integer articleId;
    String articleName;
    transient String description;
    static String imstatic;
    transient BlogProperty[] blogProperties = new BlogProperty[1];

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(blogProperties[0].getDoubleProp());
        out.writeObject(blogProperties[0].getStringProp());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        blogProperties = new BlogProperty[1];
        blogProperties[0] = new BlogProperty();
        blogProperties[0].setDoubleProp(in.readDouble());
        blogProperties[0].setStringProp((String) in.readObject());
    };

    public BlogInfo() {
        System.out.println("BlogInfo created");
    }

    public BlogInfo(Integer articleId, String articleName, String description, String imstatic) {
        this.articleId = articleId;
        this.articleName = articleName;
        this.description = description;
        this.imstatic = imstatic;
        blogProperties[0] = new BlogProperty();
        blogProperties[0].setDoubleProp(1d);
        blogProperties[0].setStringProp("string property");
    }

    @Override
    public String toString() {
        return "BlogInfo{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", description='" + description + '\'' +
                ", imstatic='" + imstatic + '\'' +
                ", blogProperty.double ='" + (blogProperties != null ? blogProperties[0].getDoubleProp() : "") + '\'' +
                ", blogProperty.string ='" + (blogProperties != null ? blogProperties[0].getStringProp() : "") + '\'' +
                '}';
    }

}
