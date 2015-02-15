package com.ac.games.db.test;

import org.junit.Test;

import junit.framework.TestCase;

import com.ac.games.data.BGGGame;
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
      database.deleteBGGGameData(cosmicEncounter);
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
      database.deleteBGGGameData(abyss);
      
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
