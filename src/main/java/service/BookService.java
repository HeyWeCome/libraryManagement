package service;


import dao.BookDao;
import model.Book;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @description: 书籍的服务类
 * @author: HeyWeCome
 * @createDate: 2020/9/29 19:04
 * @version: 1.0
 */
public class BookService {
    private ArrayList<Book> books = new ArrayList<Book>();      // 书籍都存放在这个里面
    BookDao bookDao = new BookDao();                         // 对数据库进行操作

    // 添加书籍
    public int addBook(Book book){
        int execute = 0;
        try {
            Connection conn = DBUtil.getConn();
            Statement statement = conn.createStatement();

            int queryBookByNameResult = searchOneBook(book);    // 查询这本书的结果

            if(queryBookByNameResult != 0){ // 已经存在这本书了
                return 2;
            }else{
                // 执行的SQL
                String sql = bookDao.addBook(new Book(book.getBookName(),book.getAuthor(),book.getPrice(),book.getPublishingHouse(),book.getAmount()));               // 新增书籍的SQL
                execute = statement.executeUpdate(sql);  // 存放新增的结果，成功返回1，错误返回0
            }
            
            conn.close();                   // 关闭连接
            statement.close();              // 关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;
    }

    /**
     * 查询数据库中是否存在这一本书
     * @param book
     * @return
     */
    public int searchOneBook(Book book){
        String bookName = book.getBookName();
        int result = 0;

        try {
            Connection conn = DBUtil.getConn();
            Statement statement = conn.createStatement();

            String sql = bookDao.queryOneBookByName(book.getBookName());  // 新增书籍的SQL
            System.out.println("刚执行的新增SQL = " + sql);

            ResultSet  bookListAfterSearch = statement.executeQuery(sql); // 查询书籍的结果

            String id = null;               // 存在查到的书籍ID
            while(bookListAfterSearch.next()) {
                id = bookListAfterSearch.getString("id");
            }

            result = (id != null ? 2 : 0);  // 返回2说明存在这本书，返回0说明不存在这本书
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 查询所有的书籍
    */
    public ArrayList<Book> searchAllBook(){
        ArrayList<Book> books = new ArrayList<>();              // 最后返回的结果

        Connection conn = DBUtil.getConn();;
        String querySql = bookDao.queryAllBook();               // 查询所有的书籍
        Statement statement = null;

        try {
            statement = conn.createStatement();
            ResultSet result = statement.executeQuery(querySql);    // 存放查询的结果

            // 将书籍信息添加，准备返回
            while(result.next()) {
                Book book = new Book(result.getString("id"), result.getString("book_name"), result.getString("author"),
                        result.getDouble("price"), result.getString("publishing_house"),
                        result.getInt("amount"),result.getString("create_time"));
                books.add(book);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }finally {
            try {
                conn.close();                   // 关闭连接
                statement.close();              // 关闭连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    /**
     * 修改书籍
     */
    public int modifyBook(Book modifiedBook){
        int modifyResult = 0;
        try {
            Connection conn = DBUtil.getConn();
            Statement statement = conn.createStatement();

            String modifySql = bookDao.modifyBook(modifiedBook);    // 获取修改书籍的sql语句
            modifyResult = statement.executeUpdate(modifySql);      // 执行修改语句

            conn.close();                                           // 关闭连接
            statement.close();                                      // 关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modifyResult;
    }

    /**
     * 删除图书
     */
    public int deleteBook(String bookID){
        int result = 0; // 删除成功返回1，删除失败返回0
        try {
            Connection conn = DBUtil.getConn();
            Statement statement = conn.createStatement();

            String sql = bookDao.delete(bookID);         // 获取删除的sql语句
            result = statement.executeUpdate(sql);       // 执行删除语句
            System.out.println(result > 0 ? "删除成功" : "删除失败");

            conn.close();                               // 关闭连接
            statement.close();                          // 关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 获取到所有的书籍
    public ArrayList<Book> getBooks() {
        return books;
    }

    // 初始化书籍
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
