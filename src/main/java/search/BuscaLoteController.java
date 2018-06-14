/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.supermercado.Supermercado;
import filter.FilterComunication;
import filter.data.FilterData;
import modelo.supermercado.mercadoria.Lote;
import static util.ConversorDataObjs.toDate;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaLoteController implements Initializable, FilterComunication {
    private Supermercado supermercado;
    
    @FXML
    private TableView<?> LoteTable;
    @FXML
    private TableColumn<?, ?> identifCol;
    @FXML
    private TableColumn<?, ?> codProdCol;

    public BuscaLoteController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException{
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(supermercado, "Supermercado");
        this.supermercado = supermercado;
        
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
    
    public BuscaLoteController(List<Lote> lotes){
        //TODO Add lotes to table
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
        //TODO Abrir Lote no LoteController
    }

    @FXML
    private void getDetalhesProd(ActionEvent event) {
        //TODO Abrir Produto no ProdutoController
    }

    @FXML
    private void getDetalhesForn(ActionEvent event) {
        //TODO Abrir Fornecedor no FornecedorController
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

}
