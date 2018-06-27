/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.details;

import database.supermercado.SupermercadoDAO;
import database.supermercado.mercadoria.LoteDAO;
import visao.filter.FiltroController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;
import util.Util;
import visao.main.MainScreenListener;
import modelo.supermercado.Supermercado;
import visao.search.BuscaFornecedorController;
import visao.search.BuscaProdutoController;
import visao.util.alerts.AlertCreator;
import visao.util.DateObjConversor;
import visao.util.Screen;

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
            disableFields();

        } else if (lote == null) {       //é cadastro
            apagar.setVisible(false);
            apagar.setManaged(false);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) return;
        
        listener.pullScreen();
    }

    @FXML
    private void save(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) return;
        
        String identificador = this.identificador.getText();
        Date dataCompra = DateObjConversor.toDate(this.dataCompra.getValue());
        Date dataFabric = DateObjConversor.toDate(this.dataFabric.getValue());
        Date dataVal = DateObjConversor.toDate(this.dataVal.getValue());
        int qtdUnidade = this.qtdUnidades.getValue();

        if (fornecedor == null) {
            AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Fornecedor não informado!", null);
            return;
        }

        if (lote == null) { //é cadastro de lote
            int idLote;
            try {
                Lote novoLote = new Lote(dataCompra, dataFabric, dataVal, qtdUnidade, identificador, produto);

                if (dataCompra.getTime() < dataFabric.getTime()) {
                    AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Conflito entre data de compra e data de fabricação!", "Data de compra não pode ser antes da data de fabricação!");
                    return;
                }

                if (dataVal != null && dataVal.getTime() <= dataFabric.getTime()) {
                    AlertCreator.criarAlert(Alert.AlertType.WARNING, "Aviso!", "Conflito entre data de validade e data de fabricação!", "Data de dataVal não pode ser antes ou no mesmo dia da data de fabricação!");
                    return;
                }

                idLote = LoteDAO.create(novoLote, fornecedor, produto, supermercado);
            } catch (IllegalArgumentException | ClassNotFoundException ex) {
                AlertCreator.exibeExececao(ex);
                return;
            } catch (SQLException ex) {
                if (ex.getSQLState().equals("23505")) { //Já existem um lote com mesmo identificador sobre o mesmo produto
                    AlertCreator.criarAlert(Alert.AlertType.WARNING, "Lote repetido!", "Já existem um lote com mesmo identificador sobre o mesmo produto", null);
                } else {
                    AlertCreator.exibeExececao(ex);
                }
                return;
            }

            try {
                SupermercadoDAO.addFornecedor(fornecedor, supermercado);
            } catch (ClassNotFoundException ex) {
                AlertCreator.exibeExececao(ex);
                return;
            } catch (SQLException ex) {
                if (!ex.getSQLState().equals("23505")) {
                    apagar(idLote);
                    AlertCreator.exibeExececao(ex);
                    return;
                }
            }
        } else { //é update de lote
            lote.setDataCompra(dataCompra);
            lote.setDataFabricacao(dataFabric);
            lote.setDataValidade(dataVal);
            lote.setIdentificador(identificador);
            lote.setNumUnidades(qtdUnidade);

            try {
                LoteDAO.update(lote);
            } catch (IllegalArgumentException | ClassNotFoundException ex) {
                AlertCreator.exibeExececao(ex);
                return;
            } catch (SQLException ex) {
                if (ex.getSQLState().equals("23505")) { //Já existem um lote com mesmo identificador sobre o mesmo produto
                    AlertCreator.criarAlert(Alert.AlertType.WARNING, "Lote repetido!", "Já existem um lote com mesmo identificador sobre o mesmo produto", null);
                } else {
                    AlertCreator.exibeExececao(ex);
                }
                return;
            }
        }

        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Dados foram salvos", null);
        if (listener == null) { //é uma tela avulsa
            ((Stage) this.identificador.getScene().getWindow()).close();
        } else {                //é uma tela dentro da tela com os menus
            listener.pullScreen();
        }
    }

    @FXML
    private void apagar(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) return;
        
        apagar(lote.getId());
    }

    private void apagar(int id) {
        try {
            LoteDAO.delete(id);
            AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Lote apagado com sucesso!", null);
            ((Stage) qtdUnidades.getScene().getWindow()).close();
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void searchProd(ActionEvent event) {
        FiltroController fc;
        try {
            fc = getNewFilterController();
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaProduto.fxml"));
        BuscaProdutoController bpc = new BuscaProdutoController(fc, supermercado, this);
        fc.setFilterComunication(bpc);

        subLoader.setController(bpc);
        Parent subView = null;

        try {
            subView = subLoader.load();
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        fc.setContent(subView);
    }

    @FXML
    private void searchFornec(ActionEvent event) {
        FiltroController fc;
        try {
            fc = getNewFilterController();
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaFornecedor.fxml"));
        BuscaFornecedorController bfc = new BuscaFornecedorController(fc, this);
        fc.setFilterComunication(bfc);

        subLoader.setController(bfc);
        Parent subView = null;

        try {
            subView = subLoader.load();
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        fc.setContent(subView);
    }

    private void inicializaCampos() {
        identificador.setText(lote.getIdentificador());
        dataCompra.setValue(DateObjConversor.toLocalDate(lote.getDataCompra()));
        dataFabric.setValue(DateObjConversor.toLocalDate(lote.getDataFabricacao()));
        if (lote.getDataValidade() != null) {
            dataVal.setValue(DateObjConversor.toLocalDate(lote.getDataValidade()));
        }
        codigoProd.setText(lote.getProduto().getCodigo());
        qtdUnidades.getValueFactory().setValue(lote.getNumUnidades());
        nomeFornecedor.setText(fornecedor.getNome());
    }

    private void disableFields() {
        searchProduto.setVisible(false);
        searchProduto.setManaged(false);

        searchFornecedor.setVisible(false);
        searchFornecedor.setManaged(false);

        cancel.setVisible(false);
        cancel.setManaged(false);
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        nomeFornecedor.setText(fornecedor.getNome());
    }

    public void setProduto(Produto prod) {
        produto = prod;
        codigoProd.setText(prod.getCodigo());
    }

    private FiltroController getNewFilterController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/filter/Filtro.fxml"));
        FiltroController lc = new FiltroController(null);
        loader.setController(lc);

        Screen.openNew(loader);

        return lc;
    }
}
