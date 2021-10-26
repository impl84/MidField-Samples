
package desktop;

import static desktop.DesktopMessage.CONTROL_ACCEPTED;
import static desktop.DesktopMessage.CONTROL_MESSAGE;
import static desktop.DesktopMessage.CONTROL_REFUSED;
import static desktop.DesktopMessage.START_CONTROL;
import static desktop.DesktopMessage.STOP_CONTROL;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.UUID;

import javax.swing.JFrame;

import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: DesktopServer 
 *
 * Date Modified: 2021.10.27
 *
 */

//==============================================================================
public class DesktopServer
	implements	CommPacketHandler
{
	//- PUBLIC CONSTANT VALUE --------------------------------------------------
	public static final String DESKTOP_SERVER = "DesktopServer";
	
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		STR_ACCEPT_CONTROL	= "※遠隔(%s)からのデスクトップ操作を開始します．",
		STR_REJECT_CONTROL	= "※遠隔(%s)からのデスクトップ操作を拒否します．",
		STR_STOP_CONTROL	= "※遠隔からのデスクトップ操作を終了します．";
	
	private static final String
		STR_CANT_CREATE_ROBOT	= "デスクトップ操作用のインスタンスを生成できません．",
		STR_UNREACHABLE			= "クライアントへ送信したパケットが到達できませんでした",
		STR_ILLEGAL_MESSAGE		= "不正なメッセージを受信しました．: %s from %s\n";	

	private static enum ControlState
	{
		WAIT,		// デスクトップ制御受入待機中
		CONTROLLED	// デスクトップ制御受入中
	}

//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final PacketCommunicator comm;
	
	private final double scaleX;
	private final double scaleY;
	private final Robot robot;
	
	private ImageSender imgSender = null;
	private ControlState state = ControlState.WAIT;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================

//------------------------------------------------------------------------------
//  PUBLIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public synchronized boolean handleIncomingPacket(CommPacket pkt)
	{
		boolean wasHandled = true;
		
		// パケットから DesktopMessage のインスタンスを取得する．
		DesktopMessage msg = pkt.getSerializableObject(DesktopMessage.class);
		if (msg == null) {
			wasHandled = false;
			return wasHandled;
		}
		// 送信元の ObjectId を取得する．
		ObjectId srcId = pkt.getSourceObjectId();
		
		// メッセージタイプ毎の処理を実行する．
		String type = pkt.getMessageType();
		switch (type) {
		case START_CONTROL	: msgHn_StartControl(msg, srcId);		break;
		case CONTROL_MESSAGE: msgHn_ControlMessage(msg);			break;
		case STOP_CONTROL	: msgHn_StopControl(msg);				break;
		default 			: msgHn_UnsupportedMessage(type, srcId);break;
		}
		return wasHandled;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handlePacketIoException(PacketIoException ex)
	{
		Log.warning(ex);
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handleUnreachablePacket(CommPacket inPkt)
	{
		Log.warning(STR_UNREACHABLE);
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	DesktopServer(JFrame frame)
		throws	SystemException
	{
		// PacketCommunicator を生成する．
		this.comm = new PacketCommunicator(DESKTOP_SERVER, this);
			// SystemException
		
		GraphicsConfiguration config = frame.getGraphicsConfiguration();
		AffineTransform transform = config.getDefaultTransform();
		this.scaleX = transform.getScaleX();
		this.scaleY = transform.getScaleY();
		
		// デスクトップ操作用の Robot を生成する．
		try {
			this.robot = new Robot();
		}
		catch (AWTException ex) {
			SystemException sysEx
				= new SystemException(STR_CANT_CREATE_ROBOT, ex);
			throw sysEx;
		}
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	boolean isControlled()
	{
		return (this.state == ControlState.CONTROLLED);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void close()
	{
		stopControl();
		this.comm.close();
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	synchronized void stopControl()
	{
		if (this.state == ControlState.CONTROLLED) {
			// 状態変更：デスクトップ制御受入待機中．
			this.state = ControlState.WAIT;
			
			// DesktopImageSender を終了する．
			if (this.imgSender != null) {
				this.imgSender.close();
				this.imgSender = null;
			}
			Log.message(STR_STOP_CONTROL);
		}
	}	
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: START_CONTROL
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_StartControl(DesktopMessage inMsg, ObjectId srcId)
	{
		String msgType = CONTROL_REFUSED;
		Serializable obj = null;
		
		// ソースホストアドレスを取得する．
		UUID nodeId = srcId.getNodeId();
		String srcNodeId = nodeId.toString();
		
		// デスクトップ制御の受入可否を確認する．
		boolean doAccept = checkAccept(inMsg);
		if (doAccept) {
			// デスクトップ制御を受け入れる．
			try {
				// DesktopImageSender を生成する．
				this.imgSender = new ImageSender(this);
					// SystemException, StreamException
				
				// 受入メッセージを出力する．
				Log.message(STR_ACCEPT_CONTROL, srcNodeId);
				
				// メッセージタイプの設定と出力パラメータの取得．
				msgType = CONTROL_ACCEPTED;
				obj = this.imgSender.getOutputParam();
			}
			catch (SystemException | StreamException ex) {
				// DesktopImageSender の生成に失敗したので，
				// デスクトップ制御を拒否する．
				Log.message(STR_REJECT_CONTROL, srcNodeId);
				Log.message(ex);
				msgType = CONTROL_REFUSED;
				obj = ex;
			}
		}
		else {
			// デスクトップ制御を拒否する．
			Log.message(STR_REJECT_CONTROL, srcNodeId);
			msgType = CONTROL_REFUSED;
			obj = null;
		}
		// デスクトップ制御受入要求に対する応答を返す．
		ObjectId dstId = srcId;
		CommPacket pkt = new CommPacket(msgType, dstId);
		DesktopMessage msg = new DesktopMessage(obj);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
		
		// デスクトップ制御を受け入れる場合は，
		// 状態変更：デスクトップ制御受入中．
		if (msgType.equals(CONTROL_ACCEPTED)) {
			this.state = ControlState.CONTROLLED;
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private boolean checkAccept(DesktopMessage msg)
	{
		// デスクトップ制御受入待機中の場合は，遠隔制御を受け入れる．
		// デスクトップ制御受入中の場合は，制御を受け入れない．
		return (this.state == ControlState.WAIT);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: STOP_CONTROL
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_StopControl(DesktopMessage msg)
	{
		stopControl();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: CONTROL_MESSAGE
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_ControlMessage(DesktopMessage msg)
	{
		// デスクトップ制御受入待機中の場合は戻る．
		if (this.state == ControlState.WAIT) {
			return;
		}
		// Action 毎の処理を実行する．
		DesktopMessage.Action action = msg.getAction();
		Object obj = msg.getObject();
		
		switch (action) {
		case KEY_PRESS		: evHn_KeyPress((KeyEvent)obj);				break;
		case KEY_RELEASE	: evHn_KeyRelease((KeyEvent)obj);			break;
		case MOUSE_PRESS	: evHn_MousePress((MouseEvent)obj);			break;
		case MOUSE_RELEASE	: evHn_MouseRelease((MouseEvent)obj);		break;
		case MOUSE_MOVE		: evHn_MouseMove((MouseEvent)obj);			break;
		case MOUSE_WHEEL	: evHn_MouseWheel((MouseWheelEvent)obj);	break;
		case UNKNOWN		: /* error */								break;
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_KeyPress(KeyEvent ev)
	{
		int keyCode = ev.getKeyCode();
		this.robot.keyPress(keyCode);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_KeyRelease(KeyEvent ev)
	{
		int keyCode = ev.getKeyCode();
		this.robot.keyRelease(keyCode);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MousePress(MouseEvent ev)
	{
		int btnNum = getMouseButtonNumber(ev);
		if (btnNum != 0) {
			this.robot.mousePress(btnNum);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseRelease(MouseEvent ev)
	{
		int btnNum = getMouseButtonNumber(ev);
		if (btnNum != 0) {
			this.robot.mouseRelease(btnNum);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseMove(MouseEvent ev)
	{
		int x = (int)(ev.getX() / this.scaleX);
		int y = (int)(ev.getY() / this.scaleY);
		this.robot.mouseMove(x, y);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseWheel(MouseWheelEvent ev)
	{
		int wheelAmt = ev.getWheelRotation();
		this.robot.mouseWheel(wheelAmt);
	}
	
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private int getMouseButtonNumber(MouseEvent ev)
	{
		int btnNum = 0;
		
		switch (ev.getButton()) {
		case MouseEvent.NOBUTTON: btnNum = 0;								break;
		case MouseEvent.BUTTON1	: btnNum = InputEvent.BUTTON1_DOWN_MASK;	break;
		case MouseEvent.BUTTON2	: btnNum = InputEvent.BUTTON2_DOWN_MASK;	break;
		case MouseEvent.BUTTON3	: btnNum = InputEvent.BUTTON3_DOWN_MASK;	break;
		default 				: /* error */								break;
		}
		return btnNum;
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: Illigal message
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_UnsupportedMessage(String type, ObjectId srcId)
	{
		Log.warning(STR_ILLEGAL_MESSAGE, type, srcId);
	}
}
