import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestJavaClass implements Runnable{

    public static  int Count=0;
    public static  int Count2=0;
    public static final String Name = "表哥";
    public boolean isUseStaticMethod = false;

    static {
        System.out.println("TestJavaClass 被加载");
    }

    public  synchronized void staticIncrease() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Count++;
    }
    public  synchronized void increase() {
        Count++;
    }

    public final void finalMethod(){
        System.out.println("final方法");
    }
    private void privateMethod(){
        System.out.println("private方法");
    }

    public static void createFather() {
//        System.out.println(this);
        System.out.println("静态方法");

    }

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {

//        Class<?> testJavaClass1 = Class.forName("TestJavaClass");
//
//        Method[] declaredMethods = testJavaClass1.getDeclaredMethods();
//        for (Method declaredMethod : declaredMethods) {
//            System.out.println(""+declaredMethod.getName() + ",入参数量" + declaredMethod.getParameterCount() + "，返回值类型" + declaredMethod.getReturnType());
//            if(declaredMethod.getName().equals("increase")){
//                declaredMethod.invoke(null);
//            }
//        }
//
//        Class<?> testJavaClass2 = Class.forName("TestJavaClass");
//        System.out.println(testJavaClass2.getName());

        TestJavaClass testJavaClass = new TestJavaClass();

        Thread thread = new Thread(testJavaClass);
        Thread thread1 = new Thread(testJavaClass);

        thread.start();
        thread1.start();


        thread.join();
        thread1.join();

        System.out.println(testJavaClass.Count);


        //虚方法
//        testJavaClass.say(0.5);
//        //非虚方法
//        testJavaClass.finalMethod();
//        testJavaClass.privateMethod();
//        Father father = createFather(Name, Count);


    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            int i1 = (int) (Math.random() * 10);

            if(i1%2==0){

                staticIncrease();
                System.out.println(Thread.currentThread().getId()+"-static"+Count);
            }else {
                increase();
                System.out.println(Thread.currentThread().getId()+"nonStatic"+Count);
            }


        }
    }
}
