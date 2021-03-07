package simulator.model;

import simulator.misc.Vector2D;
import simulator.model.FluentBuilder.Body;

public class MassLossingBody extends simulator.model.FluentBuilder.Body {
    private double lossFactor;
    private double lossFrequency;

    private MassLossingBody(Builder builder) {
        super(builder);
        this.lossFactor = builder.lossFactor;
        this.lossFrequency = builder.lossFrequency;
    }

    public static class Builder extends Body.Builder<Builder> {
        private double lossFactor;
        private double lossFrequency;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MassLossingBody build() {
            return new MassLossingBody(this);
        }

        public Builder lossFactor(double lossFactor) {
            if(lossFactor < 0)
                throw new IllegalArgumentException("Loss factor must be in range: [0-1]!");
            this.lossFactor = lossFactor;
            return self();
        }

        public Builder lossFrequency(double lossFrequency) {
            if(lossFrequency < 0)
                throw new IllegalArgumentException("A MassLossingBody cant gain mass! \"lossFrequency must be >= 0");
            this.lossFrequency = lossFrequency;
            return self();
        }

        public double getLossFactor() {
            return lossFactor;
        }

        public double getLossFrequency() {
            return lossFrequency;
        }
    }
}
