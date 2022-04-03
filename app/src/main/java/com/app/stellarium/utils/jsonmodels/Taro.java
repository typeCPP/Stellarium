package com.app.stellarium.utils.jsonmodels;

public class Taro {
    public class Card{
        public Integer id;
        public String name;
        public String pic_name;
        public String description;

        public Card(int id, String name, String pic_name, String description) {
            this.id = id;
            this.name = name;
            this.pic_name = pic_name;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", pic_name='" + pic_name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public class ThreeCards{
        public Card first;
        public Card second;
        public Card third;

        public ThreeCards(Card first, Card second, Card third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public String toString() {
            return "ThreeCards{" +
                    "first=" + first +
                    ", second=" + second +
                    ", third=" + third +
                    '}';
        }
    }

    public class SevenCards {
        public Card first, second, third, fourth, fifth, sixth, seventh;

        public SevenCards(Card first, Card second, Card third, Card fourth, Card fifth, Card sixth, Card seventh) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
            this.fifth = fifth;
            this.sixth = sixth;
            this.seventh = seventh;
        }

        @Override
        public String toString() {
            return "SevenCards{" +
                    "first=" + first +
                    ", second=" + second +
                    ", third=" + third +
                    ", fourth=" + fourth +
                    ", fifth=" + fifth +
                    ", sixth=" + sixth +
                    ", seventh=" + seventh +
                    '}';
        }
    }

    public class FourCards {
        public Card first, second, third, fourth;

        public FourCards(Card first, Card second, Card third, Card fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        @Override
        public String toString() {
            return "FourCards{" +
                    "first=" + first +
                    ", second=" + second +
                    ", third=" + third +
                    ", fourth=" + fourth +
                    '}';
        }
    }

    public Card day;
    public Card one;
    public ThreeCards three;
    public SevenCards seven;
    public FourCards four;

    @Override
    public String toString() {
        return "Taro{" +
                "day=" + day +
                ", one=" + one +
                ", three=" + three +
                '}';
    }
}
