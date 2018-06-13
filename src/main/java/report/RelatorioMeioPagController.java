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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import filter.FiltroController;
import filter.FilterComunication;
import filter.FilterData;
import modelo.supermercado.Supermercado;
import static util.ConversorDataObjs.toDate;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class RelatorioMeioPagController implements Initializable, FilterComunication {
    private RelatorioMeioPagController.TipoRelatorio tipo;
    private final Supermercado supermercado;
    
    @FXML
    private TableView<?> tableRelatorio;
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> infoCol;

    public enum TipoRelatorio{
        MAIS_RENTAVEL("Mais Rentável"), MAIS_UTILIZADO("Mais Utilizado");
        
        private String tipo;
        
        private TipoRelatorio(String tipo){
            this.tipo = tipo;
        }
        
        public String getTipo(){return tipo;}
    }

    public RelatorioMeioPagController(FiltroController bc, RelatorioMeioPagController.TipoRelatorio tipo, Supermercado supermercado) {
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
        if (tipo == RelatorioMeioPagController.TipoRelatorio.MAIS_UTILIZADO){
            infoCol.setText("Nº Usuários");
        }else {
            infoCol.setText("Ganhos");
        }
    }    
    
    @Override
    public void listenResponse(Map<String, Object> response) {
        Date dataMin = toDate((LocalDate) response.get("Data Min"));
        Date dataMax = toDate((LocalDate) response.get("Data Máx"));
    }
}