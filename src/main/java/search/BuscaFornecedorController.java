package search;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import search.filter.FilterComunication;
import search.filter.FilterData;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaFornecedorController implements Initializable, FilterComunication {

    @FXML
    private TableView<?> fornTable;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> cnpjCol;

    public BuscaFornecedorController(BuscaController bc) {
        
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", "Fornecedor", String.class));
        filters.add(new FilterData("CNPJ", "Fornecedor", String.class));
        
        bc.setFilters(filters);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void getDetalhes(ActionEvent event) {
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String cnpj = (String) response.get("CNPJ");
    }
    
}
