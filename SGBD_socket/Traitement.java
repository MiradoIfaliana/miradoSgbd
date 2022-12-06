package traitement;
import java.io.*;
import java.nio.channels.SelectionKey;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

import javax.lang.model.element.Element;
import exception.*;
import notefile.*;
//getHeadersSelect
//proje
public class Traitement {
      String database;
      public Traitement(){    
      }

      public String getDatabase() {
            return database;
      }
      public void setDatabase(String database) {
            this.database = database;
      }

      //type : String / int / double / Time / Date .
      //create table m (nom1 String,nom2 double...);
      public boolean isValideType(String strType){
            String type="String,int,double,Time,Date";
            String[] types=type.split(",");

            for(String tp:types){
                  if(tp.compareTo(strType)==0){
                        return true;
                  }
            }
            return false;
      }
      public boolean isValideValue(String value,String type)throws Exception{
            if(value.contains(";;")==true || value.contains("//")==true || value.contains("--")==true || value.contains("%%")==true){
                  throw new RQTException("\";;\" et \"//\" sont interdie");
            }
           if(type.compareToIgnoreCase("double")==0){
                  try{  Double.valueOf(value);     }
                  catch(Exception e){     return false;     }
           }else if(type.compareToIgnoreCase("int")==0){
                  try{  Integer.valueOf(value);     }
                  catch(Exception e){     return false;     }
           }else if(type.compareToIgnoreCase("Time")==0){
                  try{  Time.valueOf(value);     }
                  catch(Exception e){     return false;     }
            }else if(type.compareToIgnoreCase("Date")==0){
                  try{  Date.valueOf(value);     }
                  catch(Exception e){     return false;     }
            }
            return true;
      }
      public boolean isvalideNameColumn(String strnameCol,String[] namevalid){
            for(String valid:namevalid){
                  if(valid.compareToIgnoreCase(strnameCol)==0){
                        return true;
                  }
            }
            return false;
      }
      public boolean is_alphabet(String str){
            String alph="abcdefghijklmnopqrstuvwxyz1234567890";
            boolean equal1=false;
            for(int i=0;i<str.length();i++){
                equal1=false;
                for(int u=0;u<alph.length();u++){ //parcourir les alphabets..
                    if( str.substring(i, i+1).compareToIgnoreCase( alph.substring(u, u+1) )==0 ){ // et les comparer avec la charactere indice i de str
                        equal1=true;
                    }
                }
                if(equal1==false){  return false;} //si il avait qu'une seule caractere nom valide , c-a-d il avait comme ";" ou ":" ou "`"
            }
            return true;
      }
      public String[] decompositionSimple(String rqt)throws Exception{
            String[] requet=rqt.split(" ");
            Vector noSpace=new Vector();
            for(String str : requet){
                  if(str.compareTo("")!=0){
                        noSpace.add(str);
                  }
            }
            String[] requete=new String[noSpace.size()];
            for(int i=0;i<noSpace.size();i++){
                  requete[i]=(String)noSpace.elementAt(i);
                  System.out.println("requete["+i+"]="+requete[i]);
            }
            return requete;
      }
      public String[] decompoANDverifChar(String rqt)throws Exception{
            String[] requet=rqt.split(" ");
            Vector noSpace=new Vector();
            for(String str : requet){
                  if(str.compareTo("")!=0){
                        noSpace.add(str);
                  }
            }
            String[] requete=new String[noSpace.size()];
            for(int i=0;i<noSpace.size();i++){
                  requete[i]=(String)noSpace.elementAt(i);
                  //validation des characteres de chaque mots de la requete
                  if(is_alphabet(requete[i])==false){
                        throw new RQTException("charactere du mots \""+requete[i]+"\" non valide");
                  }
                  System.out.println("requete["+i+"]="+requete[i]);
            }
            return requete;
      }
//--------------------------------------------------------------------test d'existance database et table.
      public boolean isDataBaseExist(String nameDB){
            File file=new File("database/"+nameDB);
            if(file.exists()==false){  return false; }
            return true;
      }
      public boolean isTableExist(String nameDatabase,String nameTab){
            if(isDataBaseExist(nameDatabase)==false){ return false; }
            File file=new File("database/"+nameDatabase+"/"+nameTab+".txt");
            if(file.exists()==false){  return false;   }
            return true;
      }
      public String[][] getNameColumn_TypeColumn(String name_type){
            //n:m;;o:p;;q:r
            String [] nameType=name_type.split(";;");
            String [][] name_typ=new String[2][nameType.length];
            for(int i=0;i<nameType.length;i++){
                  String[] nTp=nameType[i].split(":");   
                  name_typ[0][i]=nTp[0];
                  name_typ[1][i]=nTp[1];
            }
            return name_typ;
      }
      public boolean existIdentiqueIgnoreCase(String[] names){
            for(int i=0;i<names.length;i++){
                  for(int u=0;u<names.length;u++){
                        if(u!=i && names[i].compareToIgnoreCase(names[u])==0){
                              return true;
                        }
                  }
            }
            return false;
      }
//---------------------------------------------------------------------------------------Show database
      public String[][] showDatabases(String rqt)throws Exception{ //show databases
            String[] requete=decompoANDverifChar(rqt);
            if(requete.length!=2 ){
                  throw new RQTException("Phrase du requete non valide");  
            } 
            if(requete[0].compareToIgnoreCase("show")!=0 || requete[1].compareToIgnoreCase("databases")!=0 ){
                  throw new RQTException("Phrase du requete non valide");
            }
            File folder=new File("database/");
            File[] datas=folder.listFiles();
            if(datas==null){ return null;}
            else if(datas.length<1){   return null;  }
            String[][] lesDBases=new String[datas.length][1];
            for(int i=0;i<datas.length;i++){
                  lesDBases[i][0]=datas[i].getName();
            }
            return lesDBases;
      }
//---------------------------------------------------------------------------------------CREATE DATABASE
      public void createDatabase(String rqt)throws Exception{
                 //decomposition du phrase de la requete
                 String[] requete=decompoANDverifChar(rqt);
                 //verification du syntaxe de chaque mots de la requete
                  if(requete.length!=3 ){
                        throw new RQTException("Phrase du requete non valide");  
                  }            
                  if(requete[0].compareToIgnoreCase("create")!=0 || requete[1].compareToIgnoreCase("database")!=0 ){
                              throw new RQTException("Phrase du requete non valide");
                  }
            //si tout est bien valide
                 File folder=new File("database/"+requete[2]);
                 boolean res=folder.mkdir(); //creer le dossier du nouvelle database
                 if(res){
                   System.out.println("dossier creer");
                 }else{
                  throw new RQTException("database existe deja");
                 }

      }
//---------------------------------------------------------------------------------------USE DATABASE
      public void useDatabase(String rqt)throws Exception{
            String[] requete=decompoANDverifChar(rqt);
            try{
                  if(requete[0].compareToIgnoreCase("use")!=0 || requete.length!=2){
                        throw new RQTException("syntaxe non valide");
                  }
            }catch(Exception e){
                  throw new RQTException("syntaxe non valide");
            }
            File folder=new File("database/"+requete[1]);
            boolean res=folder.exists(); //si le database a utiliser existe
            if(res){
                  setDatabase(requete[1]);
            }else{
                  throw new RQTException("database \""+requete[1]+"\""+" n'existe pas");
            }
      }
//---------------------------------------------------------------------------------------show tables 
      public String[][] showTables(String rqt)throws Exception{ //show databases
            String[] requete=decompoANDverifChar(rqt);
            if(requete.length!=2 ){
                  throw new RQTException("Phrase du requete non valide");  
            } 
            if(requete[0].compareToIgnoreCase("show")!=0 || requete[1].compareToIgnoreCase("tables")!=0 ){
                  throw new RQTException("Phrase du requete non valide");
            }
            if(this.getDatabase()==null){ throw new RQTException("non connecter a une database"); }
            File folder=new File("database/"+this.getDatabase());
            if(folder.exists()==false){
                  throw new RQTException("database "+this.getDatabase()+" n'existe pas");
            }
            File [] tables=folder.listFiles();
            if(tables==null){ return null;}
            else if(tables.length<1){   return null;  }

            Vector vTables=new Vector();
            for(int i=0;i<tables.length;i++){
                  if(tables[i].length()>=5){
                        ///nom.txt
                        if(tables[i].getName().substring(tables[i].getName().length()-4 , tables[i].getName().length()).compareToIgnoreCase(".txt")==0){
                              vTables.add( tables[i].getName().substring(0, tables[i].getName().length()-4) );
                        }
                  }
            }
            if(vTables.size()<1){ return null; }
            String[][] lesTables=new String[vTables.size()][1];
            for(int i=0;i<vTables.size();i++){
                  lesTables[i][0]=(String)vTables.elementAt(i);
            }
            return lesTables;
}
//---------------------------------------------------------------------------------------CREATE TABLE
      public void createTable(String rqt)throws Exception{
      //decomposition du phrase de la requete
            String rqte=rqt.replace(',',' ');
            System.out.println(rqte);
            rqte=rqte.replace('(',' ');
            rqte=rqte.replace(')',' ');
            System.out.println(rqte);
            String[] requete=decompoANDverifChar(rqte);
            String headTab="";
      //requete[0]=create requete[1]=table requete[2]=nomTab requete[3]=nomColumn requete[4]=type...requete[ requete.length-2 ]=LastnomColumn requete[ requete.length-1 ]=type 
            if(requete.length < 4){
                  throw new RQTException("requete non valide");
            }
            //-----------------------------------------
            if( requete[0].compareToIgnoreCase("create")==0 && requete[1].compareToIgnoreCase("table")==0 && isValideType(requete[2])==false && isValideType(requete[3])==false ){
                  //"create table koko (hoho int,jojo String,hoho int)
            //verifications des types de donnee
                  int j=4;
                  System.out.println("-------------");
                  while(j<requete.length){
                        headTab=headTab+requete[j-1]+":"+requete[j]+";;";
                        System.out.println(requete[j]);
                        if(isValideType(requete[j])==false){ //si le type donnee est valide
                              throw new RQTException("Type \""+requete[j]+"\" non valide");
                        }
                        j=j+2;
                  }
                  headTab=headTab.substring(0,headTab.length()-2);
                  String[] namesCol=getNameColumn_TypeColumn(headTab)[0];
                  if(existIdentiqueIgnoreCase(namesCol)==true){
                        throw new RQTException("il y a des noms de colonne identique");
                  }
            }else{
                  throw new RQTException("requete non valide");
            }
            //---si tous les test sont tous valident donc:
            if(database!=null){
                  if(isDataBaseExist(database)==false){   throw new RQTException("data base n'existe pas");     }
                  if(isTableExist(database,requete[2])==true){      throw new RQTException("Table existe deja");    }
                  Note note=new Note("database/"+database+"/"+requete[2]+".txt");
                  //---> nom:type;;nom:type;;nom:type//
                  note.writer(headTab+"//");
                  System.out.println(note.read());
            }else{
                  throw new RQTException("aucun database utilisé");
            }
      }
//---------------------------------------------------------------------------------------------------------------------------------Describe table
      public String[][] describeTable(String rqt)throws Exception{
            String[] requete=decompoANDverifChar(rqt);
            if(requete.length!=3 ){
                  throw new RQTException("Phrase du requete non valide");  
            } 
            if(requete[0].compareToIgnoreCase("describe")!=0 || requete[1].compareToIgnoreCase("table")!=0){
                  throw new RQTException("Phrase du requete non valide");
            }
            if(this.getDatabase()==null){ throw new RQTException("non connecter a une database"); }
            if(isDataBaseExist(this.database)==false){
                  throw new RQTException("database "+this.getDatabase()+" n'existe pas");
            }
            if(isTableExist(this.getDatabase(), requete[2])==false){
                  throw new RQTException("table "+ requete[2]+" n'existe pas dans "+this.database);
            }
            Note table=new Note("database/"+this.database+"/"+requete[2]+".txt");
            String[][] namTyp=getNameColumn_TypeColumn(table.read().split("//")[0]);
            String[][] reponse=new String[namTyp[0].length][2];
            for(int i=0;i<namTyp[0].length;i++){
                  reponse[i][0]=namTyp[0][i];
                  reponse[i][1]=namTyp[1][i];
            }
            return reponse;
      }

//-------------------------------------------------------------------------------------------------------------INSERTION
      public void insert(String rqt)throws Exception{
            //insert into nomTab (col1,col2,col2...) values(val1,val2,val3...);
            String rqte=rqt.replace('(',' ');
            rqte=rqte.replace(')',' ');
            rqte=rqte.replace(',',' ');
            String[] requete=decompositionSimple(rqte);
            //rq[0]="insert" rq[1]="into"  rq[2]="nomTab" rq[3]="nameCol1"... rq[n]="values" rq[n+1]="val1" ...

            try{
                  if( requete[0].compareToIgnoreCase("insert")==0 && requete[1].compareToIgnoreCase("into")==0 && requete.length>4){
                        if(isDataBaseExist(database)==false){    throw new RQTException("database "+database+" n'existe pas"); }
                        //verifier si la table a inseret exist
                        if(isTableExist(database,requete[2])==false){    throw new RQTException("table "+requete[2]+" n'existe pas"); }
                        Note note=new Note("database/"+database+"/"+requete[2]+".txt");
                        String contn=note.read();
                        //"namecol1:type1;;namecol2:type2//"
                        //prendre l'entete de la table et l'utiliser pour le comparer au requete
                        String[] contns=contn.split("//");
                        String[][] nameType=getNameColumn_TypeColumn(contns[0]);//c'est dans l'indice 0 qui contient la description
                        if(requete[ (nameType[0].length)+3 ].compareToIgnoreCase("values")!=0 ){   //--->car normalement il devrait avoir le mots values dans la requete
                              throw new RQTException("syntaxe du requete non valide");
                        }
                        Vector[] colsAndValues=new Vector[2]; //[0]
                        colsAndValues[0]=new Vector();
                        colsAndValues[1]=new Vector();

                        int count=0;
                        //rq[3]="nameCol1"... rq[ (nameType[0].length)+3 ]="values" rq[ (nameType[0].length)+3+1 ]="val1" ...
                        /*for(int i=0;i<nameType[0].length;i++){
                              System.out.println(nameType[0][i]);
                        }*/
                        while(count<nameType[0].length){
                              if(isvalideNameColumn( requete[ 3+count ] , nameType[0])==false){ //si les noms du colone de la table de la requete est correspondant 
                                    throw new RQTException("name column \""+ requete[ 3+count ]+"\" non valide");
                              }
                              colsAndValues[0].add(requete[ 3+count ]); //les noms des colones
                              colsAndValues[1].add(requete[ (nameType[0].length)+3+1+count ]); //leur valeur
                              count++;
                        }
                        //ranger les valeurs
                        String[] valeurRanger=new String[ colsAndValues[1].size() ];
                        for(int i=0;i<nameType[0].length;i++){
                              for(int j=0;j<colsAndValues[0].size();j++){ //pour chercher le nom de colonne correspondant ...
                                    String strNameCol=(String)colsAndValues[0].elementAt(j);
                                    if( nameType[0][i].compareToIgnoreCase(strNameCol)==0 ){ //....
                                          valeurRanger[i]=(String)colsAndValues[1].elementAt(j); //la valeur correspondant
                                          if(isValideValue(valeurRanger[i],nameType[1][i])==false){ //verifier si les valeur a affecter est valide par rapport a leur type
                                                throw new RQTException("\""+valeurRanger[i]+"\" non valide pour type "+nameType[1][j]);
                                          }
                                    }
                              }
                        }
                        // hoho:int;;hoho:int//
                        for(int i=0;i<nameType[0].length;i++){
                              note.writer(valeurRanger[i]);
                              if(i+1<nameType[0].length){
                                    note.writer(";;");
                              }
                        }
                        note.writer("//");


                  }else{
                        throw new RQTException("requete non valide");
                  }
            }catch( RQTException rqe){
                  throw rqe;
            }catch(Exception re){
                  re.printStackTrace();
            }
      }
//------------------------------------------------------------------------------------------------projection
      public String[][] projection(String[] headValues,String[][] values,String[] namesShow )throws Exception{
            //[i][0]=val0 [i][1]=val1 [i][2]=val2 [i][3]=val3 ....
            if(existIdentiqueIgnoreCase(namesShow)==true){
                  throw new RQTException("repetition de nom de colonne a afficher");
            }
            for(int i=0;i<namesShow.length;i++){
                  if(isvalideNameColumn(namesShow[i], headValues)==false){
                        throw new RQTException("colonne "+namesShow[i]+" non valide");
                  }
            }
            if(values==null){
                  return null;
            }
            String[][] project=new String[values.length][namesShow.length];
            for(int i1=0;i1<values.length;i1++){
                  for(int i2=0;i2<namesShow.length;i2++){
                        for(int i3=0;i3<headValues.length;i3++){
                              if(namesShow[i2].compareToIgnoreCase(headValues[i3])==0){ //pour chercher dans quel indice(id headValues) se trouve la valeur a afficher
                                    project[i1][i2]=values[i1][i3];
                              }
                        }
                  }
            }
            return project;
      }
//---------------------------------------------------------------------------------------------------selection
      public String[][] selection(String[] headValues,String[][] values, String nameCol_for_compar , String signCompar, String valeur)throws Exception{
            if(values==null){
                  return null;
            }
            if(isvalideNameColumn(nameCol_for_compar, headValues)==false){
                  throw new RQTException("colonne "+nameCol_for_compar+" non valide");
            }
           //get va
            Compar comp=new Compar();
            Vector vct=new Vector(); //pour mettre les valeur selectionner
            int idV=-1; // indice du valeur a comparer
            for(int i2=0;i2<headValues.length;i2++){
                  if(headValues[i2].compareToIgnoreCase(nameCol_for_compar)==0){ //je cherche la valeur indice i du nom de ---> nameCol_for_compar
                        idV=i2;
                  }
            }
               for(int i=0;i<values.length;i++){
                  if(signCompar.compareTo("<=")==0){        
                     if( comp.compare(values[i][idV], valeur)==-1 || comp.compare(values[i][idV], valeur)==0){
                        vct.add(values[i]);
                     }
         
                  }else if(signCompar.compareTo(">=")==0){
                     if( comp.compare(values[i][idV], valeur)==1 || comp.compare(values[i][idV], valeur)==0){
                        vct.add(values[i]);
                     }  
                  }else if(signCompar.compareTo("<")==0){   
                     if( comp.compare(values[i][idV], valeur)==-1 ){
                        vct.add(values[i]);
                     }   
                  }else if(signCompar.compareTo(">")==0){
                     if( comp.compare(values[i][idV], valeur)==1 ){
                        vct.add(values[i]);
                     }            
                  }else if(signCompar.compareTo("!=")==0){    
                     if( comp.compare(values[i][idV], valeur)!=0 ){
                        vct.add(values[i]);
                     }                 
                  }else if(signCompar.compareTo("=")==0){             
                     if( comp.compare(values[i][idV], valeur)==0 ){
                        vct.add(values[i]);
                     } 
                  }
               }
               if(vct.size()==0){
                  //System.out.println("vector tsy misy");
                  return null;
               }else{
                  String[][] rep=new String[vct.size()][1];
                  for(int i=0;i<vct.size();i++){
                        rep[i]=(String[])vct.elementAt(i);
                  }
                  return rep;
               }
      }
      //selection (raha multiple condition de averimberina arak'izay fotsiny le select)
//-----------------------------------------------------------------------------------------------SELECT
      //select n,m from nametable where n = 0 and n<=2 
      public String[][] select(String rqt)throws Exception{
            String rqte=rqt.replace(',', ' ');
            String[] requete=decompositionSimple(rqte);
            //jerene hoe select ve no voalohany , misy from ve , de jerena apres an'iny fotsiny le nom de table aveo , de otrizay koa ny where
            //tokony ho
            int idfrom=-1;
            for(int i=0;i<requete.length;i++){
                  if(requete[i].compareToIgnoreCase("from")==0){
                        idfrom=i;
                  }
            }
            if(idfrom==-1 || requete[0].compareToIgnoreCase("select")!=0 ){   throw new RQTException("Syntaxe non valide");   }//si il n'ya meme pas de "from" ou "select" dans la requete
            //verification de la connectivite du database et l'existance du table 
            if(isDataBaseExist(database)==false){    throw new RQTException("database "+database+" n'existe pas"); }
            if(isTableExist(database, requete[idfrom+1])==false){    throw new RQTException("table "+requete[idfrom+1]+" n'existe pas"); }
            //petite verification du syntaxe de la requete
            Note note=new Note("database/"+database+"/"+requete[idfrom+1]+".txt");
            String table=note.read();
            String entete=table.split("//")[0];  //pour avoir "nom:type1;;nom:type2..."
            String[][] nameType=getNameColumn_TypeColumn(entete);
            //avoir le nom de colonne a selectionner
            String [] colSelct=new String[idfrom-1];
            if(requete[idfrom-1].compareTo("*")==0){
                  colSelct=nameType[0];
            }else{
                  for(int i=1;i<idfrom;i++){ 
                        colSelct[i-1]=requete[i];
                        if(isvalideNameColumn(requete[i],nameType[0])==false){   throw new RQTException("nom de colonne \""+requete[i]+"\" non valide");   }
                  }
            }
           //----
            String[] data=table.split("//");
            if(data.length<=1){ //raha mbola ts nisy donnee mihintsy
                  return null;
            }
            String[][] values=new String[data.length-1][1];
            for(int i=1;i<data.length;i++){
                  values[i-1]=data[i].split(";;"); 
            }
             //maitenant verifiant si la requete a une condition
            if(requete.length == idfrom+2){//c-a-d pas de where
                  String[][] rep=projection(nameType[0],values,colSelct);
                  return rep;
            }else{
             if(requete.length<idfrom+3){ throw new RQTException("syntaxe non valide"); } //
             Vector vct=new Vector();
             //where co1= 90 and col2 =io or col6=popo
             for(int i=idfrom+3;i<requete.length;i++){
                  String forSplit="//";
                  if(requete[i].contains(">=")){ forSplit=">="; }
                  else if(requete[i].contains("<=")){forSplit="<="; }
                  else if(requete[i].contains("!=")){forSplit="!="; }
                  else if(requete[i].contains(">")){forSplit=">"; }
                  else if(requete[i].contains("<")){forSplit="<"; }
                  else if(requete[i].contains("=")){forSplit="="; }
                  String[] afterSplit=requete[i].split(forSplit);
                  boolean init=false;
                  for(int i2=0;i2<afterSplit.length;i2++){ 
                        vct.add(afterSplit[i2]);
                        System.out.println(afterSplit[i2]);

                        if(requete[i].length()>afterSplit[0].length() && init==false && requete[i].substring(0, 2).compareTo( afterSplit[0].substring(0, 2) )==0 && forSplit.compareTo("//")!=0){
                              vct.add(forSplit);  
                              init=true;      
                        }
                        
                  }
             }
             if(vct.size()==0 || vct.size()<3){ throw new RQTException("syntaxe no valide");   }
             String[] namCompVal=new String[vct.size()];
             for(int i=0;i<vct.size();i++){
                  namCompVal[i]=(String)vct.elementAt(i);
             }
             if(vct.size()==3){ //selection(String[] headValues,String[][] values, String nameCol_for_compar , String signCompar, String valeur);
                  String[][] retour=selection( nameType[0], values, namCompVal[0], namCompVal[1], namCompVal[2]);
                  // projection(String[] headValues,String[][] values,String[] namesShow )
                  retour=projection(nameType[0],retour,colSelct);
                  /*if(retour==null){
                        System.out.println("nullllllllllllllllllll");
                  }*/
                  return retour;
             }
             else{ //vct.size()>3 c-a-d il ya quelque plusieur condition
                  try{
                        int id=0;
                        String [][] result=null;
                        while(id<namCompVal.length){
                                    //selection(String[] headValues,String[][] values, String nameCol_for_compar , String signCompar, String valeur);
                             if(result==null ){  
                                    if(id==0){
                                          result=selection( nameType[0], values, namCompVal[id], namCompVal[id+1], namCompVal[id+2]);
                                    }else if(id>1){
                                          if(namCompVal[id-1].compareToIgnoreCase("or")==0)result=selection( nameType[0], values, namCompVal[id], namCompVal[id+1], namCompVal[id+2]);

                                    }
                             }else if(namCompVal[id-1].compareToIgnoreCase("or")==0){
                                    String[][] rep=selection( nameType[0], values, namCompVal[id], namCompVal[id+1], namCompVal[id+2]);
                                    result=union(result,rep);
                             }else if(namCompVal[id-1].compareToIgnoreCase("and")==0){
                                    result=selection( nameType[0], result, namCompVal[id], namCompVal[id+1], namCompVal[id+2]);
                              }
                              id=id+4;
                        }
                        
                        return projection(nameType[0],result,colSelct);
                  }catch(Exception e){
                        e.printStackTrace();
                        throw new RQTException("syntaxe no valide");
                  }
             }

            }
      }
//-----------------------------------------------------------------------------------------------
      public boolean isEqualContains(String[] strs1, String[] strs2){
            int nbEgality=0;
            if(strs1.length==strs2.length){
                  for(int u=0;u<strs1.length;u++){
                        if( strs1[u].compareTo(strs2[u])==0 ){
                              nbEgality++;
                        }
                  }
                  if(nbEgality==strs1.length){
                        return true;
                  }
            }
            return false;
      }
      /*public String[][] unionTsDTenMet(String[][] data1,String[][] data2){
            Vector rep=new Vector();
            String[][] sInf=data1;
            String[][] sSup=data2;
            if(data1.length>data2.length){
                  sInf=data2;
                  sSup=data1;
            }
            for(int i=0;i<sInf.length;i++){
                  if(sInf[i].length==sSup[i].length){ //pour eviter exception
                        if(isEqualContains(sInf[i], sSup[i])==false){
                              rep.add(sInf[i]);
                              rep.add(sSup[i]);
                        }else{
                              rep.add(sInf[i]);
                        }
                  }
                  if(i+1==sInf.length){
                        for(int u=i+1;u<sSup.length;u++){
                              rep.add(sSup[u]);
                        }
                  }
            }
            String[][] retour=new String[rep.size()][1];
            for(int i=0;i<rep.size();i++){
                  retour[i]=(String[])rep.elementAt(i);
            }
            return retour;
      }*/
      public String[][] union(String[][] data1,String[][] data2){
            Vector vrest=new Vector();
            for(int i=0;i<data2.length;i++){
                  int nbEqual=0;
                  for(int i1=0;i1<data1.length;i1++){
                        if(isEqualContains(data2[i],data1[i1])==true){
                              nbEqual++;
                        }
                  }
                  if(nbEqual==0){ //aucun indentique
                        vrest.add(data2[i]);
                  }
            }
            String[][] data=new String[data1.length+vrest.size()][1];
            for(int i=0;i<data1.length;i++){
                  data[i]=data1[i];
            }
            for(int i=0;i<vrest.size();i++){
                  data[data1.length+i]=(String[])vrest.elementAt(i);
            }
            return data;
      }
      public String[][] difference(String[][] R,String[][] S){
            if(R==null){return null;}
            if(S==null){ return R;}
            Vector vRep=new Vector();
            for(int r=0;r<R.length;r++){
                  boolean existEqual=false;
                  for(int s=0;s<S.length;s++){
                        if(isEqualContains(R[r],S[s])==true){
                              existEqual=true;
                        }
                  }
                  if(existEqual==false){ //si R[r] n'a aucun equivalant de lui dans S
                        vRep.add(R[r]);
                  }
            }
            if(vRep.size()<1){ return null; }
            String[][] resp=new String[vRep.size()][1];
            for(int i=0;i<vRep.size();i++){
                  resp[i]=(String[])vRep.elementAt(i);
            }
            return resp;
      }
 
