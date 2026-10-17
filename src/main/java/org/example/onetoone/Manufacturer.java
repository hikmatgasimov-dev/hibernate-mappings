package org.example.onetoone;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "manufacturer",cascade = CascadeType.ALL,orphanRemoval = true)
    private  Model model;

    public  int getId(){return  id;}

    public  Manufacturer(){}
    public  Manufacturer(String name){this.name=name;}

    public Model getModel(){
        return  model;
    }
    public  void setModel(Model model){
        this.model=model;
        if(model!=null){
              model.setManufacturer(this);
        }
    }

    @Override
    public String toString() {
        return "Manufacturer{id=" + id + ", name='" + name + "'}";
    }
}