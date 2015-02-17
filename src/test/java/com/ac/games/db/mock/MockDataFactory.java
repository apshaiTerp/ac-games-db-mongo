package com.ac.games.db.mock;

import java.util.ArrayList;
import java.util.List;

import com.ac.games.data.BGGGame;
import com.ac.games.data.CoolStuffIncPriceData;
import com.ac.games.data.GameAvailability;
import com.ac.games.data.GameType;
import com.ac.games.data.MiniatureMarketPriceData;

/**
 * This class helps us create Mock Data in our test categories
 * @author ac010168
 */
public class MockDataFactory {

  /** Static ID for Abyss */
  public final static long BGG_ABYSS_ID = 155987L;
  /** Static ID for Cosmic Encounter */
  public final static long BGG_COSMIC_ENCOUNTER_ID = 39463L;
  /** Static ID for Cosmic Incursion */
  public final static long BGG_COSMIC_INCURSION_ID = 61001L;
  
  /** Static ID for Abyss */
  public final static long CSI_ABYSS_ID = 203495L;
  /** Static ID for Cosmic Encounter */
  public final static long CSI_COSMIC_ENCOUNTER_ID = 136975L;
  /** Static ID for Cosmic Incursion */
  public final static long CSI_COSMIC_INCURSION_ID = 136978L;
  
  /** Static ID for Abyss */
  public final static long MM_ABYSS_ID = 40693L;
  /** Static ID for Cosmic Encounter */
  public final static long MM_COSMIC_ENCOUNTER_ID = 15138L;
  /** Static ID for Cosmic Incursion */
  public final static long MM_COSMIC_INCURSION_ID = 15102L;
  
  
  /** Helper method to generate some game data to be used for validation */
  public static BGGGame createBGGGame(long gameID) {
    if (gameID == BGG_ABYSS_ID)            return createAbyssGame();
    if (gameID == BGG_COSMIC_ENCOUNTER_ID) return createCosmicEncounterGame();
    if (gameID == BGG_COSMIC_INCURSION_ID) return createCosmicIncursionGame();
    
    //Fail Case
    return null;
  }
  
  private static BGGGame createAbyssGame() {
    BGGGame abyssGame = new BGGGame();
    abyssGame.setBggID(BGG_ABYSS_ID);
    abyssGame.setName("Abyss");
    abyssGame.setYearPublished(2014);
    abyssGame.setMinPlayers(2);
    abyssGame.setMaxPlayers(4);
    abyssGame.setMinPlayingTime(45);
    abyssGame.setMaxPlayingTime(45);
    abyssGame.setImageURL("http://cf.geekdo-images.com/images/pic1965255.jpg");
    abyssGame.setImageThumbnailURL("http://cf.geekdo-images.com/images/pic1965255_t.jpg");
    abyssGame.setDescription("The Abyss power is once again vacant, so the time has come to get your hands on the throne and its privileges. Use all of your cunning to win or buy votes in the Council. Recruit the most influential Lords and abuse their powers to take control of the most strategic territories. Finally, impose yourself as the only one able to rule the Abyssal people!\n\nAbyss is a game of development, combination and collection in which players try to take control of strategic locations in an underwater city. To achieve this, players must develop on three levels: first by collecting allies, then using them to recruit Lords of the Abyss, who will then grant access to different parts of the city. Players acquire cards through a draft of sorts, and the Lords of the Abyss acquired on those cards grant special powers to the cardholder - but once you use the cards to acquire a location, that power is shut off, so players need to time their land grabs well in order to put themselves in the best position for when the game ends.");
    abyssGame.setBggRating(7.412);
    abyssGame.setBggRatingUsers(1405);
    
    List<String> publishers = new ArrayList<String>(4);
    publishers.add("Bombyx");
    publishers.add("Asmodee");
    publishers.add("Asterion Press");
    publishers.add("REBEL.pl");
    abyssGame.setPublishers(publishers);
    
    List<String> designers = new ArrayList<String>(2);
    designers.add("Bruno Cathala");
    designers.add("Charles Chevallier");
    abyssGame.setDesigners(designers);
    
    List<String> categories = new ArrayList<String>(3);
    categories.add("Card Game");
    categories.add("Mythology");
    categories.add("Nautical");
    abyssGame.setCategories(categories);
    
    List<String> mechanisms = new ArrayList<String>(3);
    mechanisms.add("Auction/Bidding");
    mechanisms.add("Hand Management");
    mechanisms.add("Set Collection");
    abyssGame.setMechanisms(mechanisms);
    
    abyssGame.setGameType(GameType.BASE);
    abyssGame.setBggRank(412);
    
    return abyssGame;
  }