      public String[][] differencier(String rqt)throws Exception{
            //(Selection)--(selection)--(selection)....
            //String rqte=decompositionSimple(rqt);
            String[]selections=rqt.split("--");
            if(selections.length<2){  throw new RQTException("syntaxe pour operation Difference non valide"); }
            String[] lesSelect=new String[selections.length];
            for(int i=0;i<selections.length;i++){
                  String select=selections[i].replaceAll(" ","");
                  //[0]="(" [1]="s" [2]="e" [3]="l" [4]="e" [5]="c" [6]="t" .....[select.length-1]=")"----> verifier ce syntaxe
                  System.out.println(select);
                  if(select.substring(0, 7).compareToIgnoreCase("(select")!=0 || select.substring(select.length()-1, select.length()).compareToIgnoreCase(")")!=0){
                        throw new RQTException("syntaxe pour operation Difference non valide");
                  }
                  System.out.println(selections[i]);
                  lesSelect[i]=selections[i].replace('(', ' ');
                  lesSelect[i]=lesSelect[i].replace(')',' ');
            }
            //ampidirina anaty vector le resultat aveo
            String[][] difference=select(lesSelect[0]);
            for(int i=1;i<lesSelect.length;i++){
                  difference=difference(difference,select(lesSelect[i]));
            }
            return difference;
      }
      /* public String[][] produitCartesienDesordre(String[][] R, String[][] S){
            String[][] sup=R;
            String[][] inf=S;
            if(R.length<S.length){ 
                  sup=S; 
                  inf=R;
            }
            String[][] resp=new String[sup.length][1];
            for(int i=0;i<sup.length;i++){
                  if(i+1<inf.length){
                        String[] pjct=new String[sup[i].length+inf[i].length];
                        for(int p=0;p<sup[i].length;p++){
                              pjct[p]=sup[i][p];
                              if(p+1==sup[i].length){
                                    for(int f=0;f<inf[i].length;f++){
                                          pjct[p+1+f]=inf[i][f];
                                    }
                              }
                        }
                        resp[i]=pjct;
                  }else{
                        resp[i]=sup[i];
                  }
            }
            return resp;
      }*/
  /*    public String[][] produitCartesien(String[][] R, String[][] S){
            int sup=R.length;
            if(R.length<S.length){  sup=S.length; }
            String[][] data=new String[sup][1];
            int lastid=0;
            for(int i=0;i<sup;i++){
                  if(i<R.length && i<S.length){
                        String[] slst=new String[R[i].length+S[i].length];
                        for(int i1=0;i1<R[i].length;i1++){
                              slst[i1]=R[i][i1];
                              if(i1+1==R[i].length){
                                    for(int i2=0;i2<S[i].length;i2++){
                                          slst[i1+1+i2]=S[i][i2];
                                    }
                              }
                        }
                        data[i]=slst;
                        lastid=i;
                  }else if(R.length>S.length){
                        String[] slst=new String[R[i].length];
                        for(int i1=0;i1<R[i].length;i++){
                              slst[i1]=R[i][i1];
                        }
                        data[i]=slst;
                  }else if(R.length<S.length){
                        int id2=0;
                        String[] slst=new String[ R[lastid].length+S[i].length ];
                        for(int i1=0;i1<R[lastid].length+S[i].length;i1++){
                              if(i1<R[lastid].length){   slst[i1]="";   }
                              else if(i1>=R[lastid].length){
                                    slst[id2]=S[i][id2];
                                    id2++;
                              }
                        }
                  }
            }

            return data;
      }*/
//-----------------------------------------------------------------------------------------------------------------
public String[][] produitCartesien(String[][] R, String[][] S){
      String [][] resultat=new String[R.length*S.length][1];
      int id=0;
      for(int i1=0;i1<R.length;i1++){
            for(int i2=0;i2<S.length;i2++){
               int u=0;
               String[] lst=new String[R[i1].length+S[i2].length];
               for(int u1=0;u1<R[i1].length;u1++){
                  lst[u]=R[i1][u1];
                  u++;
               }
               for(int u2=0;u2<S[i2].length;u2++){
                  lst[u]=S[i2][u2];
                  u++;
               }
               resultat[id]=lst;
               id++;
            }
      }

      return resultat;
}

//----------------------------------------------------------------------------------------------------------------jointure
//-------------------------------attribu complet ve?----sa?
      public String[][] jointure(String[] Rattribu,String[] Sattribu,String RnameAttribuForJoin,String SnameAttribuForJoin,String[][] R, String[][] S)throws Exception{
            //exemple-----0----1-------2----------------0-----1------2-------3---
            //exempleR : nom,prenom,classe    et    S: nom , age , numero, classe 
            String[][] prdCart=produitCartesien(R,S);
            int idR=-1; int idS=-1;
            for(int i=0;i<Rattribu.length;i++){
                  if(Rattribu[i].compareToIgnoreCase(RnameAttribuForJoin)==0){      idR=i; }
            }
            for(int i=0;i<Sattribu.length;i++){
                  if(Sattribu[i].compareToIgnoreCase(SnameAttribuForJoin)==0){      idS=i; }
            }
            if(idR==-1){ throw new RQTException("colonne=\""+RnameAttribuForJoin+"\" inconnu"); }
            if(idS==-1){ throw new RQTException("colonne=\""+SnameAttribuForJoin+"\" inconnu"); }
            //idR-----idR+idS+1 = les indice des deux cote pour la jointure
            Vector vJoin=new Vector();
            for(int i=0;i<prdCart.length;i++){
                  if(prdCart[i][idR].compareToIgnoreCase(prdCart[i][idR+idS+1])==0){     
                        String[] elimineDoublant=new String[prdCart[i].length-1];
                        int id=0;
                        for(int u=0;u<prdCart[i].length;i++){
                              if(u!=idR+idS+1){                         //iray @ le attribu ihany no tazomina
                                    elimineDoublant[id]=prdCart[i][u];
                              }
                        }
                        vJoin.add(elimineDoublant);
                  }
            }
            if(vJoin.size()<1){ return null; }
            String[][] join=new String[vJoin.size()][1];
            for(int i=0;i<vJoin.size();i++){
                 join[i]=(String[])vJoin.elementAt(i);
            }
            return join;
      }
//----------------------------------------------------
public String[] splitByRegexPoint(String o){
      String aSpliter="";
      for(int i=0;i<o.length();i++){
        
        if(o.substring(i, i+1).compareTo(".")!=0 ){
            aSpliter=aSpliter.concat(o.substring(i, i+1));
        }else{
              aSpliter=aSpliter.concat(";;//");
        }
      }
      String[] rep=aSpliter.split(";;//");
      return rep;
   }
//-------------------------------------------------------------------------------------------------------------------join
      public String[][] join(String rqt)throws Exception{
            String [] attribus=getHeadersSelect(rqt);//[0]=eleve.nom , [1]=eleve.nee , [2]=salle.numero etc... 
            //requete[0]=select [1]=eleve.nom ....[]
            if(isDataBaseExist(database)==false){    throw new RQTException("database "+getDatabase()+" n'existe pas"); }

            //select eleve.nom,eleve.nee,salle.numero,salle.ideleve  from     salle           join         eleve       on         eleve.ideleve=eleve.ideleve
            //[0]      [1]-----[2]---------------------------------[idfrom]---[idfrom+1]----[idfrom+2]------[idfrom+3]---[idfrom+4]-----[idfrom+5]
            String rqte=rqt.replaceAll(","," ");
            String [] requete=decompositionSimple(rqt);
            //---
            if(requete.length<8){ throw new RQTException("Syntaxe non valide"); } //car si il s'agit de jointure,nb mots requete>=8
            //----------------------------------------trouver position de "from"
            int idfrom=-1;
            for(int i=0;i<requete.length;i++){
                  if(requete[i].compareToIgnoreCase("from")==0){
                        idfrom=i;
                        i=requete.length;
                  }
            } if(idfrom==-1){ throw new RQTException("Syntaxe non valide ,mots \"from\" manquante"); }
              else if(requete[idfrom+2].compareToIgnoreCase("join")!=0){ throw new RQTException("Syntaxe non valide ,mots \"join\" manquante"); }
            //-----------------------------------------test si les table a join exist
            if(isTableExist(database,requete[idfrom+1])==false){    throw new RQTException("table "+requete[idfrom+1]+" n'existe pas"); }
            if(isTableExist(database, requete[idfrom+3])==false){    throw new RQTException("table "+requete[idfrom+3]+" n'existe pas"); }
            //------------------------------------------verifie si les colonne a afficher 
            Vector vAttribTab1=new Vector();
            Vector vAttribTab2=new Vector();
                  //--------------------
            Note note=new Note("database/"+database+"/"+requete[idfrom+1]+".txt");
            String allAttribT1=note.read().split("//")[0];
            String [] namesColT1=getNameColumn_TypeColumn(allAttribT1)[0];  
                  //--------------------
            note=new Note("database/"+database+"/"+requete[idfrom+3]+".txt");
            String allAttribT2=note.read().split("//")[0];
            String [] namesColT2=getNameColumn_TypeColumn(allAttribT1)[0];
                  //-------------------
            for(int i=1;i<idfrom;i++){   //verification et recuperation des colonnes selectionner
                  String [] tabANDcol=splitByRegexPoint(requete[i]);
                  if(tabANDcol.length!=2){ throw new RQTException("syntaxe non valide, corrigez comme: \"nomTable.nomColonne\" svp"); }
                  if(tabANDcol[0].compareToIgnoreCase(requete[idfrom+1])!=0 || tabANDcol[0].compareToIgnoreCase(requete[idfrom+3])!=0){ //si le table a selecter est en dehors des deux tables a joindre
                        throw new RQTException("table \""+tabANDcol[0]+"\""+"est different de\""+requete[idfrom+1]+"\" et \""+requete[idfrom+3]+"\"");
                  }
                  if(isvalideNameColumn(tabANDcol[1], namesColT1)==true && tabANDcol[0].compareToIgnoreCase(requete[idfrom+1])==0){ //tab1
                        vAttribTab1.add(tabANDcol[1]);
                  }else if(isvalideNameColumn(tabANDcol[1], namesColT2)==true  && tabANDcol[0].compareToIgnoreCase(requete[idfrom+3])==0){ //tab2
                        vAttribTab2.add(tabANDcol[1]);
                  }else{
                        throw new RQTException("colone \""+tabANDcol[1]+"\""+"inconnu");
                  }
            }
            String[] namesForJoin=requete[idfrom+5].split("=");//[0]=tab1.col  [1]=tab2.col
            if(namesForJoin.length!=2){ throw new RQTException("syntaxe des colonnes pour jointure non valide "); }
            String colForJoinT1="";
            String colForJoinT2="";
            for(int i=0;i<namesForJoin.length;i++){
                  String [] tabANDcol=splitByRegexPoint(namesForJoin[i]);
                  if(tabANDcol.length!=2){ throw new RQTException("syntaxe non valide, corrigez comme: \"nomTable.nomColonne\" svp"); }
                  if(tabANDcol[0].compareToIgnoreCase(requete[idfrom+1])!=0 || tabANDcol[0].compareToIgnoreCase(requete[idfrom+3])!=0){ //si le table a selecter est en dehors des deux tables a joindre
                        throw new RQTException("table \""+tabANDcol[0]+"\""+"est different de\""+requete[idfrom+1]+"\" et \""+requete[idfrom+3]+"\"");
                  }
                  if(isvalideNameColumn(tabANDcol[1], namesColT1)==true && tabANDcol[0].compareToIgnoreCase(requete[idfrom+1])==0){ //tab1
                        colForJoinT1=tabANDcol[1];
                  }else if(isvalideNameColumn(tabANDcol[1], namesColT2)==true  && tabANDcol[0].compareToIgnoreCase(requete[idfrom+3])==0){ //tab2
                        colForJoinT2=tabANDcol[1];
                  }else{
                        throw new RQTException("colone \""+tabANDcol[1]+"\""+"inconnu");
                  }
            }
            String[] attribT1=new String[vAttribTab1.size()]; 
            String[] attribT2=new String[vAttribTab2.size()];
            for(int i=0;i<vAttribTab1.size();i++){ attribT1[i]=(String)vAttribTab1.elementAt(i); }
            for(int i=0;i<vAttribTab2.size();i++){ attribT2[i]=(String)vAttribTab2.elementAt(i); }
            //projection(String[] headValues,String[][] values,String[] namesShow )
            String[][] T1=projection(namesColT1, select("select * from "+requete[idfrom+1]), attribT1);
            String[][] T2=projection(namesColT2, select("select * from "+requete[idfrom+3]), attribT2);
            //jointure(String[] Rattribu,String[] Sattribu,String RnameAttribuForJoin,String SnameAttribuForJoin,String[][] R, String[][] S)
            String[][] joint=jointure(attribT1 , attribT2 , colForJoinT1, colForJoinT2, T1, T2);
            return joint;
      }
//-----------------------------------------------------------------------------------------------------------------Division
      public String[][] division(String[] attribuR, String[][] R, String[] attribuS, String[][] S)throws Exception{
            String[][] aR=new String[attribuR.length][1];
            for(int i=0;i<attribuR.length;i++){
                  aR[i][0]=attribuR[i];
            }
            String[][] aS=new String[attribuS.length][1];
            for(int i=0;i<attribuS.length;i++){
                  aS[i][0]=attribuS[i];
            }
            //ampifanalana aloha  le atribue 
            String[][] leC=difference(aR,aS);
            String[] C=new String[leC.length];
            for(int i=0;i<leC.length;i++){
                  C[i]=leC[i][0];
            }
            //T1=PROc(R)
            String[][] T1=projection(attribuR, R, C);
            //T2=PROc((SxT1)-R)
                  //SxT1
                  String[][] prodCart=produitCartesien(S,T1);
                  //SxT1 - R
                  String[][] dif=difference(prodCart, R);
                  //T2
                  String[][] T2=projection(attribuR, dif, C);
            //division = T=T1-T2;
            String[][] T=difference(T1, T2);
            return T;
      }
//-----------------------------------------------------------------------------------------------------------------diviser
      public String[][] diviser(String rqt)throws Exception{
            //(selection)%%(selection)
            //(Selection)--(selection)--(selection)....
            //String rqte=decompositionSimple(rqt);
            String[]selections=rqt.split("%%");
            if(selections.length<2){  throw new RQTException("syntaxe pour operation Division non valide"); }
            String[] lesSelect=new String[selections.length];
            for(int i=0;i<selections.length;i++){
                  String select=selections[i].replaceAll(" ","");
                  //[0]="(" [1]="s" [2]="e" [3]="l" [4]="e" [5]="c" [6]="t" [7]="".....[select.length-1]=")"----> verifier ce syntaxe
                  if(select.substring(0, 7).compareToIgnoreCase("(select")!=0 || select.substring(select.length()-1, select.length()).compareToIgnoreCase(")")!=0){
                        throw new RQTException("syntaxe pour operation Division non valide");
                  }
                  lesSelect[i]=selections[i].replace('(',' ');
                  lesSelect[i]=lesSelect[i].replace(')',' ');
            }
            //ampidirina anaty vector le resultat aveo
            String[][] division=select(lesSelect[0]);
            String[] attribu1er=getHeadersSelect(lesSelect[0]);
            for(int i=1;i<lesSelect.length;i++){
                  //division(String[] attribuR, String[][] R, String[] attribuS, String[][] S)
                  division=division(attribu1er, division, getHeadersSelect(lesSelect[i]), select(lesSelect[i]));
                  attribu1er=getHeadersSelect(lesSelect[i]);
            }
            return division;
      }
//-----------------------------------------------------------------------------------------------------------------
      public void affiche(String[][] strs){
            if(strs!=null){ 
                  for(int i=0;i<strs.length;i++){
                        for(int i2=0;i2<strs[i].length;i2++){
                              System.out.print(strs[i][i2]+"|");
                        }
                        System.out.println("");
                  }
            }else{
                  System.out.println("null");
            }
      }
      public String[] getHeadersSelect(String rqt)throws Exception{
            String rqte=rqt.replace('(', ' ');
            rqte=rqt.replace(',', ' ');
            rqte=rqt.replace(')', ' ');
            String[] requete = decompositionSimple(rqte);
            if(requete[0].compareToIgnoreCase("show")==0 && requete[1].compareToIgnoreCase("databases")==0){
                  String[] header=new String[1];
                  header[0]="databases";
                  return header;
            }else if(requete[0].compareToIgnoreCase("show")==0 && requete[1].compareToIgnoreCase("tables")==0){
                  String[] header=new String[1];
                  header[0]="Tables of "+this.getDatabase();
                  return header;
            }else if(requete[0].compareToIgnoreCase("describe")==0 && requete[1].compareToIgnoreCase("table")==0){
                  String[] header={"column","type"};
                  return header;
            }
            int idFrom=-1;
            for(int i=0;i<requete.length;i++){
                  if(requete[i].compareToIgnoreCase("from")==0){
                        idFrom=i;
                        i=requete.length; //vao mahita tonga de ajanona ny boucle
                  }
            }
            //select * from ntable 1 2 3 4
            if( idFrom>1 && requete.length>3){
                  String[] rep=new String[idFrom-1];
                  if(requete[idFrom-1].compareTo("*")==0){
                        Note note=new Note("database/"+database+"/"+requete[idFrom+1]+".txt");
                        String[] data=note.read().split("//");
                        String nmtp=data[0];
                        rep=getNameColumn_TypeColumn(nmtp)[0];
                  }else{
                        for(int i=1;i<idFrom;i++){
                              rep[i-1]=requete[i];
                        }
                  }
            return rep;
            }
            return null;
      }
      public String[] getAllHeadersSelect(String  rqt)throws Exception{
            String rqte=rqt.replace('(', ' ');
            rqte=rqt.replace(',', ' ');
            rqte=rqt.replace(')', ' ');
            String[] lesSelects=rqte.split("--");
            if(lesSelects.length<=1){
                 return getHeadersSelect(rqte);
            }
            //separer tous les selects
            Vector vect=new Vector();
            for(int i=0;i<lesSelects.length;i++){
                  String[] lesSelects2=lesSelects[i].split("%%");
                  for(int u=0;u<lesSelects2.length;u++){
                        vect.add(lesSelects2[u]);
                  }
            }
            Vector vHeaders=new Vector();
            for(int i=0;i<vect.size();i++){
                  String[] lstH=getAllHeadersSelect( vect.elementAt(i).toString());
                  for(int u=0;u<lstH.length;u++){
                        vHeaders.add(lstH[u]);
                  }
            }
            String[] headers=new String[vHeaders.size()];
            for(int i=0;i<vHeaders.size();i++){
                  headers[i]=vHeaders.elementAt(i).toString();
            }
            
            return headers;
      }
      //public 
      public String[][] requeteTraitement(String rqt)throws Exception{
            String rqte=rqt.replace('(', ' ');
            rqte=rqt.replace(',', ' ');
            rqte=rqt.replace(')', ' ');
            String[] requete = decompositionSimple(rqte);
            String[][] req=null;
            if(requete[0].compareToIgnoreCase("show")==0 && requete[1].compareToIgnoreCase("databases")==0){
                  req=showDatabases(rqt);
            }else if(requete[0].compareToIgnoreCase("use")==0){
                  useDatabase(rqt);
            }else if(requete[0].compareToIgnoreCase("create")==0 && requete[1].compareToIgnoreCase("database")==0){
                  createDatabase(rqt);
            }else if(requete[0].compareToIgnoreCase("show")==0 && requete[1].compareToIgnoreCase("tables")==0){
                  req=showTables(rqt);
            }else if(requete[0].compareToIgnoreCase("describe")==0 && requete[1].compareToIgnoreCase("table")==0){
                  req=describeTable(rqt);
            }else if(requete[0].compareToIgnoreCase("create")==0 && requete[1].compareToIgnoreCase("table")==0){
                  createTable(rqt);
            }else if(requete[0].compareToIgnoreCase("insert")==0){
                  insert(rqt);
            }else if(rqt.toUpperCase().contains(" JOIN ")==true){
                  req=join(rqt);
            }else if(rqt.contains(" %% ")==true){
                  req=diviser(rqt);
                  affiche(req);
            }else if(rqt.contains("--")){
                  req=differencier(rqt);
            }else if(requete[0].compareToIgnoreCase("select")==0){
                  req=select(rqt);
            }else {
                  throw new RQTException("Syntaxe non valide");
            }
            return req;
      }

      
}