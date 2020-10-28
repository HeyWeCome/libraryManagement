package dao;

import model.Book;

/**
 * @description: 操作书籍的相关SQL语句
 * @author: HeyWeCome
 * @createDate: 2020/10/1 15:26
 * @version: 1.0
 */
public class BookDao {

    /**
     * 查询所有的书籍
     */
    public String queryAllBook(){
        return "select * from book";
    }

    /**
     * 根据书籍名称查询
     * @param bookName
     * @return
     */
    public String queryOneBookByName(String bookName){return "select * from book where book_name ='"+bookName+"'";};

    /**
     * @param id
     * @return
     * @description 查询单个书籍
     */
    public String queryOneBook(String id){
        return "select * from book where id="+"'"+id+"'";
    }

    /**
     * @param: Book
     * @description: 添加书籍的SQL语句
     */
    public String addBook(Book book){
        return "insert into book values("+
                "'" + book.getId() + "'," +
                "'" + book.getBookName() + "'," +
                "'" + book.getAuthor() + "'," +
                    + book.getPrice() + "," +
                "'" + book.getPublishingHouse() + "'," +
                    + book.getAmount() + "," +
                "'" + book.getCreateTime() + "')";
    }

    /**
     * @param id
     * @description 删除图书
     */
    public String delete(String id){
        return "delete from book where id = '" + id +"';";
    }

    /**
     * @description 修改书籍
     * @param book
     * @return
     */
    public String modifyBook(Book book){
        return "update book set book_name ='" + book.getBookName() + "',"+
                "author ='" + book.getAuthor() + "'," +
                "price =" + book.getPrice() + ","+
                "publishing_house ='" + book.getPublishingHouse() + "',"+
                "amount = "+ book.getAmount() + " "+
                "where id ='"+book.getId()+"'";
    }
}
