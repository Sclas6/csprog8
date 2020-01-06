import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;

public class MoveChara {
    public static final int TYPE_DOWN  = 0;
    public static final int TYPE_LEFT  = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP    = 3;

    protected final String[] dirStrings  = { "d", "l", "r", "u" };
    protected final String[] kindStrings = { "1", "2", "3" };
    protected String pngPathBefore = "png/neko";
    protected final String pngPathAfter  = ".png";

    protected int posX;
    protected int posY;

    protected MapData mapData;

    protected Image[][] charaImages;
    protected ImageView[] charaImageViews;
    protected ImageAnimation[] charaImageAnimations;

    private int count   = 0;
    private int diffx   = 1;
    private int charaDir;
    public static int item_count = 0;
    public boolean isgoal = false;
    public static String message="アイテム数: 0";

    MoveChara(int startX, int startY, MapData mapData){
        this.mapData = mapData;

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i=0; i<4; i++) {
            charaImages[i] = new Image[3];
            for (int j=0; j<3; j++) {
                charaImages[i][j] = new Image(pngPathBefore + dirStrings[i] + kindStrings[j] + pngPathAfter);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation( charaImageViews[i], charaImages[i] );
        }

        posX = startX;
        posY = startY;

        setCharaDir(TYPE_DOWN);
    }
    MoveChara(){
        System.out.print("a");
    }

    public String getMessage(){
        return message;
    }

    public void changeCount(){
        count = count + diffx;
        if (count > 2) {
            count = 1;
            diffx = -1;
        } else if (count < 0){
            count = 1;
            diffx = 1;
        }
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public void setCharaDir(int cd){
        charaDir = cd;
        for (int i=0; i<4; i++) {
            if (i == charaDir) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    public static int getItemCount(){
        return item_count;
    }
    public static void setItem(int i){
        item_count = i;
    }
    //With Items, chara can enter goal flag
    public boolean canGoal(){
        if (getItemCount() == mapData.getItem()){
            return true;
        }
        else{
            return false;
        }
    }
    //return if chara is goal
    public boolean isGoal(MapData m){
        for(int y=0; y<m.getHeight(); y++){
            for(int x=0; x<m.getWidth(); x++){
                if(m.getMap(getPosX(), getPosY()) == MapData.TYPE_GOAL && canGoal() == true && isgoal == false){
                    System.out.print("GOAL");
                    isgoal = true;
                }
            }
        }
        return (isgoal==true)?true:false;
    }
    //Without Items, Can't Enter Goal 
    public boolean canMove(int dx, int dy){
        if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_WALL){
            return false;
        } else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_NONE){
            return true;
        } else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_GOAL){
            //return (canGoal()==true)?true:false;
            return true;
        } else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_ITEM){
        return true;
        }
        return false;
    }

    public boolean move(int dx, int dy){
        if (canMove(dx,dy)&&isGoal(mapData)==false){
            posX += dx;
            posY += dy;
            if(mapData.getMap(posX,posY)==MapData.TYPE_ITEM){
                item_count++;
                mapData.setMap(posX,posY,MapData.TYPE_NONE);
                mapData.setImageViews();
                message = "アイテム数: "+Integer.toString(MoveChara.item_count);
            }
            return true;
        }else if(mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_GOAL&&canGoal()==false){
            message = "アイテムが足りません!";
            return false;
        }
        else {
            return false;
        }
    }

    public ImageView getCharaImageView(){
        return charaImageViews[charaDir];
    }

    public class ImageAnimation extends AnimationTimer {
        // アニメーション対象ノード
        private ImageView   charaView     = null;
        private Image[]     charaImages   = null;
        private int         index       = 0;

        private long        duration    = 500 * 1000000L;   // 500[ms]
        private long        startTime   = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation( ImageView charaView , Image[] images ) {
            this.charaView   = charaView;
            this.charaImages = images;
            this.index      = 0;
        }

        @Override
        public void handle( long now ) {
            if( startTime == 0 ){ startTime = now; }

            preCount = count;
            count  = ( now - startTime ) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if ( index < 0 || 2 < index ) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
