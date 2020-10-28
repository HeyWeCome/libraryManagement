package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn.CellEditEvent;
import model.Book;
import service.BookService;

import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.text.FontWeight.BOLD;

/**
 * @description:
 * @author: HeyWeCome
 * @createDate: 2020/10/2 14:48
 * @version: 1.0
 */
public class Main extends Application {
    BookService bookService = new BookService();

    ObservableList<Book> data = FXCollections.observableArrayList(); // 最后的结果

    TableView table = new TableView(); // 创建表格,存放书籍的数据

    public static void main(String[] args) {
        Application.launch(args);
    }

    // 重写应用中的启动方法
    @Override
    public void start(Stage stage) {
        // 创建欢迎标语
        Label welcomeTitle = new Label("欢迎登录图书馆管理系统");
        // 设置标题样式
        welcomeTitle.setFont(Font.font("宋体", BOLD,FontPosture.REGULAR,30));
        // 创建一个Pane存放欢迎标语,StackPane会将节点放置在面板中央，并且叠加在其他节点之上
        HBox titlePane = new HBox();
        // 设置长度
        titlePane.setMinWidth(800);
        titlePane.setAlignment(Pos.CENTER);
        titlePane.getChildren().add(welcomeTitle);

        // 设置结点存放菜单栏选项，存放菜单选项
        HBox menu = new HBox();
        menu.setMinWidth(400);
        menu.setSpacing(50);

        // 创建最主要的面板
        BorderPane borderPane = new BorderPane();
        // 内边距
        borderPane.setPadding(new Insets(40));
        borderPane.setMinHeight(600);
        borderPane.setMinWidth(670);
        
        // 设置标题
        borderPane.setTop(welcomeTitle);
        // 设置选项
        borderPane.setCenter(menu);

        // 存放每一个书籍
        HBox items = new HBox();

        // 设置表格可编辑
        table.setEditable(true);

        TableColumn id = new TableColumn("书籍编号");
        id.setCellValueFactory(new PropertyValueFactory<Book, String>("id"));

        TableColumn bookName = new TableColumn("书籍名称");
        bookName.setCellValueFactory(new PropertyValueFactory<Book, String>("bookName"));
        // 设置直接点击单元格修改
        bookName.setCellFactory(TextFieldTableCell.forTableColumn());
        bookName.setOnEditCommit(
            new EventHandler<CellEditEvent<Book, String>>() {
                @Override
                public void handle(CellEditEvent<Book, String> t) {
                    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookName(t.getNewValue());

                    Book newBook = (Book) t.getTableView().getItems().get(t.getTablePosition().getRow()); // 获取最新的书籍信息
                    int modifyResult = bookService.modifyBook(newBook);                                   // 修改书籍的结果

                    System.out.println(modifyResult > 0 ? "修改成功 " : "修改失败");
                }
            }
        );

        TableColumn author = new TableColumn("书籍作者");
        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        // 设置直接点击单元格修改书籍作者
        author.setCellFactory(TextFieldTableCell.forTableColumn());
        author.setOnEditCommit(
                new EventHandler<CellEditEvent<Book, String>>() {
                    @Override
                    public void handle(CellEditEvent<Book, String> t) {
                        ((Book) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setAuthor(t.getNewValue());           // 修改图书的作者

                        Book newBook = (Book) t.getTableView().getItems().get(t.getTablePosition().getRow()); // 获取最新的书籍信息
                        int modifyResult = bookService.modifyBook(newBook);                                   // 修改书籍的结果
                    }
                }
        );

        TableColumn price = new TableColumn("书籍价格");
        price.setCellValueFactory(new PropertyValueFactory<Book, String>("price"));
        // 设置直接点击单元格修改书籍价格
//        price.setCellFactory(TextFieldTableCell.forTableColumn());
//        price.setOnEditCommit(
//            new EventHandler<CellEditEvent<Book, String>>() {
//                @Override
//                public void handle(CellEditEvent<Book, String> t) {
//                    ((Book) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setPrice(Double.valueOf((t.getNewValue())));
//                }
//            }
//        );

        TableColumn publishingHouse = new TableColumn("出版社");
        publishingHouse.setCellValueFactory(new PropertyValueFactory<Book, String>("publishingHouse"));
        // 设置直接点击单元格修改书籍出版社
        publishingHouse.setCellFactory(TextFieldTableCell.forTableColumn());
        publishingHouse.setOnEditCommit(
            new EventHandler<CellEditEvent<Book, String>>() {
                @Override
                public void handle(CellEditEvent<Book, String> t) {
                    ((Book) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPublishingHouse((t.getNewValue()));
                }
            }
        );

        TableColumn amount = new TableColumn("书籍数量");
        amount.setCellValueFactory(new PropertyValueFactory<Book, String>("amount"));
        // 设置直接点击单元格修改书籍数量
//        amount.setCellFactory(TextFieldTableCell.forTableColumn());
//        amount.setOnEditCommit(
//            new EventHandler<CellEditEvent<Book, String>>() {
//                @Override
//                public void handle(CellEditEvent<Book, String> t) {
//                    ((Book) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setAmount((Integer.valueOf(t.getNewValue())));
//                }
//            }
//        );

        TableColumn createTime = new TableColumn("创建日期");
        createTime.setCellValueFactory(new PropertyValueFactory<Book, String>("createTime"));

        TableColumn operator = new TableColumn("双击删除");
/*        operator.setCellFactory(param -> new TableCell<>() {
            private final JFXButton editButton = new JFXButton("edit");
            private final JFXButton deleteButton = new JFXButton("delete");

            protected void updateItem() {

                deleteButton.setOnAction(event -> {
//                    Patient getPatient = getTableView().getItems().get(getIndex());
//                    System.out.println(getPatient.getNom() + "   " + getPatient.getPrenom());
                });

                editButton.setOnAction(event -> {
//                    Patient getPatient = getTableView().getItems().get(getIndex());
//                    System.out.println(getPatient.getNom() + "   " + getPatient.getPrenom());
                });

                setGraphic(deleteButton);//<<<---------------add button 1
                setGraphic(editButton);//<<------------------add button 2
            }
        });*/

        // 添加至表格中
        table.getColumns().addAll(id, bookName, author, price, publishingHouse, amount, createTime,operator);

        // 双击删除某一行
        table.setRowFactory( tv -> {
            TableRow<Book> row = new TableRow<Book>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    // 判断用户是否要删除
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("是否删除该书籍");

                    // 验证用户是否真的要删除
                    Optional result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        Book book = row.getItem();      // 获取到点击那一行的书籍信息
                        int deleteResult = bookService.deleteBook(book.getId());

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

                        data.remove(book);
                    } else {
                        alert.close();
                    }
                }
            });
            return row ;
        });

        Button addBook = new Button("新增书籍"); // 新增书籍

        /**
         * 新增书籍的触发函数
         */
        addBook.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(mouseEvent);
                AddBookView addBookView = new AddBookView();

                Stage addBookStage = new Stage();
                try {
                    addBookView.start(addBookStage);
                    searchAllBooks();
                    table.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 将表格和新增都存放在一起
        VBox tableAndAdd = new VBox();
        tableAndAdd.setSpacing(5);
        tableAndAdd.setPadding(new Insets(10, 0, 0, 10));
        tableAndAdd.getChildren().addAll(table,addBook);

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
        // 将书籍都放进去
        borderPane.setBottom(tableAndAdd);

        Scene scene = new Scene(borderPane);

        stage.setTitle("图书管理页面");
        stage.setScene(scene);
        stage.show();
    }



    /**
     * 查找所有的书籍，每次都重新覆盖前台数据
     */
    public void searchAllBooks(){
        ArrayList<Book> books = bookService.searchAllBook();
        data.clear();       // 首先全部清空

        for (Book book : books) {
            data.add(book);
        }

        table.refresh();
    }
}
