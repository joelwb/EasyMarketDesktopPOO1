/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author joel-
 */
public abstract class TableViewConfigurator {
    public static void configure(TableView<List<String>> table){
        for (int i = 0; i < table.getColumns().size(); i++){
            TableColumn<List<String>,String> col = (TableColumn<List<String>,String>) table.getColumns().get(i);
            final int j = i;
            
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j));
                }
            });
        }
    }
}
