package model;

import utilities.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewGame implements Game{

    private GameMap gameMap;
    private RandomNumberGenerator randomNumberGenerator;
    private Player player;
    private Wumpus wumpus;
    private List<Bat> bats;
    private List<Pit> pits;
    private Map<String,List<? extends GameObject>> hazardsMap=new HashMap<>();

    public NewGame() {
        this.randomNumberGenerator = new RandomNumberGenerator();
    }

    public NewGame(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void startGame(){
        buildGameMap();
        initializePlayer();
        initializeWumpus();
        initializeBats();
        initializePits();
    }

    private void initializePlayer() {
        player = new Player();
        player.setId(GameInitialConfigurations.PLAYER_ID);
        setGameObjectInitialCave(player);
    }

    private void initializeWumpus() {
        wumpus = new Wumpus();
        wumpus.setId(GameInitialConfigurations.WUMPUS_ID);
        setGameObjectInitialCave(wumpus);
        List<Wumpus> wumpusList=new ArrayList<>();
        wumpusList.add(wumpus);
        hazardsMap.put(Wumpus.class.getSimpleName(),wumpusList);
    }

    private void initializeBats() {
        bats = new ArrayList<>();
        for(int index = 0; index < GameInitialConfigurations.NUMBER_OF_BATS; index++) {
            bats.add(new Bat());
            bats.get(index).setId(GameInitialConfigurations.BAT_ID_PREFIX + index);
            setGameObjectInitialCave(bats.get(index));
            hazardsMap.put(Bat.class.getSimpleName(),bats);
        }

    }

    private void initializePits() {
        pits = new ArrayList<>();
        for(int index = 0; index < GameInitialConfigurations.NUMBER_OF_PITS; index++) {
            pits.add(new Pit());
            pits.get(index).setId(GameInitialConfigurations.PITS_ID_PREFIX + index);
            setGameObjectInitialCave(pits.get(index));
            hazardsMap.put(Pit.class.getSimpleName(),pits);
        }
    }

    private void setGameObjectInitialCave(GameObject gameObject) {
        Cave cave = getInitialRandomCave();

        if(caveIsNotValidForGameObject(gameObject,cave)){
            setGameObjectInitialCave(gameObject);
        } else{
            gameObject.setCave(cave);
            cave.addGameObject(gameObject);
        }

    }

    private boolean caveIsNotValidForGameObject(GameObject gameObject, Cave cave) {
        if(!isGameObjectInTheSameCaveAsPlayer(cave)){
            return isHazardousGameObjectLocatedNearPlayerAsItsLikes(cave) ||
                    isHazardousGameObjectLocatedInTheSameCaveAsItsLikes(gameObject,cave);
        }

        return true;
    }

    private boolean isHazardousGameObjectLocatedNearPlayerAsItsLikes(Cave cave) {
        Set<Cave> linkedCaves = cave.getLinkedCaves();
        Cave playerCave = player.getCave();
        return linkedCaves.contains(playerCave);
    }

    private boolean isHazardousGameObjectLocatedInTheSameCaveAsItsLikes(GameObject gameObject, Cave cave) {
            List<? extends GameObject> hazardsList=hazardsMap.get(gameObject.getClass().getSimpleName());
            if(hazardsList!=null){
                for(GameObject hazard:hazardsList){
                    Cave hazardCave=hazard.getCave();
                    if(cave.equals(hazardCave)){
                        return true;
                    }
                }
            }

        return false;
    }

    private boolean isGameObjectInTheSameCaveAsPlayer(Cave cave) {
        Cave playerCave= player.getCave();

        if(playerCave!=null){
            return cave.equals(playerCave);
        }

        return false;
    }


    private Cave getInitialRandomCave() {
        int randomCaveIndex = randomNumberGenerator.generateNumber(GameInitialConfigurations.NUMBER_OF_CAVES);
        return gameMap.getCaves().get(randomCaveIndex);
    }

    private void buildGameMap() {
        gameMap = new GameMap();
        List<Cave> caves = new ArrayList<>();
        buildCaves(caves);
        gameMap.setCaves(caves);
        buildCaveLinks(caves);
    }

    private void buildCaves(List<Cave> caves) {
        for(int i = 0; i < GameInitialConfigurations.NUMBER_OF_CAVES; i++){
            caves.add(new Cave(i));
        }
    }

    private void buildCaveLinks(List<Cave> caves) {
        for(int i = 0; i < caves.size(); i++) {
            int[] link = GameInitialConfigurations.CAVE_LINKS[i];
            Cave cave=caves.get(i);
            for(int caveNumber : link) {
                Cave linkedCave = caves.get(caveNumber);
                cave.addLink(linkedCave);
            }
        }
    }

    public GameMap getGameMap() {
        return this.gameMap;
    }

    @Override
    public void playerMovesToCave(int cave) {
        Cave caveToMoveTo = gameMap.getCaves().get(cave);
        player.move(caveToMoveTo);
    }

    @Override
    public void playerShootsToCave(int cave) {

    }

    @Override
    public boolean isGameOver() {
        return player.isDead();
    }

    @Override
    public int getNumberOfArrows() {
        return 0;
    }

    @Override
    public List<String> getMessages() {
        return null;
    }

    @Override
    public int getWumpusCave() {
        return wumpus.getCave().getNumber();
    }

    @Override
    public int getPlayerCave() {
        return player.getCave().getNumber();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Wumpus getWumpus() {
        return this.wumpus;
    }

    public List<Bat> getBats() {
        return this.bats;
    }

    public List<Pit> getPits() {
        return this.pits;
    }
}
