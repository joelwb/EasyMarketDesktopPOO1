/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author joel-
 */
public abstract class ConversorDataObjs {
    public static LocalDate toLocalDate(java.util.Date date) {
        Date slqDate = new java.sql.Date(date.getTime());
        return slqDate.toLocalDate();
    }
    
    public static java.util.Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }
}
