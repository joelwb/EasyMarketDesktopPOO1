/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import filter.FiltroController;
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
import modelo.supermercado.Supermercado;
import modelo.usuarios.PessoaFisica;
import filter.FilterComunication;
import filter.data.FilterData;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaPessoaFisicaController implements Initializable, FilterComunication {
    private final PessoaFisicaClass pf;
    private final Supermercado supermercado;
    
    @FXML
    private TableColumn<?, ?> nameCol;
    @FXML
    private TableColumn<?, ?> cpfCol;
    @FXML
    private TableView<?> pessoaFisicaTable;
    @FXML
    private Button ordersButton;

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
    
    public BuscaPessoaFisicaController(FiltroController bc, PessoaFisicaClass pf, Supermercado supermercado) {
        this.supermercado = supermercado;
        
        this.pf = pf;
        
        List<FilterData> filters = new ArrayList<>();
        
        filters.add(new FilterData("Nome", pf.getClasse(), String.class));
        filters.add(new FilterData("CPF", pf.getClasse(), String.class));
        filters.add(new FilterData("Gênero", pf.getClasse(), PessoaFisica.Genero.class));
        
        bc.setFilters(filters);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (pf == PessoaFisicaClass.Funcionario){
            ordersButton.setVisible(false);
            ordersButton.setManaged(false);
        }
    }    

    @FXML
    private void getDetalhes(ActionEvent event) {
        //TODO Abrir Cliente ou Funcionario no DadosPessoaisController
    }

    @FXML
    private void getAllOrders(ActionEvent event) {
        //TODO tela com compras do cliente
    }
    
    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String marca = (String) response.get("CPF");
        String tipo = (String) response.get("Gênero");
    }
}
