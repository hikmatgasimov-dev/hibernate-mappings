package org.example.onetoone;

import jakarta.persistence.*;

import java.util.concurrent.RecursiveTask;

@Entity
@Table(name = "model")
public class Model {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

   @OneToOne
   @JoinColumn(name = "manufacturer_id",unique = true)
   private Manufacturer manufacturer;


    public  Model(){}
    public Model(String name){this.name=name;}

    public int getId(){return  id;}
    public String getName(){return  name;}

    public  void setName(String name){this.name=name;}

    public Manufacturer getManufacturer(){
        return  manufacturer;
    }

    public  void setManufacturer(Manufacturer manufacturer){

        this.manufacturer=manufacturer;
    }


    @Override
    public String toString() {
        return "Model{id=" + id + ", name='" + name + "'}";
    }
}