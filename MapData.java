import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_NONE   = 0;
    public static final int TYPE_WALL   = 1;
    public static final int TYPE_OTHERS = 2;
    public static final int TYPE_ITEM = 3;
    public static final int TYPE_ITEM2 = 4;
    public static final int TYPE_GOAL = 5;
    private static String mapImageFiles[] = {
        "png/SPACE.png",
        "png/WALL.png",
        "png/SPACE.png",
        "png/ITEM.png",
        "png/ITEM2.png",
        "png/GOALFLAG.png"
    };

    private Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width;
    private int height;
    private int item_count;
    public static enum Type{
        tom,
        jerry
    };

    MapData(int x, int y, int item){
        mapImages     = new Image[mapImageFiles.length];
        mapImageViews = new ImageView[y][x];
        for (int i=0; i<mapImageFiles.length; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width  = x;
        height = y;
        maps = new int[y][x];
        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        int i=((int)(Math.random()*100)%3==0)?((int)(Math.random()*100)%3==0)?3:4:5;
        //99, random
        i = (Math.abs(item) == 99)? i:item;
        if(item>0){
            setItem(i,Type.tom);
            setGoal(19,13);
        }
        else{
            setItem(Math.abs(i),Type.jerry);
        }
        setImageViews();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }

    public void setMap(int x, int y, int type){
        if (x < 1 || width <= x-1 || y < 1 || height <= y-1) {
            return;
        }
        maps[y][x] = type;
    }

    public void setImageViews() {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public void fillMap(int type){
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                maps[y][x] = type;
            }
        }
    }

    public void digMap(int x, int y){
        setMap(x, y, MapData.TYPE_NONE);
        int[][] dl = {{0,1},{0,-1},{-1,0},{1,0}};
        int[] tmp;

        for (int i=0; i<dl.length; i++) {
            int r = (int)(Math.random()*dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i=0; i<dl.length; i++){
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x+dx*2, y+dy*2) == MapData.TYPE_WALL){
                setMap(x+dx, y+dy, MapData.TYPE_NONE);
                digMap(x+dx*2, y+dy*2);

            }
        }
    }

    //Making goal at x,y
    public void setGoal(int x,int y){
        setMap(x,y,MapData.TYPE_GOAL);
    }

    public void setItem(int i, Type t){
        item_count = 0;
        while(item_count<i){
            for(int y = 0;y<getHeight();y++){
                for(int x = 0;x<getWidth();x++){
                    if(getMap(x,y)==MapData.TYPE_NONE&&(int)(Math.random()*100)%40==0&&item_count<i&&(x!=1&&y!=1)&&(x!=19&&y!=13)){
                        item_count++;
                        if(t==Type.tom){
                            setMap(x,y,MapData.TYPE_ITEM);
                        }else{
                            setMap(x,y,MapData.TYPE_ITEM2);
                        }
                    }
                    //System.out.println(count);
                }
            }
        }
    }
    public int getItem(){
        return item_count;
    }

    public void printMap(){
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                if (getMap(x,y) == MapData.TYPE_WALL){
                    System.out.print("++");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
    }
}
