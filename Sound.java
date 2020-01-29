import java.io.File;
import java.util.HashMap;

import javafx.scene.media.AudioClip;

public class Sound {
    AudioClip mediaPlayer;

    enum Type {
        MenuBgm, GameBgm, Button, Dig, Damage, GameOver
    }
    enum PlayType {
        BGM, SE
    }

    private static HashMap<Type, AudioClip> audioClips = new HashMap<Type, AudioClip>();

	Sound()
	{
		loadSound(Type.MenuBgm, PlayType.SE, "sounds/menu_bgm.wav");
		loadSound(Type.GameBgm, PlayType.BGM, "sounds/game_bgm.wav");
		loadSound(Type.Button, PlayType.SE, "sounds/button_shot.wav");
		loadSound(Type.Dig, PlayType.SE, "sounds/dig.wav");
		loadSound(Type.Damage, PlayType.SE, "sounds/damage.wav");
		loadSound(Type.GameOver, PlayType.SE, "sounds/gameover.wav");
    }
    private void loadSound(Type type, PlayType pt, String filePath)
	{
        if(!audioClips.containsKey(type)){
            audioClips.put(type, new AudioClip(new File(filePath).toURI().toString()));
            switch(pt){
			case BGM:
                audioClips.get(type).setCycleCount(-1);
				break;
			case SE:
                audioClips.get(type).setCycleCount(1);
				break;
		    }
            System.out.println("Loaded '" + filePath + "'");
        }
	}
    public void play(Type type)
    {
        stop(type);
		audioClips.get(type).play();
    }

    public void stop(Type type)
    {
		audioClips.get(type).stop();
    }
}
