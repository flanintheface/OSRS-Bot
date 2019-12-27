package mainscripts;

import java.awt.Color;
import java.awt.Graphics;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;

import tasknodes.BankNode;
import tasknodes.FishNode;

@ScriptManifest(author = "wiserbotlol", name = "Simple Fisher", version = 1.0, 
description = "fishes all kinds of fish", category = Category.FISHING)

public class PowerFisher extends TaskScript {
	
	private Timer timer;
	private FishNode fishNode;
	
	public void onStart() {
		log("Starting bot...");
		timer = new Timer();
		fishNode = new FishNode();
		addNodes(fishNode, new BankNode());
	}
	
	public void onExit() {
		log("Exiting bot...");
	}
	
	public void onPaint(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawString("Timer: " + timer.formatTime(), 20, 40);
		
		g.setColor(Color.CYAN);
		g.drawString("Caught Crustaceans / Hour: " + timer.getHourlyRate(fishNode.getTotalFish()), 20, 58);
		
		g.setColor(Color.ORANGE);
		g.drawString("Fishing Level: " + getSkills().getBoostedLevels(Skill.FISHING), 20, 76);
	}

}
