/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_info;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import main.MainButtonClickListener;
import modelo.supermercado.mercadoria.Lote;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class LoteController implements Initializable {
    private Lote lote;
    private MainButtonClickListener listener;
    
    
    @FXML
    private TextField identificador;
    @FXML
    private DatePicker dataCompra;
    @FXML
    private DatePicker dataFabric;
    @FXML
    private DatePicker dataVal;
    @FXML
    private Spinner<?> qtdUnidades;
    @FXML
    private TextField codigoProd;
    @FXML
    private Button searchProduto;
    @FXML
    private TextField nomeFornecedor;
    @FXML
    private Button searchFornecedor;
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private Button apagar;

    public LoteController(Lote lote, MainButtonClickListener listener) {
        this.lote = lote;
        this.listener = listener;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cancel(ActionEvent event) {
        listener.cancel();
    }

    @FXML
    private void save(ActionEvent event) {
        listener.save();
    }

    @FXML
    private void apagar(ActionEvent event) {
    }
    
}
