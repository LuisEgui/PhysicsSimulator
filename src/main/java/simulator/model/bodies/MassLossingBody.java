package simulator.model.bodies;

import org.json.JSONObject;
import simulator.model.bodies.FluentBuilder.Body;

public class MassLossingBody extends simulator.model.bodies.FluentBuilder.Body {
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

    @Override
    public JSONObject getState() {
        return super.getState().put("freq", lossFrequency)
                .put("factor", lossFactor);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode;
        if(result == 0) {
            result = super.hashCode();
            result = 31 * result + Double.hashCode(lossFactor);
            result = 31 * result + Double.hashCode(lossFrequency);
            hashCode = result;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean precondition = super.equals(obj);
        MassLossingBody other = (MassLossingBody) obj;
        return precondition && lossFactor == other.getLossFactor() && lossFrequency == other.getLossFrequency();
    }
}
