package com.ac.games.db.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ac.games.data.BGGGame;
import com.ac.games.data.CoolStuffIncPriceData;
import com.ac.games.data.Game;
import com.ac.games.data.GameReltn;
import com.ac.games.data.MiniatureMarketPriceData;
import com.ac.games.db.GamesDatabase;
import com.ac.games.db.exception.ConfigurationException;
import com.ac.games.db.exception.DatabaseOperationException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

/**
 * This class represents the MongoDB implementation of our {@link GamesDatabase} interface. 
 * 
 * @author ac010168
 */
public class MongoGamesDatabase implements GamesDatabase {
  
  /** Reference to the host address for mongo.  Might just be "localhost". */
  private final String mongoHostAddress;
  /** Port number used for mongo.  Is typically 27017. */
  private final int mongoPort;
  /** Database name to be connected to. */
  private final String databaseName;
  
  /** Reference to the active mongoClient. */
  private MongoClient mongoClient;
  /** Reference to the active database used by this connection. */
  private DB mongoDB;
  
  /** Global setting to help manage debug println statements */
  public static boolean debugMode = false;
  
  /**
   * Basic Constructor for a MongoGameDatabase Object.
   * @param mongoHostAddress The hostAddress where the mongoDB server is running
   * @param mongoPort The port to connect to MongoDB
   * @param databaseName The database name we want to work with.
   */
  public MongoGamesDatabase(String mongoHostAddress, int mongoPort, String databaseName) {
    this.mongoHostAddress = mongoHostAddress;
    this.mongoPort        = mongoPort;
    this.databaseName     = databaseName;
    
    setMongoClient(null);
    setMongoDB(null);
  }

  /**
   * @return the mongoHostAddress
   */
  public String getMongoHostAddress() {
    return mongoHostAddress;
  }

  /**
   * @return the mongoPort
   */
  public int getMongoPort() {
    return mongoPort;
  }

  /**
   * @return the databaseName
   */
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * @return the mongoClient
   */
  public MongoClient getMongoClient() {
    return mongoClient;
  }

