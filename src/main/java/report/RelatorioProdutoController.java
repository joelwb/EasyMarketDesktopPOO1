/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

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
import filter.FiltroController;
import filter.FilterComunication;
import filter.data.FilterData;
import modelo.supermercado.Supermercado;
import static util.ConversorDataObjs.toDate;
import util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class RelatorioProdutoController implements Initializable, FilterComunication {
    private final Supermercado supermercado;
    
    @FXML
    private TableView<List<String>> tableRelatorio;

    public RelatorioProdutoController(FiltroController bc, Supermercado supermercado) throws IllegalArgumentException{
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
        int lim = Integer.parseInt((String) response.get("Máx. Resultados"));
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        //TODO Abrir Produto no ProdutoController
        
    }

    @FXML
    private void getAllLotes(ActionEvent event) {
        //TODO Chamar BuscaLotesController para exibir os lotes
    }

    @FXML
    private void getLotesNestVencim(ActionEvent event) {
        //TODO Chamar BuscaLotesController para exibir os lotes
    }
    
}
