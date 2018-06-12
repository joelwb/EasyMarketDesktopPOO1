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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import search.BuscaController;
import search.filter.FilterComunication;
import search.filter.FilterData;
import static util.ConversorDataObjs.toDate;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class RelatorioClienteController implements Initializable, FilterComunication {
    private RelatorioClienteController.TipoRelatorio tipo;
    
    @FXML
    private TableView<?> pessoaFisicaTable;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> cpfCol;
    @FXML
    private TableColumn<?, ?> consumoCol;   
    
    public enum TipoRelatorio{
        MAIS_CONSUMISTAS("Consumo"), MEDIA_CONSUMO("Média de Consumo");
        
        private String tipo;
        
        private TipoRelatorio(String tipo){
            this.tipo = tipo;
        }
        
        public String getTipo(){return tipo;}
    }

    public RelatorioClienteController(BuscaController bc, RelatorioClienteController.TipoRelatorio tipo) {
        this.tipo = tipo;
        
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
        consumoCol.setText(tipo.getTipo());
    }    
    
    
    @FXML
    private void getDetalhes(ActionEvent event) {
    }

    @FXML
    private void getAllOrders(ActionEvent event) {
    }
    
    @Override
    public void listenResponse(Map<String, Object> response) {
        Date dataMin = toDate((LocalDate) response.get("Data Min"));
        Date dataMax = toDate((LocalDate) response.get("Data Máx"));
        int lim = Integer.parseInt((String) response.get("Máx. Resultados"));
    }
}
