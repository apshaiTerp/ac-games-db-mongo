package com.ac.games.db.test;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.ac.games.data.BGGGame;
import com.ac.games.data.CoolStuffIncPriceData;
import com.ac.games.data.Game;
import com.ac.games.data.GameAvailability;
import com.ac.games.data.GameReltn;
import com.ac.games.data.MiniatureMarketPriceData;
import com.ac.games.db.GamesDatabase;
import com.ac.games.db.MongoDBFactory;
import com.ac.games.db.exception.ConfigurationException;
import com.ac.games.db.exception.DatabaseOperationException;
import com.ac.games.db.mock.MockDataFactory;

/**
 * @author ac010168
 *
 */
public class TestMongoGamesDatabase extends TestCase {

  /** Reference to the host address for mongo. */
  protected final String mongoHostAddress     = "localhost";
  /** Reference to the true remote host for mongo */
  protected final String mongoTrueHostAddress = "107.188.249.238";
  /** Port number used for mongo. */
  protected final int mongoPort               = 27017;
  /** Database name to be connected to. */
  protected final String databaseName         = "mockDB";
  
  /** The database to be used during the tests */
  private GamesDatabase database;
  /*
   * (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception {
    //Attempting to initialize Database connection for test
    System.out.println ("Initializing Database Connection...");
    
    database = MongoDBFactory.createMongoGamesDatabase(mongoHostAddress, mongoPort, databaseName);
    //database = MongoDBFactory.createMongoGamesDatabase(mongoTrueHostAddress, mongoPort, databaseName);
    database.initializeDBConnection();
  }
  
  /*
   * (non-Javadoc)
   * @see junit.framework.TestCase#tearDown()
   */
  @Override
  protected void tearDown() throws Exception {
    //Closing Database connection for test
    System.out.println ("Initializing Database Disconnect...");
    
    database.closeDBConnection();
  }
  
  /**
   * Method to test features of BGG Data operations.  The basic steps of this test are:
   * <ol>
   * <li>Insert Cosmic Encounter</li>
   * <li>Insert Cosmic Incursion</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Cosmic Incursion and Verify</li>
   * <li>Reinsert Cosmic Encounter</li>
   * <li>Upsert Abyss</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Abyss and Verify</li>
   * <li>Modify Abyss Data</li>
   * <li>Update Abyss</li>
   * <li>Read Abyss and Verify</li>
   * <li>Run the IDs select and verify all three games found</li>
   * <li>Run the Max ID Query and verify that the largest id value is returned</li>
   * <li>Run the Count Query and Verify we got three games</li>
   * <li>Delete Cosmic Encounter and Cosmic Incursion</li>
   * <li>Read Nothing for two games, verify Abyss still exists</li>
   * <li>Delete Abyss</li>
   * <li>Test Complete</li></ol>
   */
  @Test
  public void testBGGData() {
    //MongoGamesDatabase.debugMode = true;
    try {
      //Insert Cosmic Encounter
      System.out.println ("===  Insert Cosmic Encounter  ===");
      BGGGame cosmicEncounter = MockDataFactory.createBGGGame(MockDataFactory.BGG_COSMIC_ENCOUNTER_ID);
      database.insertBGGGameData(cosmicEncounter);
      
      //Insert Cosmic Incursion
      System.out.println ("===  Insert Cosmic Incursion  ===");
      BGGGame cosmicIncursion = MockDataFactory.createBGGGame(MockDataFactory.BGG_COSMIC_INCURSION_ID);
      database.insertBGGGameData(cosmicIncursion);

      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      BGGGame cosmicEncounter2 = database.readBGGGameData(cosmicEncounter.getBggID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter2);
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter2.getBggID());
      assertTrue("The names are not equal", cosmicEncounter.getName().equalsIgnoreCase(cosmicEncounter2.getName()));
      assertTrue("The bggRatings are not equal", cosmicEncounter.getBggRating() == cosmicEncounter2.getBggRating());
      assertTrue("The publishers lists are not equal", cosmicEncounter.getPublishers().size() == cosmicEncounter2.getPublishers().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getExpansionIDs().size() == cosmicEncounter2.getExpansionIDs().size());
      assertTrue("The GameTypes are not equal", cosmicEncounter.getGameType() == cosmicEncounter2.getGameType());
      
      //Read Cosmic Incursion and Verify
      System.out.println ("===  Read Cosmic Incursion and Verify  ===");
      BGGGame cosmicIncursion2 = database.readBGGGameData(cosmicIncursion.getBggID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicIncursion2);
      assertTrue("The bggIDs are not equal", cosmicIncursion.getBggID() == cosmicIncursion2.getBggID());
      assertTrue("The names are not equal", cosmicIncursion.getName().equalsIgnoreCase(cosmicIncursion2.getName()));
      assertTrue("The bggRatings are not equal", cosmicIncursion.getBggRating() == cosmicIncursion2.getBggRating());
      assertTrue("The publishers lists are not equal", cosmicIncursion.getPublishers().size() == cosmicIncursion2.getPublishers().size());
      assertTrue("The parentGameIDs are not equal", cosmicIncursion.getParentGameID() == cosmicIncursion2.getParentGameID());
      assertTrue("The GameTypes are not equal", cosmicIncursion.getGameType() == cosmicIncursion2.getGameType());
      
      //Reinsert Cosmic Encounter
      System.out.println ("===  Reinsert Cosmic Encounter  ===");
      database.insertBGGGameData(cosmicEncounter);
      
      //Upsert Abyss
      System.out.println ("===  Upsert Abyss  ===");
      BGGGame abyss = MockDataFactory.createBGGGame(MockDataFactory.BGG_ABYSS_ID);
      database.updateBGGGameData(abyss);
      
      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      BGGGame cosmicEncounter3 = database.readBGGGameData(cosmicEncounter.getBggID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter3);
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter3.getBggID());
      assertTrue("The names are not equal", cosmicEncounter.getName().equalsIgnoreCase(cosmicEncounter3.getName()));
      assertTrue("The bggRatings are not equal", cosmicEncounter.getBggRating() == cosmicEncounter3.getBggRating());
      assertTrue("The publishers lists are not equal", cosmicEncounter.getPublishers().size() == cosmicEncounter3.getPublishers().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getExpansionIDs().size() == cosmicEncounter3.getExpansionIDs().size());
      assertTrue("The GameTypes are not equal", cosmicEncounter.getGameType() == cosmicEncounter3.getGameType());

      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      BGGGame abyss2 = database.readBGGGameData(abyss.getBggID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss2);
      assertTrue("The bggIDs are not equal", abyss.getBggID() == abyss2.getBggID());
      assertTrue("The names are not equal", abyss.getName().equalsIgnoreCase(abyss2.getName()));
      assertTrue("The bggRatings are not equal", abyss.getBggRating() == abyss2.getBggRating());
      assertTrue("The publishers lists are not equal", abyss.getPublishers().size() == abyss2.getPublishers().size());
      assertNull("The first expansionList was not empty", abyss.getExpansionIDs());
      assertNull("The second expansionList was not empty", abyss2.getExpansionIDs());
      assertTrue("The GameTypes are not equal", abyss.getGameType() == abyss2.getGameType());
      assertTrue("The ReviewStates are not equal", abyss.getReviewState() == abyss2.getReviewState());
      assertTrue("The addDates are not equal", abyss.getAddDate().getTime() == abyss2.getAddDate().getTime());
      assertTrue("The reviewDates are not equal", abyss.getReviewDate().getTime() == abyss2.getReviewDate().getTime());

