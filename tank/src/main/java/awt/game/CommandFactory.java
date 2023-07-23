package awt.game;

import awt.game.command.Command;
import awt.game.command.ConsumerCommand;
import awt.game.command.FunctionCommand;
import awt.model.domain.Bullet;
import awt.model.domain.Element;
import awt.model.domain.Tank;
import awt.proto.enums.*;

import java.util.*;
import java.util.function.Function;

/**
 * @ClassName Test
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/5 5:07 下午
 * @Version 1.0
 **/
@SuppressWarnings("rawtypes")
public class CommandFactory {

    public final Vector<Command> commandQueue = new Vector<>();

    public final Queue<Command> cache = new LinkedList<>();


    private GameContext gameContext;

    public CommandFactory() {
    }

    public CommandFactory(GameContext gameContext) {

        this.gameContext = gameContext;
    }

    /**
     * 添加转方向命令
     *
     * @param element
     * @param direct
     */
    public static Command createRedirectCommand(Element element, Direct direct) {
        return new RedirectCommand(element, direct);
    }

    /**
     * @param element
     * @param elementAction 操作指令 默认只执行一次
     * @param newData       更新的数值
     */
    public static Command createElementUpdateCommand(Element element, ElementAction elementAction, int count, int newData) {
        return new ElementUpdateCommand(element, elementAction, count, newData);
    }


    /**
     * 添加坦克命令
     *
     * @param tankAction
     * @param times
     */
    public static Command createTankCommand(Tank tank, TankAction tankAction, int times) {
        switch (tankAction) {
            case FIRE:
                return new TankFireCommand(tank, times);
            case TANK_MOVE:
                return new ElementUpdateCommand(tank,ElementAction.MOVE, times,0);
            default:
                return new TankCommand(tank, tankAction, times);

        }


    }

    /**
     * 1、 优化所有命令行
     * 2、输出可以执行的一桢命令
     *
     * @return
     */
    public CommandFactory optimizeCommand() throws Exception {

        //一个桢
        CommandFactory zhen = new CommandFactory();
        Iterator<Command> iterator = this.commandQueue.iterator();
        while (iterator.hasNext()) {
            Command command = iterator.next();
            switch (command.status()) {
                case INTERRUPT:
                    if (command.getCount() > 0) {
                        command.setNeedCount(command.getCount());
                        command.setEndTimestamp(new Date().getTime());
                        iterator.remove();
                        //输出可执行的桢
                        zhen.addCommand(command, true);
                    } else {
                        command.failed();
                    }
                    break;
                case ACCEPT:
                case WAIT:
                    if (command.getCount() > 0) {
                        //生成新的命令
                        Command copy = command.copy();
                        copy.setNeedCount(command.getNeedCount() - command.getCount());
                        copy.setCount(0);
                        copy.setStatus(Status.ACCEPT);
                        //将新的命令加入缓存，下一轮
                        cache.add(copy);

                        //将旧的命令修改成成功
                        command.setNeedCount(command.getCount());
                        command.setEndTimestamp(new Date().getTime());
                        command.setStatus(Status.SUCCESS);
                        iterator.remove();
                        //输出可执行的桢
                        zhen.addCommand(command, true);
                    }
                    break;
                case SUCCESS:
                    //输出可执行的桢
                    zhen.addCommand(command, true);
                    iterator.remove();
                    break;
                case FAILED:
                    iterator.remove();
                    break;
            }

        }
//        System.out.println("==================桢优化结束==============");
        return zhen;
    }

    /**
     * 将命令行转化为proto
     *
     * @return
     */
    public static Vector<awt.proto.command.Command> toProtoCommands(Vector<Command> commandQueue) {
        Vector<awt.proto.command.Command> protoCommands = new Vector<>();
        for (Command command : commandQueue) {
            awt.proto.command.Command.Builder builder = awt.proto.command.Command.newBuilder();
            if (command instanceof CommandFactory.RedirectCommand) {
                CommandFactory.RedirectCommand command1 = (CommandFactory.RedirectCommand) command;
                builder.setDirect(command1.getDirect())
                        .setElementId(command1.getT().getIncreaseId())
                        .setCommandType(CommandType.REDIRECT_COMMAND);
            } else if (command instanceof CommandFactory.ElementUpdateCommand) {
                CommandFactory.ElementUpdateCommand command1 = (CommandFactory.ElementUpdateCommand) command;
                Element t = (command1.getT());
                builder.setElementAction(command1.getElementAction())
                        .setNewData(command1.getNewData())
                        .setElementId(t.getIncreaseId())
                        .setCommandType(CommandType.ELEMENT_UPDATE_COMMAND);
            } else if (command instanceof CommandFactory.TankCommand) {
                CommandFactory.TankCommand command1 = (CommandFactory.TankCommand) command;
                builder.setTankAction(command1.getTankAction())
                        .setElementId(command1.getT().getIncreaseId())
                        .setCommandType(CommandType.TANK_COMMAND);

            } else if (command instanceof CommandFactory.TankFireCommand) {
                CommandFactory.TankFireCommand command1 = (CommandFactory.TankFireCommand) command;
                builder.setElementId(command1.getT().getIncreaseId())
                        .setCommandType(CommandType.TANK_FIRE_COMMAND);

            }


            awt.proto.command.Command c = builder
                    .setCount(0)
                    .setNeedCount(command.getNeedCount())
                    .setStatus(command.getStatus())
                    .setStartTimestamp(command.getStartTimestamp())
                    .setEndTimestamp(command.getEndTimestamp())
                    .build();
            protoCommands.add(c);
        }
        return protoCommands;
    }


