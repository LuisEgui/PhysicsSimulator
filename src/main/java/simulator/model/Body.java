package simulator.model;

import simulator.misc.Vector2D;


public class Body extends simulator.model.FluentBluilder.Body {

    private Body(Builder builder) {
        super(builder);
    }

    public static class Builder extends simulator.model.FluentBluilder.Body.Builder<Builder> {
        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Body build() {
            return new Body(this);
        }
    }

    public static void main(String[] args) {
        Body body = new Body.Builder().id("b1").velocity(new Vector2D()).position(new Vector2D(0,0)).mass(50).build();
        System.out.println(body.toString());
    }
}
