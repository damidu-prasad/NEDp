/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.ejb.model.common.UniDB;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.Teacher;
import javax.ejb.EJB;

/**
 *
 * @author Thilini Madagama
 */
public class UploadMarks {

    @EJB
    static UniDBLocal uni;
    public static void main(String[] args) {

            
       
        
        UploadMarks um = new UploadMarks();
        um.load(uni);

    }

    public void load( UniDBLocal uni) {
        System.out.println("uni "+uni);
        Teacher t = (Teacher) uni.find(1, Teacher.class);
    }
}