    /**
     * 将客户端发送的命令加入到服务端
     *
     * @param commandQueue
     */
    public  Vector<Command> parseProtoCommands(Collection<awt.proto.command.Command> commandQueue) {
        Vector<Command> commands = new Vector<>();
        for (awt.proto.command.Command command : commandQueue) {
            Command commandV2=null;
            System.out.println(command);
            switch (command.getCommandType()) {
                case TANK_FIRE_COMMAND:
                case TANK_COMMAND:
                    Tank tank = (Tank) gameContext.getGameMapContext().getElementByIncreaseId(command.getElementId());
                    commandV2 = createTankCommand(tank, command.getTankAction(), command.getNeedCount());
                    break;
                case ELEMENT_UPDATE_COMMAND:
                    Element e = gameContext.getGameMapContext().getElementByIncreaseId(command.getElementId());
                    commandV2 = createElementUpdateCommand(e, command.getElementAction(), command.getNeedCount(), command.getNewData());
                    break;

            }
            if(commandV2!=null)
            {  commands.add(commandV2);}
        }
     return commands;
    }


    /**
     * 将上一轮缓存清除并放入主队列
     */
    public void clearCache() {
        while (cache.size() != 0) {
            //将上一轮新生成的命令放入队列
            commandQueue.add(cache.poll());
        }
    }

    public void addCommand(Command command) {
        addCommand(command, false);
    }

    public void addCommands(Collection<Command> command){
        for (Command commandV2 : command) {
            addCommand(commandV2);
        }
    }

    public void addCommand(Command command, boolean isNeedCheckMainFunction) {
        if (isNeedCheckMainFunction ) {
            if(command!=null && command.isMainCommand()){
                commandQueue.add(command);
            }
        } else if(command!=null) {
            commandQueue.add(command);
        }

    }

    public int size() {
        return commandQueue.size();
    }


    public void clearCommandQueue() {
        this.commandQueue.clear();
    }


    /**
     * 坦克开火命令
     */
    static class TankFireCommand extends FunctionCommand<Tank, ElementUpdateCommand> {
        private final TankAction tankAction = TankAction.FIRE;
        public TankFireCommand(Tank tank, int times) {
            super(tank, new Function<Tank, ElementUpdateCommand>() {
                @Override
                public ElementUpdateCommand apply(Tank tank) {
                    Bullet fire = tank.fire();
                    if(fire!=null){
                        ElementUpdateCommand command = new ElementUpdateCommand(fire, ElementAction.MOVE, 200, 0);
                        command.setMainCommand(false);
                        return command;
                    }
                    return null;

                }
            }, times);
        }
        @Override
        public TankFireCommand copy()  {
            TankFireCommand command = new TankFireCommand(getT(), getNeedCount());
            copy(command);
            return command;
        }

        public TankAction getTankAction() {
            return tankAction;
        }
    }

    /**
     * 其他坦克命令
     */
    static class TankCommand extends ConsumerCommand<Tank> {
        private final TankAction tankAction;

        public TankCommand(Tank tank, TankAction tankAction, int times) {
            super(tank, t -> t.tankAction(tankAction), times);
            this.tankAction = tankAction;
        }

        @Override
        public TankCommand copy()  {
            TankCommand tankCommand = new TankCommand(getT(), tankAction, getNeedCount());
            copy(tankCommand);
            return tankCommand;
        }

        public TankAction getTankAction() {
            return tankAction;
        }
    }

    /**
     * 元素属性更新命令
     */
    static class ElementUpdateCommand extends ConsumerCommand<Element> {
        private final ElementAction elementAction;
        private final int newData;

        public ElementUpdateCommand(Element t, ElementAction elementAction, int times, int newData)  {
            super(t, e -> e.action(elementAction, newData), times);
            this.elementAction = elementAction;
            this.newData = newData;
        }

        @Override
        public ElementUpdateCommand copy() {
            ElementUpdateCommand command = new ElementUpdateCommand(getT(), elementAction, getNeedCount(), newData);
            copy(command);
            return command;
        }

        public ElementAction getElementAction() {
            return elementAction;
        }

        public int getNewData() {
            return newData;
        }
    }

    /**
     * 转向命令
     */
    static class RedirectCommand extends ConsumerCommand<Element> {
        private final Direct direct;

        public RedirectCommand(Element element, Direct direct)  {
            super(element, e -> e.setDirect(direct), 1);
            this.direct = direct;
        }

        @Override
        public RedirectCommand copy()  {
            RedirectCommand command = new RedirectCommand(getT(), direct);
            copy(command);
            return command;
        }

        public Direct getDirect() {
            return direct;
        }
    }
}
