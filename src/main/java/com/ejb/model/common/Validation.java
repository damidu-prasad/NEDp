/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 *
 * @author USER
 */
public class Validation {
    
    public static boolean validateInput(String input){
    
        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
        
        return encoder.canEncode(input);
        
    }
    
}
