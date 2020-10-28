package ui;

/**
 * @description:
 * @author: HeyWeCome
 * @createDate: 2020/10/28 18:43
 * @version: 1.0
 */

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Book;
import service.BookService;
import test.Person;

import java.util.ArrayList;
import java.util.Optional;

public class MainView extends Application {
    ObservableList<Book> data = FXCollections.observableArrayList(); // 所有的书籍信息
    final TableView<Book> table = new TableView<>();                 // 创建一个表格

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("图书馆管理系统");
        // 添加图标
        stage.getIcons().add(new Image("http://icons.iconarchive.com/icons/double-j-design/ravenna-3d/128/Books-icon.png"));

        /**
         * 定义列名
         * Define column names
         */
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

        table.getColumns().setAll(bookName, author, price, publishingHouse, amount,createTime,operator);
        // 设置表格自适应
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /**
         * 加载所有的书籍
         */
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                searchAllBooks();
            }
        });
        table.setItems(data);

        stage.setScene(new Scene(table));
        stage.show();
    }

    /**
     * A table cell containing a button for adding a new book.
     * 新增书籍按钮
     */
    private class AddBookCell extends TableCell<Book, Boolean> {
        // a button for adding a new person.
        final Button deleteButton = new Button("新增");
        // pads and centers the add button in the cell.
        final StackPane paddedButton = new StackPane();
        // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        /**
         * AddPersonCell constructor
         * @param stage the stage in which the table is placed.
         * @param table the table to which a new person can be added.
         */
        AddBookCell(final Stage stage, final TableView table) {
            paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(deleteButton);
            deleteButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    buttonY.set(mouseEvent.getScreenY());
                }
            });
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    showAddPersonDialog(stage, table, buttonY.get());
                    table.getSelectionModel().select(getTableRow().getIndex());
                    //Person person = new Person();
                    //table.getSelectionModel().select(getIndex());
                    //person = table.getSelectionModel().getSelectedItem();
                }
            });
        }

        /** places an add button in the row only if the row is not empty. */
        @Override protected void updateItem(Boolean item, boolean empty) {
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
                        }else {
                            Alert deleteResultNotify = new Alert(Alert.AlertType.WARNING);
                            deleteResultNotify.setTitle("删除结果提示");
                            deleteResultNotify.setHeaderText(null);
                            deleteResultNotify.setContentText("删除失败");
                            deleteResultNotify.showAndWait();
                        }
                        data.remove(deleteBook);
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
     * shows a dialog which displays a UI for adding a person to a table.
     * @param parent a parent stage to which this dialog will be modal and placed next to.
     * @param table the table to which a person is to be added.
     * @param y the y position of the top left corner of the dialog.
     */
    private void showAddPersonDialog(Stage parent, final TableView<Person> table, double y) {
        // initialize the dialog.
        final Stage dialog = new Stage();
        dialog.setTitle("New Person");
        dialog.initOwner(parent);  //对话框永远在前面
        dialog.initModality(Modality.WINDOW_MODAL);  //必须关闭对话框后才能操作其他的
        dialog.initStyle(StageStyle.UTILITY); //对话框-只保留关闭按钮
        dialog.setX(parent.getX() + parent.getWidth());
        dialog.setY(y);

        // create a grid for the data entry.
        GridPane grid = new GridPane();
        final TextField firstNameField = new TextField();
        final TextField lastNameField = new TextField();
        grid.addRow(0, new Label("First Name"), firstNameField);
        grid.addRow(1, new Label("Last Name"), lastNameField);
        grid.setHgap(10);
        grid.setVgap(10);
        GridPane.setHgrow(firstNameField, Priority.ALWAYS);
        GridPane.setHgrow(lastNameField, Priority.ALWAYS);

        // create action buttons for the dialog.
        Button ok = new Button("OK");
        ok.setDefaultButton(true);
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);

        // only enable the ok button when there has been some text entered.
        ok.disableProperty().bind(firstNameField.textProperty().isEqualTo("").or(lastNameField.textProperty().isEqualTo("")));

        // add action handlers for the dialog buttons.
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int nextIndex = table.getSelectionModel().getSelectedIndex() + 1;
                table.getItems().add(nextIndex, new Person(firstNameField.getText(), lastNameField.getText()));
                table.getSelectionModel().select(nextIndex);
                dialog.close();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });

        // layout the dialog.
        HBox buttons = new HBox();
        buttons.getChildren().addAll(ok,cancel);
//                HBoxBuilder.create().spacing(10).children(ok, cancel).alignment(Pos.CENTER_RIGHT).build();
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        dialog.setScene(new Scene(layout));
        dialog.show();
    }

    /**
     * 查找所有的书籍，每次都重新覆盖前台数据
     */
    public void searchAllBooks(){
        BookService bookService = new BookService();
        ArrayList<Book> books = bookService.searchAllBook();
        data.clear();       // 首先全部清空

        for (Book book : books) {
            data.add(book);
        }

        table.refresh();
    }
}

