package com.grp6;



public class ToolBox {

    public static boolean OnlyOneAnnotation(boolean[] annotations) {
        int count =0;
        for(int i = 0;i<annotations.length;i++){
            if(annotations[i]){
                count++;
            }
        }
        if (count == 1){
            return true;
        
        }  
        return false;
    }
    
}
