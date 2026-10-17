package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ;

        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Manufacturer.class)
                .addAnnotatedClass(Model.class)
                .buildSessionFactory()){

              int manufacturerId;

              // 1. Save a manufacturer with two models (cascade saves the models)

              try (Session session=factory.openSession()){
                    Transaction tx=session.beginTransaction();

                    Manufacturer hondo =new Manufacturer("Hondo");
                    hondo.addModel(new Model("Civili"));
                    hondo.addModel(new Model("Accord"));

                    session.persist(hondo);
                    tx.commit();
                    manufacturerId=hondo.getId();
              }
              // 2. Read it back, loading the models in the same query

              try (Session session = factory.openSession()){

                  Manufacturer m = session.createQuery(
                          "select m from Manufacturer m join fetch m.models where m.id = :id",
                          Manufacturer.class)
                          .setParameter("id",manufacturerId)
                          .getSingleResult();

                  System.out.println(m);
                  for(Model model:m.getModels()){
                      System.out.println(" "+model);
                  }


                  // 2. Read it back, loading the models in the same query
              }


          }



    }
}