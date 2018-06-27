/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.report;

import database.pagamento.CartaoDAO;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import visao.filter.FiltroController;
import visao.filter.FilterComunication;
import visao.filter.item.FilterData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.supermercado.Supermercado;
import visao.util.alerts.AlertCreator;
import static visao.util.DateObjConversor.toDate;
import visao.util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class RelatorioMeioPagController implements Initializable, FilterComunication {

    private RelatorioMeioPagController.TipoRelatorio tipo;
    private final Supermercado supermercado;
    private Map<Date, Map<String, Number>> map;

    @FXML
    private TableView<List<String>> tableRelatorio;
    @FXML
    private TableColumn<List<String>, String> infoCol;

    public enum TipoRelatorio {
        MAIS_RENTAVEL("Mais Rentável"), MAIS_UTILIZADO("Mais Utilizado");

        private String tipo;

        private TipoRelatorio(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }
    }

    public RelatorioMeioPagController(FiltroController bc, RelatorioMeioPagController.TipoRelatorio tipo, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(tipo, "Tipo de relatório");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.tipo = tipo;
        this.supermercado = supermercado;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Data Min", "Intervalo de tempo", LocalDate.class));
        filters.add(new FilterData("Data Máx", "Intervalo de tempo", LocalDate.class));

        bc.setFilters(filters);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (tipo == RelatorioMeioPagController.TipoRelatorio.MAIS_UTILIZADO) {
            infoCol.setText("Nº Compras");
        } else {
            infoCol.setText("Renda (R$)");
        }

        TableViewConfigurator.configure(tableRelatorio);
        configureSubCol();
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        Date dataMin = toDate((LocalDate) response.get("Data Min"));
        Date dataMax = toDate((LocalDate) response.get("Data Máx"));

        try {
            if (tipo == RelatorioMeioPagController.TipoRelatorio.MAIS_UTILIZADO) {
                map = CartaoDAO.getMeiosPagMaisUsado(supermercado, dataMin, dataMax);
            } else {
                map = CartaoDAO.getMeiosPagMaisRentavel(supermercado, dataMin, dataMax);
            }

            refreshTable();
        } catch (ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    public void openGrafico(ActionEvent event) {
        final BarChart<String, Number> barChar = new BarChart<>(new CategoryAxis(), new NumberAxis());
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

        lineChart.getData().addAll(getCreditoData(), getDebitoData());
        barChar.getData().addAll(getCreditoData(), getDebitoData());

        lineChart.setMinWidth(0);
        barChar.setMinWidth(0);

        SplitPane sp = new SplitPane();
        sp.getItems().addAll(barChar, lineChart);
        
        Scene scene = new Scene(sp);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.show();
    }

    private void refreshTable() {
        tableRelatorio.getItems().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Date dataCompra : map.keySet()) {
            List<String> row = new ArrayList<>();
            row.add(sdf.format(dataCompra));
            row.add(String.valueOf(map.get(dataCompra).get("Crédito")));
            row.add(String.valueOf(map.get(dataCompra).get("Débito")));
            tableRelatorio.getItems().add(row);
        }
    }

    private XYChart.Series<String, Number> getCreditoData() {
        XYChart.Series<String, Number> credito = new XYChart.Series();
        credito.setName("Crédito");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Date dataCompra : map.keySet()) {
            if (!map.get(dataCompra).keySet().contains("Crédito")) {
                credito.getData().add(new XYChart.Data(sdf.format(dataCompra), 0));
            } else {
                credito.getData().add(new XYChart.Data(sdf.format(dataCompra), map.get(dataCompra).get("Crédito")));
            }
        }

        return credito;
    }

    private XYChart.Series<String, Number> getDebitoData() {
        XYChart.Series<String, Number> debito = new XYChart.Series();
        debito.setName("Débito");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Date dataCompra : map.keySet()) {
            if (!map.get(dataCompra).keySet().contains("Débito")) {
                debito.getData().add(new XYChart.Data(sdf.format(dataCompra), 0));
            } else {
                debito.getData().add(new XYChart.Data(sdf.format(dataCompra), map.get(dataCompra).get("Débito")));
            }
        }

        return debito;
    }

    private void configureSubCol() {
        for (int i = 0; i < infoCol.getColumns().size(); i++) {
            final int g = i + 1;
            TableColumn<List<String>, String> subcol = (TableColumn<List<String>, String>) infoCol.getColumns().get(i);

            subcol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> param) {
                    if (param.getValue().get(g).equals("null")) {
                        return new SimpleStringProperty("0");
                    } else {
                        return new SimpleStringProperty(param.getValue().get(g));
                    }
                }
            });
        }
    }
}
