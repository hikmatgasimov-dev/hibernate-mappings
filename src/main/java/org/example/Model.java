package org.example;

import jakarta.persistence.*;

import java.security.PublicKey;

@Entity
@Table(name = "model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    public  Model(){}

    public  Model(String name){

        this.name=name;
    }
    public int getId(){return id;}

    public String getName() { return name; }

    public  String setName(String name){return this.name=name;}

    public   Manufacturer manufacturer(){return  manufacturer;}

    public  void setManufacturer(Manufacturer manufacturer){
        this.manufacturer =manufacturer;
    }

    @Override
    public String toString() {
        return "Model{id=" + id + ", name='" + name + "'}";
    }
}
