/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import database.supermercado.mercadoria.FornecedorDAO;
import database.supermercado.mercadoria.LoteDAO;
import filter.FiltroController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import model.details.FornecedorController;
import model.details.LoteController;
import model.details.ProdutoController;
import modelo.supermercado.mercadoria.Fornecedor;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;
import util.AlertCreator;
import static util.DateObjConversor.toDate;
import util.Screen;
import util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaLoteController implements Initializable, FilterComunication {

    private Supermercado supermercado;
    private List<Lote> lotes;

    @FXML
    private TableView<List<String>> loteTable;

    public BuscaLoteController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(supermercado, "Supermercado");
        this.supermercado = supermercado;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Identificador", "Lote", String.class));
        filters.add(new FilterData("Data Fabricação Min", "Data Fabricação", LocalDate.class));
        filters.add(new FilterData("Data Fabricação Máx", "Data Fabricação", LocalDate.class));
        filters.add(new FilterData("Data Validade Min", "Data Validade", LocalDate.class));
        filters.add(new FilterData("Data Validade Máx", "Data Validade", LocalDate.class));
        filters.add(new FilterData("Data Compra Min", "Data Compra", LocalDate.class));
        filters.add(new FilterData("Data Compra Máx", "Data Compra", LocalDate.class));

        bc.setFilters(filters);
    }

    public BuscaLoteController(List<Lote> lotes) {
        this.lotes = lotes;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableViewConfigurator.configure(loteTable);

        if (lotes != null) {
            refreshTable();
        }
    }

    @FXML
    private void getDetalhesLote(ActionEvent event) {
        int indxLote = loteTable.getSelectionModel().getSelectedIndex();
        if (indxLote == -1) {
            return;
        }

        Lote lote = lotes.get(indxLote);

        Fornecedor fornecedor;
        try {
            fornecedor = FornecedorDAO.readFornecedorByLote(lote);
        } catch (ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lote.fxml"));
        LoteController lc = new LoteController(lote, fornecedor);
        loader.setController(lc);

        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getDetalhesProd(ActionEvent event) {
        int indxLote = loteTable.getSelectionModel().getSelectedIndex();
        if (indxLote == -1) {
            return;
        }

        Produto prodSelected = lotes.get(indxLote).getProduto();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Produto.fxml"));
        ProdutoController pc = new ProdutoController(prodSelected);
        loader.setController(pc);

        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getDetalhesForn(ActionEvent event) {
        int indxLote = loteTable.getSelectionModel().getSelectedIndex();
        if (indxLote == -1) return;

        Lote lote = lotes.get(indxLote);

        Fornecedor fornecedor;
        try {
            fornecedor = FornecedorDAO.readFornecedorByLote(lote);
        } catch (ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Fornecedor.fxml"));
            FornecedorController fc = new FornecedorController(fornecedor);
            loader.setController(fc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String identif = (String) response.get("Identificador");
        Date dataFabMin = toDate((LocalDate) response.get("Data Fabricação Min"));
        Date dataFabMax = toDate((LocalDate) response.get("Data Fabricação Máx"));
        Date dataValMin = toDate((LocalDate) response.get("Data Validade Min"));
        Date dataValMax = toDate((LocalDate) response.get("Data Validade Máx"));
        Date dataCompraMin = toDate((LocalDate) response.get("Data Compra Min"));
        Date dataCompraMax = toDate((LocalDate) response.get("Data Compra Máx"));

        try {
            lotes = LoteDAO.readLotesBySupermercado(supermercado, identif, dataFabMin, dataFabMax, dataValMin, dataValMax, dataCompraMin, dataCompraMax);
            refreshTable();
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    private void refreshTable() {
        loteTable.getItems().clear();

        for (Lote lote : lotes) {
            List<String> row = new ArrayList<>();
            row.add(lote.getIdentificador());
            row.add(lote.getProduto().getCodigo());

            loteTable.getItems().add(row);
        }
    }

}
