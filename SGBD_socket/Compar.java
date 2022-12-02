package traitement;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Compar implements Comparator
{ 
    public Compar(){
    }
    public int compare(Object a,Object b){
        try{
            double nb1=Double.valueOf(String.valueOf(a));
            double nb2=Double.valueOf(String.valueOf(b));
            if(nb1>nb2)
                return 1;
            else if(nb1<nb2)
                return -1;
            else
                return 0;
        }catch(Exception e){
            int res=String.valueOf(a).compareTo(String.valueOf(b));
            if(res==0){
                return 0;
            }else{
                return -1;
            }
        }
    }
    /*Comparator comp=new Comparator();
    int valiny=comp.compare(19,18);
    System.out.println("valiny = "+valiny);*/
 
}