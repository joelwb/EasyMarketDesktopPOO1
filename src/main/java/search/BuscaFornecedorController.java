package search;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import database.supermercado.mercadoria.FornecedorDAO;
import database.supermercado.mercadoria.LoteDAO;
import filter.FiltroController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import modelo.supermercado.Supermercado;
import filter.FilterComunication;
import filter.data.FilterData;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.details.FornecedorController;
import model.details.LoteController;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.supermercado.mercadoria.Lote;
import util.AlertCreator;
import util.Screen;
import util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaFornecedorController implements Initializable, FilterComunication {

    private Supermercado supermercado;
    private LoteController lc;
    private List<Fornecedor> fornecedores;

    @FXML
    private TableView<List<String>> fornTable;
    @FXML
    private Button selectButton;

    public BuscaFornecedorController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(supermercado, "Supermercado");
        this.supermercado = supermercado;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Nome", "Fornecedor", String.class));
        filters.add(new FilterData("CNPJ", "Fornecedor", String.class));

        bc.setFilters(filters);
    }

    public BuscaFornecedorController(FiltroController bc, LoteController lc) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
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
        if (lc == null) {   //não é seleção de fornecedor pra um lote
            selectButton.setVisible(false);
            selectButton.setManaged(false);
        }

        TableViewConfigurator.configure(fornTable);
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        int indxForn = fornTable.getSelectionModel().getSelectedIndex();
        if (indxForn == -1) return;

        Fornecedor fornecedor = fornecedores.get(indxForn);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Fornecedor.fxml"));
            FornecedorController fc = new FornecedorController(fornecedor);
            loader.setController(fc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
        int indxForn = fornTable.getSelectionModel().getSelectedIndex();
        if (indxForn == -1) {
            return;
        }

        Fornecedor fornecedor = fornecedores.get(indxForn);

        List<Lote> lotes;
        try {
            lotes = LoteDAO.readLotesByFornecedor(fornecedor, supermercado);
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(lotes);
        loader.setController(blc);

        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void selectForn(ActionEvent event) {
        int indxForn = fornTable.getSelectionModel().getSelectedIndex();
        if (indxForn == -1) {
            return;
        }

        lc.setFornecedor(fornecedores.get(indxForn));
        ((Stage) selectButton.getScene().getWindow()).close();
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String cnpj = (String) response.get("CNPJ");

        try {
            if (lc != null) { //é seleção de um fornecedor pra um lote
                fornecedores = FornecedorDAO.readAllFornecedores(nome, cnpj);
            } else {
                fornecedores = FornecedorDAO.readFornecedoresBySupermercado(supermercado, nome, cnpj);
            }

            refreshTable();
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    private void refreshTable() {
        fornTable.getItems().clear();

        for (Fornecedor fornecedor : fornecedores) {
            List<String> row = new ArrayList<>();
            row.add(fornecedor.getNome());
            row.add(fornecedor.getCnpj());
            fornTable.getItems().add(row);
        }
    }
}