  /**
   * @param mongoClient the mongoClient to set
   */
  public void setMongoClient(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  /**
   * @return the mongoDB
   */
  public DB getMongoDB() {
    return mongoDB;
  }

  /**
   * @param mongoDB the mongoDB to set
   */
  public void setMongoDB(DB mongoDB) {
    this.mongoDB = mongoDB;
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#initializeDBConnection()
   */
  public void initializeDBConnection() throws ConfigurationException {
    //Checking to see if connection is already open...
    if (mongoClient != null) {
      System.out.println ("The connection is already open, do not reset.");
      return;
    }
    
    //Initializing Database Connection Client
    try {
      mongoClient = new MongoClient(mongoHostAddress, mongoPort);
      mongoClient.setWriteConcern(WriteConcern.JOURNALED);
    } catch (Throwable t) {
      mongoClient = null;
      throw new ConfigurationException("Unable to connect to MongoDB at " + mongoHostAddress + ":" + mongoPort);      
    }

    try {
      mongoDB = mongoClient.getDB(databaseName);
    } catch (Throwable t) {
      try { mongoClient.close(); } catch (Throwable t2) { /** Ignore Errors */ }
      mongoClient = null;
      mongoDB     = null;
      throw new ConfigurationException("Unable to connect to Mongo Database [" + databaseName + "]");
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#closeDBConnection()
   */
  public void closeDBConnection() throws ConfigurationException {
    //Close the current collection
    try {
      if (mongoClient != null)
        mongoClient.close();
      mongoClient = null;
      mongoDB     = null;
    } catch (Throwable t) {
      throw new ConfigurationException("This operation has not yet been implemented");
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readBGGGameData(long)
   */
  public BGGGame readBGGGameData(long bggID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (bggID < 0)
      throw new DatabaseOperationException("The provided game object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("bgggame");
      BasicDBObject searchObject  = BGGGameConverter.convertGameToIDQuery(bggID);
      
      DBCursor cursor = gameCollection.find(searchObject);
      
      if (debugMode)
        System.out.println ("Total documents found during this query:            " + cursor.size());
      
      BGGGame game = null;
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        game = BGGGameConverter.convertMongoToGame(object);
      }
      
      if (debugMode)
        System.out.println ("The game found by this query was:                   " + (game == null ? "Nothing Found" : game.getName()));
      return game;
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertBGGGameData(com.ac.games.data.BGGGame)
   */
  public void insertBGGGameData(BGGGame game) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (game == null)
      throw new DatabaseOperationException("The provided game object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Because we are using non-Mongo based primary keys, we need to specifically check first to see if the object
      //exists, and if it does, we need to do an update instead
      ObjectId prevDocID = queryForExistingBGGDocID(game.getBggID());
      if (prevDocID != null) {
        if (debugMode)
          System.out.println ("Converting insert into update because of prior document");
        updateBGGGameData(game);
        return;
      }
      
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("bgggame");
      
      BasicDBObject addObject = BGGGameConverter.convertGameToMongo(game);
      WriteResult result = gameCollection.insert(addObject);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this insert converted to an upsert?             " + result.isUpdateOfExisting());
        System.out.println ("The new document _id value added:                   " + addObject.get("_id"));
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this insert: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the insert", t);
    }
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateBGGGameData(com.ac.games.data.BGGGame)
   */
  public void updateBGGGameData(BGGGame game) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (game == null)
      throw new DatabaseOperationException("The provided game object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("bgggame");
      BasicDBObject queryObject  = BGGGameConverter.convertGameToIDQuery(game);
      BasicDBObject updateObject = BGGGameConverter.convertGameToMongo(game);
      WriteResult result = gameCollection.update(queryObject, updateObject, true, false);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this update converted to an insert?             " + !result.isUpdateOfExisting());
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this update: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the update", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteBGGGameData(long)
   */
  public void deleteBGGGameData(long bggID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (bggID < 0)
      throw new DatabaseOperationException("The provided game object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("bgggame");
      BasicDBObject deleteObject  = BGGGameConverter.convertGameToIDQuery(bggID);
      WriteResult result = gameCollection.remove(deleteObject);
      
      if (debugMode)
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }
  
  /**
   * We're going to use this method to avoid the work of running our queries.  Because of reliance
   * on this method in update and insert tasks, we can assume only one other version of this object
   * can exist in the database.
   * 
   * @param bggID The game we want to verify if it exists or not.
   * 
   * @return The "_id" key from our existing object, or null if not found
   */
  private ObjectId queryForExistingBGGDocID(long bggID) throws MongoException {
    //Open the collection, i.e. table
    DBCollection gameCollection = mongoDB.getCollection("bgggame");
    BasicDBObject searchObject  = BGGGameConverter.convertGameToIDQuery(bggID);
    
    DBCursor cursor = gameCollection.find(searchObject);
    ObjectId docID = null;
    while (cursor.hasNext()) {
      docID = (ObjectId)cursor.next().get("_id");
    }
    return docID;
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readCSIPriceData(long)
   */
  public CoolStuffIncPriceData readCSIPriceData(long csiID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (csiID < 0)
      throw new DatabaseOperationException("The provided price data object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("csidata");
      BasicDBObject searchObject  = CSIDataConverter.convertCSIToIDQuery(csiID);
      
      DBCursor cursor = gameCollection.find(searchObject);
      
      if (debugMode)
        System.out.println ("Total documents found during this query:            " + cursor.size());
      
      CoolStuffIncPriceData data = null;
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        data = CSIDataConverter.convertMongoToCSI(object);
      }
      
      if (debugMode)
        System.out.println ("The game found by this query was:                   " + (data == null ? "Nothing Found" : data.getTitle()));
      return data;
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertCSIPriceData(com.ac.games.data.CoolStuffIncPriceData)
   */
  public void insertCSIPriceData(CoolStuffIncPriceData csiData) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (csiData == null)
      throw new DatabaseOperationException("The provided price data object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Because we are using non-Mongo based primary keys, we need to specifically check first to see if the object
      //exists, and if it does, we need to do an update instead
      ObjectId prevDocID = queryForExistingCSIDocID(csiData.getCsiID());
      if (prevDocID != null) {
        if (debugMode)
          System.out.println ("Converting insert into update because of prior document");
        updateCSIPriceData(csiData);
        return;
      }
      
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("csidata");
      
      BasicDBObject addObject = CSIDataConverter.convertCSIToMongo(csiData);
      WriteResult result = gameCollection.insert(addObject);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this insert converted to an upsert?             " + result.isUpdateOfExisting());
        System.out.println ("The new document _id value added:                   " + addObject.get("_id"));
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this insert: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the insert", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateCSIPriceData(com.ac.games.data.CoolStuffIncPriceData)
   */
  public void updateCSIPriceData(CoolStuffIncPriceData csiData) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (csiData == null)
      throw new DatabaseOperationException("The provided price data object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("csidata");
      BasicDBObject queryObject  = CSIDataConverter.convertCSIToIDQuery(csiData);
      BasicDBObject updateObject = CSIDataConverter.convertCSIToMongo(csiData);
      WriteResult result = gameCollection.update(queryObject, updateObject, true, false);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this update converted to an insert?             " + !result.isUpdateOfExisting());
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this update: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the update", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteCSIPriceData(long)
   */
  public void deleteCSIPriceData(long csiID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (csiID < 0)
      throw new DatabaseOperationException("The provided price data object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("csidata");
      BasicDBObject deleteObject  = CSIDataConverter.convertCSIToIDQuery(csiID);
      WriteResult result = gameCollection.remove(deleteObject);
      
      if (debugMode)
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }

  /**
   * We're going to use this method to avoid the work of running our queries.  Because of reliance
   * on this method in update and insert tasks, we can assume only one other version of this object
   * can exist in the database.
   * 
   * @param csiID The game we want to verify if it exists or not.
   * 
   * @return The "_id" key from our existing object, or null if not found
   */
  private ObjectId queryForExistingCSIDocID(long csiID) throws MongoException {
    //Open the collection, i.e. table
    DBCollection gameCollection = mongoDB.getCollection("csidata");
    BasicDBObject searchObject  = CSIDataConverter.convertCSIToIDQuery(csiID);
    
    DBCursor cursor = gameCollection.find(searchObject);
    ObjectId docID = null;
    while (cursor.hasNext()) {
      docID = (ObjectId)cursor.next().get("_id");
    }
    return docID;
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readMMPriceData(long)
   */
  public MiniatureMarketPriceData readMMPriceData(long mmID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (mmID < 0)
      throw new DatabaseOperationException("The provided price data object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("mmdata");
      BasicDBObject searchObject  = MMDataConverter.convertMMToIDQuery(mmID);
      
      DBCursor cursor = gameCollection.find(searchObject);
      
      if (debugMode)
        System.out.println ("Total documents found during this query:            " + cursor.size());
      
      MiniatureMarketPriceData data = null;
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        data = MMDataConverter.convertMongoToMM(object);
      }
      
      if (debugMode)
        System.out.println ("The game found by this query was:                   " + (data == null ? "Nothing Found" : data.getTitle()));
      return data;
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertMMPriceData(com.ac.games.data.MiniatureMarketPriceData)
   */
  public void insertMMPriceData(MiniatureMarketPriceData mmData) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (mmData == null)
      throw new DatabaseOperationException("The provided price data object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Because we are using non-Mongo based primary keys, we need to specifically check first to see if the object
      //exists, and if it does, we need to do an update instead
      ObjectId prevDocID = queryForExistingMMDocID(mmData.getMmID());
      if (prevDocID != null) {
        if (debugMode)
          System.out.println ("Converting insert into update because of prior document");
        updateMMPriceData(mmData);
        return;
      }
      
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("mmdata");
      
      BasicDBObject addObject = MMDataConverter.convertMMToMongo(mmData);
      WriteResult result = gameCollection.insert(addObject);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this insert converted to an upsert?             " + result.isUpdateOfExisting());
        System.out.println ("The new document _id value added:                   " + addObject.get("_id"));
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this insert: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the insert", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateMMPriceData(com.ac.games.data.MiniatureMarketPriceData)
   */
  public void updateMMPriceData(MiniatureMarketPriceData mmData) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (mmData == null)
      throw new DatabaseOperationException("The provided price data object was null.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("mmdata");
      BasicDBObject queryObject  = MMDataConverter.convertMMToIDQuery(mmData);
      BasicDBObject updateObject = MMDataConverter.convertMMToMongo(mmData);
      WriteResult result = gameCollection.update(queryObject, updateObject, true, false);
      
      if (debugMode) {
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
        System.out.println ("Was this update converted to an insert?             " + !result.isUpdateOfExisting());
      }
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this update: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the update", t);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteMMPriceData(long)
   */
  public void deleteMMPriceData(long mmID) throws ConfigurationException, DatabaseOperationException {
    //Check basic pre-conditions
    if (mmID < 0)
      throw new DatabaseOperationException("The provided price data object was not valid.");
    
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    //Run the operation
    try {
      //Open the collection, i.e. table
      DBCollection gameCollection = mongoDB.getCollection("mmdata");
      BasicDBObject deleteObject  = MMDataConverter.convertMMToIDQuery(mmID);
      WriteResult result = gameCollection.remove(deleteObject);
      
      if (debugMode)
        System.out.println ("The number of documents impacted by this operation: " + result.getN());
      
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this delete: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the delete", t);
    }
  }

  /**
   * We're going to use this method to avoid the work of running our queries.  Because of reliance
   * on this method in update and insert tasks, we can assume only one other version of this object
   * can exist in the database.
   * 
   * @param mmID The game we want to verify if it exists or not.
   * 
   * @return The "_id" key from our existing object, or null if not found
   */
  private ObjectId queryForExistingMMDocID(long mmID) throws MongoException {
    //Open the collection, i.e. table
    DBCollection gameCollection = mongoDB.getCollection("mmdata");
    BasicDBObject searchObject  = MMDataConverter.convertMMToIDQuery(mmID);
    
    DBCursor cursor = gameCollection.find(searchObject);
    ObjectId docID = null;
    while (cursor.hasNext()) {
      docID = (ObjectId)cursor.next().get("_id");
    }
    return docID;
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#getBggIDList()
   */
  public List<Long> getBggIDList() throws ConfigurationException, DatabaseOperationException {
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    List<Long> resultList = new LinkedList<Long>();
    try {
      DBCollection gameCollection = mongoDB.getCollection("bgggame");
      //Limit the result set to bggIDs
      BasicDBObject columns = new BasicDBObject();
      columns.put("bggID", 1);
      columns.put("_id", 0);
      //Search for all documents, return only the bggID values
      DBCursor cursor = gameCollection.find(new BasicDBObject(), columns); 
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        long curBGGID = (Long)object.get("bggID");
        if (!resultList.contains(curBGGID))
          resultList.add(curBGGID);
      }
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this select: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the select", t);
    }
    
    return resultList;
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#getCSIIDList()
   */
  public List<Long> getCSIIDList() throws ConfigurationException, DatabaseOperationException {
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    List<Long> resultList = new LinkedList<Long>();
    try {
      DBCollection gameCollection = mongoDB.getCollection("csidata");
      //Limit the result set to bggIDs
      BasicDBObject columns = new BasicDBObject();
      columns.put("csiID", 1);
      columns.put("_id", 0);
      //Search for all documents, return only the bggID values
      DBCursor cursor = gameCollection.find(new BasicDBObject(), columns); 
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        long curCSIID = (Long)object.get("csiID");
        if (!resultList.contains(curCSIID))
          resultList.add(curCSIID);
      }
    } catch (MongoException me) {
      throw new DatabaseOperationException("Mongo raised an exception to this select: " + me.getMessage(), me);
    } catch (Throwable t) {
      throw new DatabaseOperationException("Something bad happened executing the select", t);
    }
    
    return resultList;
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#getMMIDList()
   */
  public List<Long> getMMIDList() throws ConfigurationException, DatabaseOperationException {
    if (mongoClient == null || mongoDB == null)
      throw new ConfigurationException("There is a problem with the database connection.");
    
    List<Long> resultList = new LinkedList<Long>();
    try {
      DBCollection gameCollection = mongoDB.getCollection("mmdata");
      //Limit the result set to mmIDs
      BasicDBObject columns = new BasicDBObject();
      columns.put("mmID", 1);
      columns.put("_id", 0);
      //Search for all documents, return only the bggID values
      DBCursor cursor = gameCollection.find(new BasicDBObject(), columns); 
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        long curMMID = (Long)object.get("mmID");
        if (!resultList.contains(curMMID))
          resultList.add(curMMID);
      }
    } catch (MongoException me) {
      me.printStackTrace();
      throw new DatabaseOperationException("Mongo raised an exception to this select: " + me.getMessage(), me);
    } catch (Throwable t) {
      t.printStackTrace();
      throw new DatabaseOperationException("Something bad happened executing the select", t);
    }
    
    return resultList;
  }

  public Game getGame(long gameID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    return null;
  }

  public void insertGame(Game game) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }

  public void updateGame(Game game) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }

  public void deleteGame(long gameID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }

  public GameReltn getGameReltn(long gameID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    return null;
  }

  public void insertGameReltn(GameReltn gameReltn) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }

  public void updateGameReltn(GameReltn gameReltn) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }

  public void deleteGameReltn(long reltnID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    
  }
}
