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
   * @return A new {@link MongoGamesDatabase} object.
   */
  public final static GamesDatabase createMongoGamesDatabase() {
    //TODO
    return new MongoGamesDatabase();
  }
}
