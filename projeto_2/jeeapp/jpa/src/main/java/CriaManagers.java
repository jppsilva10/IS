import data.Manager;

import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.Persistence;


public class CriaManagers {

    public static void main(String[] args){

        Manager[] managers = {
                new Manager("Ines" , "Ines"),
                new Manager("Joao", "Joao")
        };

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BusCompany");
        EntityManager em = emf.createEntityManager();


        for (Manager m : managers){
            em.persist(m);
        }

        em.close();
        emf.close();

    }
}
