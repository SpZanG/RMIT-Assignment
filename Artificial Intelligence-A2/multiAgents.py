# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        #return successorGameState.getScore()
        evalScore = 0
        secretValue = 20

        if action == "STOP":
        	#print "STOP!!!"
          evalScore = -1024
        else:
        	#print "KEEP GOING!!!"
          evalScore += sum(newScaredTimes)

          minGhostDistance = min([manhattanDistance(ghost.getPosition(), newPos) for ghost in newGhostStates])
          if len(newFood.asList()) != 0:
            minfoodDistance = min([manhattanDistance(food, newPos) for food in newFood.asList()])
            evalScore += (minGhostDistance/minfoodDistance) / secretValue
          evalScore += successorGameState.getScore()

        return evalScore

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """

    ## the max function
    def __max(self, currentGameState, currentDepth):
      currentDepth += 1
      if currentDepth == self.depth or currentGameState.isWin() or currentGameState.isLose():
        return self.evaluationFunction(currentGameState)

      #Infinity Number
      score = -9999999
      
      for action in currentGameState.getLegalActions(0):
        score = max(score, self.__min(currentGameState.generateSuccessor(0, action), currentDepth, 1))

      return score


    ## the min function
    def __min(self, currentGameState, currentDepth, nofGhost):
      # win or lose, just retuen the score of the currentGameState
      if currentGameState.isWin() or currentGameState.isLose():
        return self.evaluationFunction(currentGameState)

      #Infinity Number
      score = 9999999

      for action in currentGameState.getLegalActions(nofGhost):
        # explore all ghost
        if nofGhost == self.totalGhost-1:
          score = min(score, self.__max(currentGameState.generateSuccessor(nofGhost, action), currentDepth))
        else:
          # try to explore next ghost
          score = min(score, self.__min(currentGameState.generateSuccessor(nofGhost, action), currentDepth, nofGhost+1))

      return score


    def getAction(self, gameState):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game
        """
        "*** YOUR CODE HERE ***"
        #util.raiseNotDefined()
        self.totalGhost = gameState.getNumAgents()

        allActions = gameState.getLegalActions(0)
        maxScore = None
        minimaxAction = None


        for action in allActions:
          if action == 'STOP':
            continue

          currentScore = self.__min(gameState.generateSuccessor(0, action), 0, 1)
          if maxScore == None or maxScore < currentScore:
            maxScore = currentScore
            minimaxAction = action

        return minimaxAction


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """
     #max algorithm and also the main action for alphabeta
    def maxValue(self,state,alpha,beta,depth):
        if depth == self.depth or state.isWin() or state.isLose():
            return self.evaluationFunction(state)
        V = float("-inf")
        score = None
        AlphaBetaAction = 'STOP'
        for action in state.getLegalActions(0):
            if action == 'STOP':
                continue
            score = max(score,self.minValue(state.generateSuccessor(0, action), alpha, beta, depth, 1))
            if score > V:
                V = score
                AlphaBetaAction = action
            if V > beta:
                return V
            alpha = max(alpha, V)
        if depth == 0:
            return AlphaBetaAction
        else:
           return V
    #min algorithm
    def minValue(self,state, alpha, beta, depth, oppGhost):
        if depth == self.depth or state.isLose() or state.isWin():
            return self.evaluationFunction(state)
        score = float("inf")
        for action in state.getLegalActions(oppGhost):
            #explore ghost in the pacman game
            if oppGhost == self.ghost - 1:
                score = min(score,self.maxValue(state.generateSuccessor(oppGhost, action), alpha, beta, depth + 1))
            else:
                score = min(score,self.minValue(state.generateSuccessor(oppGhost, action),alpha, beta, depth, oppGhost+1))
            if score < alpha:
                return score
            beta = min(beta, score)
        return score

    def getAction(self, gameState):
        self.ghost = gameState.getNumAgents()

        return self.maxValue(gameState,float("-inf"), float("inf"),0)

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """
	
    #the value function of determine max/ exp or terminal state
    def __value(self, state, depth, agent):
        if depth == 0 or state.isWin() or state.isLose(): 
            return self.evaluationFunction(state)
		
        if agent == 0:
            return self.__maxScore(state, depth)
			
        if agent > 0 and agent < self.totalGhost:
            return self.__expScore(state, depth, agent)
			
    #the max function which return max score
    def __maxScore(self, state, depth):

        score = float("-inf")
		
        for action in state.getLegalActions(0):
            score = max(score, self.__value(state.generateSuccessor(0,action), depth, 1))
        
        return score
	
    #the exp function which return the average value of the ghost successor
    def __expScore(self, state, depth, agent):
		
        score = 0.0
		
        for action in state.getLegalActions(agent):
            prob = 1.0 / len(state.getLegalActions(agent))
            #explore all ghost
            if agent == self.totalGhost -1:
                score += prob * (self.__value(state.generateSuccessor(agent, action), depth-1, 0))
            #explore the remaining ghost
            else:
                score += prob * (self.__value(state.generateSuccessor(agent, action), depth, agent+1))
		
        return score
	  	  
    def getAction(self, gameState):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        "*** YOUR CODE HERE ***"
        self.totalGhost = gameState.getNumAgents()	
        
        allActions = gameState.getLegalActions(0)
        maxScore = None
        expectimaxAction = None

        for action in allActions:
            if action == 'STOP':
                continue

            currentScore = self.__value(gameState.generateSuccessor(0,action), self.depth, 1)
            if maxScore == None or maxScore < currentScore:
                maxScore = currentScore
                expectimaxAction = action

        return expectimaxAction

        #util.raiseNotDefined()

def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
	
	#useful information get from the reflex agent function above
    pacmanPos = currentGameState.getPacmanPosition()
    food = currentGameState.getFood()
    listOfFood = food.asList()
    ghostStates = currentGameState.getGhostStates()
    scaredTimes = [ghostState.scaredTimer for ghostState in ghostStates]
    mysteryNum = 150
	
    #declare the variable 
    foodDistance = float("inf")
    ghostDistance = float("inf")
    averageDistance = 1

	#calculate the food distance for all the food available
    for f in listOfFood:
        manFoodDistance = manhattanDistance(f, pacmanPos)
        foodDistance = min(manFoodDistance, foodDistance)

    foodDistance += 1
    foodCount = len(listOfFood) + 1

    #calculate the ghost distance for all the food available
    for g in ghostStates:
        manGhostDistance = manhattanDistance(g.getPosition(), pacmanPos)
        averageDistance += manGhostDistance
        ghostDistance = min(manGhostDistance * mysteryNum, ghostDistance)

    currentScore = currentGameState.getScore() + 1

    if ghostDistance < 2:
        ghostDistance = -100
    else:
        ghostDistance = 0
	
    return ghostDistance + 1.0/foodDistance + currentScore


# Abbreviation
better = betterEvaluationFunction