  private static BGGGame createCosmicEncounterGame() {
    BGGGame cosmicGame = new BGGGame();
    cosmicGame.setBggID(BGG_COSMIC_ENCOUNTER_ID);
    cosmicGame.setName("Cosmic Encounter");
    cosmicGame.setYearPublished(2008);
    cosmicGame.setMinPlayers(3);
    cosmicGame.setMaxPlayers(5);
    cosmicGame.setMinPlayingTime(60);
    cosmicGame.setMaxPlayingTime(60);
    cosmicGame.setImageURL("http://cf.geekdo-images.com/images/pic354780.jpg");
    cosmicGame.setImageThumbnailURL("http://cf.geekdo-images.com/images/pic354780_t.jpg");
    cosmicGame.setDescription("From the Manufacturer\n\nBuild a galactic empire... In the depths of space, the alien races of the Cosmos vie with each other for control of the universe. Alliances form and shift from moment to moment, while cataclysmic battles send starships screaming into the warp.  Players choose from dozens of alien races, each with its own unique power to further its efforts to build an empire that spans the galaxy.\n\nMany classic aliens from earlier editions of this beloved game return, such as the Oracle, the Loser, and the Clone. Newly discovered aliens also join the fray, including Remora, Mite, and Tick-Tock.  This classic game of alien politics returns from the warp once more.\n\nIn Cosmic Encounter, each player is the leader of an alien race. On a player's turn, he or she becomes the offense.  The offense encounters another player on a planet by moving a group of his or her ships through the hyperspace gate to that planet.  The offense draws from the destiny deck which contains colors, wilds and specials.  He or she then takes the hyperspace gate and points at one planet in the system indicated by the drawn destiny card.  The offense vs. the defenses ships are in the encounter and both sides are able to invite allies, play an encounter card as well as special cards to try and tip the encounter in their favor.\n\nThe object of the game is to establish colonies in other players' planetary systems. Players take turns trying to establish colonies. The winner(s) are the first player(s) to have five colonies on any planets outside his or her home system. A player does not need to have colonies in all of the systems, just colonies on five planets outside his or her home system. These colonies may all be in one system or scattered over multiple systems. The players must use force, cunning, and diplomacy to ensure their victory.");
    cosmicGame.setBggRating(7.58417);
    cosmicGame.setBggRatingUsers(12054);
    
    List<String> publishers = new ArrayList<String>(5);
    publishers.add("Arclight");
    publishers.add("Asterion Press");
    publishers.add("Edge Entertainment");
    publishers.add("Fantasy Flight Games");
    publishers.add("Heidelberger Spieleverlag");
    cosmicGame.setPublishers(publishers);
    
    List<String> designers = new ArrayList<String>(5);
    designers.add("Bill Eberle");
    designers.add("Jack Kittredge");
    designers.add("Bill Norton");
    designers.add("Peter Olotka");
    designers.add("Kevin Wilson");
    cosmicGame.setDesigners(designers);
    
    List<String> categories = new ArrayList<String>(4);
    categories.add("Bluffing");
    categories.add("Negotiation");
    categories.add("Science Fiction");
    categories.add("Space Exploration");
    cosmicGame.setCategories(categories);
    
    List<String> mechanisms = new ArrayList<String>(3);
    mechanisms.add("Hand Management");
    mechanisms.add("Partnerships");
    mechanisms.add("Variable Player Powers");
    cosmicGame.setMechanisms(mechanisms);
    
    cosmicGame.setGameType(GameType.BASE);
    cosmicGame.setBggRank(74);
    
    List<Long> expansions = new ArrayList<Long>(5);
    expansions.add(114276L);
    expansions.add(87507L);
    expansions.add(153971L);
    expansions.add(61001L);
    expansions.add(143760L);
    cosmicGame.setExpansionIDs(expansions);
    
    return cosmicGame;
  }

