package tasknodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;

public class SellNode extends TaskNode {

	private Item itemToSell;
	private final Area grandExchange = new Area(3153, 3501, 3175, 3476, 0);

	@Override
	public boolean accept() {
		// check step if script is stopped with note in inventory
		itemToSell = getInventory().get(item -> item != null 
				&& item.getName().toLowerCase().contains("logs"));

		// if it's in your inventory, and go sell
		if (itemToSell != null) {
			if (itemToSell.isNoted()) {
				return true;
			}
		}
		
		// if it's not in your inventory, get it from bank and go sell
		if (randomizeChanceToSell() == 25 && getBank().isOpen()) {
			getBank().setWithdrawMode(BankMode.NOTE);
			sleep(Calculations.random(1513, 2194));
			
			Item bankLogs = getBank().get(item -> item != null 
					&& item.getName().toLowerCase().contains("logs"));

			getBank().withdrawAll(bankLogs.getName());
			sleep(Calculations.random(1522, 2304));

			getBank().close();
			sleep(Calculations.random(1522, 2304));
		} 
		else {
			sleep(Calculations.random(1315, 3344));
			getBank().close();
		}
		return getInventory().contains(itemToSell) && itemToSell.isNoted();
	}

	private int randomizeChanceToSell() {
		return Calculations.random(1, 51);
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
		if (grandExchange.contains(getLocalPlayer())) {
			getMouse().getMouseSettings();
			MouseSettings.setSpeed(getRandomMouseSpeed());
			
			getGrandExchange().open("Exchange");
			getGrandExchange().openSellScreen(0);
			sleep(Calculations.random(1345, 5876));

			getGrandExchange().sellItem(itemToSell.getName(), 
					getInventory().count(itemToSell.getName()), 1);
			sleep(Calculations.random(5345, 15567));

			if (getGrandExchange().isReadyToCollect()) {
				getGrandExchange().collect();
			}
			sleep(Calculations.random(1899, 3439));
			getGrandExchange().close();
			
			return Calculations.random(1265, 8390);
		} 
		else {
			getWalking().walk(grandExchange.getRandomTile());
			steadyWalk();
			return Calculations.random(1542, 5402);
		}
	}
}