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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.MainButtonClickListener;
import modelo.supermercado.mercadoria.Fornecedor;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class FornecedorController implements Initializable {
    private Fornecedor fornecedor;
    private MainButtonClickListener listener;
    
    @FXML
    private TextField nome;
    @FXML
    private TextField cnpj;
    @FXML
    private TextField cep;
    @FXML
    private TextField rua;
    @FXML
    private Spinner<?> numero;
    @FXML
    private TextField bairro;
    @FXML
    private TextField cidade;
    @FXML
    private ChoiceBox<?> estado;
    @FXML
    private TableView<?> contatosTable;
    @FXML
    private Button cancel;

    public FornecedorController(Fornecedor fornecedor, MainButtonClickListener listener) {
        this.fornecedor = fornecedor;
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
    private void back(ActionEvent event) {
        listener.cancel();
    }
    
}