  private static BGGGame createCosmicIncursionGame() {
    BGGGame cosmicGame = new BGGGame();
    cosmicGame.setBggID(BGG_COSMIC_INCURSION_ID);
    cosmicGame.setName("Cosmic Encounter: Cosmic Incursion");
    cosmicGame.setYearPublished(2010);
    cosmicGame.setMinPlayers(3);
    cosmicGame.setMaxPlayers(6);
    cosmicGame.setMinPlayingTime(60);
    cosmicGame.setMaxPlayingTime(60);
    cosmicGame.setImageURL("http://cf.geekdo-images.com/images/pic657393.jpg");
    cosmicGame.setImageThumbnailURL("http://cf.geekdo-images.com/images/pic657393_t.jpg");
    cosmicGame.setDescription("Adapted from the Fantasy Flight web site:\n\nOver the years, alien empires have risen and fallen. The Vacuum has failed to eliminate the Zombie, the Virus has spread far and wide, and the Loser has turned defeat into victory. 50 different species have tried to conquer the galaxy, and each have made their mark on the universe. Their warring has not gone unnoticed.\n\n20 new alien cultures are now racing towards the site of the conflict. They each want to carve out their own piece of the galaxy for themselves. The aliens may be familiar to some of you (reprints of past favorites), or are aliens no one has ever seen before. The first on our list is a blast from the past; the Sniveler!\n\nIn addition, this expansion includes additions to the Cosmic Encounter universe. From the game-changing Cosmic Quakes to the bounties held within the Reward deck, these additions are sure to spice up your quest for galactic dominance.\n\nEvery game has seen five races converge to do battle for control of an empire. Now, the galaxy gets crowded. A sixth race appears to do battle, and the sixth member of your play group is able to join you. Cosmic Incursion allows a sixth player to get in on the fun.\n\nCosmic Encounter puts you in charge of the fate of an alien race. Each flavor of extraterrestrial strives to be the first to conquer five enemy colonies and establish themselves as the dominant life form. Every faction has a unique power, a power they intend to use to crush their enemies. With over 50 aliens to choose from no two games will be the same.");
    cosmicGame.setBggRating(8.27179);
    cosmicGame.setBggRatingUsers(1781);
    
    List<String> publishers = new ArrayList<String>(4);
    publishers.add("Arclight");
    publishers.add("Edge Entertainment");
    publishers.add("Fantasy Flight Games");
    publishers.add("Heidelberger Spieleverlag");
    cosmicGame.setPublishers(publishers);
    
    List<String> designers = new ArrayList<String>(1);
    designers.add("Kevin Wilson");
    cosmicGame.setDesigners(designers);
    
    List<String> categories = new ArrayList<String>(5);
    categories.add("Bluffing");
    categories.add("Negotiation");
    categories.add("Expansion for Base-game");
    categories.add("Science Fiction");
    categories.add("Space Exploration");
    cosmicGame.setCategories(categories);
    
    List<String> mechanisms = new ArrayList<String>(3);
    mechanisms.add("Hand Management");
    mechanisms.add("Partnerships");
    mechanisms.add("Variable Player Powers");
    cosmicGame.setMechanisms(mechanisms);
    
    cosmicGame.setGameType(GameType.EXPANSION);
    cosmicGame.setParentGameID(BGG_COSMIC_ENCOUNTER_ID);
    
    return cosmicGame;
  }
  
  /** Helper method to generate some game data to be used for validation */
  public static CoolStuffIncPriceData createCSIData(long csiID) {
    if (csiID == CSI_ABYSS_ID)            return createCSIAbyssData();
    if (csiID == CSI_COSMIC_ENCOUNTER_ID) return createCSICosmicEncounterData();
    if (csiID == CSI_COSMIC_INCURSION_ID) return createCSICosmicIncursionData();
    
    //Fail Case
    return null;
  }
  
