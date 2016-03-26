package ru.ivansmurygin.ocp.io_fund;

/**
 * Created by SmuryginIM on 25.03.2016.
 */
public class BlogInfo {
    private static final long serialVersionUID = -1675576111L;
    //this ID is used to distinguish object version,
    // if some fields will be changed it is better to change this ID also

    Integer articleId;
    String articleName;
    transient String description;

    public BlogInfo(Integer articleId, String articleName, String description) {
        this.articleId = articleId;
        this.articleName = articleName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "BlogInfo{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
