package ru.ivansmurygin.ocp.io_fund;

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


    public BlogInfo() {
        super(1);
        System.out.println("BlogInfo created");
    }

    public BlogInfo(Integer articleId, String articleName, String description, String imstatic) {
        super(1);
        this.articleId = articleId;
        this.articleName = articleName;
        this.description = description;
        this.imstatic = imstatic;
    }

    @Override
    public String toString() {
        return "BlogInfo{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", description='" + description + '\'' +
                ". imstatic='" + imstatic + '\'' +
                '}';
    }

}
