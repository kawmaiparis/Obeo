import database.ObeoConnection;
import database.RecordObjectHelper;
import holidays_table.ObeoHoliday;
import holidays_table.ObeoHolidayBuilder;
import interests.UserInterest;
import interests.UserInterestBuilder;
import java.util.Iterator;
import java.util.stream.Collectors;
import languages.UserLanguage;
import languages.UserLanguageBuilder;
import matching_users.MatchedObeoUser;
import matching_users.MatchingAlgorithm;
import org.junit.Test;
import user.ObeoUser;
import user.ObeoUserBuilder;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class Database_Test {


  Connection c = ObeoConnection.getInstance();
  Date date = new Date(1998-11-22);
  ObeoUser samantha = new ObeoUser("samantha", "sammy", "secondName",
      date, "casablanca", -0.202691, 51.496606,  "password", null, false, "hi!");
  ObeoUser paul = new ObeoUser("paul", "pauly", "secondname",
      date, "madrid", -0.179589, 51.498728, "password", null, false, "hi!");
  ObeoUser dave = new ObeoUser("dave", "dave", "secondname",
      date, "casablanca", -0.179589, 51.498728, "password", null, false, "hi!");


  // -------------GENERAL USER TEST-------------------
  @Test
  public void canAddUserToDatabase() {
    assertTrue(ObeoUserBuilder.getReserved().contains("samantha"));
    assertTrue(ObeoUserBuilder.addToDatabase(samantha));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void addingUserToDatabaseUpdatesObjectIdToValid() {
    assertEquals(samantha.getId(), -1);
    ObeoUserBuilder.addToDatabase(samantha);
    assertNotEquals(samantha.getId(), -1);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void databaseContainsNewlyAddedPerson() {
    ObeoUserBuilder.addToDatabase(samantha);
    long id = samantha.getId();
    ObeoUser user1 = ObeoUserBuilder.buildUserFromId(id);
    assertEquals(samantha, user1);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void cannotAddUserWithSameUsername() {
    ObeoUserBuilder.addToDatabase(samantha);
    assertTrue(ObeoUserBuilder.userExists(samantha.getId()));
    ObeoUser user2 = new ObeoUser("samantha", "firstnameNotreally", "secondName",
        date, "casablanca", 50, 50, "password", null, false, "hi!");
    assertFalse(ObeoUserBuilder.addToDatabase(user2));
    assertFalse(ObeoUserBuilder.userExists(user2.getId()));
    RecordObjectHelper.removeFromDatabase(samantha);
}


  @Test
  public void updateUserDetails() {
    ObeoUserBuilder.addToDatabase(samantha);
    assertTrue(ObeoUserBuilder.userExists(samantha.getId()));
    ObeoUser sameUser = new ObeoUser(samantha.getId(), samantha.getUser_name(), "new firstname", "new secondname", samantha
        .getDate_of_birth(),
        samantha.getHome_city(), samantha.getLongitude(), samantha.getLatitude(), samantha.getUser_password(),
        null, samantha.getHolidayList(), samantha.getUserLanguages(), samantha.getUserInterests(), true, samantha
        .isMale(), samantha.getBio());
    ObeoUserBuilder.updateInDatabase(sameUser);
    ObeoUser gotFromDatabase = ObeoUserBuilder.buildUserFromId(samantha.getId());
    assertEquals(sameUser.getFirst_name(), gotFromDatabase.getFirst_name());
    assertNotEquals(samantha.getFirst_name(), gotFromDatabase.getFirst_name());
    assertEquals(sameUser.getSecond_name(), gotFromDatabase.getSecond_name());
    assertNotEquals(samantha.getSecond_name(), gotFromDatabase.getSecond_name());
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void userCanDeleteTheirAccountFromDatabase() {
    ObeoUserBuilder.addToDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(samantha);
    assertFalse(ObeoUserBuilder.userExists(samantha.getId()));
  }

  @Test
  public void userCanLogin() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUser loggedInUser = ObeoUserBuilder.buildUserFromUsernameAndPassword(samantha.getUser_name(), samantha.getUser_password());
    assertEquals(loggedInUser, samantha);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void passwordIsHashed() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUser loggedInUser = ObeoUserBuilder.buildUserFromId(samantha.getId());
    assertNotEquals(loggedInUser.getUser_password(), "password");
    assertEquals(samantha.getUser_password(), loggedInUser.getUser_password());
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  // ------------------------LANGUAGE TESTS----------------------------
  @Test
  public void deletingUserWillDeleteAllLanguagesForSaidUser() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguage userLanguage2 = new UserLanguage(samantha.getId(), "spanish", 5);
    UserLanguageBuilder.addToDatabase(userLanguage);
    UserLanguageBuilder.addToDatabase(userLanguage2);
    RecordObjectHelper.removeFromDatabase(samantha);
    List<UserLanguage> userLanguages = UserLanguageBuilder.getLanguageFromId(samantha.getId());
    assertTrue(userLanguages.isEmpty());
  }

  @Test
  public void canAddLanguage() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    assertTrue(UserLanguageBuilder.addToDatabase(userLanguage));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void addedLanguageIsInDatabase() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguageBuilder.addToDatabase(userLanguage);
    UserLanguage databaseLanguage = UserLanguageBuilder.getSpecificUserLanguage(samantha.getId(), "english");
    assertEquals(databaseLanguage, userLanguage);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void addedLanguageCanBeAccessedThroughObeoUserObject() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguageBuilder.addToDatabase(userLanguage);
    ObeoUser obeoUser = ObeoUserBuilder.buildUserFromId(samantha.getId());
    assertTrue(obeoUser.getUserLanguages().contains(userLanguage));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void canUpdateLanguageLevel() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    userLanguage.setLevel(6);
    UserLanguageBuilder.addToDatabase(userLanguage);
    assertTrue(UserLanguageBuilder.updateInDatabase(userLanguage));
    UserLanguage databaseLanguage = UserLanguageBuilder.getSpecificUserLanguage(samantha.getId(), userLanguage.getLanguage());
    assertEquals(databaseLanguage, userLanguage);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void canDeleteALanguageFromDatabaseForAUser() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguageBuilder.addToDatabase(userLanguage);
    UserLanguageBuilder.deleteLanguage(userLanguage);
    assertNull(UserLanguageBuilder.getSpecificUserLanguage(samantha.getId(), userLanguage.getLanguage()));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void canDeleteAllLanguagesForAUserFromDatabase() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserLanguage userLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguage userLanguage2 = new UserLanguage(samantha.getId(), "spanish", 5);
    UserLanguageBuilder.addToDatabase(userLanguage);
    UserLanguageBuilder.addToDatabase(userLanguage2);
    UserLanguageBuilder.deleteAllLanguagesForUser(samantha.getId());
    List<UserLanguage> userLanguages = UserLanguageBuilder.getLanguageFromId(samantha.getId());
    assertTrue(userLanguages.isEmpty());
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  // -----------------------------HOLIDAY TESTS----------------------------------------
  @Test
  public void canAddHoliday() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoHoliday obeoHoliday = new ObeoHoliday("bangkok", date, date, samantha.getId(), 50, 50,0);
    assertTrue(ObeoHolidayBuilder.addToDatabase(obeoHoliday));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void deletingUserWillRemoveAllHolidays() {
    ObeoUserBuilder.addToDatabase(samantha);

    ObeoHoliday obeoHoliday = new ObeoHoliday("london", date, date, samantha.getId(), 50, 50,0);
    ObeoHoliday obeoHoliday1 = new ObeoHoliday("nepal", date, date, samantha.getId(), 50, 50,0);
    ObeoHolidayBuilder.addToDatabase(obeoHoliday);
    ObeoHolidayBuilder.addToDatabase(obeoHoliday1);

    RecordObjectHelper.removeFromDatabase(samantha);
    List<ObeoHoliday> obeoHolidays =ObeoHolidayBuilder.getHolidaysForUserId(samantha.getId());
    assertTrue(obeoHolidays.isEmpty());
  }

  @Test
  public void addedHolidayIsInDatabase() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoHoliday obeoHoliday = new ObeoHoliday("london", date, date, samantha.getId(), 50, 50,0);
    ObeoHolidayBuilder.addToDatabase(obeoHoliday);
    ObeoHoliday obeoHoliday1 = ObeoHolidayBuilder.buildHolidayFromId(obeoHoliday.getId());
    assertEquals(obeoHoliday, obeoHoliday1);
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  // --------------------------INTERESTS TEST ----------------------------------------------

  @Test
  public void canAddInterest() {
    ObeoUserBuilder.addToDatabase(samantha);
    UserInterest samanthaInterest1 = new UserInterest(samantha.getId(), "Football");
    assertTrue(UserInterestBuilder.addToDatabase(samanthaInterest1));
    RecordObjectHelper.removeFromDatabase(samantha);
  }

  @Test
  public void deletingUserWillRemoveAllInterests() {
    ObeoUserBuilder.addToDatabase(samantha);

    UserInterest samanthaInterest1 = new UserInterest(samantha.getId(), "Basketball");
    UserInterest samanthaInterest2 = new UserInterest(samantha.getId(), "Classical");
    UserInterestBuilder.addToDatabase(samanthaInterest1);
    UserInterestBuilder.addToDatabase(samanthaInterest2);

    RecordObjectHelper.removeFromDatabase(samantha);
    List<ObeoHoliday> obeoHolidays =ObeoHolidayBuilder.getHolidaysForUserId(samantha.getId());
    assertTrue(obeoHolidays.isEmpty());
  }


  // ------------------------ ALGORITHMS TEST ----------------------------------------------

  @Test
  public void algNoMatchingInterestScoreIsLowerThanPerfectMatchScore() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    ObeoUserBuilder.addToDatabase(dave);
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterestBuilder.addToDatabase(samanthaInterest);
    UserInterest paulInterest = new UserInterest(paul.getId(), "Football");
    UserInterestBuilder.addToDatabase(paulInterest);
    UserInterest daveInterest = new UserInterest(dave.getId(), "Classical");
    UserInterestBuilder.addToDatabase(daveInterest);

    MatchedObeoUser samanthaAndPaul = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));
    MatchedObeoUser samanthaAndDave = new MatchedObeoUser(samantha, dave, 0, MatchingAlgorithm.getTotalScore(samantha, dave, 0));

    assertTrue(samanthaAndPaul.getScore() > samanthaAndDave.getScore());

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
    RecordObjectHelper.removeFromDatabase(dave);
  }

  @Test
  public void algPartialInterestScoreIsLowerTHanPerfectMatchScore() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    ObeoUserBuilder.addToDatabase(dave);
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterestBuilder.addToDatabase(samanthaInterest);
    UserInterest paulInterest = new UserInterest(paul.getId(), "Football");
    UserInterestBuilder.addToDatabase(paulInterest);
    UserInterest daveInterest = new UserInterest(dave.getId(), "Basketball");
    UserInterestBuilder.addToDatabase(daveInterest);

    MatchedObeoUser samanthaAndPaul = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));
    MatchedObeoUser samanthaAndDave = new MatchedObeoUser(samantha, dave, 0, MatchingAlgorithm.getTotalScore(samantha, dave, 0));

    assertTrue(samanthaAndPaul.getScore() > samanthaAndDave.getScore());

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
    RecordObjectHelper.removeFromDatabase(dave);
  }

  @Test
  public void algPartialScoreIsHigherThanNoMatchScore() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    ObeoUserBuilder.addToDatabase(dave);
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterestBuilder.addToDatabase(samanthaInterest);
    UserInterest paulInterest = new UserInterest(paul.getId(), "Basketball");
    UserInterestBuilder.addToDatabase(paulInterest);
    UserInterest daveInterest = new UserInterest(dave.getId(), "Classical");
    UserInterestBuilder.addToDatabase(daveInterest);

    MatchedObeoUser samanthaAndPaul = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));
    MatchedObeoUser samanthaAndDave = new MatchedObeoUser(samantha, dave, 0, MatchingAlgorithm.getTotalScore(samantha, dave, 0));

    assertTrue(samanthaAndPaul.getScore() > samanthaAndDave.getScore());

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
    RecordObjectHelper.removeFromDatabase(dave);
  }

  @Test
  public void algSizeOfPartialInterestsHaveNoPartInTheMatchingScore() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterestBuilder.addToDatabase(samanthaInterest);

    UserInterest paulInterest1 = new UserInterest(paul.getId(), "Basketball");
    UserInterestBuilder.addToDatabase(paulInterest1);

    MatchedObeoUser samanthaAndPaulOneEach = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));

    UserInterest paulInterest2 = new UserInterest(paul.getId(), "Rugby");
    UserInterestBuilder.addToDatabase(paulInterest2);

    MatchedObeoUser samanthaAndPaulOneAndTwo = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));

    assertEquals(samanthaAndPaulOneEach.getScore(), samanthaAndPaulOneAndTwo.getScore(), 0.01);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
  }

  @Test
  public void algOnePerfactMatchAndOnePartial() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterestBuilder.addToDatabase(samanthaInterest);

    UserInterest paulInterest1 = new UserInterest(paul.getId(), "Football");
    UserInterestBuilder.addToDatabase(paulInterest1);

    MatchedObeoUser samanthaAndPaulOneEach = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));

    UserInterest paulInterest2 = new UserInterest(paul.getId(), "Rugby");
    UserInterestBuilder.addToDatabase(paulInterest2);

    MatchedObeoUser samanthaAndPaulOneAndTwo = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));

    assertNotEquals(samanthaAndPaulOneEach.getScore(), samanthaAndPaulOneAndTwo.getScore(), 0.01);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
  }

  @Test
  public void algComplexTest() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);

    UserInterest samanthaInterest1 = new UserInterest(samantha.getId(), "Football");
    UserInterest samanthaInterest2 = new UserInterest(samantha.getId(), "Basketball");
    UserInterestBuilder.addToDatabase(samanthaInterest1);
    UserInterestBuilder.addToDatabase(samanthaInterest2);

    UserInterest paulInterest1 = new UserInterest(paul.getId(), "Football");
    UserInterest paulInterest2 = new UserInterest(paul.getId(), "Basketball");
    UserInterest paulInterest3 = new UserInterest(paul.getId(), "Rugby");
    UserInterestBuilder.addToDatabase(paulInterest1);
    UserInterestBuilder.addToDatabase(paulInterest2);
    UserInterestBuilder.addToDatabase(paulInterest3);

    MatchedObeoUser samanthaAndPaul = new MatchedObeoUser(paul, samantha, 0, MatchingAlgorithm.getTotalScore(paul, samantha, 0));

    assertEquals(samanthaAndPaul.getScore(), 33, 0.01);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
  }


  // ------------------------------ GPS TESTS -----------------------------------------------
  @Test
  public void gpsTestOneMile() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);

    double distance = ObeoUserBuilder.findDistanceBetween(samantha.getId(), paul.getId());

    assertTrue(distance > 0.95 && distance < 1.05);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
  }

  // ---------------------GETTING LOCAL AND TOURIST TESTS------------------------------------
  @Test
  public void localCanFindHolidayGoer() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);

    ObeoHoliday paulHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, paul.getId(),50, 50,0);
    ObeoHolidayBuilder.addToDatabase(paulHoliday);

    UserLanguage samanthaLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguage paulLanguage = new UserLanguage(paul.getId(), "english", 5);

    UserLanguageBuilder.addToDatabase(samanthaLanguage);
    UserLanguageBuilder.addToDatabase(paulLanguage);

    List<MatchedObeoUser> matchedObeoUsers = ObeoUserBuilder.findTourists(samantha);

    List<ObeoUser> tourists = matchedObeoUsers.stream().map(i -> i.getTourist()).collect(Collectors.toList());

    assertTrue(tourists.contains(paul));

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
  }

  @Test
  public void localListIsSortedBasedOnDistance() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    ObeoUser john = new ObeoUser("john", "john", "secondname",
        date, "casablanca", 50, 50, "password", null, false, "hi!");
    ObeoUserBuilder.addToDatabase(john);

    ObeoHoliday paulHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, paul.getId(),-0.179589, 51.498728,0);
    ObeoHoliday johnHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, john.getId(), 50, 50,0);
    ObeoHolidayBuilder.addToDatabase(paulHoliday);
    ObeoHolidayBuilder.addToDatabase(johnHoliday);

    UserLanguage samanthaLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguage paulLanguage = new UserLanguage(paul.getId(), "english", 5);
    UserLanguage johnLanguage = new UserLanguage(john.getId(), "english", 5);

    UserLanguageBuilder.addToDatabase(samanthaLanguage);
    UserLanguageBuilder.addToDatabase(paulLanguage);
    UserLanguageBuilder.addToDatabase(johnLanguage);

    List<MatchedObeoUser> matchedObeoUsers = ObeoUserBuilder.findTourists(samantha);
    List<ObeoUser> tourists = matchedObeoUsers.stream().map(i -> i.getTourist()).collect(Collectors.toList());

    Iterator<ObeoUser> touristIterator = tourists.iterator();
    assertTrue(touristIterator.hasNext());

    assertEquals(touristIterator.next(), paul);

    assertTrue(touristIterator.hasNext());

    assertEquals(touristIterator.next(), john);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
    RecordObjectHelper.removeFromDatabase(john);
  }

  @Test
  public void localListIsOrderByInterests() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUserBuilder.addToDatabase(paul);
    ObeoUserBuilder.addToDatabase(dave);

    UserLanguage samanthaLanguage = new UserLanguage(samantha.getId(), "english", 5);
    UserLanguage paulLanguage = new UserLanguage(paul.getId(), "english", 5);
    UserLanguage daveLanguage = new UserLanguage(dave.getId(), "english", 5);

    UserLanguageBuilder.addToDatabase(samanthaLanguage);
    UserLanguageBuilder.addToDatabase(paulLanguage);
    UserLanguageBuilder.addToDatabase(daveLanguage);

    UserInterest paulInterest = new UserInterest(paul.getId(), "Football");
    UserInterest samanthaInterest = new UserInterest(samantha.getId(), "Football");
    UserInterest daveInterest = new UserInterest(dave.getId(), "Rugby");

    UserInterestBuilder.addToDatabase(paulInterest);
    UserInterestBuilder.addToDatabase(samanthaInterest);
    UserInterestBuilder.addToDatabase(daveInterest);

    ObeoHoliday paulHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, paul.getId(),50, 50,0);
    ObeoHoliday daveHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, dave.getId(),50, 50,0);

    ObeoHolidayBuilder.addToDatabase(paulHoliday);
    ObeoHolidayBuilder.addToDatabase(daveHoliday);

    List<MatchedObeoUser> matchedObeoUsers = ObeoUserBuilder.findTourists(samantha);
    List<ObeoUser> tourists = matchedObeoUsers.stream().map(i -> i.getTourist()).collect(Collectors.toList());

    Iterator<ObeoUser> touristIterator = tourists.iterator();
    assertTrue(touristIterator.hasNext());

    assertEquals(touristIterator.next(), paul);

    assertTrue(touristIterator.hasNext());

    assertEquals(touristIterator.next(), dave);

    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(paul);
    RecordObjectHelper.removeFromDatabase(dave);
  }

  @Test
  public void distanceIsValidBetweenALocalAndAHoliday() {
    ObeoUserBuilder.addToDatabase(samantha);
    ObeoUser john = new ObeoUser("john", "john", "secondname",
        date, "santorini", 50, 50, "password", null, false, "hi!");
    ObeoUserBuilder.addToDatabase(john);

    ObeoHoliday johnHoliday = new ObeoHoliday(samantha.getHome_city(), date, date, john.getId(),  -0.179589, 51.498728,0);
    ObeoHolidayBuilder.addToDatabase(johnHoliday);

    double distance = ObeoUserBuilder.findDistanceBetweenLocalAndHoliday(samantha.getId(), johnHoliday.getId());

    assertTrue(distance > 0.95 && distance < 1.05);
    RecordObjectHelper.removeFromDatabase(samantha);
    RecordObjectHelper.removeFromDatabase(john);

  }



