package awt.game;

import awt.model.domain.Element;
import awt.model.domain.Player;
import awt.model.domain.Tank;
import awt.proto.Msg;
import awt.proto.command.Command;
import awt.proto.enums.MsgAction;
import awt.proto.enums.TankAction;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.SocketUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName GameClient
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/3 11:15 下午
 * @Version 1.0
 **/
public class GameClient {

    private Player player;
    private Game game;
    private Channel channel;
    private String serverIp;
    private int serverPort;
    private CommandFactory commandFactory;

    public GameClient(Game game, String serverIp, int serverPort) throws Exception {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.game = game;
        this.player = new Player("一路花开");
        this.commandFactory = new CommandFactory(game.gameContext());
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Game Game() {
        return game;
    }

    public Player Player() {
        return player;
    }

    public void sendMsg(Msg msg) throws InterruptedException {
        channel.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(msg.toByteArray()),
                SocketUtils.socketAddress(serverIp, serverPort))).sync();
        System.out.println("发送消息"+msg);
    }

    public void acceptMsg(Msg msg) throws Exception {
        switch (msg.getHeader()) {
            case LOGIN:
            case GAME_JOIN:
                awt.proto.Player player = msg.getPlayer();
                this.player.setId(player.getId());
                break;
            case BROADCAST_GAME_MAP:
                List<awt.proto.Element> elementsList = msg.getElementsList();
                List<Command> commandsList = msg.getCommandsList();
                //先将客户端设置为暂停
                game.syncFromProto(commandsList, elementsList);
        }
    }

    public void userAction(MsgAction msgAction) throws Exception {
        switch (msgAction) {
            case LOGIN:
                game.loading();
                break;
            case GAME_JOIN:
                awt.proto.Player.Builder player = awt.proto.Player.newBuilder().setName(this.player.getName());
                Msg msg = Msg.newBuilder()
                        .setHeader(MsgAction.GAME_JOIN)
                        .setPlayer(player)
                        .setTimeStamp(new Date().getTime())
                        .build();
                sendMsg(msg);
                break;
        }
    }

    /**
     * 坦克移动,开火,停止
     *
     * @param tankAction
     */
    public void TankAction(TankAction tankAction, int times) throws InterruptedException {
        Set<Element> playerElement = game.gameContext().getGameMapContext().getPlayerElement(this.player.getId());
        CommandFactory commandFactory = new CommandFactory();
        for (Element element : playerElement) {
            commandFactory.addCommand( CommandFactory.createTankCommand((Tank) element, tankAction, times));
            Msg msg = Msg.newBuilder()
                    .addAllCommands(CommandFactory.toProtoCommands(commandFactory.commandQueue))
                    .setHeader(MsgAction.BROADCAST_COMMAND)
                    .setTimeStamp(new Date().getTime())
                    .build();
            sendMsg(msg);
        }

    }
}
