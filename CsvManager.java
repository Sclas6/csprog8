import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CsvManager{



    public static void exportCsv(){
        try {
            // 出力ファイルの作成
            FileWriter f = new FileWriter("csv/ScoreList.csv", true);
            PrintWriter p = new PrintWriter(new BufferedWriter(f));

            MapGameController cont = new MapGameController();
            
            FileInputStream fi = new FileInputStream("csv/ScoreList.csv");
            InputStreamReader is = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(is);
            // 内容をセットする
                p.print(cont.getScoreData());
                p.println();
            // ファイルに書き出し閉じる
            p.close();
            System.out.println("ExportedCSV");

            String line;
            int i = 0;

            FileWriter f2 = new FileWriter("csv/Ranking.csv", false);
            PrintWriter p2 = new PrintWriter(new BufferedWriter(f2));

            //行数のロード
            Map<Integer,String> map = new TreeMap<>(Comparator.reverseOrder());
            while((line = br.readLine()) != null){
                String[] temp  = line.split(",");
                map.put(Integer.parseInt(temp[1]),line);
                i++;
            }
            map.forEach((key,value) -> p2.println(value));
            p2.close();
            for(int j=0;j<i;j++){

            }
        } catch (IOException ex) {
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
            while((line = br.readLine()) != null && i<10){
                str += line;
                str+="\n";
                i++;
            }
            br.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return str;
    }
}