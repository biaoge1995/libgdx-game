package org.cbzmq.game.proto;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;

/**
 * @ClassName DemoAction
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/17 6:01 下午
 * @Version 1.0
 **/

@ActionController(1)
public class DemoAction {
    @ActionMethod(0)
    public HelloReq here(HelloReq helloReq){
        HelloReq newHelloReq = new HelloReq();
        newHelloReq.name = helloReq.name + ", I'm here ";
        return newHelloReq;
    }
}
