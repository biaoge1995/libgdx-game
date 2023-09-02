public class TestJNI {
    public native void displayHelloWorld();


    static {
        //设置查找路径为当前项目路径
//        System.setProperty("java.library.path", "/Users/chenbiao/libgdx-game/game-test/core/src/main/resources");
//        System.out.println(System.getProperty("java.library.path"));
        //加载动态库的名称

    }


    public static void main(String[] args) {

        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libhello.jnilib");
//        System.loadLibrary("hello");
        new TestJNI().displayHelloWorld();
    }
}
