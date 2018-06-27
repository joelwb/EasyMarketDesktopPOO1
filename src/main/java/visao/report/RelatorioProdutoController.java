/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.report;

import database.supermercado.mercadoria.LoteDAO;
import database.supermercado.mercadoria.ProdutoDAO;
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
import visao.filter.FiltroController;
import visao.filter.FilterComunication;
import visao.filter.item.FilterData;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import visao.details.ProdutoController;
import modelo.supermercado.Supermercado;
import modelo.supermercado.mercadoria.Lote;
import modelo.supermercado.mercadoria.Produto;
import visao.search.BuscaLoteController;
import visao.util.alerts.AlertCreator;
import static visao.util.DateObjConversor.toDate;
import visao.util.Screen;
import visao.util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class RelatorioProdutoController implements Initializable, FilterComunication {

    private final Supermercado supermercado;
    private Map<Produto, Integer> dados;

    @FXML
    private TableView<List<String>> tableRelatorio;

    public RelatorioProdutoController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.supermercado = supermercado;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Data Min", "Intervalo de tempo", LocalDate.class));
        filters.add(new FilterData("Data Máx", "Intervalo de tempo", LocalDate.class));
        filters.add(new FilterData("Máx. Resultados", "Limitador", String.class));

        bc.setFilters(filters);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableViewConfigurator.configure(tableRelatorio);
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        Date dataMin = toDate((LocalDate) response.get("Data Min"));
        Date dataMax = toDate((LocalDate) response.get("Data Máx"));
        String limString = (String) response.get("Máx. Resultados");
        Integer lim = null;
        if (limString != null && !limString.equals("")) {
            lim = Integer.parseInt(limString);
        }

        try {
            dados = ProdutoDAO.readProdutosMaisVendidos(supermercado, dataMin, dataMax, lim);
            refreshTable();
        } catch (SQLException | ClassNotFoundException | IllegalArgumentException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    private void refreshTable() {
        tableRelatorio.getItems().clear();
        for (Produto prod : dados.keySet()) {
            List<String> row = new ArrayList<>();
            row.add(prod.getNome());
            row.add(prod.getCodigo());
            row.add(dados.get(prod).toString());
            row.add(prod.getTipo());
            row.add(prod.getMarca());
            row.add(String.valueOf(prod.getQtdEstoque() + prod.getQtdPrateleira()));
            row.add(String.valueOf(prod.getQtdEstoque()));
            row.add(String.valueOf(prod.getQtdPrateleira()));

            tableRelatorio.getItems().add(row);
        }
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        int indxProd = tableRelatorio.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) {
            return;
        }

        Produto prodSelected = (Produto) dados.keySet().toArray()[indxProd];

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
        int indxProd = tableRelatorio.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) {
            return;
        }

        Produto prodSelected = (Produto) dados.keySet().toArray()[indxProd];

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
        
        int indxProd = tableRelatorio.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) {
            return;
        }

        Produto prodSelected = (Produto) dados.keySet().toArray()[indxProd];
        
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

}
