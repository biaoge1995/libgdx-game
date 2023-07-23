package com.libgdxloadingscreen;

//import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * @author Mats Svensson
 */
public class PackSkins {

    public static void main(String[] args) {

        TexturePacker.process( "Main/workfiles/finished", "Android/assets/data", "loading.pack");
    }
}
