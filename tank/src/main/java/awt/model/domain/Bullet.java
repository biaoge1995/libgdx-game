package awt.model.domain;

import awt.proto.enums.ElementType;
import lombok.Data;

import java.awt.*;
import java.util.Objects;

/**
 * @author chenbiao
 * @date 2020-12-19 12:23
 * 子弹类
 */
@Data
public class Bullet extends Element {

    private boolean isFire = false;//是否已经发射出去
    private int effectiveRange = 200;//有效射程

    public Bullet(){
        this.setElementType(ElementType.BULLET);
        this.setPower(2);
        this.setHeight(5);
        this.setWide(5);
        this.setStep(5);
        this.setSpeed(10);
        this.setBlood(1);
        this.setFullBlood(1);
        this.setCollisionDead(true);
        this.setCanFly(true);
        this.setNeedSystemControlMove(true);
        this.setMoved(false);
        this.setColor(Color.YELLOW);
    }


    public Bullet(int id) {
        this();
        this.setPlayId(id);

    }

    public Bullet(Element element) {
        toElementSon(element, this);
    }

    public boolean removeOutMap() {
        if (!this.isLive() || this.isFire) {
            return true;
        }
        return false;
    }
    @Override
    public boolean isJoinToMap() {
       if(this.isLive() && this.isFire){
           return true;
       }else {
           return false;
       }
    }
    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    public boolean move(){
        boolean move = super.move();
        if(getDistance()>=effectiveRange){
            beDead();
        }
        return move;
    }



    @Override
    public void reachMapBorderAction() {
        setLive(false);
    }

    public void collisionResult() {
        setLive(false);

    }
}
