/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import filter.FiltroController;
import filter.FilterComunication;
import filter.data.FilterData;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model_info.LoteController;
import modelo.supermercado.Supermercado;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaProdutoController implements Initializable, FilterComunication {
    private final Supermercado supermercado;
    private LoteController lc;
    
    @FXML
    private TableView<?> prodTable;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> codCol;
    @FXML
    private Button selectButton;
    
    
    public BuscaProdutoController(FiltroController bc, Supermercado supermercado) {
        this.supermercado = supermercado;
        
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", "Produto", String.class));
        filters.add(new FilterData("Marca", "Produto", String.class));
        filters.add(new FilterData("Tipo", "Produto", String.class));
        filters.add(new FilterData("Código", "Produto", String.class));
        
        bc.setFilters(filters);
    }
    
    public BuscaProdutoController(FiltroController bc, Supermercado supermercado, LoteController lc) throws IllegalArgumentException{
        this.supermercado = supermercado;
        
        Util.verificaIsObjNull(lc, "LoteController");
        
        this.lc = lc;
        
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
        if (lc == null) {
            selectButton.setVisible(false);
            selectButton.setManaged(false);
        }
    }    

    @FXML
    private void getDetalhes(ActionEvent event) {
        //TODO Abrir Produto no ProdutoController
        
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
        //TODO Chamar BuscaLotesController para exibir os lotes
    }

    @FXML
    private void getLotesNestVencim(ActionEvent event) {
        //TODO Chamar BuscaLotesController para exibir os lotes
    }
    
    @FXML
    private void selectProd(ActionEvent event) {
        //TODO Setar produto no LoteController
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String marca = (String) response.get("Marca");
        String tipo = (String) response.get("Tipo");
        String codigo = (String) response.get("Codigo");
    }
}
