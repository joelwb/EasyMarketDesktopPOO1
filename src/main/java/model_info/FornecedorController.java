/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_info;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.MainButtonClickListener;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.usuarios.Endereco;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class FornecedorController implements Initializable {
    private final Fornecedor fornecedor;
    private final MainButtonClickListener listener;
    
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

    public FornecedorController(Fornecedor fornecedor, MainButtonClickListener listener) throws NullPointerException{
        if (fornecedor == null) throw new NullPointerException("Fornecedor nulo!");
        
        this.fornecedor = fornecedor;
        this.listener = listener;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numero.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));
        
        nome.setEditable(false);
        cnpj.setEditable(false);
        cep.setEditable(false);
        rua.setEditable(false);
        numero.setEditable(false);
        bairro.setEditable(false);
        cidade.setEditable(false);
        estado.setDisable(true);
        contatosTable.setEditable(false);
        
        inicializaCampos();
    }    

    @FXML
    private void back(ActionEvent event) {
        listener.cancel();
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
