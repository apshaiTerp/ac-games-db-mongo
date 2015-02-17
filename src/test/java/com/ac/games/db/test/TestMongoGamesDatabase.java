package com.ac.games.db.test;

import org.junit.Test;

import junit.framework.TestCase;

import com.ac.games.data.BGGGame;
import com.ac.games.data.CoolStuffIncPriceData;
import com.ac.games.data.GameAvailability;
import com.ac.games.data.MiniatureMarketPriceData;
import com.ac.games.db.GamesDatabase;
import com.ac.games.db.MongoDBFactory;
import com.ac.games.db.exception.ConfigurationException;
import com.ac.games.db.exception.DatabaseOperationException;
import com.ac.games.db.mock.MockDataFactory;
import com.ac.games.db.mongo.MongoGamesDatabase;

/**
 * @author ac010168
 *
 */
public class TestMongoGamesDatabase extends TestCase {

  /** Reference to the host address for mongo. */
  protected final String mongoHostAddress = "localhost";
  /** Port number used for mongo. */
  protected final int mongoPort           = 27017;
  /** Database name to be connected to. */
  protected final String databaseName     = "mockDB";
  
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
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", abyss.getAvailability() == abyss2.getAvailability());

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
      assertTrue("The skus are not equal", abyss.getSku().equalsIgnoreCase(abyss2.getSku()));
      assertTrue("The curPrices are not equal", abyss.getCurPrice() == abyss2.getCurPrice());
      assertTrue("The GameAvailabilities are not equal", abyss.getAvailability() == abyss2.getAvailability());

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
}
