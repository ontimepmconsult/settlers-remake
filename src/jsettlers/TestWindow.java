package jsettlers;

import go.graphics.sound.ISoundDataRetriever;
import go.graphics.sound.SoundPlayer;
import go.graphics.swing.AreaContainer;

import javax.swing.JFrame;

import jsettlers.common.map.IGraphicsGrid;
import jsettlers.common.resources.ResourceManager;
import jsettlers.graphics.action.Action;
import jsettlers.graphics.action.EActionType;
import jsettlers.graphics.action.SelectAction;
import jsettlers.graphics.map.IMapInterfaceListener;
import jsettlers.graphics.map.MapInterfaceConnector;
import jsettlers.graphics.map.draw.ImageProvider;
import jsettlers.graphics.swing.JOGLPanel;
import jsettlers.graphics.swing.SwingResourceLoader;
import jsettlers.graphics.swing.SwingResourceProvider;

public class TestWindow {
	static { // sets the native library path for the system dependent jogl libs
		SwingResourceLoader.setupSwingPaths();
		ResourceManager.setProvider(new SwingResourceProvider());
	}

	private TestWindow() {
	}

	public static MapInterfaceConnector openTestWindow(IGraphicsGrid map) {
		ImageProvider.getInstance().startPreloading();

		JFrame window = new JFrame("window test");

		JOGLPanel content = new JOGLPanel(new SoundPlayer() {
			@Override
			public void playSound(int sound, float lvolume, float rvolume) {
				// TODO Auto-generated method stub
			}

			@Override
			public void setSoundDataRetriever(ISoundDataRetriever soundDataRetriever) {
				// TODO Auto-generated method stub
			}
		});
		window.add(new AreaContainer(content.getArea()));

		window.pack();
		window.setSize(1300, 800);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MapInterfaceConnector connector = content.showGameMap(map, null);

		connector.addListener(new IMapInterfaceListener() {

			@Override
			public void action(Action action) {
				if (action.getActionType() == EActionType.SELECT_POINT) {
					SelectAction selectAction = (SelectAction) action;

					System.out.println("Action preformed: " + action.getActionType() + " at: " + selectAction.getPosition());
				} else {
					System.out.println("Action preformed: " + action.getActionType());
				}
			}
		});

		return connector;
	}
}
