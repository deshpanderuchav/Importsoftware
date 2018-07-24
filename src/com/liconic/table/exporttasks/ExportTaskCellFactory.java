package com.liconic.table.exporttasks;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.util.Callback;

public class ExportTaskCellFactory implements Callback<TreeTableColumn, TreeTableCell> {

    @Override
    public TreeTableCell call(TreeTableColumn p) {

        TreeTableCell cell = new TreeTableCell<ExportTaskTableModel, Object>() {

            @Override
            public void updateItem(Object item, boolean empty) {

                super.updateItem(item, empty);

                setText(empty ? null : getString());

                setGraphic(null);

                TreeTableRow currentRow = getTreeTableRow();

                ExportTaskTableModel currentRecord = (ExportTaskTableModel) currentRow.getItem();

                try {

                    if (currentRecord != null) {

                        if (currentRecord.getNumber() % 2 == 0) {
                            currentRow.getStyleClass().remove("oddRow");
                            currentRow.getStyleClass().add("evenRow");

                        } else {
                            currentRow.getStyleClass().remove("evenRow");
                            currentRow.getStyleClass().add("oddRow");
                        }

                    }

                } catch (Exception E) {
                    System.out.println("ImportTaskCellFactory Error: " + E.getMessage());
                }

            }

            @Override
            public void updateSelected(boolean upd) {

                super.updateSelected(upd);

                System.out.println("is update");
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };

        return cell;
    }
}
