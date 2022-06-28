package book;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import data.*;
import data.Currency;

@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    @PersistenceContext(unitName = "Company")
    EntityManager em;

    @Transactional
    @GET
    @Path("/addData")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addData() {
        Managers[] managers ={
                new Managers("Filipe"),
                new Managers("Ana")
        };

        Client[] clients = {
                new Client("Joao", managers[0]),
                new Client("Ines", managers[0]),
                new Client("Paulo", managers[1]),
                new Client("Fabio", managers[1])
        };

        Currency[] currencies = {
                new Currency("dollar", 0.85f),
                new Currency("euro", 1f),
                new Currency("pound", 1.15f),
                new Currency("franc", 0.95f)
        };

        for (Managers m : managers) {
            em.persist(m);
        }

        for (Client c : clients) {
            em.persist(c);
        }

        for (Currency c : currencies) {
            em.persist(c);
        }
        return Response.status(Status.OK).entity("success").build();
    }

    @Transactional
    @GET
    @Path("/getClients/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {

        TypedQuery<Client> q = em.createQuery("SELECT c  FROM Client c", Client.class);

        List<Client> c = q.getResultList();
        List<ClientDTO> dto = c.stream().map(Client::DTO).collect(Collectors.toList());

        return Response.ok().entity(dto).build();
    }

    @Transactional
    @GET
    @Path("/getClient/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") long id) {

        TypedQuery<Client> q = em.createQuery("SELECT c  FROM Client c WHERE c.id = :id", Client.class);
        q.setParameter("id", id);


        Client c = q.getSingleResult();

        return Response.ok().entity(c).build();
    }

    @Transactional
    @GET
    @Path("/getCurrencies/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrencies() {

        TypedQuery<Currency> q = em.createQuery("SELECT c  FROM Currency c", Currency.class);

        List<Currency> c = q.getResultList();

        return Response.ok().entity(c).build();
    }

    @Transactional
    @GET
    @Path("/getManagers/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagers() {

        TypedQuery<Managers> q = em.createQuery("SELECT m FROM Managers m", Managers.class);

        List<Managers> m = q.getResultList();

        List<ManagersDTO> dto = m.stream().map(Managers::DTO).collect(Collectors.toList());

        return Response.ok().entity(dto).build();
    }

    @Transactional
    @POST
    @Path("/addManager")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addManager(ManagersDTO manager) {

        Managers m = new Managers(manager.getName());

        em.persist(m);

        return Response.status(Status.OK).entity("success").build();
    }

    @Transactional
    @POST
    @Path("/addClient")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(ClientDTO client) {
        TypedQuery<Managers> q = em.createQuery("SELECT m FROM Managers m WHERE m.id = :id", Managers.class);
        q.setParameter("id", client.getManager_id());

        Managers m = q.getSingleResult();

        Client c = new Client(client.getName(), m);

        em.persist(c);

        return Response.status(Status.OK).entity("success").build();
    }

    @Transactional
    @POST
    @Path("/addCurrency")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCurrency(Currency currency) {
        Currency c = new Currency(currency.getName(), currency.getExchangeRate());
        em.persist(currency);

        return Response.status(Status.OK).entity("success").build();
    }

    @Transactional
    @GET
    @Path("/getCreditPerClient/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCreditPerClient(@PathParam("id") long id) {

        TypedQuery<Credit> q = em.createQuery("SELECT c FROM Credit c WHERE c.id = :id", Credit.class);
        q.setParameter("id", id);

        Credit c = q.getSingleResult();

        return Response.ok().entity(c).build();
    }

    @Transactional
    @GET
    @Path("/getTotalCredits/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalCredits() {

        TypedQuery<Total_metric> q = em.createQuery("SELECT t FROM Total_metric t WHERE t.id = :id", Total_metric.class);
        q.setParameter("id", "TotalCredits");

        Total_metric t = q.getSingleResult();

        return Response.ok().entity(t).build();
    }

    @Transactional
    @GET
    @Path("/getPaymentPerClient/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentPerClient(@PathParam("id") long id) {

        TypedQuery<Payment> q = em.createQuery("SELECT p FROM Payment p WHERE p.id = :id", Payment.class);
        q.setParameter("id", id);

        Payment p = q.getSingleResult();

        return Response.ok().entity(p).build();
    }

    @Transactional
    @GET
    @Path("/getTotalPayments/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalPayments() {

        TypedQuery<Total_metric> q = em.createQuery("SELECT t FROM Total_metric t WHERE t.id = :id", Total_metric.class);
        q.setParameter("id", "TotalPayments");

        Total_metric t = q.getSingleResult();

        return Response.ok().entity(t).build();
    }

    @Transactional
    @GET
    @Path("/getBalancePerClient/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalancePerClient(@PathParam("id") long id) {

        TypedQuery<Balance> q = em.createQuery("SELECT b FROM Balance b WHERE b.id = :id", Balance.class);
        q.setParameter("id", id);

        Balance b = q.getSingleResult();

        return Response.ok().entity(b).build();
    }

    @Transactional
    @GET
    @Path("/getBalanceLastMontPerClient/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalanceLastMontPerClient(@PathParam("id") long id) {

        TypedQuery<Balance_last_month> q = em.createQuery("SELECT b FROM Balance_last_month b WHERE b.id = :id", Balance_last_month.class);
        q.setParameter("id", id);

        Balance_last_month b = q.getSingleResult();

        return Response.ok().entity(b).build();
    }

    @Transactional
    @GET
    @Path("/getTotalBalance/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalBalance() {

        TypedQuery<Total_metric> q = em.createQuery("SELECT t FROM Total_metric t WHERE t.id = :id", Total_metric.class);
        q.setParameter("id", "TotalBalance");

        Total_metric t = q.getSingleResult();

        return Response.ok().entity(t).build();
    }

    @Transactional
    @GET
    @Path("/getHighestDebt/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHighestDept() {

        TypedQuery<Highest_metric> q = em.createQuery("SELECT h FROM Highest_metric h WHERE h.id = :id", Highest_metric.class);
        q.setParameter("id", "HighestDebt");

        Highest_metric h = q.getSingleResult();

        System.out.println("------------------------------------------------");

        TypedQuery<Client> q2 = em.createQuery("SELECT c FROM Client c WHERE c.id = :id", Client.class);
        q2.setParameter("id", h.getMetric());

        Client c = q2.getSingleResult();

        return Response.ok().entity(c.DTO()).build();
    }

    @Transactional
    @GET
    @Path("/getHighestRevenue/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHighestRevenue() {

        TypedQuery<Highest_metric> q = em.createQuery("SELECT h FROM Highest_metric h WHERE h.id = :id", Highest_metric.class);
        q.setParameter("id", "HighestRevenue");

        Highest_metric h = q.getSingleResult();

        TypedQuery<Managers> q2 = em.createQuery("SELECT m FROM Managers m WHERE m.id = :id", Managers.class);
        q2.setParameter("id", h.getMetric());

        Managers m = q2.getSingleResult();

        return Response.ok().entity(m.DTO()).build();
    }


}
