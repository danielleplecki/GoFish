package cardGame;

import java.util.*;

/**
 * A class that specifies the player's strategy as smart
 */
public class SmartPlayer extends Player implements PlayerStrategy{
    private HashMap<Integer, Set<Integer>> ranksRequested = setRanksRequestList();
    private int targetPlayer;


    SmartPlayer(int playerNumber, int totalNumberOfPlayers){
        this.playerNumber = playerNumber;
        this.totalNumberOfPlayers = totalNumberOfPlayers;
    }

    /**
     * The first function called on the object to provide information that is true for the whole execution.
     * @param yourPlayerNumber a number between 0 and (totalNumberOfPlayers - 1) specifying the current player
     * @param totalNumberOfPlayers a positive integer indicating the total number of players in the game
     */
    public void initialize(int yourPlayerNumber, int totalNumberOfPlayers){
    }

    /**
     * This method looks at each card in the player's hand and determines if the player knows if any other
     * player has previously requested the same rank. If they have a rank in their hand that they know another player
     * has previously requested, they assume that that player still has at least one card of that rank in their hand.
     * @param hand The current state of the player's hand when they are to act
     * @return a strategic play if they have previous knowledge regarding a rank in their hand or
     * a randomly generated play if they have no knowledge regarding any of their cards
     */
    public Play doTurn(Hand hand){
        boolean targetFound = false;
        int possibleRankRequest;
        int requestedRank = 0;
        Card currentCard;
        Iterator searchHand = hand.iterator();
        while(searchHand.hasNext()){
          currentCard = (Card) searchHand.next();
          possibleRankRequest = currentCard.getRank();
          if(hasRequestedRank(possibleRankRequest)){
              requestedRank = possibleRankRequest;
              targetFound = true;
          }
        }
        if(targetFound){
            return new Play(targetPlayer, requestedRank);
        }
        else{
            targetPlayer = (int) (Math.random()*totalNumberOfPlayers);
            while (targetPlayer == playerNumber){
                targetPlayer = (int) (Math.random()*totalNumberOfPlayers);
            }
            int randomCard = (int) (Math.random()*hand.size());
            requestedRank = hand.getCard(randomCard).getRank();
            return new Play(targetPlayer, requestedRank);
        }
    }

    /**
     * Every time a game action takes place, the game engine invokes the following function on each player.
     * This function adds whatever rank the current player requested to their respective set in the
     * ranksRequested HashMap and removes that rank from the target player's set if cards were given
     * @param recordedPlay an object representing the information of the play that just occurred and its results.
     */
    public void playOccurred(RecordedPlay recordedPlay){
        int requestedRank = recordedPlay.getRank();
        int sourcePlayer = recordedPlay.getSourcePlayer();
        int targetPlayer = recordedPlay.getTargetPlayer();
        int cardsGiven = recordedPlay.getCardsReturned().size();

        //Adds rank to the source player's requestedRank set
        Set<Integer> sourceSet = ranksRequested.getOrDefault(sourcePlayer, new HashSet<>());
        sourceSet.add(requestedRank);
        ranksRequested.put(sourcePlayer, sourceSet);

        //If cards were given by target player, that rank is removed from their requestedRanks set
        if(cardsGiven != 0){
            Set<Integer> targetSet = ranksRequested.getOrDefault(targetPlayer, new HashSet<>());
            targetSet.remove(requestedRank);
            ranksRequested.put(targetPlayer, targetSet);
        }
    }

    /**
     * This method determines if the player has any information regarding other players' previous requests.
     * If the player does have previous knowledge, the targetPlayer is set to the player that has previously
     * requested the specified rank.
     * @param possibleRank current rank that is being checked to see if the player has previous knowledge
     * @return true if the player has information and false if they do not
     */
    private boolean hasRequestedRank(int possibleRank){
        for(int possibleTarget=0; possibleTarget<totalNumberOfPlayers; possibleTarget++){
            if(possibleTarget == playerNumber){
                continue;//A player cannot request a card from his/her self
            }
            if(ranksRequested.get(possibleTarget) != null){
                if(ranksRequested.get(possibleTarget).contains(possibleRank)){
                    targetPlayer = possibleTarget;
                    return true;
                }
            }
        }
        return false;
    }


    private HashMap<Integer, Set<Integer>> setRanksRequestList(){
        ranksRequested = new HashMap<>(totalNumberOfPlayers);
        for (Map.Entry<Integer, Set<Integer>> entry: ranksRequested.entrySet()){
            ranksRequested.put(entry.getKey(), new HashSet<>());
        }
        return ranksRequested;
    }
}
