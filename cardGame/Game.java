package cardGame;


/**
 * This class executes the functions of a game as specified in GameSetup
 * @author Danielle Plecki
 */
 class Game extends GameSetup{
    String playGame(String[] args){
        //Initialize game
        totalNumberOfPlayers = args.length;
        allPlayers = initializePlayers(args);
        dealer = getDealer(allPlayers);
        gameDeck = new Deck();
        currentPlayer = allPlayers.get(dealer);
        currentPlayerPos = currentPlayer.getPlayerNumber();
        dealCards(dealer, allPlayers, gameDeck);
        stillPlaying = true;

        while(stillPlaying){

            //Checks if player makes a catch, if they did not then the turn passes to the next player
            if(!goesNextTurn) {
                //New current player is set
                currentPlayer = allPlayers.get(getNewCurrentPlayer(currentPlayer, allPlayers));
                currentPlayerPos = currentPlayer.getPlayerNumber();
            }

            //Checks to see if player's hand is empty
            while(playersHands[currentPlayerPos].size() == 0){
                if(gameDeck.isEmpty()){
                    // If deck is empty player cannot pick a new card and is skipped
                    currentPlayer = allPlayers.get(getNewCurrentPlayer(currentPlayer, allPlayers));
                    currentPlayerPos = currentPlayer.getPlayerNumber();
                }
                else{
                    playersHands[currentPlayerPos] = new Hand(gameDeck.draw(1));
                }
            }

            //Current player makes play based on their strategy
            currentPlay = currentPlayer.getStrategy().doTurn(playersHands[currentPlayerPos]);
            requestedRank = currentPlay.getRank();
            targetPlayerPos = currentPlay.getTargetPlayer();

            //
            if(playersHands[targetPlayerPos].hasRank(requestedRank)){
               cardsGivenInPlay = giveRequestedCards(currentPlayerPos, targetPlayerPos, requestedRank);
               goesNextTurn = true;
            }
            else{
                if(!gameDeck.isEmpty()){
                    newCard = gameDeck.draw();
                    int newCardRank = newCard.getRank();
                    playersHands[currentPlayerPos] = addCard(playersHands[currentPlayerPos], newCard);
                    goesNextTurn =  (newCardRank == requestedRank);
                    if(newCardRank != requestedRank && checkForBook(currentPlayerPos, newCardRank)){
                        createBook(currentPlayerPos, newCardRank);
                        currentPlayer.score++;
                        booksMade++;
                        allPlaysInGame += "Player"+currentPlayerPos+" made a book of "+Card.CARD_NAMES[requestedRank]+"s.\n";
                    }
                }
                else{
                    goesNextTurn = false;
                }
            }

            //Record play for smart players to reference, log play for game output
            completedPlay = new RecordedPlay(currentPlayerPos, targetPlayerPos, requestedRank, cardsGivenInPlay);
            recordPlay(completedPlay, allPlayers);
            allPlaysInGame += toString(completedPlay);

            //If player received a card of their requested rank, it is possible they have made a book
            //If a book is made it is logged in the game output
            if(checkForBook(currentPlayerPos, requestedRank)){
                createBook(currentPlayerPos, requestedRank);
                currentPlayer.score++;
                booksMade++;
                allPlaysInGame += "Player"+currentPlayerPos+" made a book of "+Card.CARD_NAMES[requestedRank]+"s.\n";
            }

            //Check if game over, otherwise reset conditions
            if(gameOver(booksMade)){
                stillPlaying = false;
            }
            else{
                cardsGivenInPlay.clear();
            }
        }

        //After game is over, winner is determined and output string is returned
        allPlaysInGame += determineWinner(allPlayers);
        return allPlaysInGame;
    }
}
