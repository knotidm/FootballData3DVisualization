import Filter.PlayerFilter;
import Filter.TeamFilter;
import Interaction.ChartInteraction;
import Interaction.FilterInteraction;
import Interaction.ModeInteraction;
import Model.Competition;
import Model.Fixture;
import Model.Player;
import Model.Team;
import Object3D.Grid;
import Object3D.Object3D;
import UI.Event;
import UI.UserInterface;
import Util.Get;
import Util.Initial;
import Visualisation.Chart2D;
import peasy.PeasyCam;
import processing.core.PApplet;

import java.util.ArrayList;

public class Main extends PApplet {
    private PeasyCam peasyCam;
    private UserInterface userInterface;
    private Initial initial;
    private Competition competition;

    private TeamFilter teamFilter1;
    private TeamFilter teamFilter2;
    private ArrayList<Object3D<Team>> teamObjects3D1;
    private ArrayList<Object3D<Team>> teamObjects3D2;
    private ModeInteraction<Team> teamModeInteraction;
    private FilterInteraction<Team> teamFilterInteraction;
    private ChartInteraction<Team> teamChartInteraction;
    private Chart2D teamChart2D;
    private Team resultTeam;

    private ArrayList<Object3D<Fixture>> fixtureObjects3D;
    private ModeInteraction<Fixture> fixtureModeInteraction;
    private Fixture resultFixture;

    private PlayerFilter playerFilter1;
    private PlayerFilter playerFilter2;
    private ArrayList<Object3D<Player>> playerObjects3D1;
    private ArrayList<Object3D<Player>> playerObjects3D2;
    private ModeInteraction<Player> playerModeInteraction;
    private FilterInteraction<Player> playerFilterInteraction;
    private ChartInteraction<Player> playerChartInteraction;
    private Chart2D playerChart2D;
    private Player resultPlayer;

    private Grid grid;
    private Integer grilleSize = 10;
    private Integer gridSize = 1000;

