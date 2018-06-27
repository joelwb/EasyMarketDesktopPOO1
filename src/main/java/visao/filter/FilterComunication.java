/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.filter;

import java.util.Map;

/**
 *
 * @author joel-
 */
public interface FilterComunication {
    void listenResponse(Map<String,Object> response);
}
