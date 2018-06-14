/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_info;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.usuarios.Endereco;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class FornecedorController implements Initializable {
    private final Fornecedor fornecedor;
    
    @FXML
    private TextField nome;
    @FXML
    private TextField cnpj;
    @FXML
    private TextField cep;
    @FXML
    private TextField rua;
    @FXML
    private Spinner<Integer> numero;
    @FXML
    private TextField bairro;
    @FXML
    private TextField cidade;
    @FXML
    private ChoiceBox<Endereco.Estado> estado;
    @FXML
    private TableView<List<String>> contatosTable;

    public FornecedorController(Fornecedor fornecedor) throws IllegalArgumentException{
        Util.verificaIsObjNull(fornecedor, "Fornecedor");
        
        this.fornecedor = fornecedor;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numero.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));
        inicializaCampos();
    }    

    private void inicializaCampos() {
        nome.setText(fornecedor.getNome());
        cnpj.setText(fornecedor.getCnpj());

        Endereco endereco = fornecedor.getEndereco();
        cep.setText(endereco.getCep());
        rua.setText(endereco.getRuaAvenida());
        numero.getValueFactory().setValue(endereco.getNumero());
        bairro.setText(endereco.getBairro());
        cidade.setText(endereco.getCidade());
        estado.setValue(endereco.getEstado());
    }
}
