package service;


import dao.BookDao;
import model.Book;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 书籍的服务类
 * @author: HeyWeCome
 * @createDate: 2020/9/29 19:04
 * @version: 1.0
 */
public class BookService {
    private ArrayList<Book> books = new ArrayList<Book>();      // 书籍都存放在这个里面

    /**
     * 添加书籍，操作数据库，新增成功返回1，新增失败返回0
     * @param book
     * @return
     */
    public int addBook(Book book){
        BookDao bookDao = new BookDao();
        int result = bookDao.addBook(book);
        return result;
    }

    /**
     * 通过书名查询数据库中是否存在这一本书
     * @param book
     * @return
     */
    public int searchOneBook(Book book){
        BookDao bookDao = new BookDao();
        int result = bookDao.queryOneBookByName(book.getBookName());
        return result;
    }

    /**
     * 查询数据库中所有的书籍
     * @return
     */
    public List<Book> searchAllBook(){
        BookDao bookDao = new BookDao();
        List<Book> books = bookDao.queryAllBook();
        return books;
    }

    /**
     * 修改书籍信息
     * @param modifiedBook
     * @return
     */
    public int modifyBook(Book modifiedBook){
        BookDao bookDao = new BookDao();
        int result = bookDao.modifyBook(modifiedBook);
        return result;
    }

    /**
     * 删除图书
     * result > 0 ? "删除成功" : "删除失败"
     * @param bookID 要删除的图书ID
     * @return
     */
    public int deleteBook(String bookID){
        BookDao bookDao = new BookDao();
        int result = bookDao.delete(bookID);
        return result;
    }

    /**
     * 模糊搜索书籍信息
     * @param condition
     * @return
     */
    public List<Book> fuzzySearchBook(String condition){
        BookDao bookDao = new BookDao();
        List<Book> books = bookDao.fuzzySearchBook(condition);
        return books;
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
