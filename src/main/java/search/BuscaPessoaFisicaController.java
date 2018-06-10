/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.usuarios.PessoaFisica;
import search.filter.FilterComunication;
import search.filter.FilterData;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaPessoaFisicaController implements Initializable, FilterComunication {
    private PessoaFisicaClass pf;
    
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> cpfCol;
    @FXML
    private TableView<?> pessoaFisicaTable;

    public enum PessoaFisicaClass{
        Cliente("Cliente"), Funcionario("Funcionário");
        
        private final String classe;
        
        private PessoaFisicaClass(String classe){
            this.classe = classe;
        }
        
        public String getClasse(){
            return classe;
        }
    }
    
    public BuscaPessoaFisicaController(BuscaController bc, PessoaFisicaClass pf) {
        this.pf = pf;
        
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", pf.getClasse(), String.class));
        filters.add(new FilterData("CPF", pf.getClasse(), String.class));
        filters.add(new FilterData("Gênero", pf.getClasse(), PessoaFisica.Genero.class));
        filters.add(new FilterData("Código", pf.getClasse(), String.class));
        
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
    private void getDetalhes(ActionEvent event) {
    }

    @FXML
    private void getAllOrders(ActionEvent event) {
    }
    
    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String marca = (String) response.get("Marca");
        String tipo = (String) response.get("Tipo");
        String codigo = (String) response.get("Codigo");
    }
}
