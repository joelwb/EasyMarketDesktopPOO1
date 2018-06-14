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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import main.MainButtonClickListener;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class LoteController implements Initializable {
    private final Lote lote;
    private final MainButtonClickListener listener;
    
    
    @FXML
    private TextField identificador;
    @FXML
    private DatePicker dataCompra;
    @FXML
    private DatePicker dataFabric;
    @FXML
    private DatePicker dataVal;
    @FXML
    private Spinner<Integer> qtdUnidades;
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
        qtdUnidades.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500000, 0));
        
        if (lote == null) {     //é cadastro
            apagar.setVisible(false);
            apagar.setManaged(false);
        }else {                 //é consulta
            cancel.setText("Voltar");
            inicializaCampos();
        }
        
        //TODO se o listener for null retirar os botões
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
    
    private void inicializaCampos(){
        
    }
    
    public void setFornecedor(Fornecedor fornecedor){
        
    }
    
    public void setProduto(Produto prod){
        
    }
}
