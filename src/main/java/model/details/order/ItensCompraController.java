/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.details.order;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import modelo.supermercado.mercadoria.ItemProduto;
import util.TableViewConfigurator;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class ItensCompraController implements Initializable {
    private List<ItemProduto> itens;
    
    
    @FXML
    private TableView<List<String>> itensTable;

    public ItensCompraController(List<ItemProduto> itens) {
        this.itens = itens;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableViewConfigurator.configure(itensTable);
        
        for (ItemProduto item : itens){
            List<String> row = new ArrayList<>();
            row.add(item.getProduto().getNome());
            row.add(String.format("%.2f",item.getPrecoCompra()));
            row.add(String.valueOf(item.getQuantidade()));
            
            itensTable.getItems().add(row);
        }
    }    
    
}
