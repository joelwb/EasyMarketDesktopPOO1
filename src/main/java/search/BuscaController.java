/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PropertySheet;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaController implements Initializable {
    private FilterComunication filterComunc;

    @FXML
    private PropertySheet filter;
    @FXML
    private VBox content;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setContent(Parent view){
        content.getChildren().add(view);
    }
    
    public void setFilterComunication(FilterComunication filterComunc){
        this.filterComunc = filterComunc;
    }
    
    public void setFilters(List<FilterData> filters) {
        
    }

    @FXML
    private void buscar(ActionEvent event) {
        filterComunc.listenResponse(null);
    }

}
