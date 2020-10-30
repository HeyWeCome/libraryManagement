package model;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;
/**
 * @description: 控制分页
 *              这个Page类的作用是传入一个需要分页的数据（rowDataList）和每页显示的行数（pageSize）
 *              然后在initialize()方法中自动计算：数据的总记录数（totalRecord），总页数（totalPage）。
 *              这里使用了SimpleIntegerProperty类，这个类使得我们方便地为变量添加监听器。
 *              使用泛型，便于组件的重用
 * @author: HeyWeCome
 * @createDate: 2020/10/29 14:15
 * @version: 1.0
 */
public class Page<T> {
    private SimpleIntegerProperty totalRecord;  // 数据源中的记录数量 total record number in source data
    private SimpleIntegerProperty pageSize;     // 每页展示的数据个数 the number of data in per page
    private SimpleIntegerProperty totalPage;    // 总共的页数 total page number
    private List<T> rowDataList;                // 全部数据 total data

    /**
     * 构造方法 constructor of Page
     * @param rowDataList 数据列表 source data
     * @param pageSize 定制每个页面的数据个数 the number of data in per page
     */
    public Page(List<T> rowDataList, int pageSize) {
        this.totalRecord = new SimpleIntegerProperty();
        this.totalPage = new SimpleIntegerProperty();
        this.rowDataList = rowDataList;
        this.pageSize = new SimpleIntegerProperty(pageSize);
        initialize();
    }

    /**
     * 初始化
     */
    private void initialize() {
        // 设置数据数量 set total number
        totalRecord.set(rowDataList.size());

        // 计算总的页数 calculate the number of total pages
        totalPage.set(
                totalRecord.get() % pageSize.get() == 0 ?
                        totalRecord.get() / pageSize.get() :
                        totalRecord.get() / pageSize.get() + 1);

        // 为变量pageSize添加了一个监听器，如果pageSize的值改变，那么总页数（totalPage）也需要随之改变。
        // add listener: the number of total pages need to be change if the page size changed
        pageSize.addListener((observable, oldVal, newVal) ->
                totalPage.set(
                        totalRecord.get() % pageSize.get() == 0 ?
                                totalRecord.get() / pageSize.get() :
                                totalRecord.get() / pageSize.get() + 1)
        );
    }

    /**
     * 为了解决一个问题：
     * 删除，新增数据的时候，Page中的数据没有得到更新，导致出现了数据不统一的情况
     */
    public void updatePage(){
        initialize();
    }

    /**
     * 会根据传入的页码，返回当前页的数据
     * current page number(0-based system)
     *
     * @param currentPage 当前页面是第几页 current page number
     * @return
     */
    public List<T> getCurrentPageDataList(int currentPage) {
        int fromIndex = pageSize.get() * currentPage;
        int tmp = pageSize.get() * currentPage + pageSize.get() - 1;
        int endIndex = tmp >= totalRecord.get() ? totalRecord.get() - 1 : tmp;

        // subList(fromIndex, toIndex) -> [fromIndex, toIndex)
//        System.out.println(rowDataList.subList(fromIndex, endIndex+1));
        return rowDataList.subList(fromIndex, endIndex+1);
    }

    /**
     * 下面都是get set方法
     */
    public int getTotalRecord() {
        return totalRecord.get();
    }

    public SimpleIntegerProperty totalRecordProperty() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord.set(totalRecord);
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public SimpleIntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public int getTotalPage() {
        return totalPage.get();
    }

    public SimpleIntegerProperty totalPageProperty() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage.set(totalPage);
    }

    public List<T> getRowDataList() {
        return rowDataList;
    }

    public void setRowDataList(List<T> rowDataList) {
        this.rowDataList = rowDataList;
    }
}
