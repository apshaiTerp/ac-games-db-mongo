package com.ac.games.db;

import com.ac.games.db.mongo.MongoGamesDatabase;

/**
 * @author ac010168
 *
 */
public class MongoDBFactory {

  /**
   * Factory Creation method to generate a new MongoDB Game Database connection.
   * 
   * @param mongoHostAddress The hostAddress where the mongoDB server is running
   * @param mongoPort The port to connect to MongoDB
   * @param databaseName The database name we want to work with.
   *
   * @return A new {@link MongoGamesDatabase} object.
   */
  public final static GamesDatabase createMongoGamesDatabase(String mongoHostAddress, int mongoPort, String databaseName) {
    return new MongoGamesDatabase(mongoHostAddress, mongoPort, databaseName);
  }
}
