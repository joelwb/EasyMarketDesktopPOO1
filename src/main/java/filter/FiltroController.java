/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.MainButtonClickListener;
import filter.component.FilterItemView;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class FiltroController implements Initializable {
    private FilterComunication filterComunc;
    private MainButtonClickListener listener;

    @FXML
    private PropertySheet filter;
    @FXML
    private VBox content;

    public FiltroController(MainButtonClickListener listener) {
        this.listener = listener;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setContent(Parent view){
        VBox.setVgrow(view, Priority.ALWAYS);
        content.getChildren().add(view);
    }
    
    public void setFilterComunication(FilterComunication filterComunc){
        this.filterComunc = filterComunc;
    }
    
    public void setFilters(List<FilterData> filters) {
        for(FilterData fd : filters){
            filter.getItems().add(new FilterItemView(
                    fd.getFilterName(),
                    fd.getFilterCategory(),
                    fd.getType()));
        }
    }

    @FXML
    private void buscar(ActionEvent event) {
        Map<String,Object> response = new HashMap<>();
        
        for (Item item : filter.getItems()){
            response.put(item.getName(), item.getValue());
        }
        
        filterComunc.listenResponse(response);
    }

    @FXML
    private void back(ActionEvent event) {
        listener.cancel();
    }

}
