
package console;

import java.util.List;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SystemMenu
 *
 * Date Modified: 2021.08.23
 *
 */

//==============================================================================
public class SystemMenu
	extends		ConsoleMenu
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		MENU_TITLE				= "■システム □入力設定 □出力設定 □ストリーム操作",
		MENU_PROMPT				= "sys";
	
	private static final String
		TRANSITION_TO_INPUT		= "ストリーム入出力設定 → □入力設定",
		SHOW_STREAM_LIST		= "ストリーム一覧表示",
		SELECT_STREAM			= "ストリーム選択       → □ストリーム操作",
		EXIT					= "終了";
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================

	//- PRIVATE VARIABLE -------------------------------------------------------
	private InputSettingsMenu inputSeggingsMenu = null;
	private StreamControlMenu streamControlMenu = null;
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public SystemMenu()
	{
		//
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public void setStateInstance(InputSettingsMenu inMenu, StreamControlMenu stmMenu)
	{
		this.inputSeggingsMenu = inMenu;
		this.streamControlMenu = stmMenu;
	}
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD:
//------------------------------------------------------------------------------
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected MenuItem[] initMenuItems()
	{
		MenuItem[] menuItems = new MenuItem[] {
			new MenuItem(0,	TRANSITION_TO_INPUT,	() -> transitionToInput()),
			new MenuItem(),
			new MenuItem(1,	SHOW_STREAM_LIST,		() -> showStreamList()),
			new MenuItem(2,	SELECT_STREAM,			() -> selectStream()),
			new MenuItem(),
			new MenuItem(9,	EXIT,					() -> exit())
		};
		return menuItems;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected String getMenuTitle()
	{
		return MENU_TITLE;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected String getPrompt()
	{
		return MENU_PROMPT;
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu transitionToInput()
	{
		// SegmentIo に設定されている入出力情報を削除する．
		SegmentIo segIo = ConsoleMenu.getSegmentIo();
		segIo.removeInputParams();
		segIo.removeOutputParams();
		
		// 次のメニューとなる InputSettingsMenu を返す．
		return this.inputSeggingsMenu;
	}

	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu selectStream()
	{
		// ストリームが選択された場合，
		// 次のメニューは StreamControlMenu となる．
		ConsoleMenu nextMenu = this.streamControlMenu;
		
		// 動作中の StreamPerformer のリストを取得する．
		List<StreamPerformer> lsPfmr = StreamPerformer.getStreamPerformerList();
		int size = lsPfmr.size();
		if (size > 0) {
			// リストを一覧表示して，ストリーム番号の入力を待つ．
			printStreamPerformerList(lsPfmr);
			int num = selectNumber("ストリーム番号");
			if ((num >= 0) && (num < size)) {
				// 入力されたストリーム番号の StreamPerformer を取得する．
				StreamPerformer pfmr = lsPfmr.get(num);
				
				// 次のメニューである StreamControlMenu で
				// 取得した StreamPerformer を利用できるよう，
				// ConsoleMenu のクラス変数に設定しておく．
				ConsoleMenu.setSelectedStream(pfmr);
			}
			else {
				// 入力されたストリーム番号の StreamPerformer は無い．
				// 次のメニューは SystemMenu のまま．
				warningPause("キャンセルしました．\n");
				nextMenu = this;
			}
		}
		else {
			// 動作中の StreamPerformer は無い．
			// 次のメニューは SystemMenu のまま．
			warningPause("選択できるストリームがありません．\n");
			nextMenu = this;
		}
		// 次のメニューを返す．
		return nextMenu;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printStreamPerformerList(List<StreamPerformer> lsPfmr)
	{
		int size = lsPfmr.size();
		for (int i = 0; i < size; i++) {
			StreamPerformer pfmr = lsPfmr.get(i);
			SegmentIo segIo = pfmr.getSegmentIo();
			
			print("\n");
			print("＊ストリーム[%s] ～～～～～～～～～～～～～～～～～～～～～～～～～～～～\n",
				Integer.toString(i)
			);
			printIoParamList("入力設定", segIo.getInputParamList());
			printIoParamList("出力設定", segIo.getOutputParamList());
			print("～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～\n");
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu showStreamList()
	{
		// 動作中の StreamPerformer のリストを取得する．
		List<StreamPerformer> lsPfmr = StreamPerformer.getStreamPerformerList();
		int size = lsPfmr.size();
		if (size <= 0) {
			warningPause("ストリームがありません．\n");
			return this;
		}
		// 取得したリストを表示する．
		printStreamPerformerList(lsPfmr);
		pause();
		
		return this;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu exit()
	{
		// 次のメニューは無い．
		// ConsoleMenu による処理の終了を示す null を返す．
		return null;
	}
}