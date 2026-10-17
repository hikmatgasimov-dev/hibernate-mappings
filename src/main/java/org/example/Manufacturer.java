package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "manufacturer")

public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "manufacturer",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Model> models=new ArrayList<>();

    public Manufacturer(){}

    public  Manufacturer(String name){
        this.name=name;
    }

    public int getId(){return  id;}

    public  String getName(){return name;}
    public  String setName(String name){
       return this.name=name;
    }

    public  List<Model>getModels(){return  models;}
    // keeps BOTH sides of the relationship in sync
    public void addModel(Model model){
        models.add(model);
        model.setManufacturer(this);

    }
    public void removeModel(Model model){
        models.add(model);
        model.setManufacturer(null);

    }

    // getters and setters for id, name, models
    @Override
    public  String toString(){
        return "Manufacturer{id=" + id + ", name='" + name + "'}";
    }

}
