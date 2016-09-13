package vitalii;

//Написать код для добавления записей в таблицу и их выборки по критериям
//«стоимость от-до», «только со скидкой»,
//выбрать набор блюд так, чтобы их суммарный вес был не более 1 КГ.

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static EntityManagerFactory emf; //фабрика
    static EntityManager em; //менеджер

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPATest"); //из persistence.XML
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: add random dish");
                    System.out.println("3: select of prise");
                    System.out.println("4: select of discount");
                    System.out.println("5: select of weight");
                    System.out.println("6: view all menu");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addDish(sc);
                            break;
                        case "2":
                            insertRandomDish(sc);
                            break;
                        case "3":
                            selectOfPrise(sc);
                            break;
                        case "4":
                            selectOfDiscount(sc);
                            break;
                        case "5":
                            selectOfWeight(sc);
                            break;
                        case "6":
                            viewAllMenu();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addDish(Scanner sc) {
        System.out.print("Enter dish name: ");
        String name = sc.nextLine();

        System.out.print("Enter client price: ");
        String sPrice = sc.nextLine();
        Integer price = Integer.parseInt(sPrice);

        System.out.print("Enter client weight: ");
        String sWeight = sc.nextLine();
        Integer weight = Integer.parseInt(sWeight);

        System.out.print("Enter client discount: ");
        String sDiscount = sc.nextLine();
        Integer discount = null;
        if(!sDiscount.isEmpty()) {
            discount = Integer.parseInt(sDiscount);
        }

        em.getTransaction().begin(); //у менеджера, начинаем транзакцию
        try {
            Dish dish = new Dish(name, price, weight, discount); //создаем клиента
            em.persist(dish); //сохранить
            em.getTransaction().commit();//фиксировать
        } catch (Exception ex) {
            em.getTransaction().rollback();//откат
        }
    }

    private static void insertRandomDish(Scanner sc) {
        System.out.print("Enter dishS count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Random random = new Random();
                Dish c = new Dish("Name"+i, 50+random.nextInt(500), 50+random.nextInt(300), i%2==0? null:random.nextInt(100));
                em.persist(c); //сохраняем
            }
            em.getTransaction().commit(); //фиксируем
        } catch (Exception ex) {
            em.getTransaction().rollback(); //откат
        }
    }

    private static void selectOfPrise(Scanner sc) {
        System.out.print("Enter prise from: ");
        String sFrom = sc.nextLine();
        Integer from = Integer.parseInt(sFrom);
        System.out.print("Enter prise to: ");
        String sTo = sc.nextLine();
        Integer to = Integer.parseInt(sTo);

        Query query = em.createQuery("SELECT c FROM Dish c WHERE c.price>:from AND c.price<:to", Dish.class);
        query.setParameter("from", from); //передатьпараметр "name"
        query.setParameter("to", to); //передатьпараметр "name"
        List<Dish> dishList = (List<Dish>)query.getResultList();

        for (Dish dish : dishList) {
            System.out.println(dish);
        }
    }

    private static void selectOfDiscount(Scanner sc) {
        Query query = em.createQuery("SELECT c FROM Dish c WHERE c.discount IS NOT NULL AND c.discount > 0", Dish.class);
        List<Dish> dishList = (List<Dish>)query.getResultList();
        for (Dish dish : dishList) {
            System.out.println(dish);
        }
    }

    private static void selectOfWeight(Scanner sc){
        List<Dish> order = new ArrayList<>();
        int counter = 1000;
        while(true) {
            System.out.println("You have " + counter + "g");
            System.out.print("Enter dish name: ");
            String name = sc.nextLine();
            if(name.isEmpty())break;

            Query query = em.createQuery("SELECT c FROM Dish c WHERE c.name = :name", Dish.class);
            query.setParameter("name", name); //передатьпараметр "name"
            try {
                Dish dish = (Dish) query.getSingleResult(); //получить результат (ожидаем один результат)
                dish.getWeight();
                counter -= dish.getWeight();
                if (counter > 0) {
                    order.add(dish);
                }
                else {
                    System.out.println("It si too match!");
                    break;
                }
            }
            catch (NoResultException ex) { //если результата нет
                System.out.println("No dish with this name!");
            } catch (NonUniqueResultException ex) { //если результат не один
                System.out.println("Menu error! More than one dish with this name!");
            }
        }
        System.out.println("Your order:");
        for (Dish d : order) {
            System.out.println(d);
        }
    }

    private static void viewAllMenu() {
        Query query = em.createQuery("SELECT c FROM Dish c", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();
        for (Dish c : list)
            System.out.println(c);
    }
}