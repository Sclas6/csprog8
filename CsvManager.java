import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CsvManager{

   public static void exportCsv(){
     try {
           // outputAction
           FileWriter f = new FileWriter("csv/ScoreList.csv", true);
           PrintWriter p = new PrintWriter(new BufferedWriter(f));
           FileInputStream fi = new FileInputStream("csv/ScoreList.csv");
           InputStreamReader is = new  InputStreamReader(fi);
           BufferedReader br = new BufferedReader(is);


           //MapGameController mgc = new MapGameController();//

           //set contents  //
              p.print(MapGameController.getScoreData());
              System.out.println(MapGameController.getScoreData());
              p.println();
          // close file  //
              p.close();
              System.out.println("ExportedCSV");

           String line;
           int i = 0;


           FileWriter fw = new FileWriter("csv/Ranking.csv" , false);
           PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

          //readLine //
           Map<Integer, String> map = new TreeMap<>(Comparator.reverseOrder());
           while((line = br.readLine()) != null){
              String[] temp  = line.split(",");
              map.put(Integer.parseInt(temp[1]),line);
              i++;
          }
          map.forEach((key, value) -> pw.println(value));
          pw.close();
          for(int j = 0; j < i; j++){

          }
       }catch (IOException ex) {
          ex.printStackTrace();
      }
   }
   public static String getRanking(){
      String str = "";
      try{
         FileInputStream fi = new FileInputStream("csv/Ranking.csv");
         InputStreamReader is = new InputStreamReader(fi);
         BufferedReader br = new BufferedReader(is);
         int i = 0;
         String line;
         while((line = br.readLine()) != null && i<5){
             str += line;
             str += "\n";
             i++;
         }
         br.close();
     }catch(IOException ex){
         ex.printStackTrace();
     }
     return str;
   }
}
