package ClientServer.ServerResponse;

import ClientServer.ClientRequests.*;
import block_match.BlockedMatch;
import block_match.BlockedMatchBuilder;
import database.RecordObject;
import database.RecordObjectHelper;
import holidays_table.ObeoHoliday;
import holidays_table.ObeoHolidayBuilder;
import interests.UserInterest;
import interests.UserInterestBuilder;
import java.util.List;
import languages.UserLanguage;
import languages.UserLanguageBuilder;
import user.ObeoUser;
import user.ObeoUserBuilder;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObeoServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(59898)) {
            System.out.println("The server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (true) {
                pool.submit(new Responder(listener.accept()));
//                pool.submit(new Shouter(listener.accept()));
            }
        }
    }

    private static class Responder implements Runnable {
        private ClientRequest clientRequest;
        private Socket socket;

        public Responder(Socket socket) throws Exception {
            this.socket = socket;
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientRequest = (ClientRequest) objectInputStream.readObject();
        }

        @Override
        public void run() {
            try {

                ServerResponse serverResponse;
                if (clientRequest instanceof CreateHolidayRequest) {
                    ObeoHoliday obeoHoliday = ((CreateHolidayRequest) clientRequest).getHoliday();
                    if (ObeoHolidayBuilder.addToDatabase(obeoHoliday)) {
                        serverResponse = new CreateHolidayResponse(obeoHoliday);
                    } else {
                        serverResponse = new ErrorServerResponse();
                    }

                } else if (clientRequest instanceof CreateUserRequest) {

                    ObeoUser obeoUser = ((CreateUserRequest) clientRequest).getObeoUser();

                    ObeoUserBuilder.apiAddNewUser(obeoUser);
                    serverResponse = new CreateUserResponse(obeoUser);

                } else if (clientRequest instanceof LookingForLocalsRequest) {
                    ObeoHoliday obeoHoliday = ((LookingForLocalsRequest) clientRequest).getHoliday();

                    serverResponse = new LookingForLocalsResponse(ObeoHolidayBuilder.getLocals(obeoHoliday));

                } else if (clientRequest instanceof LookingForTouristsRequest) {
                    ObeoUser obeoUser = ((LookingForTouristsRequest) clientRequest).getLocal();
                    serverResponse = new LookingForTouristsResponse(ObeoUserBuilder.findTourists(obeoUser));

                } else if (clientRequest instanceof UpdateUserRequest) {
                    ObeoUser obeoUser = ((UpdateUserRequest) clientRequest).getObeoUser();
                    long id = obeoUser.getId();
                    ObeoUserBuilder.updateInDatabase(obeoUser);
                    obeoUser = ObeoUserBuilder.buildUserFromId(id);
                    serverResponse = new UpdateUserResponse(obeoUser);

                } else if (clientRequest instanceof LoginRequest) {
                    ObeoUser obeoUser = ObeoUserBuilder.buildUserFromUsernameAndPassword(((LoginRequest) clientRequest).getUsername(),((LoginRequest) clientRequest).getPassword());
                    if (obeoUser == null || obeoUser.getId() == -1) {
                        serverResponse = new ErrorServerResponse();

                    } else {
                        List<UserInterest> userInterestList = UserInterestBuilder
                                .getInterestsForUserid(obeoUser.getId());

                        List<ObeoHoliday> obeoHolidayList = ObeoHolidayBuilder
                                .getHolidaysForUserId(obeoUser.getId());

                        List<UserLanguage> userLanguageList = UserLanguageBuilder
                                .getLanguageFromId(obeoUser.getId());

                        serverResponse = new LoginResponse(obeoUser, obeoHolidayList, userInterestList, userLanguageList);
                    }

                } else if (clientRequest instanceof CreateLanguageRequest) {
                    UserLanguage userLanguage = ((CreateLanguageRequest) clientRequest).getUserLanguage();
                    UserLanguageBuilder.addToDatabase(userLanguage);
                    serverResponse = new CreateLanguageResponse(userLanguage);


                } else if (clientRequest instanceof UpdateLanguageRequest) {
                    UserLanguage userLanguage = ((UpdateLanguageRequest) clientRequest).getUserLanguage();
                    UserLanguageBuilder.updateInDatabase(userLanguage);
                    serverResponse = new UpdateLanguageResponse(userLanguage);
                } else if (clientRequest instanceof CreateInterestRequest) {
                    UserInterest userInterest = ((CreateInterestRequest) clientRequest).getUserInterest();
                    UserInterestBuilder.addToDatabase(userInterest);
                    serverResponse = new CreateInterestResponse(userInterest);
                } else if (clientRequest instanceof DeleteInterestRequest) {
                    UserInterest userInterest = ((DeleteInterestRequest) clientRequest).getUserInterest();
                    UserInterestBuilder.deleteInterest(userInterest);
                    serverResponse = new DeleteInterestResponse(userInterest);

                } else if (clientRequest instanceof DeleteLanguageRequest) {
                    UserLanguage userLanguage = ((DeleteLanguageRequest) clientRequest).getUserLanguage();
                    UserLanguageBuilder.deleteLanguage(userLanguage);
                    serverResponse = new DeleteLanguageResponse(userLanguage);
                } else if (clientRequest instanceof GetUserFromUsernameRequest) {
                    ObeoUser user = ObeoUserBuilder.buildUserFromUsername(((GetUserFromUsernameRequest) clientRequest).getUsername());
                    serverResponse = new GetUserFromUsernameResponse(user);
                } else if (clientRequest instanceof DeleteHolidayRequest) {
                    ObeoHoliday holiday = ((DeleteHolidayRequest) clientRequest).getObeoHoliday();
                    RecordObjectHelper.removeFromDatabase(holiday);
                    serverResponse = new DeleteHolidayResponse(holiday);
                } else if (clientRequest instanceof ReportUserRequest) {
                    BlockedMatchBuilder.addBlockMatch(new BlockedMatch(((ReportUserRequest) clientRequest).getBlockingUser(),
                            ((ReportUserRequest) clientRequest).getBlockedUser(), true));
                    serverResponse = new ReportUserResponse(BlockedMatchBuilder.areTwoUsersBlocked
                            (((ReportUserRequest) clientRequest).getBlockingUser(), ((ReportUserRequest) clientRequest).getBlockedUser()));

                } else if (clientRequest instanceof MatchedUserRequest) {
                    BlockedMatchBuilder.addBlockMatch(new BlockedMatch(((MatchedUserRequest) clientRequest).getUserOneID(),
                            ((MatchedUserRequest) clientRequest).getUserTwoID(), false));
                    serverResponse = new MatchedUserResponse(BlockedMatchBuilder.areTwoUsersMatched(((MatchedUserRequest) clientRequest).getUserOneID(),
                            ((MatchedUserRequest) clientRequest).getUserTwoID()));
                }

                else {
                    serverResponse = new ErrorServerResponse();
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(serverResponse);



            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());

            }
        }


    }
}