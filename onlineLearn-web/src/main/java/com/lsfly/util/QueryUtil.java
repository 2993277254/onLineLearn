package com.lsfly.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/19.
 */
public class QueryUtil {

    public static final String  pageNumName="pageNum";
    public static final String  pageSizeName="pageSize";
    public static final int defaultStartPage=1;
    public static final int defaultPageSize=20;


    //父类转为子类
    //当前只支持一级转换
    public static void ConvertParent2Child(Object parent,Object child){

        Map<String,Object> reMap = new HashMap<String,Object>();

        Field[] fields1 = parent.getClass().getDeclaredFields();
        Field[] fields2 = child.getClass().getDeclaredFields();


            try {
                if(fields1 != null){
                    for(int i=0;i<fields2.length;i++){
                        Field f = fields2[i];
                        f.setAccessible(true);
                        Object o = f.get(child);
                        //Field parentField= parent.getClass().getDeclaredField(f.getName());
                        //parentField.setAccessible(true);
                        //parentField.set(parent,o);
                        f.set(parent,o);
                    }
                }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static<T> T ConvertParent2Child(Class parent,Object child){
        T parentex = null;
        try{
            parentex= (T)parent.newInstance();
            ConvertParent2Child(parentex,child);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
       return parentex;
    }

    public static<T> List<T> ConvertParent2ChildLst(Class parent,List childs){
        List<T> retlst = new ArrayList<>();
        for(int i=0;i<childs.size();i++){
            T ret = ConvertParent2Child(parent,childs.get(i));
            retlst.add(ret);
        }
        return retlst;
    }
}
