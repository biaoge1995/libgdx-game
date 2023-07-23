package awt.game.command;

import java.util.function.Function;

/**
 * @ClassName FunctionCommand
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/5 11:31 下午
 * @Version 1.0
 **/
public class FunctionCommand<T, R> extends Command<T,Function<T, R>> {

    public FunctionCommand(T t, Function<T, R> function, int times)  {
        super(function,t,times);
    }

    @Override
    R function(Object... obj) {
        return getFunction().apply(getT());
    }

    @Override
    public FunctionCommand<T, R>  copy()  {
        FunctionCommand<T,R> command = new FunctionCommand<>(getT(),getFunction(), this.getCount());
        this.copy(command);
        return command;
    }



}