    private Float x = 0f;
    private Float y = 0f;
    private Boolean moveUp = false;
    private Boolean moveDown = false;
    private Boolean moveLeft = false;
    private Boolean moveRight = false;

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"--window-color=#666666", "--stop-color=#cccccc", "Main"};
        PApplet.main(concat(appletArgs, passedArgs));
    }

    @Override
    public void settings() {
        size(1280, 720, P3D);
        smooth();
    }

    @Override
    public void setup() {
        peasyCam = new PeasyCam(this, gridSize / 2);
        peasyCam.setResetOnDoubleClick(false);
        userInterface = new UserInterface(this);
        initial = new Initial(userInterface);
        initial.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        competition = initial.competitions.get(Event.competitionIndex);

        teamFilter1 = new TeamFilter(competition);
        teamFilter2 = new TeamFilter(competition);
        teamObjects3D1 = Get.getTeamObjects3D(this, competition, teamFilter1.playedGames(), gridSize);
        teamObjects3D2 = Get.getTeamObjects3D(this, competition, teamFilter2.playedGames(), gridSize);
        teamModeInteraction = new ModeInteraction<>();
        teamFilterInteraction = new FilterInteraction<>();
        teamChartInteraction = new ChartInteraction<>();
        teamChart2D = teamChartInteraction.getChart2D(this, userInterface, teamObjects3D1, teamObjects3D2, teamFilter1, teamFilter2, Event.chart2DTypeIndex);

        fixtureObjects3D = new ArrayList<>();
        fixtureModeInteraction = new ModeInteraction<>();

        playerModeInteraction = new ModeInteraction<>();
        playerFilterInteraction = new FilterInteraction<>();
        playerChartInteraction = new ChartInteraction<>();

        grid = new Grid(this, gridSize, grilleSize);
    }

    @Override
    public void mousePressed() {
        if (!competition.equals(initial.competitions.get(Event.competitionIndex))) {
            competition = initial.competitions.get(Event.competitionIndex);

            teamFilter1 = new TeamFilter(competition);
            teamFilter2 = new TeamFilter(competition);
            teamObjects3D1 = Get.getTeamObjects3D(this, competition, teamFilter1.playedGames(), gridSize);
            teamObjects3D2 = Get.getTeamObjects3D(this, competition, teamFilter2.playedGames(), gridSize);
            teamChart2D = teamChartInteraction.getChart2D(this, userInterface, teamObjects3D1, teamObjects3D2, teamFilter1, teamFilter2, Event.chart2DTypeIndex);
        }

        if (Event.modeIndex == 2 && Event.clickedObjects3D != 2 && !Event.chartView) {
            if (mouseButton == LEFT) {
                switch (Event.levelIndex) {
                    case 0:
                        teamModeInteraction.resetAllObjects3DStates(teamObjects3D1);
                        resultTeam = teamObjects3D1.get(ModeInteraction.indexObject3D).type;

                        playerFilter1 = new PlayerFilter(resultTeam);
                        playerFilter2 = new PlayerFilter(resultTeam);
                        playerObjects3D1 = Get.getPlayerObjects3D(this, resultTeam, playerFilter1.jerseyNumber(), gridSize);
                        playerObjects3D2 = Get.getPlayerObjects3D(this, resultTeam, playerFilter2.jerseyNumber(), gridSize);
                        playerChart2D = playerChartInteraction.getChart2D(this, userInterface, playerObjects3D1, playerObjects3D2, playerFilter1, playerFilter2, Event.chart2DTypeIndex);

                        fixtureObjects3D = Get.getFixtureObjects3D(this, resultTeam, gridSize);

                        userInterface.teamField.show().setOpen(false);
                        userInterface.teamField.setLabel(resultTeam.name);
                        break;
                    case 1:
                        playerModeInteraction.resetAllObjects3DStates(playerObjects3D1);
                        if (Event.teamFieldIndex == 0) {
                            resultFixture = fixtureObjects3D.get(ModeInteraction.indexObject3D).type;
                        }
                        if (Event.teamFieldIndex == 1) {
                            resultPlayer = playerObjects3D1.get(ModeInteraction.indexObject3D).type;
                        }
                        Event.levelIndex = 2;
                        Event.filterIndex1 = 0;
                        break;
                }
            }
//            if (mouseButton == RIGHT) {
//                switch (Event.levelIndex) {
//                    case 0:
//                        Object3D<Team> teamObject3D = teamObjects3D1.get(ModeInteraction.indexObject3D);
//                        teamObject3D.isClicked = true;
//                        break;
//                    case 1:
//                        Object3D<Player> playerObject3D = playerObjects3D2.get(ModeInteraction.indexObject3D);
//                        playerObject3D.isClicked = true;
//                        break;
//                }
//            }
        }
    }

    @Override
    public void draw() {
        background(0);
        lights();

        textSize(20);

        peasyCam.beginHUD();
        translate(width / 2, 20);
        if (Event.levelIndex == 0) {
            fill(0, 102, 153);
            text(competition.name, 0, 0);
            fill(255, 0, 0);
            text(teamFilter1.name, 0, 25);
        } else if (Event.levelIndex == 1 && Event.teamFieldIndex == 0) {
            fill(0, 102, 153);
            text(resultTeam.name, 0, 0);
        } else if (Event.levelIndex == 1 && Event.teamFieldIndex == 1) {
            fill(0, 102, 153);
            text(resultTeam.name, 0, 0);
            fill(255, 0, 0);
            text(playerFilter1.name, 0, 25);
        }
        peasyCam.endHUD();

        if (!Event.chartView) {
            peasyCam.beginHUD();
            translate(width / 2, height / 2);
            if (Event.levelIndex == 2 && Event.teamFieldIndex == 0) {
                fill(0, 102, 153);
                text(String.format("%s vs %s", resultFixture.homeTeamName, resultFixture.awayTeamName), 0, 0);
                fill(255, 0, 0);
                text(String.format("Date: %s", String.valueOf(resultFixture.date)), 0, 40);
                fill(0, 102, 153);
                text(String.format("Matchday: %s - Status: %s", resultFixture.matchday, resultFixture.status), 0, 80);
                fill(255, 0, 0);
                text(String.format("Result: %s - %s", resultFixture.result.goalsHomeTeam, resultFixture.result.goalsAwayTeam), 0, 120);
            } else if (Event.levelIndex == 2 && Event.teamFieldIndex == 1) {
                fill(0, 102, 153);
                text(resultPlayer.name, 0, 0);
                fill(255, 0, 0);
                text(String.format("Date Of Birth: %s %s", String.valueOf(resultPlayer.dateOfBirth).substring(0, 10), String.valueOf(resultPlayer.dateOfBirth).substring(24)), 0, 40);
                fill(0, 102, 153);
                text(String.format("Nationality: %s", resultPlayer.nationality), 0, 80);
                fill(255, 0, 0);
                text(String.format("Position: %s - Jersey Number: %s", resultPlayer.position, resultPlayer.jerseyNumber), 0, 120);
                fill(0, 102, 153);
                text(String.format("Market Value: %s", Get.getString(resultPlayer.marketValue)), 0, 160);
                fill(255, 0, 0);
                text(String.format("Contract Until: %s %s", String.valueOf(resultPlayer.contractUntil).substring(0, 10), String.valueOf(resultPlayer.dateOfBirth).substring(24)), 0, 200);
            }
            peasyCam.endHUD();

            rotateX(PI / 2);
            translate(-gridSize / 2, -gridSize / 2, 0);
            move();
            translate(x, y, 0);

            if (Event.levelIndex == 0) {
                teamModeInteraction.switchMode(this, peasyCam, grid, teamObjects3D1);
                teamObjects3D1 = teamFilterInteraction.switchTeamFilter(competition, teamObjects3D1, teamFilter1, Event.filterIndex1);
                teamObjects3D2 = teamFilterInteraction.switchTeamFilter(competition, teamObjects3D2, teamFilter2, Event.filterIndex2);
                teamChart2D = teamChartInteraction.getChart2D(this, userInterface, teamObjects3D1, teamObjects3D2, teamFilter1, teamFilter2, Event.chart2DTypeIndex);

                grid.resetZ();
                for (int i = 0; i < teamObjects3D1.size(); i++) {
                    Object3D<Team> object3D = teamObjects3D1.get(i);
                    grid.setZ(object3D.location.x, object3D.location.y, object3D.size - object3D.location.y * 0.01f);
                    object3D.draw(peasyCam, teamChart2D.colours[i]);
                }
                grid.draw();
            }

            if (Event.levelIndex == 1 && Event.teamFieldIndex == 0 && Event.clickedObjects3D != 2) {
                fixtureModeInteraction.switchMode(this, peasyCam, grid, fixtureObjects3D);

                grid.resetZ();
                for (int i = 0; i < fixtureObjects3D.size(); i++) {
                    Object3D<Fixture> object3D = fixtureObjects3D.get(i);
                    grid.setZ(object3D.location.x, object3D.location.y, object3D.size - object3D.location.y * 0.01f);
                    object3D.draw(peasyCam, teamChart2D.colours[i]);
                }
                grid.draw();
            }

            if (Event.levelIndex == 1 && Event.teamFieldIndex == 1 && Event.clickedObjects3D != 2) {
                playerModeInteraction.switchMode(this, peasyCam, grid, playerObjects3D1);
                playerObjects3D1 = playerFilterInteraction.switchPlayerFilter(resultTeam, playerObjects3D1, playerFilter1, Event.filterIndex1);
                playerObjects3D2 = playerFilterInteraction.switchPlayerFilter(resultTeam, playerObjects3D2, playerFilter2, Event.filterIndex2);
                playerChart2D = playerChartInteraction.getChart2D(this, userInterface, playerObjects3D1, playerObjects3D2, playerFilter1, playerFilter2, Event.chart2DTypeIndex);

                grid.resetZ();
                for (int i = 0; i < playerObjects3D1.size(); i++) {
                    Object3D<Player> object3D = playerObjects3D1.get(i);
                    grid.setZ(object3D.location.x, object3D.location.y, object3D.size - object3D.location.y * 0.01f);
                    object3D.draw(peasyCam, playerChart2D.colours[i]);
                }
                grid.draw();
            }
        }

        if (Event.chartView) {
            peasyCam.setActive(false);
            peasyCam.beginHUD();

            if (Event.levelIndex == 0) {
                teamObjects3D1 = teamFilterInteraction.switchTeamFilter(competition, teamObjects3D1, teamFilter1, Event.filterIndex1);
                teamObjects3D2 = teamFilterInteraction.switchTeamFilter(competition, teamObjects3D2, teamFilter2, Event.filterIndex2);
                teamChart2D = teamChartInteraction.getChart2D(this, userInterface, teamObjects3D1, teamObjects3D2, teamFilter1, teamFilter2, Event.chart2DTypeIndex);
                teamChart2D.draw(width, height);
            }
            if (Event.levelIndex == 1 && Event.teamFieldIndex == 1) {
                playerObjects3D1 = playerFilterInteraction.switchPlayerFilter(resultTeam, playerObjects3D1, playerFilter1, Event.filterIndex1);
                playerObjects3D2 = playerFilterInteraction.switchPlayerFilter(resultTeam, playerObjects3D2, playerFilter2, Event.filterIndex2);
                playerChart2D = playerChartInteraction.getChart2D(this, userInterface, playerObjects3D1, playerObjects3D2, playerFilter1, playerFilter2, Event.chart2DTypeIndex);
                playerChart2D.draw(width, height);
            }
            peasyCam.endHUD();

            if (Event.chart2DTypeIndex == 1) {
                userInterface.sliderY.setMin(teamChart2D.xychart.getMinY());
                userInterface.sliderY.setMax(teamChart2D.xychart.getMaxY());

                userInterface.onFrontOfPeasyCam(peasyCam);
            }
        }
    }

    //region MOVEMENT
    private void move() {
        if (moveUp) {
            y++;
        }
        if (moveDown) {
            y--;
        }
        if (moveLeft) {
            x++;
        }
        if (moveRight) {
            x--;
        }
    }

    @Override
    public void keyPressed() {
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    moveUp = true;
                    break;
                case DOWN:
                    moveDown = true;
                    break;
                case LEFT:
                    moveLeft = true;
                    break;
                case RIGHT:
                    moveRight = true;
                    break;
            }
        }
    }

    @Override
    public void keyReleased() {
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    moveUp = false;
                    break;
                case DOWN:
                    moveDown = false;
                    break;
                case LEFT:
                    moveLeft = false;
                    break;
                case RIGHT:
                    moveRight = false;
                    break;
            }
        }
    }
    //endregion
}