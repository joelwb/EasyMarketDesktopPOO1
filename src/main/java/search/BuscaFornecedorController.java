package search;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import filter.FiltroController;
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
import modelo.supermercado.Supermercado;
import filter.FilterComunication;
import filter.FilterData;
import javafx.scene.control.Button;
import model_info.LoteController;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaFornecedorController implements Initializable, FilterComunication {
    private final Supermercado supermercado;
    private LoteController lc;
    
    @FXML
    private TableView<?> fornTable;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> cnpjCol;
    @FXML
    private Button selectButton;

public BuscaFornecedorController(FiltroController bc, Supermercado supermercado) {
        this.supermercado = supermercado;
    
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", "Fornecedor", String.class));
        filters.add(new FilterData("CNPJ", "Fornecedor", String.class));
        
        bc.setFilters(filters);
    }

public BuscaFornecedorController(FiltroController bc, Supermercado supermercado, LoteController lc) throws IllegalArgumentException{
        this.supermercado = supermercado;
        
        Util.verificaIsObjNull(lc, "LoteController");
        
        this.lc = lc;
    
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
        if (lc == null) {
            selectButton.setVisible(false);
            selectButton.setManaged(false);
        }
    }    

    @FXML
    private void getDetalhes(ActionEvent event) {
        //TODO Abrir Fornecedor no FornecedorController
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
        //TODO Chamar BuscaLotesController para exibir os lotes
    }
    
    @FXML
    private void selectForn(ActionEvent event) {
        //TODO Setar Fornecedor no LoteController
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String cnpj = (String) response.get("CNPJ");
    }
    
}
