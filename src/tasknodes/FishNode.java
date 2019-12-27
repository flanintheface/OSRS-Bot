package tasknodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.ArrayList;
import java.util.Random;

public class FishNode extends TaskNode {

	private ArrayList<Integer> checkedSlots = new ArrayList<>();
	private Area currFishingArea;
	private Random randInt = new Random();

	@Override
	public boolean accept() {
		return !getInventory().isFull();
	}

	// Can't use api method since it's too obvious
	public boolean dropItems() {
		int slotNum;

		while (getInventory().capacity() > 3) {
			slotNum = randInt.nextInt(27) + 3; // fish net in slot 0
			if (!checkedSlots.contains(slotNum)) {
				getInventory().slotInteract(slotNum, "drop");
				checkedSlots.add(slotNum);
				sleep(Calculations.random(1212, 1849));
			}
		}
		checkedSlots.clear();
		return true;
	}

	public void travelToPort() {
		Area portSarim = new Area(3026, 3240, 3029, 3202, 0);
		
		getWalking().walk(portSarim.getRandomTile());
		steadyWalk();
		if (portSarim.contains(getLocalPlayer())) {
			NPC npc = getNpcs().closest(p -> p != null && (p.getName().equals("Seaman Thresnor")
					|| p.getName().equals("Seaman Lorris") || p.getName().equals("Captain Tobias")));
			if (npc.interact()) {
				sleep(Calculations.random(1940, 3194));
				getDialogues().clickContinue();
				sleep(Calculations.random(1940, 3194));
				getDialogues().clickContinue();
				sleep(Calculations.random(1940, 3194));
				getDialogues().clickOption("Yes please.");
				sleep(Calculations.random(1940, 3194));
				getDialogues().clickContinue();
				sleep(Calculations.random(10340, 15194));
				getDialogues().clickContinue();
			}
		}
		Area shipArea = new Area(2963, 3140, 2952, 3143, 1);
		if (shipArea.contains(getLocalPlayer())) {
			GameObject plank = getGameObjects()
					.closest(object -> object != null && object.getName().equals("Gangplank"));
			plank.interact("Cross");
		}
	}

	private Area getFishingArea() {
		if (getSkills().getBoostedLevels(Skill.FISHING) >= 40) {
			return new Area(2924, 3180, 2925, 3175, 0); // lobster area
		} 
		else if (getSkills().getBoostedLevels(Skill.FISHING) >= 30) {
			return new Area(3239, 3254, 3243, 3238, 0); // salmon area
		} 
		else { // level 0 - 29
			return new Area(3240, 3142, 3244, 3152, 0); // shrimp area
		}
	}
	
	private String getFishingInteract() {
		if (getSkills().getBoostedLevels(Skill.FISHING) >= 40) {
			return "Cage";
		}
		else if (getSkills().getBoostedLevels(Skill.FISHING) >= 30) {
			return "Lure";
		}
		else { // level 0 - 29
			return "Net";
		}
	}

	@Override
	public int execute() {
		currFishingArea = getFishingArea();
		
		if (getSkills().getBoostedLevels(Skill.FISHING) >= 40) {
			Area musaPoint = new Area(2882, 3176, 2960, 3135);
			if (!musaPoint.contains(getLocalPlayer())) { 
				travelToPort();
			} 
		}
		if (currFishingArea.contains(getLocalPlayer())) {
			NPC fishingSpot = getNpcs().closest(NPC -> NPC != null && NPC.getName().equals("Fishing spot") 
					&& NPC.hasAction(getFishingInteract()));
			if (fishingSpot.interact(getFishingInteract())) {
				sleepUntil(() -> getLocalPlayer().isInteractedWith(), 85480);
			}
			return Calculations.random(1321, 7283);
		} 
		else {
			getWalking().walk(currFishingArea.getRandomTile());
			steadyWalk();
			return Calculations.random(1261, 4780);
		}
	}

	public void steadyWalk() {
		if (getWalking().getDestinationDistance() >= 3) {
			if (getWalking().isRunEnabled()) {
				sleep(Calculations.random(3030, 4132));
			} 
			else {
				sleep(Calculations.random(5213, 8499));
			}
		}
	}

	public int getTotalFish() {
		return getInventory().count("Raw salmon") + getInventory().count("Raw trout");
	}
}