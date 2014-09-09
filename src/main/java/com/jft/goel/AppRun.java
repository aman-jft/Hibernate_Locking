package com.jft.goel;

import com.jft.goel.db.Customer;
import com.jft.goel.db.Employee;
import com.jft.goel.db.Inventory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by aman on 7/9/14.
 */
public class AppRun {
    public static SessionFactory sf;

    static {
        sf = new Configuration().configure().buildSessionFactory();
    }

    public static void save(Object object) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + " -> ");
            System.out.println(e.getMessage());
        }

    }

    public static Object fetchRecord(Class name, int id) {
        Session session = sf.openSession();
        return session.get(name, id);
    }

    public static void concurrentUpdateWithLock() {

        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {

                public void run() {

                    Session session = sf.openSession();
                    session.beginTransaction();
                    Inventory item = (Inventory) session.load(Inventory.class, 1, LockMode.UPGRADE);
                    try {
                        item.setQuantity(item.getQuantity() * 2);
                        Thread.sleep(10);
                        session.update(item);
                        session.getTransaction().commit();

                    } catch (Exception e) {
                        System.out.println(Thread.currentThread().getName() + " : error ->");
                        System.out.println("\n\n");
                    }
                    System.out.println((Inventory) session.get(Inventory.class, 1));
                    session.close();

                }
            }).start();
        }
    }

    public static void concurrentUpdateWithOutLock() {

        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {

                public void run() {

                    Inventory item = (Inventory) fetchRecord(Inventory.class, 1);
                    item.setQuantity(item.getQuantity() * 2);
                    save(item);
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {

        /*
        * Firstly save record without version.
        *
        * */

        Employee emp = new Employee("Robert Dowery", "Actor");
        save(emp);

        emp = (Employee) fetchRecord(Employee.class, 1);
        Employee empBackup = (Employee) fetchRecord(Employee.class, 1);
        System.out.println("Current Value : " + emp);
        emp.setDepartment("Director");
        empBackup.setDepartment("Stark Industry");
        save(emp);
        System.out.println("Updated Value : " + (Employee) fetchRecord(Employee.class, 1));

        /*
        * No error generate while updating previous copy of object.
        *
        * */

        System.out.println("\n ** Updating previous copy of object (changing Department) ");
        System.out.println("Current Value : " + (Employee) fetchRecord(Employee.class, 1));
        save(empBackup);
        System.out.println("Updated Value : " + (Employee) fetchRecord(Employee.class, 1));

        /*
        * Save record with Version number and when we try to update the object which is already modified
        * then it will generate the exception.
        *
        * */

        System.out.println("\n\n\n ** With Version");
        Inventory inventory = new Inventory("ABC001", "Kingfisher", 65, 30);
        save(inventory);

        /*
        * Update record and version is automatically modified.
        *
        * */

        Inventory item = (Inventory) fetchRecord(Inventory.class, 1);
        Inventory backup = (Inventory) fetchRecord(Inventory.class, 1);
        System.out.println("Current Value : " + item);
        item.setQuantity(1);
        save(item);
        System.out.println("Updated Value : " + (Inventory) fetchRecord(Inventory.class, 1));

        /*
        * Generating error .
        *
        * */
        System.out.println("\n ** Updating previous copy of object (Changing item name) ");
        System.out.println("Current Value : " + backup);
        backup.setItemCode("King123");
        save(backup);


       /*
       * Version using Timestamp
       *
       * */

        System.out.println("\n\n\n ** With Version using Timestamp");
        Customer customer = new Customer("JFT");
        save(customer);

         /*
        * Update record and version is automatically modified.
        *
        * */

        customer = (Customer) fetchRecord(Customer.class, 1);
        Customer customerBackup = (Customer) fetchRecord(Customer.class, 1);
        System.out.println("Current Value : " + customer);
        customer.setName("Jbilling");
        save(customer);
        System.out.println("Updated Value : " + (Customer) fetchRecord(Customer.class, 1));

        /*
        * Generating error .
        *
        * */

        System.out.println("\n ** Updating previous copy of object (Changing item name) ");
        System.out.println("Current Value : " + customerBackup);
        customerBackup.setName("Middleware");
        save(customerBackup);


        /*
        * Concurrency Error
        *
        * */

        System.out.println("\n\n\n **Concurrency error");
        concurrentUpdateWithOutLock();

         /*
        * Pessimistic locking
        *
        * */

        Thread.sleep(1000);
        System.out.println("\n\n\n ** Avoid Concurrency error");
        concurrentUpdateWithLock();
    }
}
