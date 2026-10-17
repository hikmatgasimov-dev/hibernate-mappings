package org.example.onetoone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainOneToOne {
    public static void main(String[] args) {


        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                // DIFF: use another schema than the one in the xml
                .setProperty("hibernate.connection.url",
                        "jdbc:mysql://localhost:3306/hb_one_to_one?useSSL=false&allowPublicKeyRetrieval=true")
                .addAnnotatedClass(Manufacturer.class)
                .addAnnotatedClass(Model.class)
                .buildSessionFactory()) {



            int manufacturerId;

            // 1. Save one manufacturer with exactly ONE model
            try(Session session = factory.openSession()){
                Transaction tx = session.beginTransaction();

                Manufacturer honda=new Manufacturer("Hondo");
                honda.setModel(new Model("Civic"));

                session.persist(honda);
                tx.commit();
                manufacturerId = honda.getId();
            }

            // 2. Read it back
            try (Session session = factory.openSession()) {
                // DIFF: `m.model` is a single object, not a collection
                Manufacturer m = session.createQuery(
                                "select m from Manufacturer m join fetch m.model where m.id = :id",
                                Manufacturer.class)
                        .setParameter("id", manufacturerId)
                        .getSingleResult();

                System.out.println(m);
                System.out.println("  " + m.getModel());          // DIFF: one model, no for-loop
            }

            // 3. DEMO: a SECOND model for the same manufacturer must be rejected by UNIQUE
            try (Session session = factory.openSession()) {
                Transaction tx = session.beginTransaction();
                try {
                    Manufacturer m = session.find(Manufacturer.class, manufacturerId);
                    Model second = new Model("Accord");
                    second.setManufacturer(m);
                    session.persist(second);
                    tx.commit();
                    System.out.println("Unexpected: second model was saved");
                } catch (Exception e) {
                    tx.rollback();
                    System.out.println("Rejected as expected: " + e.getClass().getSimpleName());
                }
            }

        }
    }
}