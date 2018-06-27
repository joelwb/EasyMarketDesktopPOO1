/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.search;

import database.supermercado.mercadoria.LoteDAO;
import database.supermercado.mercadoria.ProdutoDAO;
import visao.filter.FiltroController;
import visao.filter.FilterComunication;
import visao.filter.item.FilterData;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import visao.details.LoteController;
import visao.details.ProdutoController;
import modelo.supermercado.Supermercado;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;
import visao.util.alerts.AlertCreator;
import visao.util.Screen;
import visao.util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaProdutoController implements Initializable, FilterComunication {

    private final Supermercado supermercado;
    private LoteController lc;
    private List<Produto> produtos;

    @FXML
    private TableView<List<String>> prodTable;
    @FXML
    private Button selectButton;

    public BuscaProdutoController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.supermercado = supermercado;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Nome", "Produto", String.class));
        filters.add(new FilterData("Marca", "Produto", String.class));
        filters.add(new FilterData("Tipo", "Produto", String.class));
        filters.add(new FilterData("Código", "Produto", String.class));

        bc.setFilters(filters);
    }

    public BuscaProdutoController(FiltroController bc, Supermercado supermercado, LoteController lc) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(lc, "LoteController");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.supermercado = supermercado;
        this.lc = lc;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Nome", "Produto", String.class));
        filters.add(new FilterData("Marca", "Produto", String.class));
        filters.add(new FilterData("Tipo", "Produto", String.class));
        filters.add(new FilterData("Código", "Produto", String.class));

        bc.setFilters(filters);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (lc == null) {
            selectButton.setVisible(false);
            selectButton.setManaged(false);
        }

        TableViewConfigurator.configure(prodTable);
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        int indxProd = prodTable.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) return;
        
        Produto prodSelected = produtos.get(indxProd);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details/Produto.fxml"));
        ProdutoController pc = new ProdutoController(prodSelected);
        loader.setController(pc);
        
        
        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
        int indxProd = prodTable.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) return;
        
        Produto prodSelected = produtos.get(indxProd);
        
        List<Lote> lotes;
        try {
            lotes = LoteDAO.readLotesByProduto(prodSelected);
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(lotes);
        loader.setController(blc);
        
        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getLotesNestVencim(ActionEvent event) {
        Integer diasParaVencer = AlertCreator.getInt("Informe um valor", 
                "Insira a quantidade de dias para um lote vencer (a partir de hoje):",null);
        
        if (diasParaVencer == null){
            return;
        }
        
        int indxProd = prodTable.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) return;
        
        Produto prodSelected = produtos.get(indxProd);
        
        List<Lote> lotes;
        try {
            lotes = LoteDAO.readLotesProxVal(prodSelected, diasParaVencer);
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(lotes);
        loader.setController(blc);

        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void selectProd(ActionEvent event) {
        int indxProd = prodTable.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) return;
        
        Produto prodSelected = produtos.get(indxProd);
        
        lc.setProduto(prodSelected);
        ((Stage) selectButton.getScene().getWindow()).close();
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String marca = (String) response.get("Marca");
        String tipo = (String) response.get("Tipo");
        String codigo = (String) response.get("Código");

        try {
            produtos = ProdutoDAO.readProdutosBySupermercado(supermercado, nome, marca, tipo, codigo);
            refreshTable();
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    private void refreshTable() {
        prodTable.getItems().clear();
        
        for (Produto prod : produtos) {
            List<String> row = new ArrayList<>();
            row.add(prod.getNome());
            row.add(prod.getCodigo());
            prodTable.getItems().add(row);
        }
    }
}
