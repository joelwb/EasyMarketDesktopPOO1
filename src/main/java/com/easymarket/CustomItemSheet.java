/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easymarket;

import java.util.Optional;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;

/**
 *
 * @author joel-
 * Essa classe sera usada para fazer os componentes com filtros futuramente
 */
public class CustomItemSheet implements PropertySheet.Item{
    private String nome;
    private String categoria;
    private Object valor;
    private Class classe;

    public CustomItemSheet(String nome, String categoria, Object valor) {
        this.nome = nome;
        this.categoria = categoria;
        this.valor = valor;
        this.classe = valor.getClass();
    }
    
    public CustomItemSheet(String nome, String categoria, Class classe) {
        this.nome = nome;
        this.categoria = categoria;
        this.classe = classe;
    }

    @Override
    public Class<?> getType() {
        return classe;
    }

    @Override
    public String getCategory() {
        return categoria;
    }

    @Override
    public String getName() {
        return nome;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Object getValue() {
        return valor;
    }

    @Override
    public void setValue(Object valor) {
        this.valor = valor;
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.empty();
    }

}
