package com.lsfly.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael.Jing on 2017/5/17.
 */
public class ConvertToMap {

    public static Map ObjToMap(Object obj){
        Map<String,Object> reMap = new HashMap<String,Object>();
         if (obj == null)
            return null;
        Field[] fields1 = obj.getClass().getDeclaredFields();
        Field[] fields2 = obj.getClass().getSuperclass() != null?obj.getClass().getSuperclass().getDeclaredFields():null;
        Field[] fields3 = obj.getClass().getSuperclass() != null?(obj.getClass().getSuperclass().getSuperclass()!=null?obj.getClass().getSuperclass().getSuperclass().getDeclaredFields():null):null;

        try {
                try {
                   if(fields3 != null){
                       for(int i=0;i<fields3.length;i++){
                           Field f = obj.getClass().getSuperclass().getSuperclass().getDeclaredField(fields3[i].getName());
                           f.setAccessible(true);
                           Object o = f.get(obj);
                           reMap.put(fields3[i].getName(), o);
                       }
                   }

                    if(fields2 != null){
                        for(int i=0;i<fields2.length;i++){
                            Field f = obj.getClass().getSuperclass().getDeclaredField(fields2[i].getName());
                            f.setAccessible(true);
                            Object o = f.get(obj);
                            reMap.put(fields2[i].getName(), o);
                        }
                    }
                    if(fields1 != null){
                        for(int i=0;i<fields1.length;i++){
                            Field f = obj.getClass().getDeclaredField(fields1[i].getName());
                            f.setAccessible(true);
                            Object o = f.get(obj);
                            reMap.put(fields1[i].getName(), o);
                        }
                    }

                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reMap;
    }
}