      //Modify Abyss Data and Update
      System.out.println ("===  Modify Abyss Data and Update  ===");
      abyss.setMaxPlayingTime(abyss.getMaxPlayingTime() + 45);
      abyss.setBggRatingUsers(abyss.getBggRatingUsers() + 400);
      abyss.setBggRank(2321);
      
      //Update Abyss
      System.out.println ("===  Update Abyss  ===");
      database.updateBGGGameData(abyss);
      
      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      BGGGame abyss3 = database.readBGGGameData(abyss.getBggID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss3);
      assertTrue("The maxPlayingTimes are not equal", abyss.getMaxPlayingTime() == abyss3.getMaxPlayingTime());
      assertTrue("The bggRatingsUsers are not equal", abyss.getBggRating() == abyss3.getBggRating());
      assertTrue("The bggRanks are not equal", abyss.getBggRank() == abyss3.getBggRank());
      
      //Run the IDs select and verify all three games found
      System.out.println ("===  Run the IDs select and verify all three games found  ===");
      List<Long> bggIDList = database.getBggIDList();
      assertNotNull("I didn't get an ID List", bggIDList);
      assertTrue("My List didn't contain Cosmic Encounter", bggIDList.contains(MockDataFactory.BGG_COSMIC_ENCOUNTER_ID));
      assertTrue("My List didn't contain Cosmic Incursion", bggIDList.contains(MockDataFactory.BGG_COSMIC_INCURSION_ID));
      assertTrue("My List didn't contain Abyss", bggIDList.contains(MockDataFactory.BGG_ABYSS_ID));
      
      //Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned
      System.out.println ("===  Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned  ===");
      long maxValue = database.getMaxBGGGameID();
      assertTrue("I got a value other than I was expecting for maxValue", maxValue == MockDataFactory.BGG_ABYSS_ID);
      
      //Run the Count Query and Verify we got three games
      System.out.println ("===  Run the Count Query and Verify we got three games  ===");
      int count = database.getBGGGameCount();
      assertTrue("I got a value other than I was expecting for maxValue", count == 3);
      
      //Delete Cosmic Encounter and Cosmic Incursion
      System.out.println ("===  Delete Cosmic Encounter and Cosmic Incursion  ===");
      database.deleteBGGGameData(cosmicEncounter.getBggID());
      database.deleteBGGGameData(cosmicIncursion.getBggID());
      
      //Read Nothing for two games, verify Abyss still exists
      System.out.println ("===  Read Nothing for two games, verify Abyss still exists  ===");
      BGGGame notFound1  = database.readBGGGameData(cosmicEncounter.getBggID());
      BGGGame notFound2  = database.readBGGGameData(cosmicIncursion.getBggID());
      BGGGame foundAbyss = database.readBGGGameData(abyss.getBggID());
      
      assertNull("I shouldn't have found Cosmic Encounter, but did.", notFound1);
      assertNull("I shouldn't have found Cosmic Incursion, but did.", notFound2);
      assertNotNull("I should have found Abyss, but didn't.", foundAbyss);
      
