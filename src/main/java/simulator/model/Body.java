package simulator.model;

import simulator.misc.Vector2D;


public class Body extends simulator.model.FluentBuilder.Body {

    private Body(Builder builder) {
        super(builder);
    }

    public static class Builder extends simulator.model.FluentBuilder.Body.Builder<Builder> {
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
}