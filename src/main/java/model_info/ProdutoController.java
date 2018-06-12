package model_info;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.MainButtonClickListener;
import modelo.supermercado.mercadoria.Produto;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class ProdutoController implements Initializable {
    private Produto prod;
    private MainButtonClickListener listener;

    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private TextField nome;
    @FXML
    private TextField tipo;
    @FXML
    private Spinner<?> qtdPrateleira;
    @FXML
    private Spinner<?> qtdEstoque;
    @FXML
    private TextField marca;
    @FXML
    private TextField codigo;
    @FXML
    private Spinner<Double> custo;
    @FXML
    private Spinner<?> preco;
    @FXML
    private TextArea descricao;

    public ProdutoController(Produto prod, MainButtonClickListener listener) {
        this.prod = prod;
        this.listener = listener;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        custo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 0.5));
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.cancel();
    }

    @FXML
    private void save(ActionEvent event) {
        listener.save();
    }

}