  private static CoolStuffIncPriceData createCSIAbyssData() {
    CoolStuffIncPriceData data = new CoolStuffIncPriceData();
    
    data.setCsiID(CSI_ABYSS_ID);
    data.setTitle("Abyss");
    data.setSku("ASMABY01US");
    data.setImageURL("http://a4.res.cloudinary.com/csicdn/image/upload/v1/Images/Products/Misc%20Art/Asmodee%20Editions/full/ASMABY01US.jpg");
    data.setAvailability(GameAvailability.INSTOCK);
    data.setReleaseDate(null);
    data.setMsrpValue(59.99);
    data.setCurPrice(40.99);
    
    return data;
  }

  private static CoolStuffIncPriceData createCSICosmicEncounterData() {
    CoolStuffIncPriceData data = new CoolStuffIncPriceData();
    
    data.setCsiID(CSI_COSMIC_ENCOUNTER_ID);
    data.setTitle("Cosmic Encounter Board Game");
    data.setSku("FFGCE01");
    data.setImageURL("http://a1.res.cloudinary.com/csicdn/image/upload/v1/Images/Products/Misc%20Art/Fantasy%20Flight%20Games/full/FFGCosmicEncounter.jpg");
    data.setAvailability(GameAvailability.PREORDER);
    data.setReleaseDate("First quarter 2015");
    data.setMsrpValue(59.95);
    data.setCurPrice(41.49);
    
    return data;
  }

  private static CoolStuffIncPriceData createCSICosmicIncursionData() {
    CoolStuffIncPriceData data = new CoolStuffIncPriceData();
    
    data.setCsiID(CSI_COSMIC_INCURSION_ID);
    data.setTitle("Cosmic Encounter: Cosmic Incursion Expansion");
    data.setSku("FFGCE02");
    data.setImageURL("http://a5.res.cloudinary.com/csicdn/image/upload/v1/Images/Products/Misc%20Art/Fantasy%20Flight%20Games/full/ffg_cosmicencounterCosmicIncursion.jpg");
    data.setAvailability(GameAvailability.OUTOFSTOCK);
    data.setReleaseDate(null);
    //data.setMsrpValue(0.0);
    data.setCurPrice(16.99);
    
    return data;
  }

  /** Helper method to generate some game data to be used for validation */
  public static MiniatureMarketPriceData createMMData(long mmID) {
    if (mmID == MM_ABYSS_ID)            return createMMAbyssData();
    if (mmID == MM_COSMIC_ENCOUNTER_ID) return createMMCosmicEncounterData();
    if (mmID == MM_COSMIC_INCURSION_ID) return createMMCosmicIncursionData();
    
    //Fail Case
    return null;
  }
  
  private static MiniatureMarketPriceData createMMAbyssData() {
    MiniatureMarketPriceData data = new MiniatureMarketPriceData();
    
    data.setMmID(MM_ABYSS_ID);
    data.setTitle("Abyss");
    data.setSku("ASMABY01US");
    data.setImageURL("http://a4.res.cloudinary.com/csicdn/image/upload/v1/Images/Products/Misc%20Art/Asmodee%20Editions/full/ASMABY01US.jpg");
    data.setAvailability(GameAvailability.INSTOCK);
    data.setMsrpValue(59.99);
    data.setCurPrice(41.39);

    return data;
  }

  private static MiniatureMarketPriceData createMMCosmicEncounterData() {
    MiniatureMarketPriceData data = new MiniatureMarketPriceData();
    
    data.setMmID(MM_COSMIC_ENCOUNTER_ID);
    data.setTitle("Cosmic Encounter");
    data.setSku("FFGCE01");
    data.setImageURL("http://cdn.miniaturemarket.com/media/catalog/product/f/f/ffgce01.jpg");
    data.setAvailability(GameAvailability.OUTOFSTOCK);
    data.setMsrpValue(59.95);
    data.setCurPrice(41.37);

    return data;
  }

  private static MiniatureMarketPriceData createMMCosmicIncursionData() {
    MiniatureMarketPriceData data = new MiniatureMarketPriceData();
    
    data.setMmID(MM_COSMIC_INCURSION_ID);
    data.setTitle("Cosmic Encounter Cosmic Incursion Expansion");
    data.setSku("FFGCE02");
    data.setImageURL("http://cdn.miniaturemarket.com/media/catalog/product/F/F/FFGCE02.jpg");
    data.setAvailability(GameAvailability.OUTOFSTOCK);
    data.setMsrpValue(24.95);
    data.setCurPrice(17.22);

    return data;
  }
}
