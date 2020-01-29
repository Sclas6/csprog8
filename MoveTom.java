import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MoveTom extends MoveChara{
    private String pngPathBefore = "png/tom/tom";

    MoveTom(int startX, int startY, MapData mapData){
        super(startX, startY, mapData);
        super.mapData = mapData;
        super.pngPathBefore = this.pngPathBefore;
        posX = startX;
        posY = startY;

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
        setCharaDir(TYPE_DOWN);
    }
}