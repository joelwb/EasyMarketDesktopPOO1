/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaProdutoController implements Initializable, FilterComunication {
    
    public BuscaProdutoController(BuscaController bc) {
        //TODO envia paramentro pra construção do filtro
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", "Produto", String.class));
        filters.add(new FilterData("Marca", "Produto", String.class));
        filters.add(new FilterData("Tipo", "Produto", String.class));
        filters.add(new FilterData("Código", "Produto", String.class));
        
        
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

    @FXML
    private void getLotesNestVencim(ActionEvent event) {
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String marca = (String) response.get("Marca");
        String tipo = (String) response.get("Tipo");
        String codigo = (String) response.get("Codigo");
    }
    
}
