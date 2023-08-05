package org.cbzmq.game.test;

/**
 * @ClassName TestModel
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/4 10:38 上午
 * @Version 1.0
 **/
public class TestModel {
    private int id;
    private String name;
    private int age;



    private TestModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
    }

    public static Builder newBuild(){
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", name=" + name +
                ", age=" + age +
                '}';
    }

    public static class Builder{
        private int id;
        private String name;
        private int age;

        public Builder() {
        }


        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public TestModel build(){
            return new TestModel(this);
        }
    }
}
