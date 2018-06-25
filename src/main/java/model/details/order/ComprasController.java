/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.details.order;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import modelo.supermercado.Compra;
import util.AlertCreator;
import util.Screen;
import util.TableViewConfigurator;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class ComprasController implements Initializable {
    private List<Compra> compras;
    
    @FXML
    private TableView<List<String>> compraTable;

    
    public ComprasController(List<Compra> compras){
        this.compras = compras;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableViewConfigurator.configure(compraTable);
        
        for (Compra compra : compras){
            List<String> row = new ArrayList<>();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            row.add(sdf.format(compra.getDataHora()));
            row.add(String.format("%.2f",compra.getValorTotal()));
            
            compraTable.getItems().add(row);
        }
    }    

    @FXML
    private void getItensCompra(ActionEvent event) {
        int indxCompra = compraTable.getSelectionModel().getSelectedIndex();
        if (indxCompra == -1) return;
        
        Compra compra = compras.get(indxCompra);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/model/details/order/ItensCompra.fxml"));
        ItensCompraController icc = new ItensCompraController(compra.getItens());
        loader.setController(icc);
        
        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }
    
}
