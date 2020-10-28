package model;

import utils.MyDateAndId;


/**
 * @description: 图书类
 * @author: HeyWeCome
 * @createDate: 2020/9/29 13:53
 * @version: 1.0
 */
public class Book {
    private String id;                  // 图书的ID编号
    private String bookName;            // 图书的名字
    private String author;              // 作者的名字
    private Double price;               // 图书的价格
    private String publishingHouse;     // 出版社
    private int amount;                 // 图书的总数
    private String createTime;          // 入库时间

    // 有参构造方法
    public Book(String bookName, String author, Double price, String publishingHouse, int amount) {
        this.id = MyDateAndId.shortID_get();
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.publishingHouse = publishingHouse;
        this.amount = amount;
        this.createTime = MyDateAndId.detailTime_get();
    }

    // 无参构造方法
    public Book() {
    }

    public Book(String id, String bookName, String author, Double price, String publishingHouse, int amount, String createTime) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.publishingHouse = publishingHouse;
        this.amount = amount;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "书籍{" +
                "书籍编码：'" + id + '\'' +
                "书名：'" + bookName + '\'' +
                "作者：'" + author + '\'' +
                "价格：" + price +
                "出版社：'" + publishingHouse + '\'' +
                "数量：" + amount +
                "入库时间：'" + createTime + '\'' +
                '}';
    }
}
