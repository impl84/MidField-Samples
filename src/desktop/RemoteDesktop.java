
package desktop;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;
import com.midfield_system.gui.misc.MessagePanel;

import util.AppUtilities;
import util.Dialog;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: RemoteDesktop
 *
 * Date Modified: 2021.09.20
 *
 */

//==============================================================================
@SuppressWarnings("serial")
public class RemoteDesktop
	extends		JFrame
	implements	ActionListener
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		STR_FRAME_TITLE			= "遠隔デスクトップ操作";
	
	private static final String	
		STR_ACCEPTANCE			= "遠隔操作を受け入れる",
		STR_NEW_REMOCON			= "新しい遠隔操作用ウィンドウを生成する";
	
	private static final String
		STR_OPEN_SERVER			= "遠隔操作の受け入れを開始します．",
		STR_CLOSE_SERVER		= "遠隔操作の受け入れを終了します．";
	
	private static final String
		STR_ALREADY_IN_USE		= "既に遠隔操作受け入れ処理中です．",
		STR_NOT_RUNNING			= "遠隔操作受け入れ処理は行われていません．";
	
	private static final Dimension DIM_FRAME = new Dimension(640, 480);
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		RemoteDesktop.launch(args);
	}
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void launch(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try {
				// RemoteDesktop のインスタンスを生成する．
				new RemoteDesktop(args);
					// SystemException
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final MfsNode mfs;
	private final List<DesktopViewer> viewerList;
	
	private DesktopServer server = null;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: ActionListener
//------------------------------------------------------------------------------	
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: ActionListener
	//
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		String actCmd = ev.getActionCommand();
		Object obj = ev.getSource();
		
		switch (actCmd) {
		case STR_ACCEPTANCE:	evHn_Acceptance((JToggleButton)obj);break;
		case STR_NEW_REMOCON:	evHn_Control();		break;
		default:									break;
		}
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void removeViewer(DesktopViewer viewer)
	{
		this.viewerList.remove(viewer);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//	
	private RemoteDesktop(String[] args)
		throws	SystemException
	{
		// GUIをセットアップする．
		setupGui();
		
		// MidField System を起動する．
		this.mfs = MfsNode.initialize();	// SystemException
		this.mfs.activate();				// SystemException
		
		// DesktopViewer のリストを生成する．
		this.viewerList = new ArrayList<DesktopViewer>();
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void setupGui()
	{
		// MessagePanel を生成し，ログの出力先として設定する．
		MessagePanel panel = new MessagePanel();
		Log.setLogPrinter(panel);
		
		// ボタンとコントロールボックスを生成する．
		JToggleButton btnAcceptance = new JToggleButton(STR_ACCEPTANCE);
		btnAcceptance.setActionCommand(STR_ACCEPTANCE);
		btnAcceptance.addActionListener(this);
		
		JButton btnControl = new JButton(STR_NEW_REMOCON);
		btnControl.setActionCommand(STR_NEW_REMOCON);
		btnControl.addActionListener(this);
		
		Box box = Box.createHorizontalBox();
		box.add(btnAcceptance);
		box.add(btnControl);
		
		// この JFrame のコンテナ区画に
		// メッセージパネルとコントロールボックスを追加する．
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(box, BorderLayout.NORTH);
		container.add(panel, BorderLayout.CENTER);
		
		// L&F を設定する．
		AppUtilities.setLookAndFeel(this);
		
		// タイトルと最小サイズを設定する．
		setTitle(STR_FRAME_TITLE);
		setMinimumSize(DIM_FRAME);
		setPreferredSize(DIM_FRAME);
		pack();
		
		// 画面中央にフレームの位置を合わせる．
		AppUtilities.setLocationToCenter(this);
		
		// 終了処理を登録する．
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				close();
			}
		});
		// 可視状態にする．
		setVisible(true);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void close()
	{
		// 動作中の DesktopViewer があれば全て終了する．
		for (DesktopViewer viewer : this.viewerList) {
			viewer.close();
		}
		// DesktopServer が動作中であれば終了する．
		if (this.server != null) {
			Log.message(STR_CLOSE_SERVER);
			this.server.close();
		}
		// MidField System を終了する．
		this.mfs.shutdown();
		
		// このフレームの終了処理を実行する．
		dispose();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: ActionEvent ハンドラ
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_Acceptance(JToggleButton btn)
	{
		// 遠隔操作受け入れ用トグルボタンが選択状態かどうかを確認する．
		if (btn.isSelected()) {
			// 遠隔操作受け入れ用トグルボタンが選択状態で，
			// DesktopServer のインスタンスが存在しない場合，
			// DesktopServer のインスタンスを生成する．
			if (this.server == null) {
				try {
					Log.message(STR_OPEN_SERVER);
					this.server = new DesktopServer(this);
						// SystemException
				}
				catch (SystemException ex) {
					Log.error(ex);
					Dialog.error(this, ex);
				}
			}
			else {
				Log.error(STR_ALREADY_IN_USE);
				Dialog.error(this, STR_ALREADY_IN_USE);
			}
		}
		else {
			// 遠隔操作受け入れ用トグルボタンが非選択状態で，
			// DesktopServer のインスタンスが存在する場合，
			// DesktopServer のインスタンスを解放する．
			if (this.server != null) {
				Log.message(STR_CLOSE_SERVER);
				this.server.close();
				this.server = null;
			}
			else {
				Log.error(STR_NOT_RUNNING);
				Dialog.error(this, STR_NOT_RUNNING);
			}
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_Control()
	{
		// DesktopClient のインスタンスを生成し，リストへ追加する．
		DesktopViewer viewer = new DesktopViewer(this);
		this.viewerList.add(viewer);
	}
}