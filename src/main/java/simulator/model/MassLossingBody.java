package simulator.model;

import simulator.model.FluentBuilder.Body;

public class MassLossingBody extends simulator.model.FluentBuilder.Body {
    private double lossFactor;
    private double lossFrequency;
    private double movementTime;

    private MassLossingBody(Builder builder) {
        super(builder);
        this.lossFactor = builder.lossFactor;
        this.lossFrequency = builder.lossFrequency;
        this.movementTime = builder.movementTime;
    }

    public static class Builder extends Body.Builder<Builder> {
        private double lossFactor = 0;
        private double lossFrequency = 0;
        private double movementTime = 0;

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
            if(lossFactor < 0 || lossFactor > 1)
                throw new IllegalArgumentException("Loss factor must be in range: [0-1]!");
            this.lossFactor = lossFactor;
            return self();
        }

        public Builder lossFrequency(double lossFrequency) {
            if(lossFrequency < 0)
                throw new IllegalArgumentException("A MassLossingBody cant gain mass! \"lossFrequency\" must be >= 0");
            this.lossFrequency = lossFrequency;
            return self();
        }
    }

    public double getLossFactor() {
        return lossFactor;
    }

    public double getLossFrequency() {
        return lossFrequency;
    }

    private boolean hasMass() { return (this.getMass() > 0 && this.getMass() != 0); }

    private void massLoss() {
        mass *= (1-lossFactor);
    }

    @Override
    public void move(double time) {
        movementTime += time;
        if(hasMass()) {
            if(movementTime >= lossFrequency) {
                int kPeriods = (int)(movementTime/lossFrequency);
                double deltaTime = kPeriods * lossFrequency;
                for(int i = 0; i < kPeriods; i++) {
                    if(hasMass()) {
                        super.move(lossFrequency);
                        massLoss();
                    }
                    else break;
                }
                if(hasMass())
                    super.move(movementTime - deltaTime);
                movementTime = 0;
            } else if (hasMass())
                super.move(movementTime);
        }
    }
}
