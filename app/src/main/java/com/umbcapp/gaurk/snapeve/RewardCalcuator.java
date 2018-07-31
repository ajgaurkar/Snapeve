package com.umbcapp.gaurk.snapeve;

public class RewardCalcuator {

    int current_points;
    int min_range;
    int max_range;



    public RewardCalcuator calculate(int user_total_pts) {
        RewardCalcuator calculatedReward = new RewardCalcuator();

        calculatedReward.current_points = user_total_pts;
        calculatedReward.max_range = 100;
        calculatedReward.min_range = 51;

        return calculatedReward;
    }


    public int getCurrent_points() {
        return current_points;
    }

    public void setCurrent_points(int current_points) {
        this.current_points = current_points;
    }

    public int getMin_range() {
        return min_range;
    }

    public void setMin_range(int min_range) {
        this.min_range = min_range;
    }

    public int getMax_range() {
        return max_range;
    }

    public void setMax_range(int max_range) {
        this.max_range = max_range;
    }
}
