package networklib.server.game;

import java.util.List;
import java.util.TimerTask;

import networklib.NetworkConstants;
import networklib.common.packets.ArrayOfMatchInfosPacket;
import networklib.common.packets.MatchInfoPacket;
import networklib.server.db.IDBFacade;

/**
 * This {@link TimerTask} implementation gets the logged in players and sends them the open matches on every call to {@link #run()}.
 * 
 * @author Andreas Eberle
 * 
 */
public class MatchSendingTimerTask extends TimerTask {
	private final IDBFacade db;

	public MatchSendingTimerTask(IDBFacade db) {
		this.db = db;
	}

	@Override
	public void run() {
		List<Player> loggedInPlayers = db.getPlayers(EPlayerState.LOGGED_IN);
		ArrayOfMatchInfosPacket packet = getArrayOfMatchInfosPacket();

		for (Player currPlayer : loggedInPlayers) {
			sendMatchesPacketToPlayer(currPlayer, packet);
		}
	}

	private void sendMatchesPacketToPlayer(Player player, ArrayOfMatchInfosPacket arrayOfMatchesPacket) {
		player.sendPacket(NetworkConstants.Keys.ARRAY_OF_MATCHES, arrayOfMatchesPacket);
	}

	private ArrayOfMatchInfosPacket getArrayOfMatchInfosPacket() {
		List<Match> matches = db.getJoinableMatches();

		MatchInfoPacket[] matchInfoPackets = new MatchInfoPacket[matches.size()];
		int i = 0;
		for (Match curr : matches) {
			matchInfoPackets[i] = new MatchInfoPacket(curr);
			i++;
		}

		return new ArrayOfMatchInfosPacket(matchInfoPackets);
	}

	public void sendMatchesTo(Player player) {
		ArrayOfMatchInfosPacket arrayOfMatchesPacket = getArrayOfMatchInfosPacket();
		sendMatchesPacketToPlayer(player, arrayOfMatchesPacket);
	}
}
