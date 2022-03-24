package com.app.stellarium.utils.jsonmodels;

public class PythagoreanSquare {
    public static class Number {
        public Integer count;
        public String text;

        public Number(Integer count, String text) {
            this.count = count;
            this.text = text;
        }

        @Override
        public String toString() {
            return "Number{" +
                    "count=" + count +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    public Number one, two, three, four, five, six, seven, eight, nine;

    public PythagoreanSquare(Number one, Number two, Number three, Number four, Number five, Number six, Number seven, Number eight, Number nine) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
        this.seven = seven;
        this.eight = eight;
        this.nine = nine;
    }

    @Override
    public String toString() {
        return "PythagoreanSquare{" +
                "one=" + one +
                ", two=" + two +
                ", three=" + three +
                ", four=" + four +
                ", five=" + five +
                ", six=" + six +
                ", seven=" + seven +
                ", eight=" + eight +
                ", nine=" + nine +
                '}';
    }
}
