import java.net.URL;
import java.nio.channels.FileChannel.MapMode;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public MapData mapData2;
    public MoveChara chara2;

    public GridPane mapGrid;
    public ImageView[] mapImageViews;
    public ImageView[] mapImageViews2;
    public Label label1;
    public boolean map2 = false;
    public int item_count = 0;
//    public Group[] mapGroups;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21,15);
        chara = new MoveChara(1,1,mapData);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        mapPrint(chara, mapData);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        mapImageViews = new ImageView[m.getHeight()*m.getWidth()];
        for(int y=0; y<m.getHeight(); y++){
            for(int x=0; x<m.getWidth(); x++){
                int index = y*m.getWidth() + x;
                //added
                mapImageViews[index] = m.getImageView(x,y);
                if (x==cx && y==cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
    }

    public void getItemAction(MoveChara c){
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                if(mapData.getMap(c.getPosX(), c.getPosY()) == MapData.TYPE_ITEM){
                    mapData.setMap(c.getPosX(), c.getPosY(), MapData.TYPE_NONE);
                    mapData.setImageViews();
                    item_count++;
                    label1.setText(Integer.toString(getItemCount()));
                }
            }
        }
    }
    public int getItemCount(){
        return item_count;
    }

    public void func1ButtonAction(ActionEvent event) { 
        outputAction("RESET");
        mapData = new MapData(21,15);
        chara = new MoveChara(1, 1, mapData);
        mapPrint(chara, mapData);
        map2 = false;
    }
    public void func2ButtonAction(ActionEvent event) {
        mapData2 = new MapData(23,17);
        chara2 = new MoveChara(1,1,mapData2);
        mapPrint(chara2,mapData2);
        map2 = true;
    }
    public void func3ButtonAction(ActionEvent event) {
        mapPrint(chara,mapData);
        map2 = false;
    }
    public void func4ButtonAction(ActionEvent event) {
        mapPrint(chara2,mapData2);
        map2 = true;
    }

    public void keyAction(KeyEvent event){
        KeyCode key = event.getCode();
        if (key == KeyCode.DOWN){
            downButtonAction();
        }else if (key == KeyCode.RIGHT){
            rightButtonAction();
        }else if (key == KeyCode.LEFT){
            leftButtonAction();
        }else if (key == KeyCode.UP){
            upButtonAction();
        }
        
    }

    public void outputAction(String actionString) {
        System.out.println("Select Action: " + actionString);
    }

    public void downButtonAction(){
        outputAction("DOWN");
        if(map2 == false){
            chara.setCharaDir(MoveChara.TYPE_DOWN);
            chara.move(0, 1);
            getItemAction(chara);
            mapPrint(chara, mapData);
        }
        else{
            chara2.setCharaDir(MoveChara.TYPE_DOWN);
            chara2.move(0, 1);
            getItemAction(chara2);
            mapPrint(chara2, mapData2);
        }
    }
    public void downButtonAction(ActionEvent event) {
        downButtonAction();
    }

    public void rightButtonAction(){
        outputAction("RIGHT");
        chara.setCharaDir(MoveChara.TYPE_RIGHT);
        chara.move( 1, 0);
        getItemAction(chara);
        mapPrint(chara, mapData);
    }
    public void rightButtonAction(ActionEvent event) {
        rightButtonAction();
    }
    public void leftButtonAction(){
        outputAction("LEFT");
        chara.setCharaDir(MoveChara.TYPE_LEFT);
        chara.move( -1, 0);
        getItemAction(chara);
        mapPrint(chara, mapData);
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }
    public void upButtonAction(){
        outputAction("UP");
        chara.setCharaDir(MoveChara.TYPE_UP);
        chara.move( 0, -1);
        getItemAction(chara);
        mapPrint(chara, mapData);
    }
    public void upButtonAction(ActionEvent event) {
        upButtonAction();
    }
}
