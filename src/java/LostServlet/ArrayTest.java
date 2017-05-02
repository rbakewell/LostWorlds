/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LostServlet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author russ
 */
public class ArrayTest {
    public static boolean contains(ArrayList<?> list, Object o) {
        Integer h = (int)(long)o;
        if(list != null){
          for(Object item : list){
              if(item == h){
                  return true;
              }
          }
        return list.contains(o);
      }
      return true;
   }
    
}
