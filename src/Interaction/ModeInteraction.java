package Interaction;

import Object3D.Grid;
import Object3D.Object3D;
import UI.Event;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class ModeInteraction<T> {
    private static Integer indexGrid = 0;
    private static Float mouseShortestDistance;
    public static Integer indexObject3D = 0;

    private void resetIsSelectedObjects3DState(ArrayList<Object3D<T>> objects3D) {
        for (Object3D<T> object3D : objects3D) {
            object3D.isSelected = false;
        }
    }

    private void setIsSelectedObject3DState(PApplet pApplet, ArrayList<Object3D<T>> objects3D) {
        indexObject3D = 0;
        resetIsSelectedObjects3DState(objects3D);
        closestObject3DInRelationToPosition(pApplet, objects3D);
        objects3D.get(indexObject3D).isSelected = true;
    }

    private void dragObject3D(PApplet pApplet, Grid grid, ArrayList<Object3D<T>> objects3D) {
        positionInRelationToGrid(pApplet, grid);

        if (pApplet.mousePressed) {
            if (pApplet.mouseButton == PConstants.LEFT) {
                objects3D.get(indexObject3D).location.x += (grid.x[indexGrid] - objects3D.get(indexObject3D).location.x) / 6;
                objects3D.get(indexObject3D).location.y += (grid.y[indexGrid] - objects3D.get(indexObject3D).location.y) / 6;
            }
            if (pApplet.mouseButton == PConstants.RIGHT) {
                objects3D.get(indexObject3D).location.z += pApplet.pmouseY - pApplet.mouseY;
            }
        } else {
            setIsSelectedObject3DState(pApplet, objects3D);
        }
    }

    private void clickObject3D(PApplet pApplet, Grid grid, ArrayList<Object3D<T>> objects3D) {
        positionInRelationToGrid(pApplet, grid);

        if (!pApplet.mousePressed) {
            setIsSelectedObject3DState(pApplet, objects3D);
        }
    }

    private void positionInRelationToGrid(PApplet pApplet, Grid grid) {
        mouseShortestDistance = PApplet.sqrt(PApplet.sq(pApplet.mouseX - pApplet.screenX(grid.x[indexGrid], grid.y[indexGrid], 0)) + PApplet.sq(pApplet.mouseY - pApplet.screenY(grid.x[indexGrid], grid.y[indexGrid], 0)));
        for (int i = 0; i < grid.x.length; i++) {
            float mouseDistance = PApplet.sqrt(PApplet.sq(pApplet.mouseX - pApplet.screenX(grid.x[i], grid.y[i], 0)) + PApplet.sq(pApplet.mouseY - pApplet.screenY(grid.x[i], grid.y[i], 0)));
            if (mouseDistance <= mouseShortestDistance) {
                indexGrid = i;
                mouseShortestDistance = mouseDistance;
            }
        }
    }

    private void closestObject3DInRelationToPosition(PApplet pApplet, ArrayList<Object3D<T>> objects3D) {
        mouseShortestDistance = PApplet.sqrt(PApplet.sq(pApplet.mouseX - pApplet.screenX(objects3D.get(indexObject3D).location.x, objects3D.get(indexObject3D).location.y, 0)) + PApplet.sq(pApplet.mouseY - pApplet.screenY(objects3D.get(indexObject3D).location.x, objects3D.get(indexObject3D).location.y, 0)));
        for (Object3D<T> object3D : objects3D) {
            float mouseDistance = PApplet.sqrt(PApplet.sq(pApplet.mouseX - pApplet.screenX(object3D.location.x, object3D.location.y, 0)) + PApplet.sq(pApplet.mouseY - pApplet.screenY(object3D.location.x, object3D.location.y, 0)));
            if (mouseDistance <= mouseShortestDistance) {
                indexObject3D = object3D.index;
                mouseShortestDistance = mouseDistance;
            }
        }
    }

    public void switchMode(PApplet pApplet, PeasyCam peasyCam, Grid grid, ArrayList<Object3D<T>> objects3D) {
        switch (Event.modeIndex) {
            case 0:
                peasyCam.setActive(true);
                resetIsSelectedObjects3DState(objects3D);
                break;
            case 1:
                peasyCam.setActive(false);
                dragObject3D(pApplet, grid, objects3D);
                break;
            case 2:
                peasyCam.setActive(false);
                clickObject3D(pApplet, grid, objects3D);
                break;
        }
    }

    public void resetAllObjects3DStates(ArrayList<Object3D<T>> objects3D) {
        for (Object3D<T> object3D : objects3D) {
            object3D.isSelected = false;
            object3D.isClicked = false;
        }
    }
}