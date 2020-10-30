package dao;

import model.Book;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 操作书籍的相关SQL语句
 * @author: HeyWeCome
 * @createDate: 2020/10/1 15:26
 * @version: 1.0
 */
public class BookDao {
    private static BookDao bookDao;

    // 同一时刻只有一个方法可以进入到临界区，同时它还可以保证共享变量的内存可见性
    public static BookDao getInstance(){
        if(bookDao == null){
            synchronized (BookDao.class){
                if(bookDao==null){
                    bookDao = new BookDao();
                }
            }
        }
        return bookDao;
    }

    /**
     * 查询所有的书籍
     */
    public List<Book> queryAllBook(){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;                 // 查询的结果
        List<Book> books = new ArrayList<>();       // 最后返回的结果

        String sql = "select * from book order by create_time desc";

        try{
            conn = DBUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();       // 执行查询之后的结果

            while (resultSet.next()){
                String id = resultSet.getString("id");
                String bookName = resultSet.getString("book_name");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                String publishingHouse = resultSet.getString("publishing_house");
                int amount = resultSet.getInt("amount");
                String createTime = resultSet.getString("create_time");

                books.add(new Book(id,bookName,author,price,publishingHouse,amount,createTime));
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("根据ID查询数据失败");
        }finally{
            DBUtils.close(resultSet, preparedStatement, conn);
        }
        return books;
    }

    /**
     * 根据书籍名称查询
     * @param bookName
     * @return
     */
    public int queryOneBookByName(String bookName){
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet books = null;

        String sql = "select * from book where book_name=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bookName);    // 设置书籍名称
            books = ps.executeQuery();      // 执行查询之后的结果

            if(books.next()){
                result = 1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("根据ID查询数据失败");
        }finally{
            DBUtils.close(books, ps, conn);
        }

        return result;
    };

    /**
     * 模糊搜索书籍信息
     * @param condition 用户输入的关键词
     * @return
     */
    public List<Book> fuzzySearchBook(String condition){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;                 // 查询的结果
        List<Book> books = new ArrayList<>();       // 最后返回的结果

        String sql = "select * from book where book_name LIKE ? or author LIKE ?" +
                     " or publishing_house LIKE ? or create_time LIKE ?";
        try{
            conn = DBUtils.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,"%"+condition+"%");
            preparedStatement.setString(2,"%"+condition+"%");
            preparedStatement.setString(3,"%"+condition+"%");
            preparedStatement.setString(4,"%"+condition+"%");

            resultSet = preparedStatement.executeQuery();       // 执行查询之后的结果

            while (resultSet.next()){
                String id = resultSet.getString("id");
                String bookName = resultSet.getString("book_name");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                String publishingHouse = resultSet.getString("publishing_house");
                int amount = resultSet.getInt("amount");
                String createTime = resultSet.getString("create_time");

                books.add(new Book(id,bookName,author,price,publishingHouse,amount,createTime));
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("模糊查询数据失败");
        }finally{
            DBUtils.close(resultSet, preparedStatement, conn);
        }
        return books;
    }


    /**
     * @param: Book
     * @description: 添加书籍的SQL语句
     */
    public int addBook(Book book){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into Book(id,book_name,author,price,publishing_house,amount,create_time)values(?,?,?,?,?,?,?)";
        int result = 0;
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getId());
            ps.setString(2, book.getBookName());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setString(5,book.getPublishingHouse());
            ps.setInt(6,book.getAmount());
            ps.setString(7,book.getCreateTime());
            result = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("添加数据失败");
        }finally{
            // 关闭连接
            DBUtils.close(null, ps, conn);
        }

        return result;
    }

    /**
     * 删除图书
     * result > 0 ? "删除成功" : "删除失败"
     *
     * @param id    要删除的图书的ID
     * @description 删除图书
     */
    public int delete(String id){
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0; // 删除成功返回1，删除失败返回0

        String sql = "delete from book where id=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,id);
            result = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("删除数据失败");
        }finally{
            DBUtils.close(null, ps, conn);
        }

        return result;
    }

    /**
     * 修改书籍信息
     * @param book
     * @return
     */
    public int modifyBook(Book book){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update book set book_name=?,author=?,price=?,publishing_house=?,amout=? where id=?";
        int result = 0;

        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getBookName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setString(4,book.getPublishingHouse());
            ps.setInt(5,book.getAmount());
            ps.setString(6,book.getId());
            result = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("更新数据失败");
        }finally{
            // 关闭连接
            DBUtils.close(null, ps, conn);
        }

        return result;
    }
}
