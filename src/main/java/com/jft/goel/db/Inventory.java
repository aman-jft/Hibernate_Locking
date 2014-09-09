package com.jft.goel.db;

import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by aman on 7/9/14.
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String itemCode;
    String name;
    int price;
    int quantity;
    @Version
    @Column(name="OPTLOCK")
    @GenericGenerator(name = "paymentableGenerator", strategy = "guid")
    int version;

    public Inventory() {
    }

    public Inventory(String itemCode, String name, int price, int quantity) {
        this.itemCode = itemCode;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", itemCode='" + itemCode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", version=" + version +
              '}';
    }
}
