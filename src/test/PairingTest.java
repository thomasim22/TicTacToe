package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Event;
import model.User;
import server.DatabaseHelper;
import server.SocketServer;
import socket.PairingResponse;
import socket.Request;
import socket.Response;

import java.sql.SQLException;

/**
 * Tests the pairing process
 */
public class PairingTest {
    public static void main(String[] args) {
        try {
            Thread mainThread = new Thread(() -> {
                try {
                    DatabaseHelper.getInstance().truncateTables();
                }
                catch (SQLException se) {
                    throw new RuntimeException(se);
                }
                SocketServer.main(null);
            });
            mainThread.start();
            Thread.sleep(1000);
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            // Instantiate users
            User user1 = new User("BoPeep123", "f1076yiuX8", "Bo Peep", false);
            SocketClientHelper scu1 = new SocketClientHelper();
            User user2 = new User("AlphaBetaCent", "Knights2222", "Alvin Bunchin", false);
            SocketClientHelper scu2 = new SocketClientHelper();
            User user3 = new User("AnimeMan", "CrunchyRoll", "Anime Man", false);
            SocketClientHelper scu3 = new SocketClientHelper();
            User user4 = new User("TacTacTac", "QYPUKIL", "Sacco Thomas Cookie", false);
            SocketClientHelper scu4 = new SocketClientHelper();
            int eventId;

            System.out.println("Test 1");
            Request request1 = new Request(Request.RequestType.LOGIN, gson.toJson(user1));
            Response response1 = scu1.sendRequest(request1, Response.class);
            System.out.println(gson.toJson(response1));

            System.out.println("Test 2");
            Request request2 = new Request(Request.RequestType.REGISTER, gson.toJson(user1));
            Response response2 = scu1.sendRequest(request2, Response.class);
            System.out.println(gson.toJson(response2));

            System.out.println("Test 3");
            User user1wrongpassword = new User("BoPeep123", "shheeps1", "Bo Peep", true);
            Request request3 = new Request(Request.RequestType.LOGIN, gson.toJson(user1wrongpassword));
            Response response3 = scu1.sendRequest(request3, Response.class);
            System.out.println(gson.toJson(response3));

            System.out.println("Test 4");
            Request request4 = new Request(Request.RequestType.LOGIN, gson.toJson(user1));
            Response response4 = scu1.sendRequest(request4, Response.class);
            System.out.println(gson.toJson(response4));

            //Register other users
            scu2.sendRequest(new Request(Request.RequestType.REGISTER, gson.toJson(user2)), Response.class);
            scu3.sendRequest(new Request(Request.RequestType.REGISTER, gson.toJson(user3)), Response.class);
            scu4.sendRequest(new Request(Request.RequestType.REGISTER, gson.toJson(user4)), Response.class);

            System.out.println("Test 5"); // Where do I view the result?
            Request request5 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user1));
            PairingResponse response5 = scu1.sendRequest(request5, PairingResponse.class);
            System.out.println(gson.toJson(response5));

            System.out.println("Test 6");
            Request request6 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response6 = scu2.sendRequest(request6, PairingResponse.class);
            System.out.println(gson.toJson(response6));

            scu2.sendRequest(new Request(Request.RequestType.LOGIN, gson.toJson(user2)), Response.class); //Login user 2

            System.out.println("Test 7");
            Request request7 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user1));
            PairingResponse response7 = scu1.sendRequest(request7, PairingResponse.class);
            System.out.println(gson.toJson(response7));

            //Login remaining users
            scu3.sendRequest(new Request(Request.RequestType.LOGIN, gson.toJson(user3)), Response.class);
            scu4.sendRequest(new Request(Request.RequestType.LOGIN, gson.toJson(user4)), Response.class);

            System.out.println("Test 8");
            Request request8 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response8 = scu2.sendRequest(request8, PairingResponse.class);
            System.out.println(gson.toJson(response8));

            System.out.println("Test 9");
            scu4.close(); //Logout user 4
            Request request9 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response9 = scu2.sendRequest(request9, PairingResponse.class);
            System.out.println(gson.toJson(response9));

            System.out.println("Test 10");
            scu4 = new SocketClientHelper();
            scu4.sendRequest(new Request(Request.RequestType.LOGIN, gson.toJson(user4)), Response.class);
            Request request10 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response10 = scu2.sendRequest(request10, PairingResponse.class);
            System.out.println(gson.toJson(response10));

            System.out.println("Test 11");
            Request request11 = new Request(Request.RequestType.SEND_INVITATION, "AlphaBetaCent");
            Response response11 = scu1.sendRequest(request11, Response.class);
            System.out.println(gson.toJson(response11));

            System.out.println("Test 12");
            Request request12 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response12 = scu2.sendRequest(request12, PairingResponse.class);
            System.out.println(gson.toJson(response12));

            System.out.println("Test 13");
            Event invitation1 = response12.getInvitation();
            eventId = invitation1.getEventID();
            Request request13 = new Request(Request.RequestType.DECLINE_INVITATION, String.valueOf(eventId));
            Response response13 = scu2.sendRequest(request13, Response.class);
            System.out.println(gson.toJson(response13));

            System.out.println("Test 14");
            Request request14 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user1));
            PairingResponse response14 = scu1.sendRequest(request14, PairingResponse.class);
            System.out.println(gson.toJson(response14));

            System.out.println("Test 15"); //ACKNOWLEDGE_INVITATION isn't a request type
            Request request15 = new Request(Request.RequestType.ACKNOWLEDGE_RESPONSE, String.valueOf(eventId));
            Response response15 = scu1.sendRequest(request15, Response.class);
            System.out.println(gson.toJson(response15));

            System.out.println("Test 16");
            Request request16 = new Request(Request.RequestType.SEND_INVITATION, "AnimeMan");
            Response response16 = scu1.sendRequest(request16, Response.class);
            System.out.println(gson.toJson(response16));

            System.out.println("Test 17");
            Request request17 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user3));
            PairingResponse response17 = scu3.sendRequest(request17, PairingResponse.class);
            System.out.println(gson.toJson(response17));

            System.out.println("Test 18");
            Event invitation2 = response17.getInvitation();
            eventId = invitation2.getEventID();
            Request request18 = new Request(Request.RequestType.ACCEPT_INVITATION, String.valueOf(eventId));
            Response response18 = scu3.sendRequest(request18, Response.class);
            System.out.println(gson.toJson(response18));

            System.out.println("Test 19");
            Request request19 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user1));
            PairingResponse response19 = scu1.sendRequest(request19, PairingResponse.class);
            System.out.println(gson.toJson(response19));

            System.out.println("Test 20"); //ACKNOWLEDGE_INVITATION isn't a request type
            Request request20 = new Request(Request.RequestType.ACKNOWLEDGE_RESPONSE, String.valueOf(eventId));
            Response response20 = scu1.sendRequest(request20, Response.class);
            System.out.println(gson.toJson(response20));

            System.out.println("Test 21");
            Request request21 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response21 = scu2.sendRequest(request21, PairingResponse.class);
            System.out.println(gson.toJson(response21));

            System.out.println("Test 22");
            Request request22 = new Request(Request.RequestType.ABORT_GAME, gson.toJson(user1));
            Response response22 = scu1.sendRequest(request22, Response.class);
            System.out.println(gson.toJson(response22));

            System.out.println("Test 23");
            Request request23 = new Request(Request.RequestType.UPDATE_PAIRING, gson.toJson(user2));
            PairingResponse response23 = scu2.sendRequest(request23, PairingResponse.class);
            System.out.println(gson.toJson(response23));

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
