package model.details;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import database.supermercado.mercadoria.ProdutoDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.supermercado.mercadoria.Produto;
import util.Util;
import main.MainScreenListener;
import modelo.supermercado.Supermercado;
import util.AlertCreator;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class ProdutoController implements Initializable {

    private final Produto prod;
    private Supermercado supermercado;
    private MainScreenListener listener;

    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private TextField nome;
    @FXML
    private TextField tipo;
    @FXML
    private Spinner<Integer> qtdPrateleira;
    @FXML
    private Spinner<Integer> qtdEstoque;
    @FXML
    private TextField marca;
    @FXML
    private TextField codigo;
    @FXML
    private Spinner<Double> custo;
    @FXML
    private Spinner<Double> preco;
    @FXML
    private TextArea descricao;

    public ProdutoController(Produto prod, MainScreenListener listener, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(listener, "Listener");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.prod = prod;
        this.supermercado = supermercado;
        this.listener = listener;
    }

    public ProdutoController(Produto prod) throws IllegalArgumentException {
        Util.verificaIsObjNull(prod, "Produto");
        this.prod = prod;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        qtdPrateleira.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));
        qtdEstoque.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));
        custo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 0.5));
        preco.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 0.5));

        if (listener == null) {         //é consulta, mas pode editar alguns campos
            cancel.setVisible(false);
            cancel.setManaged(false);

            inicializaCampos();

            save.setText("Salvar");
        }
    }

    private void inicializaCampos() {
        nome.setText(prod.getNome());
        tipo.setText(prod.getTipo());
        marca.setText(prod.getMarca());
        codigo.setText(prod.getCodigo());
        descricao.setText(prod.getDescricao());
        qtdPrateleira.getValueFactory().setValue(prod.getQtdPrateleira());
        qtdEstoque.getValueFactory().setValue(prod.getQtdEstoque());
        custo.getValueFactory().setValue(prod.getCusto());
        preco.getValueFactory().setValue(prod.getPrecoVenda());
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.pullScreen();
    }

    @FXML
    private void save(ActionEvent event) {
        String nome = this.nome.getText();
        String tipo = this.tipo.getText();
        String marca = this.marca.getText();
        String codigo = this.codigo.getText();
        String descricao = this.descricao.getText();
        int qtdPrateleira = this.qtdPrateleira.getValue();
        int qtdEstoque = this.qtdEstoque.getValue();
        double custo = this.custo.getValue();
        double preco = this.preco.getValue();

        try {
            if (prod == null) {     //é cadastro

                Produto novoProd = new Produto(codigo, custo, descricao, marca, nome, preco, qtdPrateleira, qtdEstoque, tipo);
                ProdutoDAO.create(novoProd, supermercado);

            } else {                //é atualização de dados
                prod.setNome(nome);
                prod.setCodigo(codigo);
                prod.setMarca(marca);
                prod.setTipo(tipo);
                prod.setCusto(custo);
                prod.setDescricao(descricao);
                prod.setPrecoVenda(preco);
                prod.setQtdEstoque(qtdEstoque);
                prod.setQtdPrateleira(qtdPrateleira);

                ProdutoDAO.update(prod);
            }
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Dados foram salvos", null);

        if (listener == null) { //é uma tela avulsa
            ((Stage) this.nome.getScene().getWindow()).close();
        } else {                //é uma tela dentro da tela com os menus
            listener.pullScreen();
        }
    }

}
