package mainscripts;

import java.awt.Color;
import java.awt.Graphics;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;

import tasknodes.ChopNode;
import tasknodes.DropNode;
import tasknodes.BankNode;
import tasknodes.SellNode;

@ScriptManifest(author = "totallynotanosrsbotlol2", name = "Simple Woodcutter", version = 1.0, 
description = "cuts all kinds of wood", category = Category.WOODCUTTING)

public class PowerChopper extends TaskScript {
	
	private Timer timer;
	private ChopNode chopNode;
	private BankNode bankNode;
	private SellNode sellNode;

	// chop, bank, randomize chance to sell at GE if we've collected 1000+ logs
	public void onStart() {
		timer = new Timer();
		chopNode = new ChopNode();
		bankNode = new BankNode();
		sellNode = new SellNode();
		addNodes(chopNode, new DropNode()); 
	}
	
	private String getCurrNode() {
		if (chopNode.accept()) {
			return "Chop";
		}
		else if (bankNode.accept()) {
			return "Bank";
		}
		else {
			return "Sell";
		}
	}
	
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("Node: " + getCurrNode(), 20, 40);
		
		g.setColor(Color.GREEN);
		g.drawString("Runtime: " + timer.formatTime(), 20, 60);
		
		g.setColor(Color.CYAN);
		g.drawString("Trees Chopped / Hour: " + timer.getHourlyRate(chopNode.getTreesChopped()), 20, 80);
		
		g.setColor(Color.ORANGE);
		g.drawString("Woodcutting Level: " + getSkills().getBoostedLevels(Skill.WOODCUTTING), 20, 100);
	}
}
