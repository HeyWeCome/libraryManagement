package ui;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Book;
import model.Page;
import model.TableWithPaginationAndSorting;
import service.BookService;

import java.util.List;
import java.util.Optional;
/**
 * @description:
 * @author: HeyWeCome
 * @createDate: 2020/10/28 18:43
 * @version: 1.0
 */

public class MainView extends Application {
    private List<Book> dataList = getTableData();                            // 所有的书籍信息
    private TableView<Book> table = new TableView<>();                       // 创建一个表格
    private TableWithPaginationAndSorting<Book> bookTable;
    private Page<Book> page = new Page<>(dataList, 10);             // 创建Page对象 create Page object
    private BorderPane mainPane = new BorderPane();

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("图书馆管理系统");
        // 添加图标
        stage.getIcons().add(new Image("http://icons.iconarchive.com/icons/double-j-design/ravenna-3d/128/Books-icon.png"));

        table = createTable(stage);
        table.setItems(FXCollections.observableList(dataList));

        // add pagination into table
        bookTable = new TableWithPaginationAndSorting<>(page, table);

        Button addButton = new Button("新增书籍");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showAddBookDialog(stage, table);
            }
        });

        Label label = new Label("搜一搜");
        TextField search = new TextField();

        search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                dynamicRefreshTable(search.getText());
            }
        });

        HBox head = new HBox();
        head.setSpacing(700);
        HBox searchComp = new HBox(label,search);
        searchComp.setSpacing(20);
        searchComp.getChildren().addAll();
        head.getChildren().addAll(addButton,searchComp);
        mainPane.setTop(head);
        mainPane.setCenter(bookTable.getTableViewWithPaginationPane());
        stage.setScene(new Scene(mainPane,1000,400));
        stage.setResizable(false);
        stage.show();
    }

    private TableView<Book> createTable(Stage stage){
        /**
         * 定义列名
         * Define column names
         */
        TableView<Book> table = new TableView<>();

        TableColumn<Book, String> bookName = new TableColumn<>("书名");
        bookName.setCellValueFactory(new PropertyValueFactory("bookName"));

        TableColumn<Book, String> author = new TableColumn<>("作者");
        author.setCellValueFactory(new PropertyValueFactory("author"));

        TableColumn<Book, Double> price = new TableColumn<>("价格");
        price.setCellValueFactory(new PropertyValueFactory("price"));

        TableColumn<Book, String> publishingHouse = new TableColumn<>("出版社");
        publishingHouse.setCellValueFactory(new PropertyValueFactory("publishingHouse"));

        TableColumn<Book, Integer> amount = new TableColumn<>("数量");
        amount.setCellValueFactory(new PropertyValueFactory("amount"));

        TableColumn<Book, String> createTime = new TableColumn<>("入库时间");
        createTime.setCellValueFactory(new PropertyValueFactory("createTime"));

        TableColumn<Book, Boolean> operator = new TableColumn<>("操作");
        operator.setSortable(false);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        // 操作只会出现在非空的数据行
        operator.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Book, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        // create a cell value factory with an delete button for each row in the table.
        // 在表格中的每一行都放置一个删除以及修改按钮
        operator.setCellFactory(new Callback<TableColumn<Book, Boolean>, TableCell<Book, Boolean>>() {
            @Override public TableCell<Book, Boolean> call(TableColumn<Book, Boolean> personBooleanTableColumn) {
                return new operatorCell(stage, table);
            }
        });

        table.getColumns().addAll(bookName, author, price, publishingHouse, amount,createTime,operator);
        // 设置表格自适应
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    /**
     * A table cell containing a button for deleting book and a button for modifying book.
     * 修改书籍和删除书籍按钮组
     */
    private class operatorCell extends TableCell<Book,Boolean>{
        //删除一本书的按钮
        final Button deleteButton = new Button("删除");
        final Button modifyButton = new Button("修改");
        // 在单元格中放置新增按钮并居中的布局
        final HBox paddedButton = new HBox();
        // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        /**
         * 构造函数 operatorCell constructor
         * @param stage：表格放置的舞台 the stage in which the table is placed.
         * @param table：按钮放置的表格 the table to which buttons can be added.
         */
        operatorCell(final Stage stage, final TableView table) {
            paddedButton.getChildren().addAll(modifyButton,deleteButton);

            /**
             * 修改操作
             */
            modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // 聚焦至要删除的那一行数据
                    table.getSelectionModel().select(getTableRow().getIndex());
                    // 获取要修改的书籍信息
                    Book modifiedBook = (Book) table.getSelectionModel().getSelectedItem();
                    showModifyBookDialog(stage,table,modifiedBook);
                }
            });

            /**
             * 删除操作
             */
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    // 聚焦至要删除的那一行数据
                    table.getSelectionModel().select(getTableRow().getIndex());
                    // 判断用户是否要删除
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("是否删除该书籍");
                    Optional result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        // 获取要删除的书籍信息
                        Book deleteBook = (Book) table.getSelectionModel().getSelectedItem();
                        System.out.println(deleteBook.toString());
                        BookService bookService = new BookService();

                        int deleteResult = bookService.deleteBook(deleteBook.getId());

                        // 返回1 删除成功 返回 0 删除失败
                        if(deleteResult == 1){
                            Alert deleteResultNotify = new Alert(Alert.AlertType.INFORMATION);
                            deleteResultNotify.setTitle("删除结果提示");
                            deleteResultNotify.setHeaderText(null);
                            deleteResultNotify.setContentText("删除成功");
                            deleteResultNotify.showAndWait();
                            dataList.remove(deleteBook);
                            bookTable.updateTable(dataList);
                        }else {
                            Alert deleteResultNotify = new Alert(Alert.AlertType.WARNING);
                            deleteResultNotify.setTitle("删除结果提示");
                            deleteResultNotify.setHeaderText(null);
                            deleteResultNotify.setContentText("删除失败");
                            deleteResultNotify.showAndWait();
                        }
                    } else {
                        alert.close();
                    }
                }
            });
    }

        /**
         * 当这一行的数据不为空时，放置一个新增按钮
         * places an add button in the row only if the row is not empty.
         */
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }

    /**
     * shows a dialog which displays a UI for adding a book to a table.
     * 在添加书籍的时候展示一个新的框
     * @param parent a parent stage to which this dialog will be modal and placed next to.
     * @param table the table to which a book is to be added.
     */
    private void showAddBookDialog(Stage parent, final TableView<Book> table) {
        // 初始化对话框
        final Stage dialog = new Stage();
        dialog.setTitle("新增书籍");
        dialog.initOwner(parent);                    // 对话框永远在前面
        dialog.initModality(Modality.WINDOW_MODAL);  // 必须关闭对话框后才能操作其他的
        dialog.initStyle(StageStyle.UTILITY);        // 对话框-只保留关闭按钮
        dialog.setX(parent.getX() + parent.getWidth());

        // create a grid for the data entry.
        // 为输入创建面板
        GridPane grid = new GridPane();
        final TextField bookName = new TextField();
        final TextField author = new TextField();
        final TextField price = new TextField();
        final TextField publishingHouse = new TextField();
        final TextField amount = new TextField();

        // 将输入框添加至面板中
        grid.addRow(0, new Label("书籍名称"), bookName);
        grid.addRow(1, new Label("书籍作者"), author);
        grid.addRow(2, new Label("书籍价格"), price);
        grid.addRow(3,new Label("书籍出版社"),publishingHouse);
        grid.addRow(4,new Label("书籍数量"),amount);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setHgrow(bookName, Priority.ALWAYS);
        grid.setHgrow(author, Priority.ALWAYS);
        grid.setHgrow(price, Priority.ALWAYS);
        grid.setHgrow(publishingHouse, Priority.ALWAYS);
        grid.setHgrow(amount, Priority.ALWAYS);

        // create action buttons for the dialog.
        // 为新增框创建一个操作按钮
        Button ok = new Button("新增");
        ok.setDefaultButton(true);
        Button cancel = new Button("取消");
        cancel.setCancelButton(true);

        // only enable the ok button when there has been some text entered.
        // 只有在输入框中有数据输入才会允许点击提交按钮
        ok.disableProperty().bind(bookName.textProperty().isEqualTo("").or(author.textProperty().isEqualTo("")
                            .or(price.textProperty().isEqualTo("")).or(publishingHouse.textProperty().isEqualTo(""))
                            .or(amount.textProperty().isEqualTo(""))));

        // add action handlers for the dialog buttons.
        // 将新增的数据添加到表格中
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Book book = new Book(bookName.getText(), author.getText(),
                                     Double.valueOf(price.getText()), publishingHouse.getText(),
                                     Integer.valueOf(amount.getText()));

                BookService bookService = new BookService();
                int addResult = bookService.addBook(book);

                // 根据后台传回数据的不同，展示不同的信息
                String result;
                if(addResult == 1) result = "添加成功";
                else if(addResult == 2) result = "已经存在这本书了";
                else result = "添加失败";

                // 返回新增的结果
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("新增结果提示");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();

                // 清空输入框
                bookName.clear();
                author.clear();
                price.clear();
                publishingHouse.clear();
                amount.clear();
                // 解决表格中新增书籍，超过每页限额的时候还是会添加的bug，还有删除新增书籍的bug
                bookTable.updateTable(getTableData());
                dialog.close();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });

        // layout the dialog.
        // 放置对话框
        HBox buttons = new HBox();
        buttons.getChildren().addAll(ok,cancel);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        dialog.setScene(new Scene(layout));
        dialog.setResizable(false);     // 禁止缩放
        dialog.show();
    }

    /**
     * shows a dialog which displays a UI for modifying a book in the table.
     * 在用户点击修改表格的时候，弹出一个修改对话框
     *
     * @param parent 表格展示所在的舞台，
     * @param table  数据来源，修改的书籍所在的表格
     * @param book   修改的书籍信息
     */
    private void showModifyBookDialog(Stage parent, final TableView<Book> table, Book book){
        // 初始化对话框
        final Stage dialog = new Stage();
        dialog.setTitle("修改书籍");
        dialog.initOwner(parent);                    // 对话框永远在前面
        dialog.initModality(Modality.WINDOW_MODAL);  // 必须关闭对话框后才能操作其他的
        dialog.initStyle(StageStyle.UTILITY);        // 对话框-只保留关闭按钮
        dialog.setX(parent.getX() + parent.getWidth());

        // create a grid for the data entry and put data into it.
        // 为输入创建面板，并填充数据
        GridPane grid = new GridPane();
        final TextField bookName = new TextField(book.getBookName());
        final TextField author = new TextField(book.getAuthor());
        final TextField price = new TextField(String.valueOf(book.getPrice()));
        final TextField publishingHouse = new TextField(book.getPublishingHouse());
        final TextField amount = new TextField(String.valueOf(book.getAmount()));

        // 将输入框添加至面板中
        grid.addRow(0, new Label("书籍名称"), bookName);
        grid.addRow(1, new Label("书籍作者"), author);
        grid.addRow(2, new Label("书籍价格"), price);
        grid.addRow(3,new Label("书籍出版社"),publishingHouse);
        grid.addRow(4,new Label("书籍数量"),amount);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setHgrow(bookName, Priority.ALWAYS);
        grid.setHgrow(author, Priority.ALWAYS);
        grid.setHgrow(price, Priority.ALWAYS);
        grid.setHgrow(publishingHouse, Priority.ALWAYS);
        grid.setHgrow(amount, Priority.ALWAYS);

        // create action buttons for the dialog.
        // 为新增框创建一个操作按钮
        Button ok = new Button("修改");
        ok.setDefaultButton(true);
        Button cancel = new Button("取消");
        cancel.setCancelButton(true);

        // only enable the ok button when there has been some text entered.
        // 只有在输入框中有数据输入才会允许点击提交按钮
        ok.disableProperty().bind(bookName.textProperty().isEqualTo("").or(author.textProperty().isEqualTo("")
                .or(price.textProperty().isEqualTo("")).or(publishingHouse.textProperty().isEqualTo(""))
                .or(amount.textProperty().isEqualTo(""))));

        // add action handlers for the dialog buttons.
        // 处理需要修改的数据
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                book.setBookName(bookName.getText());
                book.setAuthor(author.getText());
                book.setPrice(Double.valueOf(price.getText()));
                book.setPublishingHouse(publishingHouse.getText());
                book.setAmount(Integer.valueOf(amount.getText()));

                System.out.println("要修改的书籍信息：" + book.toString());
                BookService bookService = new BookService();
                int modifyResult = bookService.modifyBook(book);

                // 根据后台传回数据的不同，展示不同的信息
                String result = (modifyResult == 1) ? "修改成功" : "修改失败";

                // 返回新增的结果
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("修改结果提示");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();

                // 解决表格中新增书籍，超过每页限额的时候还是会添加的bug，还有删除新增书籍、修改书籍的bug
                bookTable.updateTable(getTableData());
                dialog.close();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });

        // layout the dialog.
        // 放置对话框
        HBox buttons = new HBox();
        buttons.getChildren().addAll(ok,cancel);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        dialog.setScene(new Scene(layout));
        dialog.setResizable(false);     // 禁止缩放
        dialog.show();
    }

    /**
     * 根据模糊搜索的结果 动态刷新表格数据
     * @param condition
     */
    public void dynamicRefreshTable(String condition){
        BookService bookService = new BookService();
        List<Book> books = bookService.fuzzySearchBook(condition);
        dataList.clear();

        for (Book book : books) {
            dataList.add(book);
        }

        bookTable.updateTable(dataList);
    }

    /**
     * 查找所有的书籍
     * @return
     */
    private List<Book> getTableData() {
        BookService bookService = new BookService();
        return bookService.searchAllBook();
    }
}

