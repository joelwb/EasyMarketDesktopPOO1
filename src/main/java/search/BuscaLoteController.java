/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import search.filter.FilterComunication;
import search.filter.FilterData;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaLoteController implements Initializable, FilterComunication {

    @FXML
    private TableView<?> LoteTable;
    @FXML
    private TableColumn<?, ?> identifCol;
    @FXML
    private TableColumn<?, ?> codProdCol;

    public BuscaLoteController(BuscaController bc) {
        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Identificador", "Lote", String.class));
        filters.add(new FilterData("Cód. Prod.", "Lote", String.class));
        filters.add(new FilterData("Data Fabricação Min", "Data Fabricação", LocalDate.class));
        filters.add(new FilterData("Data Fabricação Máx", "Data Fabricação", LocalDate.class));
        filters.add(new FilterData("Data Validade Min", "Data Validade", LocalDate.class));
        filters.add(new FilterData("Data Validade Máx", "Data Validade", LocalDate.class));
        filters.add(new FilterData("Data Compra Min", "Data Compra", LocalDate.class));
        filters.add(new FilterData("Data Compra Máx", "Data Compra", LocalDate.class));

        bc.setFilters(filters);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void getDetalhesLote(ActionEvent event) {
    }

    @FXML
    private void getDetalhesProd(ActionEvent event) {
    }

    @FXML
    private void getDetalhesForn(ActionEvent event) {
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String identif = (String) response.get("Identificador");
        String codProd = (String) response.get("Cód. Prod.");
        Date dataFabricMin = toDate((LocalDate) response.get("Data Fabricação Min"));
        Date dataFabricMax = toDate((LocalDate) response.get("Data Fabricação Máx"));
        Date dataVencMin = toDate((LocalDate) response.get("Data Validade Min"));
        Date dataVencMax = toDate((LocalDate) response.get("Data Validade Máx"));
        Date dataCompraMin = toDate((LocalDate) response.get("Data Compra Min"));
        Date dataCompraMax = toDate((LocalDate) response.get("Data Compra Máx"));
    }

    private Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }
}
