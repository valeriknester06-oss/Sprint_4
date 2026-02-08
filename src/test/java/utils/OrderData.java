package utils;

public class OrderData {
    public final String name;
    public final String surname;
    public final String address;
    public final String metro;
    public final String phone;
    public final String date;
    public final String rentPeriod;
    public final String color;
    public final String comment;

    public OrderData(String name, String surname, String address, String metro, String phone,
                     String date, String rentPeriod, String color, String comment) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.rentPeriod = rentPeriod;
        this.color = color;
        this.comment = comment;
    }


    public static OrderData defaultBlack() {
        return new OrderData(
                "Иван",
                "Иванов",
                "Москва, ул. Пушкина, д. 1",
                "Черкизовская",
                "79990000000",
                "15.10.2026",
                "двое суток",
                "black",
                "Позвоните за час"
        );
    }

    public static OrderData defaultGrey() {
        return new OrderData(
                "Петр",
                "Петров",
                "Москва, ул. Ленина, д. 5",
                "Сокольники",
                "79991111111",
                "16.10.2026",
                "сутки",
                "grey",
                "Без звонка"
        );
    }
}
