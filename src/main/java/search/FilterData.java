/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import util.Util;

/**
 *
 * @author joel-
 */
public class FilterData {
    private final String filterName;
    private final String filterCategory;
    private final Class type;

    public FilterData(String filterName, String filterCategory, Class type) throws IllegalArgumentException{
        Util.verificaStringNullVazia(filterName);
        Util.verificaStringNullVazia(filterCategory);
        Util.verificaIsObjNull(type);
        
        this.filterName = filterName;
        this.filterCategory = filterCategory;
        this.type = type;
    }

    public String getFilterName() {
        return filterName;
    }

    public String getFilterCategory() {
        return filterCategory;
    }

    public Class getType() {
        return type;
    }
    
}
