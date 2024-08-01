/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.ejb.model.common.Security;
import com.ejb.model.common.UniDBLocal;
import com.webapps.controller.utilities.PasswordGenerator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author Thilini Madagama
 */
public class test {

    public static void main(String[] args) {

//        try { 
//            System.out.println("pass "+Security.decrypt("lxtCvh9LM7OUCSwd94cAMg=="));
//        } catch (GeneralSecurityException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//         PasswordGenerator usernameGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
//                                        .useDigits(false)
//                                        .useLower(false)
//                                        .useUpper(true)
//                                        .build();
//                              String  username = usernameGenerator.generate(8);
//                              System.out.println(username);
        double[] data = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80, 0, 90.0, 100.0};

// The mean average
        double mean = 0.0;
        for (int i = 0; i < data.length; i++) {
            mean += data[i];
        }
        mean /= data.length;
        System.out.println("mean "+mean);

// The variance
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance += (data[i] - mean) * (data[i] - mean);
            System.out.println("g "+(data[i] - mean));
        }
        System.out.println("va "+variance);
        variance /= data.length;
        System.out.println("variance "+variance);
// Standard Deviation
        double std = Math.sqrt(variance);
        
        System.out.println("q "+std/mean);

    }
}