      //Delete Abyss
      System.out.println ("===  Delete Abyss  ===");
      database.deleteBGGGameData(abyss.getBggID());
      
    } catch (ConfigurationException ce) {
      ce.printStackTrace();
      fail("I failed with a ConfigurationException: " + ce.getLocalizedMessage());
    } catch (DatabaseOperationException doe) {
      doe.printStackTrace();
      fail("I failed with a DatabaseOperationException: " + doe.getLocalizedMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      fail("I failed for some gorram reason: " + t.getLocalizedMessage());
    }
  }

  /**
   * Method to test features of CSI Data operations.  The basic steps of this test are:
   * <ol>
   * <li>Insert Cosmic Encounter</li>
   * <li>Insert Cosmic Incursion</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Cosmic Incursion and Verify</li>
   * <li>Reinsert Cosmic Encounter</li>
   * <li>Upsert Abyss</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Abyss and Verify</li>
   * <li>Modify Abyss Data</li>
   * <li>Update Abyss</li>
   * <li>Read Abyss and Verify</li>
   * <li>Run the IDs select and verify all three games found</li>
   * <li>Run the Max ID Query and verify that the largest id value is returned</li>
   * <li>Run the Count Query and Verify we got three games</li>
   * <li>Delete Cosmic Encounter and Cosmic Incursion</li>
   * <li>Read Nothing for two games, verify Abyss still exists</li>
   * <li>Delete Abyss</li>
   * <li>Test Complete</li></ol>
   */
  @Test
  public void testCSIData() {
    //MongoGamesDatabase.debugMode = true;
    try {
      //Insert Cosmic Encounter
      System.out.println ("===  Insert Cosmic Encounter  ===");
      CoolStuffIncPriceData cosmicEncounter = MockDataFactory.createCSIData(MockDataFactory.CSI_COSMIC_ENCOUNTER_ID);
      database.insertCSIPriceData(cosmicEncounter);
      
      //Insert Cosmic Incursion
      System.out.println ("===  Insert Cosmic Incursion  ===");
      CoolStuffIncPriceData cosmicIncursion = MockDataFactory.createCSIData(MockDataFactory.CSI_COSMIC_INCURSION_ID);
      database.insertCSIPriceData(cosmicIncursion);

      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      CoolStuffIncPriceData cosmicEncounter2 = database.readCSIPriceData(cosmicEncounter.getCsiID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter2);
      assertTrue("The csiIDs are not equal", cosmicEncounter.getCsiID() == cosmicEncounter2.getCsiID());
      assertTrue("The titles are not equal", cosmicEncounter.getTitle().equalsIgnoreCase(cosmicEncounter2.getTitle()));
      assertTrue("The categories are not equal", cosmicEncounter.getCategory() == cosmicEncounter2.getCategory());
      assertTrue("The skus are not equal", cosmicEncounter.getSku().equalsIgnoreCase(cosmicEncounter2.getSku()));
      assertTrue("The curPrices are not equal", cosmicEncounter.getCurPrice() == cosmicEncounter2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicEncounter.getAvailability() == cosmicEncounter2.getAvailability());
      
      //Read Cosmic Incursion and Verify
      System.out.println ("===  Read Cosmic Incursion and Verify  ===");
      CoolStuffIncPriceData cosmicIncursion2 = database.readCSIPriceData(cosmicIncursion.getCsiID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicIncursion2);
      assertTrue("The csiIDs are not equal", cosmicIncursion.getCsiID() == cosmicIncursion2.getCsiID());
      assertTrue("The titles are not equal", cosmicIncursion.getTitle().equalsIgnoreCase(cosmicIncursion2.getTitle()));
      assertTrue("The categories are not equal", cosmicIncursion.getCategory() == cosmicIncursion2.getCategory());
      assertTrue("The skus are not equal", cosmicIncursion.getSku().equalsIgnoreCase(cosmicIncursion2.getSku()));
      assertTrue("The curPrices are not equal", cosmicIncursion.getCurPrice() == cosmicIncursion2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicIncursion.getAvailability() == cosmicIncursion2.getAvailability());
      
      //Reinsert Cosmic Encounter
      System.out.println ("===  Reinsert Cosmic Encounter  ===");
      database.insertCSIPriceData(cosmicEncounter);
      
      //Upsert Abyss
      System.out.println ("===  Upsert Abyss  ===");
      CoolStuffIncPriceData abyss = MockDataFactory.createCSIData(MockDataFactory.CSI_ABYSS_ID);
      database.updateCSIPriceData(abyss);
      
      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      CoolStuffIncPriceData cosmicEncounter3 = database.readCSIPriceData(cosmicEncounter.getCsiID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter3);
      assertTrue("The csiIDs are not equal", cosmicEncounter.getCsiID() == cosmicEncounter3.getCsiID());
      assertTrue("The titles are not equal", cosmicEncounter.getTitle().equalsIgnoreCase(cosmicEncounter3.getTitle()));
      assertTrue("The categories are not equal", cosmicEncounter.getCategory() == cosmicEncounter3.getCategory());
      assertTrue("The skus are not equal", cosmicEncounter.getSku().equalsIgnoreCase(cosmicEncounter3.getSku()));
      assertTrue("The curPrices are not equal", cosmicEncounter.getCurPrice() == cosmicEncounter3.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicEncounter.getAvailability() == cosmicEncounter3.getAvailability());

      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      CoolStuffIncPriceData abyss2 = database.readCSIPriceData(abyss.getCsiID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss2);
      assertTrue("The csiIDs are not equal", abyss.getCsiID() == abyss2.getCsiID());
      assertTrue("The titles are not equal", abyss.getTitle().equalsIgnoreCase(abyss2.getTitle()));
      assertTrue("The skus are not equal", abyss.getSku().equalsIgnoreCase(abyss2.getSku()));
      assertTrue("The categories are not equal", abyss.getCategory() == abyss2.getCategory());
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", abyss.getAvailability() == abyss2.getAvailability());
      assertTrue("The ReviewStates are not equal", abyss.getReviewState() == abyss2.getReviewState());
      assertTrue("The addDates are not equal", abyss.getAddDate().getTime() == abyss2.getAddDate().getTime());
      assertTrue("The reviewDates are not equal", abyss.getReviewDate().getTime() == abyss2.getReviewDate().getTime());

      //Modify Abyss Data and Update
      System.out.println ("===  Modify Abyss Data and Update  ===");
      abyss.setCurPrice(abyss.getCurPrice() + 12.75);
      abyss.setAvailability(GameAvailability.PREORDER);
      
      //Update Abyss
      System.out.println ("===  Update Abyss  ===");
      database.updateCSIPriceData(abyss);
      
      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      CoolStuffIncPriceData abyss3 = database.readCSIPriceData(abyss.getCsiID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss3);
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss3.getCurPrice());
      assertTrue("The availabilities are not equal", abyss.getAvailability() == abyss3.getAvailability());
      
      //Run the IDs select and verify all three games found
      System.out.println ("===  Run the IDs select and verify all three games found  ===");
      List<Long> csiIDList = database.getCSIIDList();
      assertNotNull("I didn't get an ID List", csiIDList);
      assertTrue("My List didn't contain Cosmic Encounter", csiIDList.contains(MockDataFactory.CSI_COSMIC_ENCOUNTER_ID));
      assertTrue("My List didn't contain Cosmic Incursion", csiIDList.contains(MockDataFactory.CSI_COSMIC_INCURSION_ID));
      assertTrue("My List didn't contain Abyss", csiIDList.contains(MockDataFactory.CSI_ABYSS_ID));
      
      //Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned
      System.out.println ("===  Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned  ===");
      long maxValue = database.getMaxCSIDataID();
      assertTrue("I got a value other than I was expecting for maxValue", maxValue == MockDataFactory.CSI_ABYSS_ID);
      
      //Run the Count Query and Verify we got three games
      System.out.println ("===  Run the Count Query and Verify we got three games  ===");
      int count = database.getCSIDataCount();
      assertTrue("I got a value other than I was expecting for maxValue", count == 3);

      //Delete Cosmic Encounter and Cosmic Incursion
      System.out.println ("===  Delete Cosmic Encounter and Cosmic Incursion  ===");
      database.deleteCSIPriceData(cosmicEncounter.getCsiID());
      database.deleteCSIPriceData(cosmicIncursion.getCsiID());
      
      //Read Nothing for two games, verify Abyss still exists
      System.out.println ("===  Read Nothing for two games, verify Abyss still exists  ===");
      CoolStuffIncPriceData notFound1  = database.readCSIPriceData(cosmicEncounter.getCsiID());
      CoolStuffIncPriceData notFound2  = database.readCSIPriceData(cosmicIncursion.getCsiID());
      CoolStuffIncPriceData foundAbyss = database.readCSIPriceData(abyss.getCsiID());
      
      assertNull("I shouldn't have found Cosmic Encounter, but did.", notFound1);
      assertNull("I shouldn't have found Cosmic Incursion, but did.", notFound2);
      assertNotNull("I should have found Abyss, but didn't.", foundAbyss);
      
      //Delete Abyss
      System.out.println ("===  Delete Abyss  ===");
      database.deleteCSIPriceData(abyss.getCsiID());
      
    } catch (ConfigurationException ce) {
      ce.printStackTrace();
      fail("I failed with a ConfigurationException: " + ce.getLocalizedMessage());
    } catch (DatabaseOperationException doe) {
      doe.printStackTrace();
      fail("I failed with a DatabaseOperationException: " + doe.getLocalizedMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      fail("I failed for some gorram reason: " + t.getLocalizedMessage());
    }
  }

  /**
   * Method to test features of CSI Data operations.  The basic steps of this test are:
   * <ol>
   * <li>Insert Cosmic Encounter</li>
   * <li>Insert Cosmic Incursion</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Cosmic Incursion and Verify</li>
   * <li>Reinsert Cosmic Encounter</li>
   * <li>Upsert Abyss</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Abyss and Verify</li>
   * <li>Modify Abyss Data</li>
   * <li>Update Abyss</li>
   * <li>Read Abyss and Verify</li>
   * <li>Run the IDs select and verify all three games found</li>
   * <li>Run the Max ID Query and verify that the largest id value is returned</li>
   * <li>Run the Count Query and Verify we got three games</li>
   * <li>Delete Cosmic Encounter and Cosmic Incursion</li>
   * <li>Read Nothing for two games, verify Abyss still exists</li>
   * <li>Delete Abyss</li>
   * <li>Test Complete</li></ol>
   */
  @Test
  public void testMMData() {
    //MongoGamesDatabase.debugMode = true;
    try {
      //Insert Cosmic Encounter
      System.out.println ("===  Insert Cosmic Encounter  ===");
      MiniatureMarketPriceData cosmicEncounter = MockDataFactory.createMMData(MockDataFactory.MM_COSMIC_ENCOUNTER_ID);
      database.insertMMPriceData(cosmicEncounter);
      
      //Insert Cosmic Incursion
      System.out.println ("===  Insert Cosmic Incursion  ===");
      MiniatureMarketPriceData cosmicIncursion = MockDataFactory.createMMData(MockDataFactory.MM_COSMIC_INCURSION_ID);
      database.insertMMPriceData(cosmicIncursion);

      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      MiniatureMarketPriceData cosmicEncounter2 = database.readMMPriceData(cosmicEncounter.getMmID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter2);
      assertTrue("The mmIDs are not equal", cosmicEncounter.getMmID() == cosmicEncounter2.getMmID());
      assertTrue("The titles are not equal", cosmicEncounter.getTitle().equalsIgnoreCase(cosmicEncounter2.getTitle()));
      assertTrue("The categories are not equal", cosmicEncounter.getCategory() == cosmicEncounter2.getCategory());
      assertTrue("The skus are not equal", cosmicEncounter.getSku().equalsIgnoreCase(cosmicEncounter2.getSku()));
      assertTrue("The curPrices are not equal", cosmicEncounter.getCurPrice() == cosmicEncounter2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicEncounter.getAvailability() == cosmicEncounter2.getAvailability());
      
      //Read Cosmic Incursion and Verify
      System.out.println ("===  Read Cosmic Incursion and Verify  ===");
      MiniatureMarketPriceData cosmicIncursion2 = database.readMMPriceData(cosmicIncursion.getMmID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicIncursion2);
      assertTrue("The mmIDs are not equal", cosmicIncursion.getMmID() == cosmicIncursion2.getMmID());
      assertTrue("The titles are not equal", cosmicIncursion.getTitle().equalsIgnoreCase(cosmicIncursion2.getTitle()));
      assertTrue("The categories are not equal", cosmicIncursion.getCategory() == cosmicIncursion2.getCategory());
      assertTrue("The skus are not equal", cosmicIncursion.getSku().equalsIgnoreCase(cosmicIncursion2.getSku()));
      assertTrue("The curPrices are not equal", cosmicIncursion.getCurPrice() == cosmicIncursion2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicIncursion.getAvailability() == cosmicIncursion2.getAvailability());
      
      //Reinsert Cosmic Encounter
      System.out.println ("===  Reinsert Cosmic Encounter  ===");
      database.insertMMPriceData(cosmicEncounter);
      
      //Upsert Abyss
      System.out.println ("===  Upsert Abyss  ===");
      MiniatureMarketPriceData abyss = MockDataFactory.createMMData(MockDataFactory.MM_ABYSS_ID);
      database.updateMMPriceData(abyss);
      
      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      MiniatureMarketPriceData cosmicEncounter3 = database.readMMPriceData(cosmicEncounter.getMmID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter3);
      assertTrue("The mmIDs are not equal", cosmicEncounter.getMmID() == cosmicEncounter3.getMmID());
      assertTrue("The titles are not equal", cosmicEncounter.getTitle().equalsIgnoreCase(cosmicEncounter3.getTitle()));
      assertTrue("The categories are not equal", cosmicEncounter.getCategory() == cosmicEncounter3.getCategory());
      assertTrue("The skus are not equal", cosmicEncounter.getSku().equalsIgnoreCase(cosmicEncounter3.getSku()));
      assertTrue("The curPrices are not equal", cosmicEncounter.getCurPrice() == cosmicEncounter3.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", cosmicEncounter.getAvailability() == cosmicEncounter3.getAvailability());

      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      MiniatureMarketPriceData abyss2 = database.readMMPriceData(abyss.getMmID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss2);
      assertTrue("The mmIDs are not equal", abyss.getMmID() == abyss2.getMmID());
      assertTrue("The titles are not equal", abyss.getTitle().equalsIgnoreCase(abyss2.getTitle()));
      assertTrue("The categories are not equal", abyss.getCategory() == abyss2.getCategory());
      assertTrue("The skus are not equal", abyss.getSku().equalsIgnoreCase(abyss2.getSku()));
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", abyss.getAvailability() == abyss2.getAvailability());
      assertTrue("The ReviewStates are not equal", abyss.getReviewState() == abyss2.getReviewState());
      assertTrue("The addDates are not equal", abyss.getAddDate().getTime() == abyss2.getAddDate().getTime());
      assertTrue("The reviewDates are not equal", abyss.getReviewDate().getTime() == abyss2.getReviewDate().getTime());

      //Modify Abyss Data and Update
      System.out.println ("===  Modify Abyss Data and Update  ===");
      abyss.setCurPrice(abyss.getCurPrice() + 12.75);
      abyss.setAvailability(GameAvailability.PREORDER);
      
      //Update Abyss
      System.out.println ("===  Update Abyss  ===");
      database.updateMMPriceData(abyss);
      
      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      MiniatureMarketPriceData abyss3 = database.readMMPriceData(abyss.getMmID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss3);
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss3.getCurPrice());
      assertTrue("The availabilities are not equal", abyss.getAvailability() == abyss3.getAvailability());
      
      //Run the IDs select and verify all three games found
      System.out.println ("===  Run the IDs select and verify all three games found  ===");
      List<Long> mmIDList = database.getMMIDList();
      assertNotNull("I didn't get an ID List", mmIDList);
      assertTrue("My List didn't contain Cosmic Encounter", mmIDList.contains(MockDataFactory.MM_COSMIC_ENCOUNTER_ID));
      assertTrue("My List didn't contain Cosmic Incursion", mmIDList.contains(MockDataFactory.MM_COSMIC_INCURSION_ID));
      assertTrue("My List didn't contain Abyss", mmIDList.contains(MockDataFactory.MM_ABYSS_ID));

      //Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned
      System.out.println ("===  Run the Max ID Query and verify that the largest id value (in this case, Abyss) is returned  ===");
      long maxValue = database.getMaxMMDataID();
      assertTrue("I got a value other than I was expecting for maxValue", maxValue == MockDataFactory.MM_ABYSS_ID);
      
      //Run the Count Query and Verify we got three games
      System.out.println ("===  Run the Count Query and Verify we got three games  ===");
      int count = database.getMMDataCount();
      assertTrue("I got a value other than I was expecting for maxValue", count == 3);

      //Delete Cosmic Encounter and Cosmic Incursion
      System.out.println ("===  Delete Cosmic Encounter and Cosmic Incursion  ===");
      database.deleteMMPriceData(cosmicEncounter.getMmID());
      database.deleteMMPriceData(cosmicIncursion.getMmID());
      
      //Read Nothing for two games, verify Abyss still exists
      System.out.println ("===  Read Nothing for two games, verify Abyss still exists  ===");
      MiniatureMarketPriceData notFound1  = database.readMMPriceData(cosmicEncounter.getMmID());
      MiniatureMarketPriceData notFound2  = database.readMMPriceData(cosmicIncursion.getMmID());
      MiniatureMarketPriceData foundAbyss = database.readMMPriceData(abyss.getMmID());
      
      assertNull("I shouldn't have found Cosmic Encounter, but did.", notFound1);
      assertNull("I shouldn't have found Cosmic Incursion, but did.", notFound2);
      assertNotNull("I should have found Abyss, but didn't.", foundAbyss);
      
      //Delete Abyss
      System.out.println ("===  Delete Abyss  ===");
      database.deleteMMPriceData(abyss.getMmID());
      
    } catch (ConfigurationException ce) {
      ce.printStackTrace();
      fail("I failed with a ConfigurationException: " + ce.getLocalizedMessage());
    } catch (DatabaseOperationException doe) {
      doe.printStackTrace();
      fail("I failed with a DatabaseOperationException: " + doe.getLocalizedMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      fail("I failed for some gorram reason: " + t.getLocalizedMessage());
    }
  }

  /**
   * Method to test features of BGG Data operations.  The basic steps of this test are:
   * <ol>
   * <li>Insert Cosmic Encounter</li>
   * <li>Insert Cosmic Incursion</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Cosmic Incursion and Verify</li>
   * <li>Reinsert Cosmic Encounter</li>
   * <li>Upsert Abyss</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Abyss and Verify</li>
   * <li>Modify Abyss Data</li>
   * <li>Update Abyss</li>
   * <li>Read Abyss and Verify</li>
   * <li>Run the IDs select and verify all three games found</li>
   * <li>Run the Max ID Query and verify that the largest id value is returned</li>
   * <li>Run the Count Query and Verify we got three games</li>
   * <li>Delete Cosmic Encounter and Cosmic Incursion</li>
   * <li>Read Nothing for two games, verify Abyss still exists</li>
   * <li>Delete Abyss</li>
   * <li>Test Complete</li></ol>
   */
  @Test
  public void testGameData() {
    //MongoGamesDatabase.debugMode = true;
    try {
      //Insert Cosmic Encounter
      System.out.println ("===  Insert Cosmic Encounter  ===");
      Game cosmicEncounter = MockDataFactory.createGameData(MockDataFactory.COSMIC_ENCOUNTER_ID);
      database.insertGame(cosmicEncounter);
      
      //Insert Cosmic Incursion
      System.out.println ("===  Insert Cosmic Incursion  ===");
      Game cosmicIncursion = MockDataFactory.createGameData(MockDataFactory.COSMIC_INCURSION_ID);
      database.insertGame(cosmicIncursion);

      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      Game cosmicEncounter2 = database.readGame(cosmicEncounter.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter2);
      assertTrue("The gameIDs are not equal", cosmicEncounter.getGameID() == cosmicEncounter2.getGameID());
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter2.getBggID());
      assertTrue("The names are not equal", cosmicEncounter.getName().equalsIgnoreCase(cosmicEncounter2.getName()));
      assertTrue("The primaryPublishers are not equal", cosmicEncounter.getPrimaryPublisher().equalsIgnoreCase(cosmicEncounter2.getPrimaryPublisher()));
      assertTrue("The publishers lists are not equal", cosmicEncounter.getPublishers().size() == cosmicEncounter2.getPublishers().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getExpansionIDs().size() == cosmicEncounter2.getExpansionIDs().size());
      assertTrue("The GameTypes are not equal", cosmicEncounter.getGameType() == cosmicEncounter2.getGameType());
      
      //Read Cosmic Incursion and Verify
      System.out.println ("===  Read Cosmic Incursion and Verify  ===");
      Game cosmicIncursion2 = database.readGame(cosmicIncursion.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicIncursion2);
      assertTrue("The gameIDs are not equal", cosmicIncursion.getGameID() == cosmicIncursion2.getGameID());
      assertTrue("The bggIDs are not equal", cosmicIncursion.getBggID() == cosmicIncursion2.getBggID());
      assertTrue("The names are not equal", cosmicIncursion.getName().equalsIgnoreCase(cosmicIncursion2.getName()));
      assertTrue("The primaryPublishers are not equal", cosmicIncursion.getPrimaryPublisher().equalsIgnoreCase(cosmicIncursion2.getPrimaryPublisher()));
      assertTrue("The publishers lists are not equal", cosmicIncursion.getPublishers().size() == cosmicIncursion2.getPublishers().size());
      assertTrue("The parentGameIDs are not equal", cosmicIncursion.getParentGameID() == cosmicIncursion2.getParentGameID());
      assertTrue("The GameTypes are not equal", cosmicIncursion.getGameType() == cosmicIncursion2.getGameType());
      
      //Reinsert Cosmic Encounter
      System.out.println ("===  Reinsert Cosmic Encounter  ===");
      database.insertGame(cosmicEncounter);
      
      //Upsert Abyss
      System.out.println ("===  Upsert Abyss  ===");
      Game abyss = MockDataFactory.createGameData(MockDataFactory.ABYSS_ID);
      database.updateGame(abyss);
      
      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      Game cosmicEncounter3 = database.readGame(cosmicEncounter.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter3);
      assertTrue("The gameIDs are not equal", cosmicEncounter.getGameID() == cosmicEncounter3.getGameID());
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter3.getBggID());
      assertTrue("The names are not equal", cosmicEncounter.getName().equalsIgnoreCase(cosmicEncounter3.getName()));
      assertTrue("The primaryPublishers are not equal", cosmicEncounter.getPrimaryPublisher().equalsIgnoreCase(cosmicEncounter3.getPrimaryPublisher()));
      assertTrue("The publishers lists are not equal", cosmicEncounter.getPublishers().size() == cosmicEncounter3.getPublishers().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getExpansionIDs().size() == cosmicEncounter3.getExpansionIDs().size());
      assertTrue("The GameTypes are not equal", cosmicEncounter.getGameType() == cosmicEncounter3.getGameType());

      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      Game abyss2 = database.readGame(abyss.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss2);
      assertTrue("The gameIDs are not equal", abyss.getGameID() == abyss2.getGameID());
      assertTrue("The bggIDs are not equal", abyss.getBggID() == abyss2.getBggID());
      assertTrue("The names are not equal", abyss.getName().equalsIgnoreCase(abyss2.getName()));
      assertTrue("The primaryPublishers are not equal", abyss.getPrimaryPublisher().equalsIgnoreCase(abyss2.getPrimaryPublisher()));
      assertTrue("The publishers lists are not equal", abyss.getPublishers().size() == abyss2.getPublishers().size());
      assertNull("The first expansionList was not empty", abyss.getExpansionIDs());
      assertNull("The second expansionList was not empty", abyss2.getExpansionIDs());
      assertTrue("The GameTypes are not equal", abyss.getGameType() == abyss2.getGameType());
      assertTrue("The addDates are not equal", abyss.getAddDate().getTime() == abyss2.getAddDate().getTime());

      //Modify Abyss Data and Update
      System.out.println ("===  Modify Abyss Data and Update  ===");
      abyss.setMaxPlayingTime(abyss.getMaxPlayingTime() + 45);
      
      //Update Abyss
      System.out.println ("===  Update Abyss  ===");
      database.updateGame(abyss);
      
      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      Game abyss3 = database.readGame(abyss.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss3);
      assertTrue("The maxPlayingTimes are not equal", abyss.getMaxPlayingTime() == abyss3.getMaxPlayingTime());
      
      //Run the IDs select and verify all three games found
      System.out.println ("===  Run the IDs select and verify all three games found  ===");
      List<Long> gameIDList = database.getGameIDList();
      assertNotNull("I didn't get an ID List", gameIDList);
      assertTrue("My List didn't contain Cosmic Encounter", gameIDList.contains(MockDataFactory.COSMIC_ENCOUNTER_ID));
      assertTrue("My List didn't contain Cosmic Incursion", gameIDList.contains(MockDataFactory.COSMIC_INCURSION_ID));
      assertTrue("My List didn't contain Abyss", gameIDList.contains(MockDataFactory.ABYSS_ID));
      
      //Run the Max ID Query and verify that the largest id value (in this case, Cosmic Incursion) is returned
      System.out.println ("===  Run the Max ID Query and verify that the largest id value is returned  ===");
      long maxValue = database.getMaxGameID();
      assertTrue("I got a value other than I was expecting for maxValue", maxValue == MockDataFactory.COSMIC_INCURSION_ID);
      
      //Run the Count Query and Verify we got three games
      System.out.println ("===  Run the Count Query and Verify we got three games  ===");
      int count = database.getGameCount();
      assertTrue("I got a value other than I was expecting for maxValue", count == 3);

      //Delete Cosmic Encounter and Cosmic Incursion
      System.out.println ("===  Delete Cosmic Encounter and Cosmic Incursion  ===");
      database.deleteGame(cosmicEncounter.getGameID());
      database.deleteGame(cosmicIncursion.getGameID());
      
      //Read Nothing for two games, verify Abyss still exists
      System.out.println ("===  Read Nothing for two games, verify Abyss still exists  ===");
      Game notFound1  = database.readGame(cosmicEncounter.getGameID());
      Game notFound2  = database.readGame(cosmicIncursion.getGameID());
      Game foundAbyss = database.readGame(abyss.getGameID());
      
      assertNull("I shouldn't have found Cosmic Encounter, but did.", notFound1);
      assertNull("I shouldn't have found Cosmic Incursion, but did.", notFound2);
      assertNotNull("I should have found Abyss, but didn't.", foundAbyss);
      
      //Delete Abyss
      System.out.println ("===  Delete Abyss  ===");
      database.deleteGame(abyss.getGameID());
      
    } catch (ConfigurationException ce) {
      ce.printStackTrace();
      fail("I failed with a ConfigurationException: " + ce.getLocalizedMessage());
    } catch (DatabaseOperationException doe) {
      doe.printStackTrace();
      fail("I failed with a DatabaseOperationException: " + doe.getLocalizedMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      fail("I failed for some gorram reason: " + t.getLocalizedMessage());
    }
  }
  
  /**
   * Method to test features of BGG Data operations.  The basic steps of this test are:
   * <ol>
   * <li>Insert Cosmic Encounter</li>
   * <li>Insert Cosmic Incursion</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Cosmic Incursion and Verify</li>
   * <li>Reinsert Cosmic Encounter</li>
   * <li>Upsert Abyss</li>
   * <li>Read Cosmic Encounter and Verify</li>
   * <li>Read Abyss and Verify</li>
   * <li>Modify Abyss Data</li>
   * <li>Update Abyss</li>
   * <li>Read Abyss and Verify</li>
   * <li>Run the IDs select and verify all three games found</li>
   * <li>Delete Cosmic Encounter and Cosmic Incursion</li>
   * <li>Read Nothing for two games, verify Abyss still exists</li>
   * <li>Delete Abyss</li>
   * <li>Test Complete</li></ol>
   */
  @Test
  public void testGameReltnData() {
    //MongoGamesDatabase.debugMode = true;
    try {
      //Insert Cosmic Encounter
      System.out.println ("===  Insert Cosmic Encounter  ===");
      GameReltn cosmicEncounter = MockDataFactory.createGameReltnData(MockDataFactory.COSMIC_ENCOUNTER_RELTN_ID);
      database.insertGameReltn(cosmicEncounter);
      
      //Insert Cosmic Incursion
      System.out.println ("===  Insert Cosmic Incursion  ===");
      GameReltn cosmicIncursion = MockDataFactory.createGameReltnData(MockDataFactory.COSMIC_INCURSION_RELTN_ID);
      database.insertGameReltn(cosmicIncursion);

      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      GameReltn cosmicEncounter2 = database.readGameReltn(cosmicEncounter.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter2);
      assertTrue("The reltnIDs are not equal", cosmicEncounter.getReltnID() == cosmicEncounter2.getReltnID());
      assertTrue("The gameIDs are not equal", cosmicEncounter.getGameID() == cosmicEncounter2.getGameID());
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter2.getBggID());
      assertTrue("The csiIDs are not equal", cosmicEncounter.getCsiID() == cosmicEncounter2.getCsiID());
      assertTrue("The mmIDs are not equal", cosmicEncounter.getMmID() == cosmicEncounter2.getMmID());
      assertTrue("The publishers lists are not equal", cosmicEncounter.getAsinKeys().size() == cosmicEncounter2.getAsinKeys().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getOtherSites().size() == cosmicEncounter2.getOtherSites().size());
      
      //Read Cosmic Incursion and Verify
      System.out.println ("===  Read Cosmic Incursion and Verify  ===");
      GameReltn cosmicIncursion2 = database.readGameReltn(cosmicIncursion.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicIncursion2);
      assertTrue("The reltnIDs are not equal", cosmicIncursion.getReltnID() == cosmicIncursion2.getReltnID());
      assertTrue("The gameIDs are not equal", cosmicIncursion.getGameID() == cosmicIncursion2.getGameID());
      assertTrue("The bggIDs are not equal", cosmicIncursion.getBggID() == cosmicIncursion2.getBggID());
      assertTrue("The csiIDs are not equal", cosmicIncursion.getCsiID() == cosmicIncursion2.getCsiID());
      assertTrue("The mmIDs are not equal", cosmicIncursion.getMmID() == cosmicIncursion2.getMmID());
      assertTrue("The publishers lists are not equal", cosmicIncursion.getAsinKeys().size() == cosmicIncursion2.getAsinKeys().size());
      //Should be null
      assertTrue("The expansion lists are not equal", cosmicIncursion.getOtherSites() == cosmicIncursion2.getOtherSites());
      
      //Reinsert Cosmic Encounter
      System.out.println ("===  Reinsert Cosmic Encounter  ===");
      database.insertGameReltn(cosmicEncounter);
      
      //Upsert Abyss
      System.out.println ("===  Upsert Abyss  ===");
      GameReltn abyss = MockDataFactory.createGameReltnData(MockDataFactory.ABYSS_RELTN_ID);
      database.updateGameReltn(abyss);
      
      //Read Cosmic Encounter and Verify
      System.out.println ("===  Read Cosmic Encounter and Verify  ===");
      GameReltn cosmicEncounter3 = database.readGameReltn(cosmicEncounter.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", cosmicEncounter3);
      assertTrue("The reltnIDs are not equal", cosmicEncounter.getReltnID() == cosmicEncounter3.getReltnID());
      assertTrue("The gameIDs are not equal", cosmicEncounter.getGameID() == cosmicEncounter3.getGameID());
      assertTrue("The bggIDs are not equal", cosmicEncounter.getBggID() == cosmicEncounter3.getBggID());
      assertTrue("The csiIDs are not equal", cosmicEncounter.getCsiID() == cosmicEncounter3.getCsiID());
      assertTrue("The mmIDs are not equal", cosmicEncounter.getMmID() == cosmicEncounter3.getMmID());
      assertTrue("The publishers lists are not equal", cosmicEncounter.getAsinKeys().size() == cosmicEncounter3.getAsinKeys().size());
      assertTrue("The expansion lists are not equal", cosmicEncounter.getOtherSites().size() == cosmicEncounter3.getOtherSites().size());

      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      GameReltn abyss2 = database.readGameReltn(abyss.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss2);
      assertTrue("The reltnIDs are not equal", abyss.getReltnID() == abyss2.getReltnID());
      assertTrue("The gameIDs are not equal", abyss.getGameID() == abyss2.getGameID());
      assertTrue("The bggIDs are not equal", abyss.getBggID() == abyss2.getBggID());
      assertTrue("The csiIDs are not equal", abyss.getCsiID() == abyss2.getCsiID());
      assertTrue("The mmIDs are not equal", abyss.getMmID() == abyss2.getMmID());
      assertTrue("The publishers lists are not equal", abyss.getAsinKeys().size() == abyss2.getAsinKeys().size());
      //Should be null
      assertTrue("The expansion lists are not equal", cosmicIncursion.getOtherSites() == abyss2.getOtherSites());

      //Modify Abyss Data and Update
      System.out.println ("===  Modify Abyss Data and Update  ===");
      abyss.setCsiID(MockDataFactory.CSI_ABYSS_ID + 20);
      abyss.setMmID(MockDataFactory.MM_ABYSS_ID + 20);
      
      //Update Abyss
      System.out.println ("===  Update Abyss  ===");
      database.updateGameReltn(abyss);
      
      //Read Abyss and Verify
      System.out.println ("===  Read Abyss and Verify  ===");
      GameReltn abyss3 = database.readGameReltn(abyss.getGameID());
      
      //Spot checking as opposed to all possible values.
      assertNotNull("I didn't find my result", abyss3);
      assertTrue("The csiIDs are not equal", abyss.getCsiID() == abyss3.getCsiID());
      assertTrue("The mmIDs are not equal", abyss.getMmID() == abyss3.getMmID());
      
      //Run the IDs select and verify all three games found
      System.out.println ("===  Run the IDs select and verify all three games found  ===");
      List<Long> reltnIDList = database.getGameReltnIDList();
      assertNotNull("I didn't get an ID List", reltnIDList);
      assertTrue("My List didn't contain Cosmic Encounter", reltnIDList.contains(MockDataFactory.COSMIC_ENCOUNTER_RELTN_ID));
      assertTrue("My List didn't contain Cosmic Incursion", reltnIDList.contains(MockDataFactory.COSMIC_INCURSION_RELTN_ID));
      assertTrue("My List didn't contain Abyss", reltnIDList.contains(MockDataFactory.ABYSS_RELTN_ID));
      
      //Delete Cosmic Encounter and Cosmic Incursion
      System.out.println ("===  Delete Cosmic Encounter and Cosmic Incursion  ===");
      database.deleteGameReltn(cosmicEncounter.getReltnID());
      database.deleteGameReltn(cosmicIncursion.getReltnID());
      
      //Read Nothing for two games, verify Abyss still exists
      System.out.println ("===  Read Nothing for two games, verify Abyss still exists  ===");
      GameReltn notFound1  = database.readGameReltn(cosmicEncounter.getGameID());
      GameReltn notFound2  = database.readGameReltn(cosmicIncursion.getGameID());
      GameReltn foundAbyss = database.readGameReltn(abyss.getGameID());
      
      assertNull("I shouldn't have found Cosmic Encounter, but did.", notFound1);
      assertNull("I shouldn't have found Cosmic Incursion, but did.", notFound2);
      assertNotNull("I should have found Abyss, but didn't.", foundAbyss);
      
      //Delete Abyss
      System.out.println ("===  Delete Abyss  ===");
      database.deleteGameReltn(abyss.getReltnID());
      
    } catch (ConfigurationException ce) {
      ce.printStackTrace();
      fail("I failed with a ConfigurationException: " + ce.getLocalizedMessage());
    } catch (DatabaseOperationException doe) {
      doe.printStackTrace();
      fail("I failed with a DatabaseOperationException: " + doe.getLocalizedMessage());
    } catch (Throwable t) {
      t.printStackTrace();
      fail("I failed for some gorram reason: " + t.getLocalizedMessage());
    }
  }

}
