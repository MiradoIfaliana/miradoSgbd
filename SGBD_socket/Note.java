package notefile;
import java.io.*;

public class Note extends File{
      FileWriter fw;
      public Note(String stg) throws Exception
      {
            super(stg);
            if(!this.exists())
            {
                  this.createNewFile();
            }
      }      
      public void writer(String nom) throws Exception
      {     String r1=this.read();
            FileWriter fw=new FileWriter(this);
            if(r1==null){
                  fw.write(nom);
            }else{
                  fw.write(r1+nom);
            }
            fw.close();
      }
      public String read() throws Exception
      {
            BufferedReader rd=new BufferedReader(new InputStreamReader(new FileInputStream(this)));
            String vln=rd.readLine();
            return vln;
      }
      public void deleteAndRead(String nom) throws Exception
      {
            FileWriter fw=new FileWriter(this);
            fw.write(nom);
            fw.close();
      }
      public void deleteAll() throws Exception
      {
            FileWriter fw=new FileWriter(this);
            fw.write("");
            fw.close();
      }

}