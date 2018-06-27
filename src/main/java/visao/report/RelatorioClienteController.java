/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.report;

import database.supermercado.CompraDAO;
import database.usuarios.ClienteDAO;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import visao.filter.FiltroController;
import visao.filter.FilterComunication;
import visao.filter.item.FilterData;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import visao.details.DadosPessoaisController;
import visao.details.ComprasController;
import modelo.supermercado.Compra;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Cliente;
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
public class RelatorioClienteController implements Initializable, FilterComunication {

    private RelatorioClienteController.TipoRelatorio tipo;
    private final Supermercado supermercado;
    private Map<Cliente, Double> mapRelatorio;

    @FXML
    private TableView<List<String>> tableRelatorio;
    @FXML
    private TableColumn<List<String>, String> consumoCol;

    public enum TipoRelatorio {
        MAIS_CONSUMISTAS("Consumo"), MEDIA_CONSUMO("Média de Consumo");

        private String tipo;

        private TipoRelatorio(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }
    }

    public RelatorioClienteController(FiltroController bc, RelatorioClienteController.TipoRelatorio tipo, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(tipo, "Tipo do relatorio");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.tipo = tipo;
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
        consumoCol.setText(tipo.getTipo() + " (R$)");
        TableViewConfigurator.configure(tableRelatorio);
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        int indxProd = tableRelatorio.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) {
            return;
        }

        Cliente cliente = (Cliente) mapRelatorio.keySet().toArray()[indxProd];
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details/DadosPessoais.fxml"));
        
        try {
            DadosPessoaisController dpc = new DadosPessoaisController(cliente);

            loader.setController(dpc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getAllOrders(ActionEvent event) {
        int indxProd = tableRelatorio.getSelectionModel().getSelectedIndex();
        if (indxProd == -1) {
            return;
        }

        Cliente cliente = (Cliente) mapRelatorio.keySet().toArray()[indxProd];

        try {
            List<Compra> compras = CompraDAO.readHistoricoComprasByCliente(cliente, supermercado);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details/Compras.fxml"));
            ComprasController cc = new ComprasController(compras);
            loader.setController(cc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
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
            if (tipo == TipoRelatorio.MAIS_CONSUMISTAS) {
                mapRelatorio = ClienteDAO.readClientesMaisConsumistas(supermercado, dataMin, dataMax, lim);
            } else {
                mapRelatorio = ClienteDAO.readClientesMediaConsumo(supermercado, dataMin, dataMax, lim);
            }

            refreshTable();
        } catch (ClassNotFoundException | IllegalArgumentException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
        }

    }

    private void refreshTable() {
        tableRelatorio.getItems().clear();

        for (Cliente cliente : mapRelatorio.keySet()) {
            List<String> row = new ArrayList<>();
            row.add(cliente.getNome());
            row.add(cliente.getCpf());
            row.add(String.format("%.2f", mapRelatorio.get(cliente)));

            tableRelatorio.getItems().add(row);
        }
    }
}
