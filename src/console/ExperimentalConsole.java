
package console;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ExperimentalConsole
 *
 * Date Modified: 2021.10.07
 *
 */
public class ExperimentalConsole
{
// =============================================================================
//  CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------

	//======== [MAIN METHOD] ===================================================
	//
	public static void main(String[] args)
	{
		ExperimentalConsole console = null;
		try {
			// ExperimentalConsole のインスタンスを生成する．
			console = new ExperimentalConsole();
				// InvocationTargetException, InterruptedException,
				// SystemException
			
			// 生成したインスタンスによる処理を繰り返し実行する．
			console.mainLoop();
		}
		catch (InvocationTargetException | InterruptedException ex) {
			// GUIコンポーネントのセットアップに失敗した．
			ex.printStackTrace();
		}
		catch (SystemException ex) {
			// MidField System の起動に失敗した．
			ex.printStackTrace();
		}
		finally {
			// 終了処理をする．
			if (console != null) {
				console.close();
			}
		}
	}
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================

	// - PRIVATE VARIABLE ------------------------------------------------------
	private final MfsNode mfs;
	private ExConsoleFrame consoleFrame = null;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	ExperimentalConsole()
		throws	InvocationTargetException,
				InterruptedException,
				SystemException
	{
		// GUIコンポーネントをセットアップする．
		SwingUtilities.invokeAndWait(() -> { this.consoleFrame = setupGui(); });
			// InvocationTargetException,
			// InterruptedException
			
		// MidField System を初期化する．
		this.mfs = MfsNode.initialize();
			// SystemException
			
		// イベントリスナを登録する．
		this.mfs.addSystemEventListener(this.consoleFrame);
			
		// MidField System を起動する．
		this.mfs.activate();
			// SystemException
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void mainLoop()
	{
		// ConsoleMenu を拡張した各クラスのインスタンス(メニュー)を生成し，
		// 最初に利用するメニューの参照を取得する．
		ConsoleMenu menu = setupConsoleMenu();
		
		// メニュー毎の処理を繰り返し実行する．
		while (menu != null) {
			try {
				// 現在のメニューにおけるアクションを実行し，
				// 次のメニューの参照を取得する．
				// 次のメニューの参照が null の場合は，
				// このループを抜ける．
				menu = menu.action();
			}
			catch (Exception ex) {
				menu.warning(ex.getMessage());
			}
		}
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void close()
	{
		if (MfsNode.isActive() == false) {
			return;
		}
		// MidField System に登録したイベントリスナの設定を解除し，
		// MidField System の処理を終了する．
		this.mfs.removeSystemEventListener(this.consoleFrame);
		this.mfs.shutdown();
		
		// ExConsoleFrame を終了する．
		if (this.consoleFrame != null) {
			SwingUtilities.invokeLater(() -> this.consoleFrame.dispose());
		}
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ExConsoleFrame setupGui()
	{
		// ExConsoleFrame を生成する．
		ExConsoleFrame frame = new ExConsoleFrame();
		
		// ExperimentalConsole の入力元を設定する．
		ConsoleMenu.setLineReader(frame.getLineReader());
		
		// ExperimentalConsole の出力先を設定する．
		ConsoleMenu.setLogPrinter(frame.getConsolePrinter());
		
		// MidField System のログの出力先を設定する．
		Log.setLogPrinter(frame.getSystemLogPrinter());
		
		// ExConsoleFrame のインスタンスを返す．
		return frame;
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ConsoleMenu setupConsoleMenu()
	{
		// 各メニューのインスタンスを生成する．
		SystemMenu			sysMenu = new SystemMenu();
		InputSettingsMenu	inMenu  = new InputSettingsMenu();
		OutputSettingsMenu	outMenu = new OutputSettingsMenu();
		StreamControlMenu	stmMenu = new StreamControlMenu();
		
		// 各メニューから遷移するメニューのインスタンスを設定する．
		sysMenu.setStateInstance(inMenu,  stmMenu);
		inMenu.setStateInstance( sysMenu, outMenu);
		outMenu.setStateInstanue(sysMenu, stmMenu);
		stmMenu.setStateInstance(sysMenu);
		
		// 初期メニューとして SystemMenu のインスタンスを返す．
		return sysMenu;
	}
}
