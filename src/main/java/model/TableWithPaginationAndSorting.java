package model;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * @description:
 * 这个类中最重要的是方法updatePagination，这个方法体中设置了Pagination的页面工厂，
 * 这里使用Lambda表达式传入一个回调方法，回调方法会在一个页面被选中时触发。
 * 它会加载并返回被选中页面的内容。如果当前被选中的页面索引不存在，则必须返回null值。
 * 在这个回调方法中：我们接收传过来的当前页码（pageIndex，从0开始），
 * 然后利用Page对象的getCurrentPageDataList(pageIndex)方法获取当页的数据，转换格式并添加到TableView中，最后返回TableView。
 *
 * 关于page factory，可以将它想象成一个加工厂，它负责根据提供的页码生产对应的页面，
 * 所以可以根据不同的页码显示不同的内容。并且这里我们不用每次都新建一个表格，只需要每次将数据添加到建好了的表格框架
 *
 * @author: HeyWeCome
 * @createDate: 2020/10/29 14:36
 * @version: 1.0
 */

public class TableWithPaginationAndSorting<T> {
    private Page<T> page;
    private TableView<T> tableView;
    private Pagination tableViewWithPaginationPane;

    /** getter **/
    public TableWithPaginationAndSorting(Page<T> page, TableView<T> tableView) {
        this.page = page;
        this.tableView = tableView;
        tableViewWithPaginationPane = new Pagination();
        tableViewWithPaginationPane.pageCountProperty().bindBidirectional(page.totalPageProperty());
        updatePagination();
    }

    private void updatePagination() {
        tableViewWithPaginationPane.setPageFactory(pageIndex -> {
            tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
            System.out.println("当前的页面为："+pageIndex);
            return tableView;
        });
    }

    /**
     * tableViewWithPaginationPane.pageCountProperty().bindBidirectional(page.totalPageProperty());
     * 这一段代码是将Pagination的pageCountProperty和Page对象的page.totalPageProperty属性进行双向绑定，
     * 这样他们的值就会同步：其中一个改变，另一个也会改变，并且值保持一样。
     * 如果不使用Lambda，也可以使用匿名函数：
     */
    /* private void updatePagination() {
        tableViewWithPaginationPane.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
                return tableView;
            }
        });
    }*/

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<T> tableView) {
        this.tableView = tableView;
    }

    public Pagination getTableViewWithPaginationPane() {
        return tableViewWithPaginationPane;
    }

    public void setTableViewWithPaginationPane(Pagination tableViewWithPaginationPane) {
        this.tableViewWithPaginationPane = tableViewWithPaginationPane;
    }
}