//  @Test
//  public void localCanFindHolidayGoer() throws Exception {
//    ObeoUser user2 = new ObeoUser("paris", "sam", "Paris","bangkok", date);
//    samantha.addToDatabase();
//    user2.addToDatabase();
//    ObeoHoliday holiday = new ObeoHoliday("bangkok", Date.valueOf("2019-05-27"), Date.valueOf("2019-05-30"), samantha.getId());
//    assertTrue(holiday.addToDatabase());
//
//    List<ObeoUser> tourists = user2.findTourists();
//    assertTrue(tourists.contains(samantha));
//
//    holiday.removeFromDatabase();
//    samantha.removeFromDatabase();
//    user2.removeFromDatabase();
//  }

//  @Test
//  public void holidayCanFindLocal() throws Exception {
//    ObeoUser user2 = new ObeoUser("paris", "sam", "Paris", "bangkok", date);
//    samantha.addToDatabase();
//    user2.addToDatabase();
//    ObeoHoliday holiday = new ObeoHoliday("bangkok", Date.valueOf("2019-05-27"), Date.valueOf("2019-05-30"), samantha.getId());
//    assertTrue(holiday.addToDatabase());
//    List<ObeoUser> locals = holiday.getLocals();
//    assertTrue(locals.contains(user2));
//    holiday.removeFromDatabase();
//    samantha.removeFromDatabase();
//    user2.removeFromDatabase();
//  }
}
