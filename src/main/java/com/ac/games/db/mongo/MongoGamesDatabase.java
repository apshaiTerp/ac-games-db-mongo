package com.ac.games.db.mongo;

import com.ac.games.data.BGGGame;
import com.ac.games.data.CoolStuffIncPriceData;
import com.ac.games.data.MiniatureMarketPriceData;
import com.ac.games.db.GamesDatabase;
import com.ac.games.db.exception.ConfigurationException;
import com.ac.games.db.exception.DatabaseOperationException;

/**
 * This class represents the MongoDB implementation of our {@link GamesDatabase} interface. 
 * 
 * @author ac010168
 */
public class MongoGamesDatabase implements GamesDatabase {

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#initializeDBConnection()
   */
  public void initializeDBConnection() throws ConfigurationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#closeDBConnection()
   */
  public void closeDBConnection() throws ConfigurationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readBGGGameData(long)
   */
  public BGGGame readBGGGameData(long bggID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertBGGGameData(com.ac.games.data.BGGGame)
   */
  public void insertBGGGameData(BGGGame game) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateBGGGameData(com.ac.games.data.BGGGame)
   */
  public void updateBGGGameData(BGGGame game) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteBGGGameData(com.ac.games.data.BGGGame)
   */
  public void deleteBGGGameData(BGGGame game) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteBGGGameData(long)
   */
  public void deleteBGGGameData(long bggID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }
  
  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readCSIPriceData(long)
   */
  public CoolStuffIncPriceData readCSIPriceData(long csiID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertCSIPriceData(com.ac.games.data.CoolStuffIncPriceData)
   */
  public void insertCSIPriceData(CoolStuffIncPriceData csiData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateCSIPriceData(com.ac.games.data.CoolStuffIncPriceData)
   */
  public void updateCSIPriceData(CoolStuffIncPriceData csiData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteCSIPriceData(com.ac.games.data.CoolStuffIncPriceData)
   */
  public void deleteCSIPriceData(CoolStuffIncPriceData csiData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteCSIPriceData(long)
   */
  public void deleteCSIPriceData(long csiID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#readMMPriceData(long)
   */
  public MiniatureMarketPriceData readMMPriceData(long mmID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#insertMMPriceData(com.ac.games.data.MiniatureMarketPriceData)
   */
  public void insertMMPriceData(MiniatureMarketPriceData mmData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#updateMMPriceData(com.ac.games.data.MiniatureMarketPriceData)
   */
  public void updateMMPriceData(MiniatureMarketPriceData mmData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteMMPriceData(com.ac.games.data.MiniatureMarketPriceData)
   */
  public void deleteMMPriceData(MiniatureMarketPriceData mmData) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }

  /*
   * (non-Javadoc)
   * @see com.ac.games.db.GamesDatabase#deleteMMPriceData(long)
   */
  public void deleteMMPriceData(long mmID) throws ConfigurationException, DatabaseOperationException {
    // TODO Auto-generated method stub
    throw new ConfigurationException("This operation has not yet been implemented");
  }
}
