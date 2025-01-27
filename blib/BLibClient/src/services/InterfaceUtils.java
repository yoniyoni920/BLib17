package services;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

/**
 * Utility functions useful for some situations on the UI
 */
public class InterfaceUtils {
    /**
     * Formats LocalDateTime to something more common in Israel
     * @param date
     * @return
     */
    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yy, HH:mm"));
    }

    /**
     * Formats LocalDate to something more common in Israel
     * @param date
     * @return
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }

    /**
     * A useful utility function to make a grid with equal sized cells
     * @param gridPane The grid pane
     * @param colsPerRow Columns per row
     * @param list The list to loop over
     * @param makeItem A method that receives an item from the list, expects a node (any JavaFX GUI element)
     * @param <T> The type of the items in the list
     */
    public static <T> void makeGrid(GridPane gridPane, int colsPerRow, List<T> list, Function<T, Node> makeItem) {
        double rowCount = Math.ceil((double)list.size()/colsPerRow);

        // Clear previous row constraints
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100.0 / rowCount);

        for (int i = 0; i < rowCount; i++) {
            gridPane.getRowConstraints().add(rc);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100.0 / colsPerRow);

        for (int i = 0; i < colsPerRow; i++) {
            gridPane.getColumnConstraints().add(cc);
        }

        // Make items and assign them into each row, col
        int row = 0, col = 0;
        for(T obj : list) {
            Node node = makeItem.apply(obj);
            gridPane.add(node, col, row);
            gridPane.setAlignment(Pos.CENTER);
            GridPane.setHalignment(node, HPos.CENTER);
            col++;
            if (col == colsPerRow) {
                col = 0;
                row++;
            }
        }
    }
}
