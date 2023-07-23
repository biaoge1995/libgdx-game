package hhs.mygame.text1;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import hhs.mygame.text1.games.MyGame;
 
public class MainActivity extends AndroidApplication { 
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration conf = new AndroidApplicationConfiguration();
		initialize(new MyGame(),conf);
    }
	
} 
