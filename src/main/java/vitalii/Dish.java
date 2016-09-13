package vitalii;

//Создать таблицу «Меню в ресторане». Колонки: название блюда, его стоимость, вес, наличие скидки.

import javax.persistence.*;

@Entity//нечто из базы данных
@Table(name="Menu") //в какой таблице хранятся обьекты класса (если не указано = название класса )
public class Dish {

    @Id //первичный ключ
    @GeneratedValue(strategy = GenerationType.AUTO) //статегия генерации ключей
    @Column(nullable = false) //поле связано с колонкой
    private Long id;


    @Column(nullable = false, unique = true) //связь и колонка не null
    private String name;

    @Column(nullable = false) //связь и колонка не null
    private Integer price;

    @Column(nullable = false) //связь и колонка не null
    private Integer weight;

    private Integer discount;

    public Dish() {
    }

    public Dish(String name, Integer price, Integer weight, Integer discount) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", weight=" + weight +
                ", discount=" + discount +
                '}';
    }
}
