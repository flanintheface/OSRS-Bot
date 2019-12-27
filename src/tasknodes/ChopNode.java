package tasknodes;

import java.awt.Point;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

public class ChopNode extends TaskNode {
	private String currTree;
	private Area currTreeArea;
	private int treesChopped;

	@Override
	public boolean accept() {
		Item treeLog = getInventory().get(item -> item != null && item.getName().toLowerCase().contains("logs"));

		if (treeLog != null) {
			if (treeLog.isNoted()) {
				return false;
			}
		}
		return !getInventory().isFull();
	}

	public int getTreesChopped() {
		return treesChopped;
	}

	private String getCurrTree() {
		if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 60) {
			return "Yew";
		}
		if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 30) {
			return "Willow";
		} 
		else {
			return "Oak";
		}
	}

	private Area getCurrTreeArea() {
		if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 60) {
			return new Area(3002, 3321, 3050, 3315, 0);
		}
		else if (getSkills().getBoostedLevels(Skill.WOODCUTTING) >= 30) {
			return new Area(3157, 3272, 3184, 3268, 0); // levels 30+
		} 
		else {
			return new Area(3168, 3419, 3161, 3363, 0); // levels 1 - 30
		}
	}
	
	private int randomX() {
		return Calculations.random(679, 764);
	}
	
	private int randomY() {
		return Calculations.random(104, 380);
	}
	
	private int getRandomMouseSpeed() {
		return Calculations.random(2, 6);
	}

	@Override
	public int execute() {
		if (getDialogues().canContinue()) {
			getDialogues().clickContinue();
			sleep(Calculations.random(1265, 6213));
		}
		currTree = getCurrTree();
		currTreeArea = getCurrTreeArea();

		if (currTreeArea.contains(getLocalPlayer())) {
			GameObject tree = getGameObjects().closest(t -> t != null && t.getName().equals(currTree));
			if (tree.distance(getLocalPlayer()) >= 1) {
				getWalking().walk(tree);
			}
			if (tree.interact("Chop down")) {
				getMouse().getMouseSettings();
				MouseSettings.setSpeed(getRandomMouseSpeed());
				getMouse().move(new Point(randomX(), randomY()));
				sleepUntil(() -> !tree.exists(), 360480);
				treesChopped++;
			}
			return Calculations.random(2235, 3900);
		} 
		else {
			getWalking().walk(currTreeArea.getRandomTile());
			steadyWalk();
			return Calculations.random(1261, 4780);
		}
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
}
