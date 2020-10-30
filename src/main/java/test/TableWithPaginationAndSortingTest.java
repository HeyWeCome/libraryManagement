package test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Page;
import model.TableWithPaginationAndSorting;

import java.util.Arrays;
import java.util.List;

public class TableWithPaginationAndSortingTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create table
        TableView<People> peopleTable = createTable();

        // get data
        List<People> peopleList = getTableData();
        peopleTable.setItems(FXCollections.observableList(peopleList));

        // create Page object
        Page<People> page = new Page<>(peopleList, 2);

        // add pagination into table
        TableWithPaginationAndSorting<People> table = new TableWithPaginationAndSorting<>(page, peopleTable);

        Scene scene = new Scene(new BorderPane(table.getTableViewWithPaginationPane()), 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<People> createTable() {
        TableView<People> table = new TableView<>();
        TableColumn<People, String> nameCol = new TableColumn<>("name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<People, Integer> ageCol = new TableColumn<>("age");
        ageCol.setCellValueFactory(new PropertyValueFactory("age"));
        table.getColumns().addAll(nameCol, ageCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private List<People> getTableData() {
        return Arrays.asList(new People[]{
                        new People("Huang1", 18),
                        new People("Huang2", 11),
                        new People("Huang3", 13),
                        new People("Huang4", 28),
                        new People("Huang5", 38)
                }
        );
    }

    public class People {
        private String name;
        private int age;

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}