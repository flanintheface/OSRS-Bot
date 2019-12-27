package tasknodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

public class BankNode extends TaskNode {
	private Area currBankArea;

	@Override
	public boolean accept() {
		return getInventory().isFull();
	}
	
	private Area getBankArea() {
		if (getSkills().getBoostedLevels(Skill.FISHING) >= 40) {
			Area shipArea = new Area(2963, 3140, 2952, 3143, 1);
			
			if (shipArea.contains(getLocalPlayer())) {
				NPC npc = getNpcs().closest(p -> p != null && p.getName().equals("Customs officer"));
				
				if (npc.interact()) {
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickOption("Can I journey on this ship?");
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickOption("Search away, I have nothing to hide.");
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickOption("Ok.");
					sleep(Calculations.random(1149, 1393));
					getDialogues().clickContinue();
					sleep(Calculations.random(7149, 15693));
					getDialogues().clickContinue();
					
					GameObject plank = getGameObjects()
							.closest(object -> object != null && object.getName().equals("Gangplank"));
					plank.interact("Cross");
					
					currBankArea = new Area(3050, 3234, 3039, 3237, 0);
					GameObject bank = getGameObjects().closest(b -> b != null && b.getName().equals("Bank deposit box"));
					
					if (bank.interact()) {
						getBank().depositAllExcept("Coins", "Lobster pot");
					}
				}
			}
			else {
				getWalking().walk(shipArea.getRandomTile());
			}
		}
		if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 60) {
			return new Area(3009, 3355, 3017, 3353, 0);
		}
		else if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 30) {
			return new Area(3092, 3245, 3094, 3246, 0);
		}
		else if (getSkills().getBoostedLevels(Skill.WOODCUTTING) < 30) {
			return new Area(3153, 3501, 3175, 3476, 0);
		}
		return null;
	}
	
	public void steadyWalk() {
		if (getWalking().getDestinationDistance() >= 3) {
			if (getWalking().isRunEnabled()) {
				sleep(Calculations.random(2030, 3132));
			} 
			else {
				sleep(Calculations.random(5213, 8499));
			}
		}
	}
	
	private int getRandomMouseSpeed() {
		return Calculations.random(2, 6);
	}

	@Override
	public int execute() {
		currBankArea = getBankArea();
		
		if (currBankArea.contains(getLocalPlayer())) {
			NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
			if (banker.interact("Bank")) {
				sleep(Calculations.random(1354, 3293));
				getBank().depositAllExcept("Iron axe");
				sleep(Calculations.random(2829, 4290));
			}
			return Calculations.random(1693, 3203);
		} 
		else {
			getMouse().getMouseSettings();
			MouseSettings.setSpeed(getRandomMouseSpeed());
			getWalking().walk(currBankArea.getRandomTile());
			steadyWalk();
			return Calculations.random(3350, 6266);
		}
	}
}
