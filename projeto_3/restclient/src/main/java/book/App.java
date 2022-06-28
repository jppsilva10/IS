package book;

import data.*;

import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();

        Scanner sc = new Scanner(System.in);

        WebTarget target;
        Response response;



        while(true){
            String name;
            long managerId = 0;
            long id = 0;
            float ER = 0;

            Entity<ManagersDTO> inputManagers;
            Entity<ClientDTO> inputClients;
            Entity<Currency> inputCurrencies;

            Credit credit;
            Payment payment;
            Balance balance;
            Total_metric tm;

            String value;

            menu();
            int option = Integer.parseInt(sc.nextLine());
            switch (option){
                case 1:

                    // Add managers to the database

                    do {
                        System.out.println("Name: ");
                        name = sc.nextLine();
                    }while(name.length()<=0);


                    target = client.target("http://localhost:8080/restws/rest/myservice/addManager");
                    ManagersDTO m = new ManagersDTO(name);
                    inputManagers = Entity.entity(m, MediaType.APPLICATION_JSON);
                    response = target.request().post(inputManagers);
                    value = response.readEntity(String.class);
                    System.out.println("RESPONSE: " + value);
                    response.close();

                    break;
                case 2:

                    // Add clients to the database

                    do {
                        System.out.println("Name: ");
                        name = sc.nextLine();
                    }while(name.length()<=0);

                    do {
                        System.out.println("Manager: ");
                        managerId = Long.parseLong(sc.nextLine());
                    }while(managerId == 0);


                    target = client.target("http://localhost:8080/restws/rest/myservice/addClient");
                    ClientDTO c = new ClientDTO(name, managerId);
                    inputClients = Entity.entity(c, MediaType.APPLICATION_JSON);
                    response = target.request().post(inputClients);
                    value = response.readEntity(String.class);
                    System.out.println("RESPONSE: " + value);
                    response.close();


                    break;
                case 3:

                    // Add a currency and respective exchange rate for the euro to the database

                    do {
                        System.out.println("Name: ");
                        name = sc.nextLine();
                    }while(name.length()<=0);

                    do {
                        System.out.println("ER: ");
                        ER = Float.parseFloat(sc.nextLine());
                    }while(ER == 0);


                    target = client.target("http://localhost:8080/restws/rest/myservice/addCurrency");
                    Currency currency = new Currency(name, ER);
                    inputCurrencies = Entity.entity(currency, MediaType.APPLICATION_JSON);
                    response = target.request().post(inputCurrencies);
                    value = response.readEntity(String.class);
                    System.out.println("RESPONSE: " + value);
                    response.close();

                    break;
                case 4:

                    // List managers from the database

                    target = client.target("http://localhost:8080/restws/rest/myservice/getManagers");
                    response = target.request().get();
                    List<ManagersDTO> managersDTOList = response.readEntity(new GenericType<List<ManagersDTO>>() {
                    });


                    System.out.println("RESPONSE: ");
                    for (ManagersDTO managersDTO : managersDTOList){
                        System.out.println(managersDTO);
                    }

                    response.close();
                    break;
                case 5:


                    // List clients from the database

                    target = client.target("http://localhost:8080/restws/rest/myservice/getClients");
                    response = target.request().get();
                    List<ClientDTO> clientDTOList = response.readEntity(new GenericType<List<ClientDTO>>() {
                    });


                    System.out.println("RESPONSE: ");
                    for (ClientDTO clientDTO : clientDTOList){
                        System.out.println(clientDTO);
                    }

                    response.close();

                    break;
                case 6:


                    // List currencies

                    target = client.target("http://localhost:8080/restws/rest/myservice/getCurrencies");
                    response = target.request().get();
                    List<Currency> currencyList = response.readEntity(new GenericType<List<Currency>>() {
                    });


                    System.out.println("RESPONSE: ");
                    for (Currency curr : currencyList){
                        System.out.println(curr);
                    }

                    response.close();

                    break;
                case 7:


                    // Get the credit per client

                    do {
                        System.out.println("Client: ");
                        id = Long.parseLong(sc.nextLine());
                    }while(id == 0);

                    target = client.target("http://localhost:8080/restws/rest/myservice/getCreditPerClient/"+id);
                    response = target.request().get();
                    credit = response.readEntity(Credit.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(credit);

                    response.close();
                    break;
                case 8:

                    // Get the payments per client

                    do {
                        System.out.println("Client: ");
                        id = Long.parseLong(sc.nextLine());
                    }while(id == 0);

                    target = client.target("http://localhost:8080/restws/rest/myservice/getPaymentPerClient/"+id);
                    response = target.request().get();
                    payment = response.readEntity(Payment.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(payment);

                    response.close();
                    break;
                case 9:

                    // Get the current balance of a client.

                    do {
                        System.out.println("Client: ");
                        id = Long.parseLong(sc.nextLine());
                    }while(id == 0);

                    target = client.target("http://localhost:8080/restws/rest/myservice/getBalancePerClient/"+id);
                    response = target.request().get();
                    balance = response.readEntity(Balance.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(balance);

                    response.close();
                    break;
                case 10:


                    // Get the total credits

                    target = client.target("http://localhost:8080/restws/rest/myservice/getTotalCredits");
                    response = target.request().get();
                    tm = response.readEntity(Total_metric.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(tm);

                    response.close();

                    break;
                case 11:

                    // Get the total payments

                    target = client.target("http://localhost:8080/restws/rest/myservice/getTotalPayments");
                    response = target.request().get();
                    tm = response.readEntity(Total_metric.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(tm);

                    break;
                case 12:

                    // Get the total payments

                    target = client.target("http://localhost:8080/restws/rest/myservice/getTotalBalance");
                    response = target.request().get();
                    tm = response.readEntity(Total_metric.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(tm);

                    break;
                case 13:

                    // Compute the bill for each client for the last month.

                    do {
                        System.out.println("Client: ");
                        id = Long.parseLong(sc.nextLine());
                    }while(id == 0);

                    target = client.target("http://localhost:8080/restws/rest/myservice/getBalanceLastMontPerClient/"+id);
                    response = target.request().get();
                    Balance_last_month balance_last_month = response.readEntity(Balance_last_month.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(balance_last_month);

                    response.close();

                    break;
                case 14:
                    break;
                case 15:

                    // Get the data of the person with the highest outstanding debt

                    target = client.target("http://localhost:8080/restws/rest/myservice/getHighestDebt");
                    response = target.request().get();
                    ClientDTO clientDTO = response.readEntity(ClientDTO.class);


                    System.out.println("RESPONSE: ");
                    System.out.println(clientDTO);

                    break;
                case 16:

                    // Get the data of the manager who has made the highest revenue

                    target = client.target("http://localhost:8080/restws/rest/myservice/getHighestRevenue");
                    response = target.request().get();
                    ManagersDTO managersDTO = response.readEntity(ManagersDTO.class);

                    System.out.println("RESPONSE: ");
                    System.out.println(managersDTO);
                    break;
                default:

            }

        }
    }

    public static void menu(){
        System.out.println();
        System.out.println("Menu:");
        System.out.println();
        System.out.println("\t1 - Add manager");
        System.out.println("\t2 - Add client");
        System.out.println("\t3 - Add currency");
        System.out.println("\t4 - List managers");
        System.out.println("\t5 - List clients");
        System.out.println("\t6 - List currencies");
        System.out.println("\t7 - Get the credit per client");
        System.out.println("\t8 - Get the payments per client");
        System.out.println("\t9 - Get the current balance per client");
        System.out.println("\t10 - Get the total credits");
        System.out.println("\t11 - Get the total payments");
        System.out.println("\t12 - Get the total balance");
        System.out.println("\t13 - Get the bill for each client for the last month");
        System.out.println("\t14 - Get the list of clients without payments for the last two months");
        System.out.println("\t15 - Get the data of the person with the highest outstanding debt");
        System.out.println("\t16 - Get the data of the manager who has made the highest revenue");
        System.out.println();

    }

}
