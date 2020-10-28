package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import service.BookService;

/**
 * @description:
 * @author: HeyWeCome
 * @createDate: 2020/10/7 14:57
 * @version: 1.0
 */
public class AddBookView extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 创建最主要的面板
        BorderPane borderPane = new BorderPane();
        // 内边距
        borderPane.setPadding(new Insets(40));
        borderPane.setMinHeight(200);
        borderPane.setMinWidth(370);

        HBox bookName = new HBox();            // 输入书籍的名称
        HBox author = new HBox();              // 输入书籍的作者
        HBox price = new HBox();               // 输入书籍的价格
        HBox publishingHouse = new HBox();     // 输入书籍的出版社
        HBox amount = new HBox();              // 输入书籍的数量

        // 美化书籍名称
        Text bookNameNotify = new Text("请输入书籍名称：");
        TextField bookNameInput = new TextField();
        bookNameInput.setPromptText("书籍名称");
        bookNameInput.setMaxWidth(bookName.getPrefWidth());
        bookName.getChildren().addAll(bookNameNotify,bookNameInput);

        // 美化书籍作者
        Text authorNotify = new Text("请输入书籍作者：");
        TextField authorInput = new TextField();
        authorInput.setPromptText("书籍作者");
        authorInput.setMaxWidth(author.getPrefWidth());
        author.getChildren().addAll(authorNotify,authorInput);

        // 美化书籍价格
        Text priceNotify = new Text("请输入书籍价格：");
        TextField priceInput = new TextField();
        priceInput.setPromptText("书籍价格");
        priceInput.setMaxWidth(price.getPrefWidth());
        price.getChildren().addAll(priceNotify,priceInput);

        // 美化书籍出版社信息
        Text publishingHouseNotify = new Text("请输入出版信息：");
        TextField publishingHouseInput = new TextField();
        publishingHouseInput.setPromptText("书籍出版社信息");
        publishingHouseInput.setMaxWidth(price.getPrefWidth());
        publishingHouse.getChildren().addAll(publishingHouseNotify,publishingHouseInput);

        // 美化书籍数量
        Text amountNotify = new Text("请输入书籍数量：");
        TextField amountnput = new TextField();
        amountnput.setPromptText("书籍数量");
        amountnput.setMaxWidth(price.getPrefWidth());
        amount.getChildren().addAll(amountNotify,amountnput);

        Button addButton = new Button("新增");
        addButton.setMinSize(255,30);

        VBox allInputBox = new VBox();              // 所有的输入框总体的存放
        allInputBox.setPadding(new Insets(20));
        allInputBox.getChildren().addAll(bookName,author,price,publishingHouse,amount,addButton); // 将新增按钮放置到面板中
        borderPane.setCenter(allInputBox);          // 放置到底层面板中

        // 新增按钮触发的事件，与数据库进行交互
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Book newBook = new Book(bookNameInput.getText(), authorInput.getText(),
                                        Double.valueOf(priceInput.getText()), publishingHouseInput.getText(),
                                        Integer.valueOf(amountnput.getText()));

                BookService bookService = new BookService();
                int addResult = bookService.addBook(newBook);

                String result;                  // 根据后台传回数据的不同，展示不同的信息
                if(addResult == 1){
                    result = "添加成功";
                }else if(addResult == 2){
                    result = "已经存在这本书了";
                }else {
                    result = "添加失败";
                }

                // 返回新增的结果
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("新增结果提示");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();

                // 清空输入框
                bookNameInput.clear();
                authorInput.clear();
                priceInput.clear();
                publishingHouseInput.clear();
                amountnput.clear();
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setTitle("新增图书页面");
        stage.setScene(scene);
        stage.show();
    }
}
