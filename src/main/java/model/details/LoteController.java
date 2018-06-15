/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.details;

import database.supermercado.SupermercadoDAO;
import database.supermercado.mercadoria.LoteDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;
import util.Util;
import main.MainScreenListener;
import modelo.supermercado.Supermercado;
import util.AlertCreator;
import util.ConversorDataObjs;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class LoteController implements Initializable {

    private final Lote lote;
    private MainScreenListener listener;
    private Supermercado supermercado;
    private Fornecedor fornecedor;
    private Produto produto;

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

    public LoteController(Lote lote, MainScreenListener listener, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(listener, "Listener");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.lote = lote;
        this.listener = listener;
        this.supermercado = supermercado;
    }

    public LoteController(Lote lote, Fornecedor fornecedor) throws IllegalArgumentException {
        Util.verificaIsObjNull(lote, "Lote");
        Util.verificaIsObjNull(fornecedor, "Fornecedor");

        this.lote = lote;
        this.fornecedor = fornecedor;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        qtdUnidades.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500000, 0));

        if (listener == null) {         //é uma nova janela com exibição dos dados do lote
            inicializaCampos();
            disableAllFields();

        } else if (lote == null) {       //é cadastro
            apagar.setVisible(false);
            apagar.setManaged(false);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.pullScreen();
    }

    @FXML
    private void save(ActionEvent event) {
        //TODO Verificar se vai ser possivel ou não atualizar dados do lote

        String identificador = this.identificador.getText();
        Date dataCompra = ConversorDataObjs.toDate(this.dataCompra.getValue());
        Date dataFabric = ConversorDataObjs.toDate(this.dataFabric.getValue());
        Date dataVal = ConversorDataObjs.toDate(this.dataVal.getValue());
        int qtdUnidade = this.qtdUnidades.getValue();

        if (fornecedor == null) {
            AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Fornecedor não informado!", null);
            return;
        }

        int idLote;
        try {
            Lote novoLote = new Lote(dataCompra, dataFabric, dataVal, qtdUnidade, identificador, produto);

            if (dataCompra.getTime() < dataFabric.getTime()) {
                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Conflito entre data de compra e data de fabricação!", "Data de compra não pode ser antes da data de fabricação!");
                return;
            }

            if (dataVal.getTime() <= dataFabric.getTime()) {
                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Conflito entre data de validade e data de fabricação!", "Data de dataVal não pode ser antes ou no mesmo dia da data de fabricação!");
                return;
            }

            idLote = LoteDAO.create(novoLote, fornecedor, produto, supermercado);
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        try {
            SupermercadoDAO.addFornecedor(fornecedor, supermercado);
        } catch (ClassNotFoundException | SQLException ex) {
            apagar(idLote);
            AlertCreator.exibeExececao(ex);
            return;
        }

        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Dados foram salvos", null);
        listener.pullScreen();
    }

    @FXML
    private void apagar(ActionEvent event) {
        apagar(lote.getId());
    }

    private void apagar(int id) {
        try {
            LoteDAO.delete(id);
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void searchProd(ActionEvent event) {
        //TODO Abrir BuscaProduto
    }

    @FXML
    private void searchFornec(ActionEvent event) {
        //TODO Abrir BuscaFornec
    }

    private void inicializaCampos() {
        identificador.setText(lote.getIdentificador());
        dataCompra.setValue(ConversorDataObjs.toLocalDate(lote.getDataCompra()));
        dataFabric.setValue(ConversorDataObjs.toLocalDate(lote.getDataFabricacao()));
        dataVal.setValue(ConversorDataObjs.toLocalDate(lote.getDataValidade()));
        codigoProd.setText(lote.getProduto().getCodigo());
        qtdUnidades.getValueFactory().setValue(lote.getNumUnidades());
        nomeFornecedor.setText(fornecedor.getNome());
    }

    private void disableAllFields() {
        identificador.setDisable(true);
        dataCompra.setDisable(true);
        dataFabric.setDisable(true);
        dataVal.setDisable(true);

        searchProduto.setVisible(false);
        searchProduto.setManaged(false);

        searchFornecedor.setVisible(false);
        searchFornecedor.setManaged(false);

        cancel.setVisible(false);
        cancel.setManaged(false);

        //TODO tornar visivel caso possa atualizar os dados
        save.setVisible(false);
        save.setManaged(false);
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        nomeFornecedor.setText(fornecedor.getNome());
    }

    public void setProduto(Produto prod) {
        produto = prod;
        codigoProd.setText(prod.getCodigo());
    }
}
