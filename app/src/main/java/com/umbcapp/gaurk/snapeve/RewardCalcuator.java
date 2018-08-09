package com.umbcapp.gaurk.snapeve;

public class RewardCalcuator {

    float current_points;
    float min_range;
    float max_range;
    float relativerewardsValue;
    float maxCalculatedRange = 0;
    float minCalculatedRange = 0;
    int calculatedLevel = 1;
    int level = 1;

    public RewardCalcuator calculate(float user_total_pts) {
        RewardCalcuator calculatedReward = new RewardCalcuator();

        calculatedReward.current_points = user_total_pts;
        getRange(calculatedReward.current_points);

        calculatedReward.max_range = maxCalculatedRange;
        calculatedReward.min_range = minCalculatedRange;
        calculatedReward.level = calculatedLevel;

        System.out.println("(this.current_points - this.min_range) " + (calculatedReward.current_points - calculatedReward.min_range));
        System.out.println("(this.max_range - this.min_range) " + (calculatedReward.max_range - calculatedReward.min_range));
        System.out.println("((this.max_range - this.min_range) / 100) " + ((calculatedReward.max_range - calculatedReward.min_range) / 100));
        calculatedReward.relativerewardsValue = (calculatedReward.current_points - calculatedReward.min_range) / ((calculatedReward.max_range - calculatedReward.min_range) / 100);

        return calculatedReward;
    }

    private void getRange(float current_points) {

        if (current_points < 10) {
            float minCalculatedRange = 0;
            float maxCalculatedRange = 9;
            calculatedLevel = 1;

        } else if (current_points >= 10 && current_points < 50) {
            float minCalculatedRange = 10;
            float maxCalculatedRange = 49;
            calculatedLevel = 2;

        } else if (current_points >= 50 && current_points < 200) {
            float minCalculatedRange = 50;
            float maxCalculatedRange = 199;
            calculatedLevel = 3;

        } else if (current_points >= 200 && current_points < 500) {
            float minCalculatedRange = 200;
            float maxCalculatedRange = 499;
            calculatedLevel = 4;

        } else if (current_points >= 500 && current_points < 1000) {
            float minCalculatedRange = 500;
            float maxCalculatedRange = 999;
            calculatedLevel = 5;

        } else if (current_points >= 1000 && current_points < 2000) {
            float minCalculatedRange = 1000;
            float maxCalculatedRange = 1999;
            calculatedLevel = 6;

        } else if (current_points >= 2000 && current_points < 5000) {
            float minCalculatedRange = 2000;
            float maxCalculatedRange = 4999;
            calculatedLevel = 7;

        } else if (current_points >= 5000 && current_points < 10000) {
            float minCalculatedRange = 5000;
            float maxCalculatedRange = 9999;
            calculatedLevel = 8;

        } else if (current_points >= 10000 && current_points < 50000) {
            float minCalculatedRange = 10000;
            float maxCalculatedRange = 49999;
            calculatedLevel = 9;

        } else {
            float minCalculatedRange = 50000;
            float maxCalculatedRange = 100000;
            calculatedLevel = 10;

        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getCurrent_points() {
        return current_points;
    }

    public void setCurrent_points(float current_points) {
        this.current_points = current_points;
    }

    public float getMin_range() {
        return min_range;
    }

    public void setMin_range(float min_range) {
        this.min_range = min_range;
    }

    public float getMax_range() {
        return max_range;
    }

    public void setMax_range(float max_range) {
        this.max_range = max_range;
    }

    public float getRelativerewardsValue() {
        return relativerewardsValue;
    }

    public void setRelativerewardsValue(float relativerewardsValue) {
        this.relativerewardsValue = relativerewardsValue;
    }
}
