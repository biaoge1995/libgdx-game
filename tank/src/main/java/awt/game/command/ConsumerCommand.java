package awt.game.command;

import java.util.function.Consumer;

/**
 * @ClassName ConsumerCommand
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/5 11:30 下午
 * @Version 1.0
 **/
public class ConsumerCommand<T> extends Command<T,Consumer<T>> {

    public ConsumerCommand(T t,Consumer<T> consumer, int times) {
        super(consumer,t,times);
    }

    @Override
    Object function(Object... obj) {
        getFunction().accept(getT());
        return null;
    }

    @Override
    public ConsumerCommand<T> copy()  {
        ConsumerCommand<T> command = new ConsumerCommand<>(getT(),getFunction(), this.getCount());
        this.copy(command);
        return command;
    }



}